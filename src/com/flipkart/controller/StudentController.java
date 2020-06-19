package com.flipkart.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grade;
import com.flipkart.bean.Payment;
import com.flipkart.service.StudentService;

@Path("/student")
public class StudentController {

	private static StudentService studentService = new StudentService();
	private final static Logger logger = Logger.getLogger(StudentController.class);


	// Retrieve list of students' courses
	@GET
	@Path("/courselist/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getStudentCourses(@PathParam("studentId") int studentId) {
		return studentService.getStudentCourses(studentId);
	}

	//Fetch course catalog
	@GET
	@Path("/getCatalog/{catalogId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCourseCatalog(@PathParam("catalogId") int catalogId){
		return studentService.getCourseCatalog(catalogId);
	}
	
	// Add a course
	@POST
	@Path("/addCourse/{studentId}/{courseId}")
	public void addCourse(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {

		if (studentService.checkForRegistration(studentId)) {
			logger.info("Cannot add a course after final registration");
			return;
		}

		if (!studentService.canAddCourse(studentId)) {
			return;
		}

		String studentName = studentService.getStudentName(studentId);
		if (studentService.addCourse(studentId, studentName, courseId)) {
			logger.info("Course successfully added!");
		}
	}

	// Delete a course
	@DELETE
	@Path("/deleteCourse/{studentId}/{courseId}")
	public void deleteCourse(@PathParam("studentId") int studentId, @PathParam("courseId") int courseId) {

		if (studentService.checkForRegistration(studentId)) {
			logger.info("Cannot delete a course after final registration");
			return;
		}

		if (studentService.deleteCourse(studentId, courseId)) {
			logger.info("Course deleted successfully");
		} else {
			// Throw Exception
			logger.error("Could not delete course");
		}
	}

	// Submit Registration
	@POST
	@Path("/submitRegistration/{studentId}/{paymentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> submitRegistration(@PathParam("studentId") int studentId,
			@PathParam("paymentId") int paymentId) {
		return studentService.submitRegistration(studentId, paymentId);
	}

	// View Marks
	@GET
	@Path("/viewGrades/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Grade> getGradeList(@PathParam("studentId") int studentId) {
		return studentService.viewMarksByCourse(studentId);
	}
}
