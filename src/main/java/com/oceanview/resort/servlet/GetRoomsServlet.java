package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.RoomDAO;
import com.google.gson.Gson; // If you don't have Gson, I can show a simple version
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/get-rooms")
public class GetRoomsServlet extends HttpServlet {
    private RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<RoomDAO.Room> rooms = roomDAO.getAllRooms();

        // Convert the list to JSON
        String json = new Gson().toJson(rooms);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}