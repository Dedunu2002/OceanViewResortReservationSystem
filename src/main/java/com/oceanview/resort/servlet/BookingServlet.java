package com.oceanview.resort.servlet;
import com.oceanview.resort.util.SMSUtil;
import com.oceanview.resort.dao.ReservationDAO;
import com.oceanview.resort.dao.RoomDAO;
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
    private RoomDAO roomDAO = new RoomDAO();

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

        String assignedRoomNumber = roomDAO.assignAndOccupyRoom(type);
        if (assignedRoomNumber == null) {
            response.sendRedirect("index.html?error=no_available_rooms");
            return;
        }

        // 2. Data Processing (Tier 3 Logic)
        // We use the DAO to calculate the bill based on rates in the DB
        double total = resDAO.calculateTotalBill(type, checkIn, checkOut);

        // We calculate nights here just for the display/UI part
        long nights = ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut));
        if (nights <= 0) nights = 1;

        // 3. Database Operation
        boolean success = resDAO.createReservation(name, contact, email, addr, type, assignedRoomNumber, checkIn, checkOut, total);

        // 4. Response Rendering (Tier 2 UI Logic)
        if (success) {

            boolean smsTriggered = SMSUtil.sendBookingSMS(name, contact, checkIn, checkOut, total);

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

            // Notification Toast Styling
            out.println(".toast { position: fixed; top: 20px; right: 20px; background: white; padding: 15px 25px; border-radius: 8px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); border-left: 5px solid var(--success); display: flex; align-items: center; gap: 15px; transform: translateX(120%); transition: 0.5s ease-in-out; }");
            out.println(".toast.show { transform: translateX(0); }");
            out.println(".toast i { color: var(--success); font-size: 20px; }");

            out.println("</style></head><body>");

            // THE POPUP NOTIFICATION
            if (smsTriggered) {
                out.println("<div id='smsToast' class='toast'>");
                out.println("<i class='fa-solid fa-circle-check'></i>");
                out.println("<div><strong style='display:block; font-size:12px;'>SYSTEM ALERT</strong>");
                out.println("<span style='font-size:13px; color:#555;'>SMS Notification successfully sent to " + contact + "</span></div>");
                out.println("</div>");
            }

            out.println("<div class='card'>");
            out.println("<h1>Reservation Confirmed</h1>");
            out.println("<p>Guest Name: <strong>" + name + "</strong></p>");
            out.println("<p>Room Assigned: <strong>" + assignedRoomNumber + "</strong> (" + type + ")</p>");
            out.println("<p>Room Category: " + type + "</p>");
            out.println("<p>Stay Duration: " + nights + " Night(s)</p>");

            out.println("<div class='amount'>TOTAL: Rs. " + String.format("%.2f", total) + "</div>");

            out.println("<p style='font-size: 12px;'>A confirmation email has been sent to " + email + "</p>");
            out.println("<a href='index.html' class='btn'>Make Another Booking</a>");
            out.println("</div>");

            // Script to show the toast
            out.println("<script>");
            out.println("setTimeout(() => { document.getElementById('smsToast').classList.add('show'); }, 500);");
            out.println("setTimeout(() => { document.getElementById('smsToast').classList.remove('show'); }, 5000);");
            out.println("</script>");

            out.println("</body></html>");
        } else {
            // If the database fails, send them back with an error
            response.sendRedirect("index.html?error=db_failure");
        }
    }
}