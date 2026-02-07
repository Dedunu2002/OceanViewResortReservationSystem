package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("guestName");
        String addr = request.getParameter("address");
        String contact = request.getParameter("contact");
        String type = request.getParameter("roomType");
        String in = request.getParameter("checkIn");
        String out = request.getParameter("checkOut");

        // Calculate Bill
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(in), LocalDate.parse(out));
        double price = type.equals("Deluxe") ? 15000.0 : 10000.0;
        double total = nights * price;

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO reservations (guest_name, address, contact_number, room_type, check_in, check_out, total_bill) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, addr);
            ps.setString(3, contact);
            ps.setString(4, type);
            ps.setString(5, in);
            ps.setString(6, out);
            ps.setDouble(7, total);
            ps.executeUpdate();

            // Refined Success Page Output
            response.setContentType("text/html");
            response.getWriter().println("<html><head><title>Success</title>");
            response.getWriter().println("<style>body{font-family:'Times New Roman'; text-align:center; padding:50px; background:#f0f8ff;} .card{background:white; padding:30px; border-radius:10px; box-shadow:0 4px 8px rgba(0,0,0,0.1); display:inline-block;} .btn{display:inline-block; margin-top:20px; padding:10px 20px; background:#0047ab; color:white; text-decoration:none; border-radius:5px;}</style>");
            response.getWriter().println("</head><body><div class='card'>");
            response.getWriter().println("<h1 style='color:green;'>Reservation Confirmed!</h1>");
            response.getWriter().println("<p>Thank you, <strong>" + name + "</strong>. Your booking has been saved.</p>");
            response.getWriter().println("<h3>Total Amount Due: Rs." + total + "</h3>");
            response.getWriter().println("<a href='index.html' class='btn'>Make Another Booking</a>");
            response.getWriter().println("</div></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}