package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.ReservationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controller for guest bookings.
 * Tier 2: Handles the request and renders the UI.
 * Tier 3: Calls ReservationDAO for price calculations and DB insertion.
 */
public class BookingServlet extends HttpServlet {

    private ReservationDAO resDAO = new ReservationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Retrieve parameters from the web form
        String name = request.getParameter("guest_name");
        String contact = request.getParameter("contact_number");
        String email = request.getParameter("email");
        String addr = request.getParameter("address");
        String type = request.getParameter("room_type");
        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");

        // 2. Data Processing (Tier 3 Logic)
        // We use the DAO to calculate the bill based on rates in the DB
        double total = resDAO.calculateTotalBill(type, checkIn, checkOut);

        // We calculate nights here just for the display/UI part
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut));
        if (nights <= 0) nights = 1;

        // 3. Database Operation
        boolean success = resDAO.createReservation(name, contact, email, addr, type, checkIn, checkOut, total);

        // 4. Response Rendering (Tier 2 UI Logic)
        if (success) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html><head><title>Booking Confirmed | Ocean View</title>");
            out.println("<style>");
            out.println(":root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }");
            out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
            out.println(".card { background: white; padding: 50px; border-radius: 4px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); text-align: center; border-top: 5px solid var(--primary); max-width: 500px; width: 90%; }");
            out.println("h1 { color: var(--primary); letter-spacing: 2px; text-transform: uppercase; font-size: 22px; margin-bottom: 20px; }");
            out.println(".amount { font-size: 32px; color: var(--accent); font-weight: bold; margin: 25px 0; border-top: 1px solid #eee; border-bottom: 1px solid #eee; padding: 15px 0; }");
            out.println("p { color: #555; line-height: 1.6; margin: 5px 0; }");
            out.println(".btn { display: inline-block; margin-top: 25px; padding: 15px 30px; background: var(--primary); color: white; text-decoration: none; border-radius: 3px; font-size: 13px; letter-spacing: 1px; font-weight: bold; text-transform: uppercase; }");
            out.println("</style></head><body>");

            out.println("<div class='card'>");
            out.println("<h1>Reservation Confirmed</h1>");
            out.println("<p>Guest Name: <strong>" + name + "</strong></p>");
            out.println("<p>Room Category: " + type + "</p>");
            out.println("<p>Stay Duration: " + nights + " Night(s)</p>");

            out.println("<div class='amount'>TOTAL: Rs. " + String.format("%.2f", total) + "</div>");

            out.println("<p style='font-size: 12px;'>A confirmation email has been sent to " + email + "</p>");
            out.println("<a href='index.html' class='btn'>Make Another Booking</a>");
            out.println("</div>");

            out.println("</body></html>");
        } else {
            // If the database fails, send them back with an error
            response.sendRedirect("index.html?error=db_failure");
        }
    }
}