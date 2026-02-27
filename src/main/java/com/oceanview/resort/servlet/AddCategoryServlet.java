package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.RatesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AddCategoryServlet extends HttpServlet {
    private RatesDAO ratesDAO = new RatesDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.setStatus(401);
            return;
        }

        try {
            String roomType = request.getParameter("room_type");
            double price = Double.parseDouble(request.getParameter("price"));
            int capacity = Integer.parseInt(request.getParameter("capacity"));

            boolean success = ratesDAO.addRoomCategory(roomType, price, capacity);

            if (success) {
                response.sendRedirect("admin-dashboard.html?msg=category_added");
            } else {
                response.sendRedirect("add-room-category.html?error=failed");
            }
        } catch (Exception e) {
            response.sendRedirect("add-room-category.html?error=invalid_input");
        }
    }
}