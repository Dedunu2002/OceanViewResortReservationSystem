package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AddRoomServlet extends HttpServlet {
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Security check: Only Admins should add rooms
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.setStatus(401);
            return;
        }

        String roomNumber = request.getParameter("room_number");
        String roomType = request.getParameter("room_type");

        boolean success = roomDAO.addPhysicalRoom(roomNumber, roomType);

        if (success) {
            response.sendRedirect("admin-dashboard.html?msg=room_added");
        } else {
            response.sendRedirect("add-room.html?error=failed");
        }
    }
}