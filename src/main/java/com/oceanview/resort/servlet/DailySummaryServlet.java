package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

public class DailySummaryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Only Admin can access reports
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        LocalDate today = LocalDate.now();

        out.println("<html><head><title>Daily Report | Ocean View</title>");
        out.println("<style>");
        out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; }");
        out.println(".report-card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); max-width: 800px; margin: 0 auto; }");
        out.println(".stat-box { display: flex; gap: 20px; margin-bottom: 30px; }");
        out.println(".stat { flex: 1; padding: 20px; background: var(--primary); color: white; border-radius: 4px; text-align: center; }");
        out.println(".stat h3 { margin: 0; font-size: 12px; text-transform: uppercase; color: var(--accent); }");
        out.println(".stat p { margin: 10px 0 0; font-size: 24px; font-weight: bold; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("th { text-align: left; border-bottom: 2px solid var(--bg); padding: 10px; font-size: 12px; color: var(--primary); }");
        out.println("td { padding: 10px; border-bottom: 1px solid #eee; font-size: 14px; }");
        out.println("</style></head><body>");

        try (Connection conn = DBUtil.getConnection()) {
            // Query for guests currently checked in (Today is between check-in and check-out)
            String sql = "SELECT * FROM reservations WHERE ? BETWEEN check_in AND check_out";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, today.toString());
            ResultSet rs = ps.executeQuery();

            int guestCount = 0;
            double totalRevenue = 0;
            StringBuilder tableRows = new StringBuilder();

            while (rs.next()) {
                guestCount++;
                totalRevenue += rs.getDouble("total_bill");
                tableRows.append("<tr>")
                        .append("<td>").append(rs.getString("guest_name")).append("</td>")
                        .append("<td>").append(rs.getString("room_type")).append("</td>")
                        .append("<td>").append(rs.getString("check_out")).append("</td>")
                        .append("<td>Rs. ").append(String.format("%.2f", rs.getDouble("total_bill"))).append("</td>")
                        .append("</tr>");
            }

            out.println("<div class='report-card'>");
            out.println("<h2 style='color:var(--primary);'>DAILY SUMMARY: " + today + "</h2>");

            out.println("<div class='stat-box'>");
            out.println("<div class='stat'><h3>Active Check-ins</h3><p>" + guestCount + "</p></div>");
            out.println("<div class='stat'><h3>Total Revenue</h3><p>Rs. " + String.format("%.2f", totalRevenue) + "</p></div>");
            out.println("</div>");

            out.println("<h3>Current In-House Guests</h3>");
            out.println("<table><tr><th>Guest</th><th>Room Wing</th><th>Departure</th><th>Total Bill</th></tr>");
            out.println(tableRows.toString());
            out.println("</table>");

            out.println("<br><a href='admin-dashboard.jsp' style='color:var(--primary); text-decoration:none; font-weight:bold;'>&larr; Back to Dashboard</a>");
            out.println("</div>");



            // ... inside doGet method ...
            out.println("<style>");
            out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }");
            out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; }");
// ... your existing styles ...

// ADD THIS: Print-Specific Styles
            out.println("@media print {");
            out.println("  body { background: white; padding: 0; }");
            out.println("  .no-print, .back-link { display: none !important; }"); // Hide buttons when printing
            out.println("  .report-card { box-shadow: none; border: 1px solid #eee; width: 100%; max-width: 100%; }");
            out.println("  .stat { background: white !important; color: black !important; border: 1px solid #0A2540; }");
            out.println("  .stat h3 { color: #0A2540 !important; }");
            out.println("}");
            out.println("</style></head><body>");

            out.println("<div class='report-card'>");
// Header with Logo for the PDF
            out.println("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:20px;'>");
            out.println("  <h2 style='color:var(--primary); margin:0;'>DAILY SUMMARY: " + today + "</h2>");
            out.println("  <img src='images/logo.png' style='height:40px;' class='print-logo'>");
            out.println("</div>");

// ... your existing stat boxes and table ...

// ADD THIS: The Print Button
            out.println("<div class='no-print' style='margin-top:30px; text-align:right;'>");
            out.println("  <button onclick='window.print()' style='padding:12px 25px; background:var(--accent); color:white; border:none; cursor:pointer; font-weight:bold; border-radius:4px;'>EXPORT AS PDF / PRINT</button>");
            out.println("</div>");

            out.println("<br><a href='admin-dashboard.jsp' class='back-link' style='color:var(--primary); text-decoration:none; font-weight:bold;'>&larr; Back to Dashboard</a>");
            out.println("</div></body></html>");

        } catch (SQLException e) { e.printStackTrace(); }
        out.println("</body></html>");
    }
}