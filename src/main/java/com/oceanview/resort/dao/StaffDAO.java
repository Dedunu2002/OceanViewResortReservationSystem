package com.oceanview.resort.dao;

import com.oceanview.resort.util.DBUtil;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    // --- NEW: Data Model for the Frontend ---
    public static class StaffMember {
        public int id;
        public String username;
        public String role;

        public StaffMember(int id, String username, String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }
    }

    // --- NEW: Fetch list for the HTML API ---
    public List<StaffMember> getAllStaff() {
        List<StaffMember> list = new ArrayList<>();
        // GDPR Data Minimization: We specifically DO NOT select the password column here.
        String sql = "SELECT id, username, role FROM staff ORDER BY role ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new StaffMember(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- EXISTING LOGIC ---
    public String checkLogin(String username, String password) {
        String sql = "SELECT password, role FROM staff WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String role = rs.getString("role");
                    if (BCrypt.checkpw(password, storedHash)) {
                        return role;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStaff(String username, String password, String role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO staff (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int deleteStaff(int id, String currentAdmin) {
        String sql = "DELETE FROM staff WHERE id = ? AND username != ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, currentAdmin);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}