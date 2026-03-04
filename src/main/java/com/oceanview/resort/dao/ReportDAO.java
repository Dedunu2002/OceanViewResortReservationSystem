package com.oceanview.resort.dao;

import com.oceanview.resort.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    // Simple class to hold the daily summary data
    public static class DailySummary {
        public double totalRevenue;
        public int activeBookings;
        public List<GuestRow> guests;

        public DailySummary() {
            this.guests = new ArrayList<>();
        }
    }

    public static class GuestRow {
        public String name, roomType, checkIn, checkOut;
        public double bill;

        public GuestRow(String name, String roomType, String checkIn, String checkOut, double bill) {
            this.name = name;
            this.roomType = roomType;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            this.bill = bill;
        }
    }

    /**
     * Compiles all financial and guest data for a specific date.
     */
    public DailySummary getDailyReport(LocalDate date) {
        DailySummary summary = new DailySummary();
        String dateStr = date.toString();

        try (Connection conn = DBUtil.getConnection()) {
            // 1. Calculate Total Revenue and Booking Count for the day
            String statsSql = "SELECT SUM(total_bill), COUNT(*) FROM reservations WHERE check_in <= ? AND check_out > ?";
            try (PreparedStatement ps = conn.prepareStatement(statsSql)) {
                ps.setString(1, dateStr);
                ps.setString(2, dateStr);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    summary.totalRevenue = rs.getDouble(1);
                    summary.activeBookings = rs.getInt(2);
                }
            }

            // 2. Fetch Detailed Guest List
            String listSql = "SELECT guest_name, room_type, check_in, check_out, total_bill FROM reservations WHERE check_in <= ? AND check_out > ?";
            try (PreparedStatement ps = conn.prepareStatement(listSql)) {
                ps.setString(1, dateStr);
                ps.setString(2, dateStr);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    summary.guests.add(new GuestRow(
                            rs.getString("guest_name"),
                            rs.getString("room_type"),
                            rs.getString("check_in"),
                            rs.getString("check_out"),
                            rs.getDouble("total_bill")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summary;
    }
}