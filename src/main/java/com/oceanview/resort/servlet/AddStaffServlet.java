package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AddStaffServlet extends HttpServlet {
    private StaffDAO staffDAO = new StaffDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String role = request.getParameter("role");

        // Logic handled by the DAO
        boolean success = staffDAO.addStaff(user, pass, role);

        if (success) {
            response.sendRedirect("manage-staff.html?success=1");
        } else {
            response.sendRedirect("manage-staff.html?error=1");
        }
    }
}