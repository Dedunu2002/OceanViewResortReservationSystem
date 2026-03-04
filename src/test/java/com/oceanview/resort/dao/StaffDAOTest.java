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
}