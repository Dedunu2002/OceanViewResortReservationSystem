package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.StaffDAO;
import com.oceanview.resort.dao.StaffDAO.StaffMember;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class ProfileServlet extends HttpServlet {
    private StaffDAO staffDAO = new StaffDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(401); // Unauthorized
            return;
        }

        String username = (String) session.getAttribute("user");
        StaffMember profile = staffDAO.getStaffByUsername(username);

        if (profile != null) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(String.format(
                    "{\"id\": %d, \"username\": \"%s\", \"role\": \"%s\"}",
                    profile.id, profile.username, profile.role
            ));
        } else {
            response.setStatus(404);
        }
    }
}