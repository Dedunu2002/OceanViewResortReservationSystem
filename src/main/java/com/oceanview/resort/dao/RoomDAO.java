package com.oceanview.resort.dao;

import com.oceanview.resort.util.DBUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Room Inventory.
 * Encapsulates complex logic for calculating live availability.
 */
public class RoomDAO {

    // A simple inner class to hold room data
    public static class RoomInventory {
        public String type;
        public int occupied;
        public int total;
        public int vacant;

        public RoomInventory(String type, int occupied, int total) {
            this.type = type;
            this.occupied = occupied;
            this.total = total;
            this.vacant = Math.max(0, total - occupied);
        }
    }

    /**
     * Fetches live occupancy stats by comparing room capacities
     * against active reservations for today.
     */
    public List<RoomInventory> getLiveInventory() {
        List<RoomInventory> report = new ArrayList<>();
        LocalDate today = LocalDate.now();

        String sql = "SELECT rr.room_type, rr.max_capacity, " +
                "(SELECT COUNT(*) FROM reservations res " +
                " WHERE res.room_type = rr.room_type " +
                " AND res.check_in <= ? AND res.check_out > ?) as occupied_count " +
                "FROM room_rates rr";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(today));
            ps.setDate(2, Date.valueOf(today));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.add(new RoomInventory(
                            rs.getString("room_type"),
                            rs.getInt("occupied_count"),
                            rs.getInt("max_capacity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    public boolean addPhysicalRoom(String roomNumber, String roomType) {
        // We set status to 'Available' by default for new rooms
        String sql = "INSERT INTO rooms (room_number, room_type, status) VALUES (?, ?, 'Available')";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            ps.setString(2, roomType);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String assignAndOccupyRoom(String roomType) {
        String roomNumber = null;
        // 1. Find the first available room of this type
        String findSql = "SELECT room_number FROM rooms WHERE room_type = ? AND status = 'Available' LIMIT 1";
        // 2. Mark it as occupied
        String updateSql = "UPDATE rooms SET status = 'Occupied' WHERE room_number = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement psFind = conn.prepareStatement(findSql)) {
                psFind.setString(1, roomType);
                ResultSet rs = psFind.executeQuery();

                if (rs.next()) {
                    roomNumber = rs.getString("room_number");

                    try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                        psUpdate.setString(1, roomNumber);
                        psUpdate.executeUpdate();
                    }
                }
                conn.commit(); // Save changes
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomNumber; // Returns null if no rooms are available
    }
}