package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateRatesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Security Check (Admin Only)
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String roomType = request.getParameter("room_type");
        String newPriceStr = request.getParameter("new_price");

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE room_rates SET price_per_night = ?, max_capacity = ? WHERE room_type = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(request.getParameter("new_price")));
            ps.setInt(2, Integer.parseInt(request.getParameter("new_capacity")));
            ps.setString(3, request.getParameter("room_type"));
            ps.executeUpdate();
            // Redirect back with a success message
            response.sendRedirect("admin-dashboard.jsp?update=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-dashboard.jsp?error=db");
        }
    }
}