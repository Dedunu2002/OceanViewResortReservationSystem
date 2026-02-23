package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class GetBillServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String resId = request.getParameter("id");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {
            // Using reservation_number as per your database screenshot
            String sql = "SELECT * FROM reservations WHERE reservation_number = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, resId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 1. Get Dates from Database
                String checkInStr = rs.getString("check_in");
                String checkOutStr = rs.getString("check_out");
                double totalBill = rs.getDouble("total_bill");

                // 2. Calculate Nights
                LocalDate cin = LocalDate.parse(checkInStr);
                LocalDate cout = LocalDate.parse(checkOutStr);
                long nights = ChronoUnit.DAYS.between(cin, cout);

                // Handle same-day check-outs to avoid division by zero
                if (nights <= 0) nights = 1;

                // 3. Calculate Nightly Rate
                double rate = totalBill / nights;

                // 4. Send back the JSON with all data
                String json = String.format(
                        "{\"id\": \"%s\", \"guestName\": \"%s\", \"roomType\": \"%s\", " +
                                "\"checkIn\": \"%s\", \"checkOut\": \"%s\", \"nights\": %d, " +
                                "\"rate\": %.2f, \"totalBill\": %.2f}",
                        rs.getString("reservation_number"),
                        rs.getString("guest_name"),
                        rs.getString("room_type"),
                        checkInStr, checkOutStr, nights, rate, totalBill
                );
                out.print(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}