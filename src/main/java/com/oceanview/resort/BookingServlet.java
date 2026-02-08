package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Updated Parameter Names to match the new luxury HTML form
        String name = request.getParameter("guest_name");
        String type = request.getParameter("room_type");
        String check_in_str = request.getParameter("check_in");
        String check_out_str = request.getParameter("check_out");

        // These are optional depending on your latest form fields
        String addr = request.getParameter("address") != null ? request.getParameter("address") : "N/A";
        String contact = request.getParameter("contact") != null ? request.getParameter("contact") : "N/A";

        // 2. Safety Guard: Prevent NullPointerException
        if (check_in_str == null || check_out_str == null || check_in_str.isEmpty() || check_out_str.isEmpty()) {
            response.sendRedirect("index.html?error=invalid_dates");
            return;
        }

        try {
            // 3. Parsing Dates and Calculating Bill
            LocalDate start = LocalDate.parse(check_in_str);
            LocalDate end = LocalDate.parse(check_out_str);
            long nights = ChronoUnit.DAYS.between(start, end);

            // Validation: Must stay at least 1 night
            if (nights <= 0) nights = 1;

            double price = 10000.0; // Default Standard
            if ("Deluxe".equals(type)) price = 15000.0;
            if ("Luxury".equals(type)) price = 30000.0;

            double total = nights * price;

            try (Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO reservations (guest_name, address, contact_number, room_type, check_in, check_out, total_bill) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, addr);
                ps.setString(3, contact);
                ps.setString(4, type);
                ps.setString(5, check_in_str);
                ps.setString(6, check_out_str);
                ps.setDouble(7, total);
                ps.executeUpdate();

                // 4. Refined Luxury Success Page
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<html><head><title>Reservation Confirmed | Ocean View</title>");
                out.println("<style>");
                out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }");
                out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
                out.println(".card { background: white; padding: 50px; border-radius: 4px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); text-align: center; border-top: 5px solid var(--primary); max-width: 500px; }");
                out.println("h1 { color: var(--primary); letter-spacing: 2px; text-transform: uppercase; font-size: 22px; }");
                out.println(".amount { font-size: 28px; color: var(--accent); font-weight: bold; margin: 20px 0; }");
                out.println(".btn { display: inline-block; padding: 15px 30px; background: var(--primary); color: white; text-decoration: none; border-radius: 3px; font-size: 13px; letter-spacing: 1px; font-weight: bold; text-transform: uppercase; }");
                out.println("</style></head><body>");
                out.println("<div class='card'>");
                out.println("<h1>Reservation Confirmed</h1>");
                out.println("<p>Guest: <strong>" + name + "</strong></p>");
                out.println("<p>Stay Duration: " + nights + " Night(s)</p>");
                out.println("<div class='amount'>Rs. " + String.format("%.2f", total) + "</div>");
                out.println("<a href='receptionist-dashboard.jsp' class='btn'>Back to Dashboard</a>");
                out.println("</div></body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.html?error=system");
        }
    }
}