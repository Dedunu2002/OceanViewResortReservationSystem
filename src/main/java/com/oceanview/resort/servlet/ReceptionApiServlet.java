package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class ReceptionApiServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. Security check - ensure only RECEPTIONIST can access this
        HttpSession session = request.getSession(false);
        if (session == null || !"RECEPTIONIST".equals(session.getAttribute("role"))) {
            response.setStatus(401);
            return;
        }

        // 2. Initialize variables
        int todayArrivals = 0;
        int todayDepartures = 0;
        int activeBookings = 0;
        int totalRooms = 50;
        int availableRooms = 0;
        int occupied = 0;


        // 3. Business Logic
        try (Connection conn = DBUtil.getConnection()) {

            // Query 1: Today's Arrivals
            Statement st1 = conn.createStatement();
            ResultSet rs1 = st1.executeQuery("SELECT COUNT(*) AS arrival_count FROM reservations WHERE check_in = CURRENT_DATE");
            if (rs1.next()) {
                todayArrivals = rs1.getInt("arrival_count");
            }

            // Query 2: Today's Departures (Check-outs)
            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("SELECT COUNT(*) AS departure_count FROM reservations WHERE check_out = CURRENT_DATE");
            if (rs2.next()) {
                todayDepartures = rs2.getInt("departure_count");
            }

            // Query 3: Active Bookings (In-House Guests)
            Statement st3 = conn.createStatement();
            ResultSet rs3 = st3.executeQuery("SELECT COUNT(*) AS active_count FROM reservations WHERE check_in <= CURRENT_DATE AND check_out >= CURRENT_DATE");
            if (rs3.next()) {
                activeBookings = rs3.getInt("active_count");
            }

            Statement st4 = conn.createStatement();
            ResultSet rs4 = st4.executeQuery("SELECT COUNT(*) AS occupied_count FROM reservations");
            if (rs4.next()) {
                occupied = rs4.getInt("occupied_count");
            }



            // Calculate Availability
            availableRooms = totalRooms - occupied;



        } catch (Exception e) {
            e.printStackTrace();
        }



        // 4. Return JSON (Keys match receptionist-dashboard.html)
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"todayArrivals\":" + todayArrivals + ", \"todayDepartures\":" + todayDepartures + ", \"activeBookings\":" + activeBookings + ",  \"availableRooms\":" + availableRooms + ", \"totalRooms\":" + totalRooms +  "}");

        out.flush();
    }
}