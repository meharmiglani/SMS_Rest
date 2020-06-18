package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.flipkart.constant.SQLConstantQueries;
import com.flipkart.utils.CloseConnectionInterface;
import com.flipkart.utils.DBUtil;

public class FinalRegistrationDao implements CloseConnectionInterface{
	
	public boolean checkRegistration(int studentId){
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement(SQLConstantQueries.IS_REGISTERED);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getString(1).equals("yes");

        }catch (SQLException e){
            logger.error(e.getMessage());
            return false;
        }
    }
	
	public boolean registerStudent(int studentId) {
        Connection conn = DBUtil.getConnection();
        PreparedStatement statement = null;

        try{
            statement = conn.prepareStatement(SQLConstantQueries.REGISTER_STUDENT);
            statement.setString(1, "yes");
            statement.setInt(2, studentId);
            int row = statement.executeUpdate();

            if(row == 1){
                logger.info("Student " + studentId + " registered successfully");
                return true;
            }else{
                logger.info("Could not submit registration");
                return false;
            }

        }catch (SQLException e){
            logger.error(e.getMessage());
            return false;
        }finally {
            closeConnection(statement, conn);
        }
    }
}
