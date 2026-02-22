package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Retrieve parameters from the form
        String name = request.getParameter("guest_name");
        String contact = request.getParameter("contact_number");
        String email = request.getParameter("email");
        String addr = request.getParameter("address");
        String type = request.getParameter("room_type");
        String check_in_str = request.getParameter("check_in");
        String check_out_str = request.getParameter("check_out");

        // 2. Safety Guard: Prevent null or empty date strings
        if (check_in_str == null || check_out_str == null || check_in_str.isEmpty() || check_out_str.isEmpty()) {
            response.sendRedirect("index.html?error=invalid_dates");
            return;
        }

        try {
            // 3. Parse dates and calculate the stay duration
            LocalDate start = LocalDate.parse(check_in_str);
            LocalDate end = LocalDate.parse(check_out_str);
            long nights = ChronoUnit.DAYS.between(start, end);
            if (nights <= 0) nights = 1;

            try (Connection conn = DBUtil.getConnection()) {

                // --- 4. FETCH DYNAMIC PRICE AND CAPACITY FROM DATABASE ---
                double price = 0;
                int maxCapacity = 0;

                String rateSql = "SELECT price_per_night, max_capacity FROM room_rates WHERE room_type = ?";
                PreparedStatement ratePs = conn.prepareStatement(rateSql);
                ratePs.setString(1, type);
                ResultSet rateRs = ratePs.executeQuery();

                if (rateRs.next()) {
                    price = rateRs.getDouble("price_per_night");
                    maxCapacity = rateRs.getInt("max_capacity");
                } else {
                    // Fallback if the room type isn't found in the new table
                    response.sendRedirect("index.html?error=invalid_room_type");
                    return;
                }

                // --- 5. ROOM AVAILABILITY CHECK LOGIC ---
                // Uses the maxCapacity fetched from the database
                String checkSql = "SELECT COUNT(*) FROM reservations " +
                        "WHERE room_type = ? " +
                        "AND (check_in < ? AND check_out > ?)";

                PreparedStatement checkPs = conn.prepareStatement(checkSql);
                checkPs.setString(1, type);
                checkPs.setString(2, check_out_str);
                checkPs.setString(3, check_in_str);

                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    int occupiedRooms = rs.getInt(1);
                    if (occupiedRooms >= maxCapacity) {
                        // Redirect with both error and type for the JavaScript alert
                        response.sendRedirect("index.html?error=no_availability&type=" + type);
                        return;
                    }
                }

                // --- 6. PROCEED WITH BOOKING ---
                double total = nights * price;
                String sql = "INSERT INTO reservations (guest_name, contact_number, email, address, room_type, check_in, check_out, total_bill) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.setString(4, addr);
                ps.setString(5, type);
                ps.setString(6, check_in_str);
                ps.setString(7, check_out_str);
                ps.setDouble(8, total);
                ps.executeUpdate();

                // 7. Generate Success Response
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