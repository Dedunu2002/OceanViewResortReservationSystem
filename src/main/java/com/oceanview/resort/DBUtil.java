package com.oceanview.resort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // Database URL: localhost means your computer, 3306 is the MySQL port,
    // and ocean_view_db is the name of the database we created in WAMP
    private static final String URL = "jdbc:mysql://localhost:3306/ocean_view_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // WAMP default is empty

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // This is the "Driver String" that tells Java how to speak to MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // This line actually opens the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Success: Database connected!");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL Driver not found. Did you refresh Maven?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: Could not connect to WAMP MySQL. Is WAMP green?");
            e.printStackTrace();
        }
        return connection;
    }
}