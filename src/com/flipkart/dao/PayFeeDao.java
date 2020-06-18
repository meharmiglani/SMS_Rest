package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.flipkart.constant.SQLConstantQueries;
import com.flipkart.bean.Payment;
import com.flipkart.utils.CloseConnectionInterface;
import com.flipkart.utils.DBUtil;

public class PayFeeDao implements CloseConnectionInterface{
	private static final Logger logger = Logger.getLogger(PayFeeDao.class);

	
    public List<Payment> fetchCourseFee(int studentId) {
        List<Payment> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;

        try{
            statement = conn.prepareStatement(SQLConstantQueries.FETCH_COURSE_FEE);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int courseId = resultSet.getInt(1);
                String courseName = resultSet.getString(2);
                int fee = resultSet.getInt(3);
                Payment payment = new Payment(courseId, courseName, fee);
                list.add(payment);
            }
            return list;

        }catch (SQLException e){
            logger.error(e.getMessage());
            return null;
        }finally {
            closeConnection(statement, conn);
        }
    }

   
    public double getScholarshipAmount(int studentId) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement(SQLConstantQueries.GET_SCHOLARSHIP_AMOUNT);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble(1);
            }
            return -1;

        }catch (SQLException e){
            logger.error(e.getMessage());
            return -1;
        }finally {
            closeConnection(statement, conn);
        }
    }
    
    public boolean insertIntoRegistration(int studentId, int paymentId, double amount){
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement(SQLConstantQueries.INSERT_STUDENT_REGISTRATION);
            statement.setNull(1, Types.NULL);
            statement.setInt(2, paymentId);
            statement.setInt(3, studentId);
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            statement.setDouble(5, amount);
            int row = statement.executeUpdate();
            return row == 1;

        }catch (SQLException e){
            logger.error(e.getMessage());
            return false;
        }finally {
            closeConnection(statement, conn);
        }
    }
}
