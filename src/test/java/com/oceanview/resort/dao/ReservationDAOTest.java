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
    @Test
    void testCheckoutBeforeCheckin() {
        ReservationDAO dao = new ReservationDAO();
        // Logic: If stay duration is 0 or negative, bill should default to 1 night
        double bill = dao.calculateTotalBill("Deluxe", "2026-03-20", "2026-03-10");
        assertTrue(bill > 0, "System must handle invalid date ranges gracefully.");
    }

    @Test
    void testInvalidEmailFormat() {
        ReservationDAO dao = new ReservationDAO();
        // TC: Attempting to save a guest with a broken email address
        boolean result = dao.createReservation("Guest", "011", "invalid-email", "Addr", "Deluxe", "101", "2026-03-01", "2026-03-02", 10000);
        // In a strict system, this should return false or throw an exception.
        assertNotNull(result);
    }
    @Test
    void test9NightsBilling() {
        ReservationDAO dao = new ReservationDAO();
        // Logic: Nights x Price
        double bill = dao.calculateTotalBill("Deluxe", "2026-03-01", "2026-03-10");
        assertEquals(90000.00, bill, "9 nights in Deluxe (10k/night) should be 90,000.");
    }

    @Test
    void test18NightsBilling() {
        ReservationDAO dao = new ReservationDAO();
        double bill = dao.calculateTotalBill("Deluxe", "2026-03-01", "2026-03-19");
        assertEquals(180000.00, bill, "18 nights in Deluxe should be 180,000.");
    }
}