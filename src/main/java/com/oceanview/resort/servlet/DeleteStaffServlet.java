package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteStaffServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Security Guard: Only Admins can delete staff
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String staffId = request.getParameter("id");

        if (staffId != null) {
            try (Connection conn = DBUtil.getConnection()) {
                // 2. Prevent the Admin from deleting themselves (Safety Check)
                // We assume the admin's username is stored in the session
                String currentAdmin = (String) session.getAttribute("user");

                String sql = "DELETE FROM staff WHERE id = ? AND username != ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(staffId));
                ps.setString(2, currentAdmin);

                int rowsDeleted = ps.executeUpdate();

                if (rowsDeleted > 0) {
                    response.sendRedirect("manage-staff.jsp?deleted=success");
                } else {
                    // This happens if they tried to delete themselves
                    response.sendRedirect("manage-staff.jsp?error=protected");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("manage-staff.jsp?error=db");
            }
        } else {
            response.sendRedirect("manage-staff.jsp");
        }
    }
}