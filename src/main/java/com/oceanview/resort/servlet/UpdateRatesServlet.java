package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.RatesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class UpdateRatesServlet extends HttpServlet {

    private RatesDAO ratesDAO = new RatesDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Security Check (Admin Only)
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        // 2. Extract and Parse Parameters
        try {
            String roomType = request.getParameter("room_type");
            double newPrice = Double.parseDouble(request.getParameter("new_price"));
            int newCapacity = Integer.parseInt(request.getParameter("new_capacity"));

            // 3. Delegate to DAO (Tier 3)
            boolean success = ratesDAO.updateRoomSettings(roomType, newPrice, newCapacity);

            if (success) {
                response.sendRedirect("admin-dashboard.html?update=success");
            } else {
                response.sendRedirect("admin-dashboard.html?error=not_found");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("admin-dashboard.html?error=invalid_input");
        }
    }
}