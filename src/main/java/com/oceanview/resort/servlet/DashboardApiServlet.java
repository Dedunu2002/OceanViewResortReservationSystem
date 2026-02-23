package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class DashboardApiServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Security check
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.setStatus(401);
            return;
        }

        int totalBookings = 0;
        double totalRevenue = 0.0;

        // Business Logic (In a bigger app, move this to a ReportDAO)
        try (Connection conn = DBUtil.getConnection()) {
            // 1. Get Total Bookings with an Alias
            Statement st1 = conn.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT COUNT(*) AS total_count FROM reservations");
            if (rs1.next()) {
                totalBookings = rs1.getInt("total_count");
            }

            // 2. Get Total Revenue with an Alias
            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT SUM(total_bill) AS total_sum FROM reservations");
            if (rs2.next()) {
                totalRevenue = rs2.getDouble("total_sum");
            }
        } catch (Exception e) {
            // This will print the exact SQL error to your IntelliJ console
            e.printStackTrace();
        }

        // Return JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"totalBookings\":" + totalBookings + ", \"totalRevenue\":" + totalRevenue + "}");
        out.flush();
    }
}