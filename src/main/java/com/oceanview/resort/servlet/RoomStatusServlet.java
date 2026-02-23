package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.RoomDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RoomStatusServlet extends HttpServlet {

    private RoomDAO roomDAO = new RoomDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Tier 2 calls Tier 3 to get processed data
        List<RoomDAO.RoomInventory> inventory = roomDAO.getLiveInventory();

        out.println("<html><head><title>Live Inventory | Ocean View</title>");
        out.println("<style>");
        out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; }");
        out.println(".container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); }");
        out.println(".inventory-row { display: flex; justify-content: space-between; align-items: center; padding: 20px 0; border-bottom: 1px solid #eee; }");
        out.println(".status-badge { padding: 5px 12px; border-radius: 20px; font-size: 11px; font-weight: bold; text-transform: uppercase; }");
        out.println(".available { background: #E8F5E9; color: #2E7D32; }");
        out.println(".full { background: #FFEBEE; color: #C62828; }");
        out.println("</style></head><body>");

        out.println("<div class='container'>");
        out.println("<h2 style='color: var(--primary);'>Current Room Availability</h2>");

        for (RoomDAO.RoomInventory item : inventory) {
            String statusClass = (item.vacant > 0) ? "available" : "full";
            String statusText = (item.vacant > 0) ? item.vacant + " VACANT" : "FULLY BOOKED";

            out.println("<div class='inventory-row'>");
            out.println("  <div><strong>" + item.type + "</strong><br>");
            out.println("  <small style='color:#666;'>" + item.occupied + " of " + item.total + " occupied</small></div>");
            out.println("  <span class='status-badge " + statusClass + "'>" + statusText + "</span>");
            out.println("</div>");
        }

        out.println("<br><a href='admin-dashboard.html' style='color: var(--primary); text-decoration: none; font-weight: bold;'>&larr; Back to Dashboard</a>");
        out.println("</div></body></html>");
    }
}