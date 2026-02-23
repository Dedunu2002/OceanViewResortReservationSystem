package com.oceanview.resort.dao;

import com.oceanview.resort.util.DBUtil;
import java.sql.*;

/**
 * Data Access Object for Room Settings.
 * Handles the "Master Data" of the resort (Pricing and Capacity).
 */
public class RatesDAO {

    /**
     * Updates the pricing and total available rooms for a specific category.
     * This affects all future bill calculations.
     */
    public boolean updateRoomSettings(String type, double price, int capacity) {
        String sql = "UPDATE room_rates SET price_per_night = ?, max_capacity = ? WHERE room_type = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, price);
            ps.setInt(2, capacity);
            ps.setString(3, type);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}