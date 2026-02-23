package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ViewReservationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String role = (String) session.getAttribute("role");
        String searchQuery = request.getParameter("searchQuery");
        String homePage = "ADMIN".equals(role) ? "admin-dashboard.html" : "receptionist-dashboard.html";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Reservations | Ocean View</title>");
        out.println("<style>");
        out.println(":root { --primary: #0A2540; --secondary: #1E6F9F; --accent: #C9A24D; --bg: #F5F7FA; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; margin: 0; }");
        out.println(".top-nav { background: var(--primary); color: white; padding: 15px 30px; margin: -40px -40px 40px -40px; display: flex; justify-content: space-between; align-items: center; }");
        out.println("table { width: 100%; border-collapse: collapse; background: white; border-radius: 4px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.03); }");
        out.println("th { background: #f8fafc; color: var(--primary); text-transform: uppercase; font-size: 11px; letter-spacing: 1px; padding: 20px; text-align: left; border-bottom: 2px solid var(--bg); }");
        out.println("td { padding: 18px 20px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; }");
        out.println(".search-box { margin-bottom: 30px; display: flex; gap: 10px; }");
        out.println("input[type='text'] { padding: 12px; border: 1px solid #e2e8f0; border-radius: 4px; width: 300px; }");
        out.println("button { padding: 12px 25px; background: var(--primary); color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: 600; }");
        out.println(".btn-edit { color: var(--secondary); text-decoration: none; font-weight: bold; font-size: 12px; margin-right: 15px; }");
        out.println(".btn-print { color: #A57B0A; text-decoration: none; font-weight: bold; font-size: 12px; margin-right: 15px; }");

        out.println(".btn-cancel { color: #ef4444; text-decoration: none; font-weight: bold; font-size: 12px; }");
        out.println("</style></head><body>");

        // Top Navigation
        out.println("<div class='top-nav'>");
        out.println("<div style='display:flex; align-items:center;'>");
        out.println("<img src='images/logo.png' style='height:30px; margin-right:15px;' alt='Logo'>");        out.println("<span style='letter-spacing:1px; font-weight:bold;'>RESERVATION MANAGEMENT</span>");
        out.println("</div>");
        out.println("<a href='" + homePage + "' style='color:var(--accent); text-decoration:none; font-size:13px;'>DASHBOARD</a>");
        out.println("</div>");

        // Search Bar
        out.println("<form action='view-reservations' method='GET' class='search-box'>");
        out.println("<input type='text' name='searchQuery' placeholder='Search Guest or ID...' value='" + (searchQuery != null ? searchQuery : "") + "'>");
        out.println("<button type='submit'>FILTER</button>");
        out.println("<a href='view-reservations' style='padding:12px; color:var(--secondary); text-decoration:none;'>Clear</a>");
        out.println("</form>");

        out.println("<table><tr><th>Ref ID</th><th>Guest Name</th><th>Contact Number</th><th>Address</th><th>Email Address</th><th>Room Wing</th><th>Arrival</th><th>Departure</th><th>Total Bill</th><th>Actions</th></tr>");

        try (Connection conn = DBUtil.getConnection()) {
            String sql = (searchQuery != null && !searchQuery.trim().isEmpty())
                    ? "SELECT * FROM reservations WHERE reservation_number = ? OR guest_name LIKE ?"
                    : "SELECT * FROM reservations";

            PreparedStatement ps = conn.prepareStatement(sql);
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                int id = 0; try { id = Integer.parseInt(searchQuery); } catch (Exception e) {}
                ps.setInt(1, id);
                ps.setString(2, "%" + searchQuery + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int resId = rs.getInt("reservation_number");
                out.println("<tr>");
                out.println("<td>#OR-" + resId + "</td>");
                out.println("<td><strong>" + rs.getString("guest_name") + "</strong></td>");
                out.println("<td>" + rs.getString("contact_number") + "</td>");
                out.println("<td>" + rs.getString("address") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("room_type") + "</td>");
                out.println("<td>" + rs.getString("check_in") + "</td>");
                out.println("<td>" + rs.getString("check_out") + "</td>");
                out.println("<td>Rs. " + String.format("%.2f", rs.getDouble("total_bill")) + "</td>");


                out.println("<td>");

                // EDIT Button: Available to everyone
                out.println("<a href='edit-reservation?id=" + resId + "' class='btn-edit'>EDIT</a>");

                // CANCEL Button: Restrict to ADMIN only
                if ("ADMIN".equals(role)) {
                    out.println("<a href='delete-reservation?id=" + resId + "' class='btn-cancel' onclick='return confirm(\"Confirm cancellation?\")'>CANCEL</a>");
                }

                // Print Bill
                out.println("<a href='view-bill.html?id=" + resId + "' target='_blank' class='btn-print'>PRINT BILL</a>");
                out.println("</td>");

                out.println("</tr>");
            }
        } catch (SQLException e) { e.printStackTrace(); }

        out.println("</table></body></html>");
    }
}