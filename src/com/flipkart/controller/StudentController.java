package com.flipkart.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.URIReferenceException;

import org.apache.log4j.Logger;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grade;
import com.flipkart.bean.Payment;
import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.service.StudentService;

@Path("/student")
public class StudentController {
	
	private static StudentService studentService = new StudentService();
	private final static Logger logger = Logger.getLogger(StudentController.class);
	
	//Retrieve list of all students
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> getAllStudents(){
		return studentService.getStudents();
	}
	
	//Retrieve list of students' courses
	@GET
	@Path("/courselist/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getStudentCourses(@PathParam("studentId") int studentId){
		return studentService.getStudentCourses(studentId);
	}
	
	//Create a new student
	@POST
	@Path("/post")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertStudent(Student student){
		int userId = student.getId();
		String username = student.getUsername();
		String password = student.getPassword();
		int roleId = student.getRoleId();
		User user = new User(userId, username, password, roleId);
		studentService.createUser(user);
		studentService.createStudent(student);
		logger.info("Hit post service");
		logger.info("Value of id from UI " + student.getId());
		logger.info("Value of name from UI " + student.getName());
		String result = "Track saved : " + student;
		return Response.status(201).entity(result).build();
	}
	
	//Delete a student
	@DELETE
	@Path("/delete/{studentId}")
	public Response deleteStudent(@PathParam("studentId") int studentId)
	throws URIReferenceException{
		studentService.deleteUser(studentId);
		studentService.deleteStudent(studentId);
		logger.info("Student with id " + studentId + " deleted successfully");
		return Response.status(200).entity("Track Id " + studentId + " successfully deleted").build();
	}
	
	//Update student details
	@PUT
	@Path("/update/{studentId}")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Student updateStudent(@PathParam("studentId") int studentId, Student student)
	throws URIReferenceException{
		if(studentService.updateStudent(studentId, student)){
			logger.info("Student updated");
		}
		return student;
	}
	
	public String getStudentName(int studentId){
		return studentService.getStudentName(studentId);
	}
	
	//Add a course
	@POST
	@Path("/addCourse/{studentId}/{courseId}")
	public void addCourse(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId){

        if(studentService.checkForRegistration(studentId)){
            logger.info("Cannot add a course after final registration");
            return;
        }

        if(!studentService.canAddCourse(studentId)){
            return;
        }

        String studentName = getStudentName(studentId);
        if(studentService.addCourse(studentId, studentName, courseId)) {
            logger.info("Course successfully added!");
        }
    }
	
	//Delete a course
	@DELETE
	@Path("/deleteCourse/{studentId}/{courseId}")
	public void deleteCourse(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId){
		
		if(studentService.checkForRegistration(studentId)){
            logger.info("Cannot delete a course after final registration");
            return;
        }

        if(studentService.deleteCourse(studentId, courseId)){
            logger.info("Course deleted successfully");
        }else{
            //Throw Exception
            logger.error("Could not delete course");
        }
	}
	
	//Submit Registration
	@GET
	@Path("/submitRegistration/{studentId}/{paymentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> submitRegistration(@PathParam("studentId") int studentId, @PathParam("paymentId") int paymentId){
		return studentService.submitRegistration(studentId, paymentId);
	}
	
	//View Marks
	@GET
	@Path("/viewGrades/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Grade> getGradeList(@PathParam("studentId") int studentId){
		return studentService.viewMarksByCourse(studentId);
	}
}
