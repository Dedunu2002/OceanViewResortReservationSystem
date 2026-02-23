package com.oceanview.resort.util;

import com.oceanview.resort.dao.StaffDAO;

public class DatabaseSeeder {
    public static void main(String[] args) {
        StaffDAO dao = new StaffDAO();

        // This will trigger the BCrypt.hashpw() logic inside your DAO
        boolean success = dao.addStaff("admin", "admin123", "ADMIN");

        if (success) {
            System.out.println("✅ Initial Admin created successfully with a HASHED password!");
        } else {
            System.out.println("❌ Failed to create admin. Check your DB connection.");
        }
    }
}