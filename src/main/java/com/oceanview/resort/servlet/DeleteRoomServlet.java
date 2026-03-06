package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet to handle physical room deletion.
 * Accessible only by ADMIN users.
 */
@WebServlet("/DeleteRoomServlet")
public class DeleteRoomServlet extends HttpServlet {

    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Security Check: Only Admins should be allowed to delete rooms
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            // Unauthorized access attempt
            response.sendRedirect("login.html?error=unauthorized");
            return;
        }

        // 2. Get the room number from the URL (e.g., DeleteRoomServlet?id=D_101)
        String roomNumber = request.getParameter("id");

        if (roomNumber != null && !roomNumber.trim().isEmpty()) {
            // 3. Call the DAO to delete the record
            boolean success = roomDAO.deleteRoom(roomNumber);

            // 4. Redirect back to the view page with a status message
            if (success) {
                response.sendRedirect("view-rooms.html?msg=deleted");
            } else {
                response.sendRedirect("view-rooms.html?error=failed");
            }
        } else {
            // If no ID was provided, just go back
            response.sendRedirect("view-rooms.html");
        }
    }
}