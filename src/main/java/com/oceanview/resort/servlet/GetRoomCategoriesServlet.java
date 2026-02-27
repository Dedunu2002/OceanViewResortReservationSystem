package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/get-categories")
public class GetRoomCategoriesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder("[");

        try (Connection conn = DBUtil.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT room_type FROM room_rates");

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                json.append("\"").append(rs.getString("room_type")).append("\"");
                first = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        json.append("]");
        out.print(json.toString());
        out.flush();
    }
}