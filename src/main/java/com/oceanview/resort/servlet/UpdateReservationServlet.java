package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.ReservationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class UpdateReservationServlet extends HttpServlet {
    private ReservationDAO resDAO = new ReservationDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("res_id"));
        String contact = request.getParameter("contact_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String type = request.getParameter("room_type");
        String checkOut = request.getParameter("check_out");

        // The DAO now handles the fetch, calculation, and update in one step
        boolean success = resDAO.updateReservation(id, contact, email, address, type, checkOut);

        if (success) {
            response.sendRedirect("view-reservations?update=success");
        } else {
            response.sendRedirect("view-reservations?error=update_failed");
        }
    }
}