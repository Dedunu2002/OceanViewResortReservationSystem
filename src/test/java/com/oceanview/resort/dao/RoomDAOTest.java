package com.oceanview.resort.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RoomDAOTest {
    @Test
    void testAssignAndOccupyRoom() {
        RoomDAO dao = new RoomDAO();
        // Returns a String (e.g., "R01") if a room is available
        String roomNumber = dao.assignAndOccupyRoom("Deluxe");
        assertNotNull(roomNumber, "System should return an available room number.");
    }
}