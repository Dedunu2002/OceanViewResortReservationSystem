package com.oceanview.resort.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RatesDAOTest {
    @Test
    void testAddRoomCategory() {
        RatesDAO dao = new RatesDAO();
        // Tests adding a new type of room to the resort settings
        boolean result = dao.addRoomCategory("Suite", 25000.00, 5);
        assertTrue(result, "Should successfully add a new room category.");
    }
}