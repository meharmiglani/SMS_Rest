package com.flipkart.service;

import java.util.List;

import com.flipkart.bean.Professor;
import com.flipkart.bean.Student;
import com.flipkart.bean.User;
import com.flipkart.dao.ProfessorDao;
import com.flipkart.dao.StudentDao;

public class AdminService implements AdminServiceInterface {
	private static StudentDao studentDao = new StudentDao();
	private static ProfessorDao professorDao = new ProfessorDao();
	
	public List<Student> getStudents() {
		return studentDao.viewAllStudents();
	}
	
	public void createStudent(Student student) {
		studentDao.insertStudent(student);
	}

	public void createUser(User user) {
		studentDao.createUser(user);
	}

	public boolean deleteUser(int userId) {
		return studentDao.deleteUser(userId);
	}

	public boolean deleteStudent(int studentId) {
		return studentDao.deleteStudent(studentId);
	}
	
	public boolean updateStudent(int studentId, Student student) {
		return studentDao.updateStudent(studentId, student);
	}
	
	public List<Professor> getProfessors() {
		return professorDao.viewAllProfessors();
	}
	
	public boolean updateProfessor(int professorId, Professor newProfessor){
		return professorDao.updateProfessor(professorId, newProfessor);
	}
	
	public boolean deleteProfessor(int professorId) {
		return professorDao.deleteProfessor(professorId);
	}
	
	public void createProfessor(Professor professor) {
		professorDao.insertProfessor(professor);
	}
}
