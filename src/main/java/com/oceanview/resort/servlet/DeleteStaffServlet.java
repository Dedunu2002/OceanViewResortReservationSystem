package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class DeleteStaffServlet extends HttpServlet {
    private StaffDAO staffDAO = new StaffDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String staffId = request.getParameter("id");
        String currentAdmin = (String) session.getAttribute("user");

        if (staffId != null) {
            int result = staffDAO.deleteStaff(Integer.parseInt(staffId), currentAdmin);

            if (result > 0) {
                response.sendRedirect("manage-staff.html?deleted=success");
            } else if (result == 0) {
                // Tried to delete themselves (the username matched the protected criteria)
                response.sendRedirect("manage-staff.html?error=protected");
            } else {
                response.sendRedirect("manage-staff.html?error=db");
            }
        } else {
            response.sendRedirect("manage-staff.html");
        }
    }
}