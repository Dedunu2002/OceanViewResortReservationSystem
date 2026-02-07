package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ViewReservationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Manage Reservations</title>");
        out.println("<style>body{font-family:Arial; padding:20px;} table{width:100%; border-collapse:collapse;} th,td{border:1px solid #ddd; padding:10px; text-align:left;} th{background:#2c3e50; color:white;} .search-bar{margin-bottom:20px;}</style></head><body>");

        out.println("<h2>Ocean View Reservation Management</h2>");

        // Integrated Search Form
        out.println("<div class='search-bar'>");
        out.println("<form action='view-reservations' method='GET'>");
        out.println("<input type='text' name='searchQuery' placeholder='Search by Name or ID...' value='" + (searchQuery != null ? searchQuery : "") + "'>");
        out.println("<button type='submit'>Filter</button>");
        out.println("<a href='view-reservations' style='margin-left:10px;'>Clear</a>");
        out.println("</form></div>");

        out.println("<table><tr><th>ID</th><th>Guest Name</th><th>Room</th><th>Check-in</th><th>Check-out</th><th>Bill</th><th>Actions</th></tr>");

        try (Connection conn = DBUtil.getConnection()) {
            String sql;
            PreparedStatement ps;

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Filtered Query
                sql = "SELECT * FROM reservations WHERE reservation_number = ? OR guest_name LIKE ?";
                ps = conn.prepareStatement(sql);
                int searchId = 0;
                try { searchId = Integer.parseInt(searchQuery); } catch (NumberFormatException e) {}
                ps.setInt(1, searchId);
                ps.setString(2, "%" + searchQuery + "%");
            } else {
                // View All Query
                sql = "SELECT * FROM reservations";
                ps = conn.prepareStatement(sql);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("reservation_number");
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + rs.getString("guest_name") + "</td>");
                out.println("<td>" + rs.getString("room_type") + "</td>");
                out.println("<td>" + rs.getString("check_in") + "</td>");
                out.println("<td>" + rs.getString("check_out") + "</td>");
                out.println("<td>$" + rs.getDouble("total_bill") + "</td>");
                // The Delete link we made earlier
                out.println("<td><a href='delete-reservation?id=" + id + "' style='color:red;' onclick='return confirm(\"Cancel this booking?\")'>Cancel</a></td>");
                out.println("</tr>");
            }
        } catch (SQLException e) { e.printStackTrace(); }

        out.println("</table><br><a href='receptionist-dashboard.jsp'>Back to Dashboard</a></body></html>");
    }
}