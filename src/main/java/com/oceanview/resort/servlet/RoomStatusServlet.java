package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RoomStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        LocalDate today = LocalDate.now();

        try (Connection conn = DBUtil.getConnection()) {
            // 1. Fetch ALL Room Types and their current Max Capacities from the database
            // This ensures if you add a new room type or change a capacity, it reflects here immediately.
            Map<String, Integer> capacities = new HashMap<>();
            String capSql = "SELECT room_type, max_capacity FROM room_rates";
            try (Statement stmt = conn.createStatement(); ResultSet rsCap = stmt.executeQuery(capSql)) {
                while (rsCap.next()) {
                    capacities.put(rsCap.getString("room_type"), rsCap.getInt("max_capacity"));
                }
            }

            // 2. Query for current occupancy based on today's date
            String sql = "SELECT room_type, COUNT(*) as occupied FROM reservations " +
                    "WHERE ? BETWEEN check_in AND (check_out - INTERVAL 1 DAY) GROUP BY room_type";
            // Note: Use (check_out - INTERVAL 1 DAY) if check-out day rooms are considered vacant for new guests

            Map<String, Integer> occupancy = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, today.toString());
                try (ResultSet rsOcc = ps.executeQuery()) {
                    while (rsOcc.next()) {
                        occupancy.put(rsOcc.getString("room_type"), rsOcc.getInt("occupied"));
                    }
                }
            }

            // CSS and Header
            out.println("<html><head><title>Room Inventory | Ocean View</title>");
            out.println("<style>");
            out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; --success: #28a745; --danger: #dc3545; }");
            out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; }");
            out.println(".status-container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); }");
            out.println(".inventory-row { display: flex; justify-content: space-between; align-items: center; padding: 20px; border-bottom: 1px solid #eee; }");
            out.println(".type-label { font-weight: bold; color: var(--primary); font-size: 18px; }");
            out.println(".vacancy-badge { padding: 8px 15px; border-radius: 20px; font-weight: bold; font-size: 14px; }");
            out.println(".available { background: #e8f5e9; color: var(--success); }");
            out.println(".full { background: #ffebee; color: var(--danger); }");
            out.println("</style></head><body>");

            out.println("<div class='status-container'>");
            out.println("<h2 style='color:var(--primary); margin-top:0;'>Live Room Inventory - " + today + "</h2>");

            // 3. DYNAMIC RENDERING: Loop through the capacities map
            // This fixes the issue where hard-coded names might not match database updates
            if (capacities.isEmpty()) {
                out.println("<p>No room categories found in database.</p>");
            } else {
                for (Map.Entry<String, Integer> entry : capacities.entrySet()) {
                    String roomType = entry.getKey();
                    int maxCap = entry.getValue();
                    int currentlyOccupied = occupancy.getOrDefault(roomType, 0);

                    renderRow(out, roomType, currentlyOccupied, maxCap);
                }
            }

            out.println("<br><a href='admin-dashboard.jsp' style='color:var(--primary); text-decoration:none; font-weight:bold;'>&larr; Back to Admin Hub</a>");
            out.println("</div></body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<div style='color:red;'>Database Error: " + e.getMessage() + "</div>");
        }
    }

    private void renderRow(PrintWriter out, String type, int occupied, int total) {
        int left = total - occupied;
        // Ensure 'left' doesn't go below zero if overbooked
        if (left < 0) left = 0;

        String statusClass = (left > 0) ? "available" : "full";
        String statusText = (left > 0) ? left + " VACANT" : "FULLY BOOKED";

        out.println("<div class='inventory-row'>");
        out.println("  <span class='type-label'>" + type + "</span>");
        out.println("  <div>");
        out.println("    <span style='margin-right:20px; color:#666;'>" + occupied + " / " + total + " Occupied</span>");
        out.println("    <span class='vacancy-badge " + statusClass + "'>" + statusText + "</span>");
        out.println("  </div>");
        out.println("</div>");
    }
}