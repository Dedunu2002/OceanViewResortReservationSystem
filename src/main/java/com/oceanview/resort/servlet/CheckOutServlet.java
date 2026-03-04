package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.ReservationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class CheckOutServlet extends HttpServlet {
    private ReservationDAO resDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            boolean success = resDAO.processCheckOut(id);

            if (success) {
                response.sendRedirect("view-reservations?msg=checkout_success");
            } else {
                response.sendRedirect("view-reservations?error=checkout_failed");
            }
        }
    }
}