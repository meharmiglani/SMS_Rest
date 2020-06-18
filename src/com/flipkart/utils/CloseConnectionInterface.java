package com.flipkart.utils;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface CloseConnectionInterface {
    Logger logger = Logger.getLogger(CloseConnectionInterface.class);

    default void closeStatement(PreparedStatement statement){
        try {
            if(statement != null) {
                statement.close();
            }
        }catch(SQLException e) {
            logger.error(e.getMessage());
        }
    }

    default void closeConnection(PreparedStatement statement, Connection conn){

        try {
            if(statement != null) {
                statement.close();
            }
        }catch(SQLException e) {
            logger.error(e.getMessage());
        }

        try {
            if(conn != null) {
                conn.close();
            }
        }catch(SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
