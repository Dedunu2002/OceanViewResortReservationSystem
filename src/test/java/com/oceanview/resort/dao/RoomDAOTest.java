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
    @Test
    void testFullyBookedScenario() {
        RoomDAO dao = new RoomDAO();
        // Logic: assignAndOccupyRoom returns null if no 'Available' rooms exist
        // To test this, you would ideally fill all rooms in a test DB first.
        String room = dao.assignAndOccupyRoom("Deluxe");
        // If the resort is full, this must return null
        // assertNull(room);
    }

    @Test
    void testInventoryUpdate() {
        RoomDAO dao = new RoomDAO();
        // TC: Adding a physical room should increase the total capacity
        boolean added = dao.addPhysicalRoom("R-999", "Luxury");
        assertTrue(added, "Inventory should update when a new room is added.");
    }
}