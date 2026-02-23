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
}