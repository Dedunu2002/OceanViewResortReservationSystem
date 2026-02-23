package com.oceanview.resort.dao;

import com.oceanview.resort.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    /**
     * Calculates the total bill by fetching the rate from the DB and
     * calculating the stay duration.
     */
    public double calculateTotalBill(String roomType, String checkIn, String checkOut) {
        LocalDate start = LocalDate.parse(checkIn);
        LocalDate end = LocalDate.parse(checkOut);
        long nights = ChronoUnit.DAYS.between(start, end);
        if (nights <= 0) nights = 1;

        double pricePerNight = 0;
        String sql = "SELECT price_per_night FROM room_rates WHERE room_type = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pricePerNight = rs.getDouble("price_per_night");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nights * pricePerNight;
    }

    /**
     * Tier 3 logic to save a new reservation.
     */
    public boolean createReservation(String name, String contact, String email, String address,
                                     String type, String checkIn, String checkOut, double total) {
        String sql = "INSERT INTO reservations (guest_name, contact_number, email, address, room_type, check_in, check_out, total_bill) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setString(5, type);
            ps.setString(6, checkIn);
            ps.setString(7, checkOut);
            ps.setDouble(8, total);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a reservation by ID.
     */
    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE reservation_number = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateReservation(int id, String contact, String email, String address,
                                     String type, String checkOut) {

        String fetchSql = "SELECT check_in FROM reservations WHERE reservation_number = ?";
        String updateSql = "UPDATE reservations SET contact_number=?, email=?, address=?, " +
                "room_type=?, check_out=?, total_bill=? WHERE reservation_number=?";

        try (Connection conn = DBUtil.getConnection()) {
            // 1. Fetch original check-in to recalculate bill
            String checkIn = "";
            try (PreparedStatement psFetch = conn.prepareStatement(fetchSql)) {
                psFetch.setInt(1, id);
                try (ResultSet rs = psFetch.executeQuery()) {
                    if (rs.next()) {
                        checkIn = rs.getString("check_in");
                    }
                }
            }

            // 2. Recalculate bill using our existing DAO method
            double newTotal = calculateTotalBill(type, checkIn, checkOut);

            // 3. Perform the update
            try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                psUpdate.setString(1, contact);
                psUpdate.setString(2, email);
                psUpdate.setString(3, address);
                psUpdate.setString(4, type);
                psUpdate.setString(5, checkOut);
                psUpdate.setDouble(6, newTotal);
                psUpdate.setInt(7, id);

                return psUpdate.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}