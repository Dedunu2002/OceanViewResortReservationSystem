package com.oceanview.resort.servlet.;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddStaffServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Security check
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String role = request.getParameter("role");

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO staff (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass); // In a real system, you would hash this password
            ps.setString(3, role);

            ps.executeUpdate();
            // Redirect back to the staff list after successful creation
            response.sendRedirect("manage-staff.jsp?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-staff.jsp?error=1");
        }
    }
}