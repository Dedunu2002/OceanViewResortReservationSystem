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
        double price = type.equals("Deluxe") ? 150.0 : 100.0;
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

            response.getWriter().println("<h1>Reservation Success!</h1><p>Total Bill: $" + total + "</p>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}