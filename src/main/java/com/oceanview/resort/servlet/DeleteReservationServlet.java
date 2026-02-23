package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.ReservationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Controller for deleting reservations.
 * separation of logic: The Servlet handles the request,
 * the DAO handles the database.
 */
public class DeleteReservationServlet extends HttpServlet {

    // Instantiate the DAO
    private ReservationDAO resDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id != null) {
            try {
                // Tier 2 calling Tier 3
                resDAO.deleteReservation(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Redirect back to the view list
        response.sendRedirect("view-reservations");
    }
}