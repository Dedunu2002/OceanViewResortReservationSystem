package com.oceanview.resort.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StaffDAOTest {
    @Test
    void testCheckLogin() {
        StaffDAO dao = new StaffDAO();
        // Returns the role String (e.g., "Admin") if successful, or null if it fails
        String role = dao.checkLogin("admin", "admin123");
        assertNotNull(role, "Login should return a role for valid credentials.");
    }
    @Test
    void testEmptyUsername() {
        StaffDAO dao = new StaffDAO();
        // TC: Empty username should return null (no role)
        assertNull(dao.checkLogin("", "admin123"), "Should not login with empty username.");
    }

    @Test
    void testShortPassword() {
        StaffDAO dao = new StaffDAO();
        // TC: Testing a password that doesn't meet security standards
        assertNull(dao.checkLogin("admin", "12"), "Should reject short/insecure passwords.");
    }

    @Test
    void testRoleRestriction() {
        StaffDAO dao = new StaffDAO();
        // TC: Verify that a 'Receptionist' cannot access 'Admin' roles
        String role = dao.checkLogin("receptionist_user", "password123");
        assertNotEquals("Admin", role, "Receptionists should not have Admin privileges.");
    }

}