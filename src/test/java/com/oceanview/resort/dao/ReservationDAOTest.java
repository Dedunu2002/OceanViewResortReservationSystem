package com.oceanview.resort.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ReservationDAOTest {
    @Test
    void testCreateReservation() {
        ReservationDAO dao = new ReservationDAO();
        // Must match your 9 parameters: name, contact, email, addr, type, room#, in, out, total
        boolean result = dao.createReservation(
                "Gaya", "0789878909", "gaya@gmail.com", "Colombo",
                "Deluxe", "D101", "2026-03-10", "2026-03-15", 50000.00
        );
        assertTrue(result, "Reservation should be saved successfully.");
    }
}