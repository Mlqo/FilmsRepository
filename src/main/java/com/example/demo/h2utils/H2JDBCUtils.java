package com.example.demo.h2utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class H2JDBCUtils {



   private static String jdbcURL = "jdbc:h2:mem:dbprod";
    private static String jdbcUsername = "sa";
    private static String jdbcPassword = "sa";



   public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }
}