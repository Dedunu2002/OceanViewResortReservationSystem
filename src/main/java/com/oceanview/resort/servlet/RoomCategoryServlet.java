package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class RoomCategoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder("[");

        try (Connection conn = DBUtil.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT room_type, price_per_night, max_capacity FROM room_rates");

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{")
                        .append("\"type\":\"").append(rs.getString("room_type")).append("\",")
                        .append("\"price\":").append(rs.getDouble("price_per_night")).append(",")
                        .append("\"capacity\":").append(rs.getInt("max_capacity"))
                        .append("}");
                first = false;
            }
        } catch (Exception e) { e.printStackTrace(); }

        json.append("]");
        out.print(json.toString());
    }
}