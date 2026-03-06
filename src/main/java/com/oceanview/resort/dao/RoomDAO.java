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

    // This holds the actual record of a specific room
    public static class Room {
        public String roomNumber;
        public String roomType;
        public String status;

        public Room(String roomNumber, String roomType, String status) {
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.status = status;
        }
    }

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
     * Fetches all rooms from the database for the View Rooms table.
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    // --- 3. NEW METHOD: DELETE A ROOM ---
    /**
     * Deletes a specific room record using the unique Room Number.
     */
    public boolean deleteRoom(String roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            e.printStackTrace();
            return false;
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