package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/view-reservations")
public class ViewReservationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        var out = response.getWriter();

        out.println("<html><head><title>All Reservations</title>");
        out.println("<style>table{width:90%; border-collapse:collapse; margin:20px auto;} th,td{border:1px solid #ddd; padding:12px; text-align:left;} th{background:#0047ab; color:white;}</style>");
        out.println("</head><body><h2 style='text-align:center;'>Ocean View Resort - Booking List</h2>");
        out.println("<table><tr><th>ID</th><th>Guest</th><th>Room</th><th>Check-in</th><th>Check-out</th><th>Bill</th></tr>");

        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservations");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("reservation_number") + "</td>");
                out.println("<td>" + rs.getString("guest_name") + "</td>");
                out.println("<td>" + rs.getString("room_type") + "</td>");
                out.println("<td>" + rs.getString("check_in") + "</td>");
                out.println("<td>" + rs.getString("check_out") + "</td>");
                out.println("<td>$" + rs.getDouble("total_bill") + "</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            out.println("<p>Error fetching data: " + e.getMessage() + "</p>");
        }
        out.println("</table><p style='text-align:center;'><a href='index.html'>Back to Form</a></p></body></html>");
    }
}