package com.flipkart.service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.log4j.Logger;

import com.flipkart.bean.Course;
import com.flipkart.bean.Grade;
import com.flipkart.bean.Payment;
import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.dao.FetchReportCardDao;
import com.flipkart.dao.FinalRegistrationDao;
import com.flipkart.dao.PayFeeDao;
import com.flipkart.dao.RegisterCourseDao;
import com.flipkart.dao.StudentDao;

public class StudentService {
	private final static LocalDate localDate = LocalDate.now();
    private final static LocalTime localTime = LocalTime.now();
	private static StudentDao studentDao = new StudentDao();
	private static RegisterCourseDao registerCourseDao = new RegisterCourseDao();
	private final static Logger logger = Logger.getLogger(StudentService.class);
	private static FinalRegistrationDao finalRegistrationDao = new FinalRegistrationDao();
	private static PayFeeDao payFeeDao = new PayFeeDao();
	private static FetchReportCardDao fetchReportCardDao = new FetchReportCardDao();

	
	public List<Student> getStudents(){
		return studentDao.viewAllStudents();
	}
	
	public List<Course> getStudentCourses(int studentId){
		return studentDao.viewRegisteredCourses(studentId);
	}
	
	public void createStudent(Student student){
		studentDao.insertStudent(student);
	}
	
	public void createUser(User user){
		studentDao.createUser(user);
	}
	
	public boolean deleteUser(int userId){
		return studentDao.deleteUser(userId);
	}
	
	public boolean deleteStudent(int studentId){
		return studentDao.deleteStudent(studentId);
	}
	
	public boolean canAddCourse(int studentId){
        int enrolledCourses = registerCourseDao.courseLimitCheck(studentId);
        if(enrolledCourses == 4){
            logger.info("You have reached the maximum course limit.");
            return false;
        }
        return true;
    }

    public boolean checkForRegistration(int studentId){
        return finalRegistrationDao.checkRegistration(studentId);
    }
	
	public boolean updateStudent(int studentId, Student student){
		return studentDao.updateStudent(studentId, student);
	}
	
	public String getStudentName(int studentId){
		return studentDao.getStudentName(studentId);
	}
	
	public boolean addCourse(int studentId, String studentName, int courseId){
		if(registerCourseDao.addCourse(studentId, studentName, courseId)){
			return true;
		}
		return false;
	}
	
	public boolean deleteCourse(int studentId, int courseId){
		if(registerCourseDao.deleteCourse(studentId, courseId)){
			return true;
		}
		return false;
	}
	
	public List<Payment> submitRegistration(int studentId, int paymentId){
		if(finalRegistrationDao.checkRegistration(studentId)){
            logger.info("You are already registered");
            return null;
        }

        int enrolledCourses = registerCourseDao.courseLimitCheck(studentId);
        if(enrolledCourses < 4){
            logger.info("Please select 4 courses to submit your registration. You are currently enrolled in " + enrolledCourses + " courses");
            return null;
        }

        //Pay Fee
        logger.info("Please pay the fee in order to confirm your registration.");
        List<Payment> feeList = payFeeDao.fetchCourseFee(studentId);
        double fee = 0;
        logger.info("Fee payable: " + fee);

        double scholarshipAmount = payFeeDao.getScholarshipAmount(studentId);

        if(scholarshipAmount != -1){
            double reduction = (scholarshipAmount/100) * fee;
            fee -= reduction;
        }
        
        logger.info("Scholarship Amount for student " + studentId + " = " + scholarshipAmount + "%");
        logger.info("Fee after scholarship: " + fee);

        logger.info("Total fee paid: " + fee + "on " + localDate + " " + localTime.getHour() + ":" + localTime.getMinute() + " " + localDate.getDayOfWeek());

        if(payFeeDao.insertIntoRegistration(studentId, paymentId, fee)){
        	finalRegistrationDao.registerStudent(studentId);
        }
        
        return feeList;
	}
	
	public List<Grade> viewMarksByCourse(int studentId) {
        if(!finalRegistrationDao.checkRegistration(studentId)){
            logger.info("Register to view your marks");
            return null;
        }

        return fetchReportCardDao.getMarksByCourse(studentId);
    }
}
