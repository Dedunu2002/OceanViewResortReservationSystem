package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Controller for Login operations.
 * This represents the Controller/Logic Tier (Tier 2).
 */
public class LoginServlet extends HttpServlet {

    // Instantiate the DAO
    private StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        HttpSession session = request.getSession();

        // Tier 2 calling Tier 3
        String role = staffDAO.checkLogin(user, pass);

        if (role != null) {
            // Success: Set session attributes
            session.setAttribute("user", user);
            session.setAttribute("role", role);

            // Role-based redirection logic
            if ("ADMIN".equals(role)) {
                response.sendRedirect("admin-dashboard.html");
            } else {
                response.sendRedirect("receptionist-dashboard.html");
            }
        } else {
            // Failure: Redirect back to login with error
            response.sendRedirect("login.html?error=invalid");
        }
    }
}