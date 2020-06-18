package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.flipkart.constant.SQLConstantQueries;
import com.flipkart.bean.Grade;
import com.flipkart.utils.CloseConnectionInterface;
import com.flipkart.utils.DBUtil;

public class FetchReportCardDao implements CloseConnectionInterface{
    private final static Logger logger = Logger.getLogger(FetchReportCardDao.class);
    
    public boolean checkForReportCard(int studentId) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement(SQLConstantQueries.CHECK_FOR_REPORT_CARD);
            statement.setInt(1,studentId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) == 4;

        }catch (SQLException e){
            logger.error(e.getMessage());
            return false;
        }finally {
            closeConnection(statement, conn);
        }
    }

    public List<Grade> getReportCard(int studentId){
        if(!checkForReportCard(studentId)){
            logger.error("Report card isn't available as all courses haven't been graded. View marks by course");
            return null;
        }
        return getMarksByCourse(studentId);
    }

    public List<Grade> getMarksByCourse(int studentId){
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;
        List<Grade> gradeList = new ArrayList<>();
        try{
            statement = conn.prepareStatement(SQLConstantQueries.GET_REPORT_CARD);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int courseId = resultSet.getInt(1);
                String courseName = resultSet.getString(2);
                int marks = resultSet.getInt(3);
                Grade grade = new Grade(courseId, courseName, marks);
                gradeList.add(grade);
            }
            return gradeList;

        }catch(SQLException e){
            logger.error(e.getMessage());
            return null;
        }finally {
            closeConnection(statement, conn);
        }
    }

    public double getFinalPercentage(int studentId){
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;

        try{
            statement = conn.prepareStatement(SQLConstantQueries.GET_FINAL_MARKS);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return resultSet.getDouble(1);
            }

            return -1;
        }catch(SQLException e){
            logger.error(e.getMessage());
            return -1;
        }finally {
            closeConnection(statement, conn);
        }
    }
}
