package com.oceanview.resort;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Testing Connection...");

        Connection conn = DBUtil.getConnection();

        if (conn != null) {
            System.out.println("Congratulations! Your setup is 100% correct.");
        } else {
            System.out.println("Connection failed. Check if WAMP icon is GREEN.");
        }
    }
}