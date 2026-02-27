package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.ReportDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class DailySummaryServlet extends HttpServlet {

    private ReportDAO reportDAO = new ReportDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        LocalDate today = LocalDate.now();
        // Tier 2 calls Tier 3
        ReportDAO.DailySummary summary = reportDAO.getDailyReport(today);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Daily Report | Ocean View</title>");
        out.println("<style>");
        out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; }");
        out.println(".report-card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); }");
        out.println(".stat-box { display: inline-block; width: 45%; padding: 20px; background: var(--primary); color: white; border-radius: 4px; margin-right: 10px; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 30px; }");
        out.println("th { text-align: left; border-bottom: 2px solid var(--primary); padding: 10px; }");
        out.println("td { padding: 10px; border-bottom: 1px solid #eee; }");
        out.println("@media print { .no-print { display: none; } }");
        out.println("</style></head><body>");

        out.println("<div class='report-card'>");
        out.println("<h2>DAILY SUMMARY: " + today + "</h2>");

        // Display Totals from DAO
        out.println("<div class='stat-box'><h3>Total Revenue</h3><p>Rs. " + String.format("%.2f", summary.totalRevenue) + "</p></div>");
        out.println("<div class='stat-box'><h3>Active Guests</h3><p>" + summary.activeBookings + " Bookings</p></div>");

        // Display Table
        out.println("<table><thead><tr><th>Guest Name</th><th>Room</th><th>Stay Dates</th><th>Total Bill</th></tr></thead><tbody>");
        for (ReportDAO.GuestRow guest : summary.guests) {
            out.println("<tr>");
            out.println("<td>" + guest.name + "</td>");
            out.println("<td>" + guest.roomType + "</td>");
            out.println("<td>" + guest.checkIn + " to " + guest.checkOut + "</td>");
            out.println("<td>Rs. " + String.format("%.2f", guest.bill) + "</td>");
            out.println("</tr>");
        }
        out.println("</tbody></table>");

        out.println("<div class='no-print' style='margin-top:20px;'>");
        out.println("<button onclick='window.print()'>Generate Reports</button>");
        out.println("<br><br><a href='admin-dashboard.html'>&larr; Back to Dashboard</a>");
        out.println("</div></div></body></html>");
    }
}