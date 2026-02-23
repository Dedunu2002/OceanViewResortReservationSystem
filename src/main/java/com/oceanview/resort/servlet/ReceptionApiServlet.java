package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ReceptionApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Security & Ethical Design: Enforce Principle of Least Privilege
        HttpSession session = request.getSession(false);
        if (session == null || !"RECEPTIONIST".equals(session.getAttribute("role"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Retrieve the username from the session (assuming you set it during login)
        String username = (String) session.getAttribute("user");
        if (username == null) username = "Staff";

        int totalRooms = 50; // Total inventory as defined in your original JSP
        int occupied = 0;

        // 2. Fetch Active Bookings from Database
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS occupied_count FROM reservations")) {

            if (rs.next()) {
                occupied = rs.getInt("occupied_count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Send a 500 Internal Server Error if the database fails
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        int availableRooms = totalRooms - occupied;

        // 3. Return Data Minimization (JSON format)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Construct the JSON string: {"availableRooms": 45, "totalRooms": 50, "username": "john_doe"}
        String jsonResponse = String.format(
                "{\"availableRooms\": %d, \"totalRooms\": %d, \"username\": \"%s\"}",
                availableRooms, totalRooms, username
        );

        out.print(jsonResponse);
        out.flush();
    }
}