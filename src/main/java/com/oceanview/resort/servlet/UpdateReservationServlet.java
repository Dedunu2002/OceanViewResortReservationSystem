package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UpdateReservationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("res_id");
        String contact = request.getParameter("contact_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String type = request.getParameter("room_type");
        String check_out_str = request.getParameter("check_out");

        try (Connection conn = DBUtil.getConnection()) {
            // 1. Get the original check-in date to recalculate bill
            PreparedStatement ps1 = conn.prepareStatement("SELECT check_in FROM reservations WHERE reservation_number = ?");
            ps1.setString(1, id);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                LocalDate start = LocalDate.parse(rs.getString("check_in"));
                LocalDate end = LocalDate.parse(check_out_str);
                long nights = ChronoUnit.DAYS.between(start, end);
                if (nights <= 0) nights = 1;

                double price = type.equals("Luxury") ? 30000.0 : (type.equals("Deluxe") ? 15000.0 : 10000.0);
                double total = nights * price;

                // 2. Update the record
                PreparedStatement ps2 = conn.prepareStatement("UPDATE reservations SET contact_number=?, email=?, address=?, room_type=?, check_out=?, total_bill=? WHERE reservation_number=?");
                ps2.setString(1, contact);
                ps2.setString(2, email);
                ps2.setString(3, address);

                ps2.setString(4, type);
                ps2.setString(5, check_out_str);
                ps2.setDouble(6, total);
                ps2.setString(7, id);
                ps2.executeUpdate();
            }
            response.sendRedirect("view-reservations?msg=updated");
        } catch (Exception e) { e.printStackTrace(); }
    }
}