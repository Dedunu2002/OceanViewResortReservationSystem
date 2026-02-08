package com.oceanview.resort;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class EditReservationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM reservations WHERE reservation_number = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                out.println("<html><head><title>Edit Reservation | Ocean View</title>");
                out.println("<style>:root{--primary:#0A2540; --accent:#C9A24D; --bg:#F5F7FA;} body{font-family:'Segoe UI',sans-serif; background:var(--bg); padding:50px;} .form-container{max-width:500px; margin:0 auto; background:white; padding:40px; border-radius:4px; border-top:5px solid var(--primary); box-shadow:0 10px 30px rgba(0,0,0,0.05);}</style></head><body>");
                out.println("<div class='form-container'><h2>Update Reservation #" + id + "</h2>");
                out.println("<form action='update-reservation' method='POST'>");
                out.println("<input type='hidden' name='res_id' value='" + id + "'>");
                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>GUEST NAME</label>");
                out.println("<input type='text' name='guest_name' value='" + rs.getString("guest_name") + "' style='width:100%; padding:10px; margin-bottom:20px;' readonly>");
                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>ROOM TYPE</label>");
                out.println("<select name='room_type' style='width:100%; padding:10px; margin-bottom:20px;'>");
                out.println("<option value='Standard' " + (rs.getString("room_type").equals("Standard") ? "selected" : "") + ">Standard</option>");
                out.println("<option value='Deluxe' " + (rs.getString("room_type").equals("Deluxe") ? "selected" : "") + ">Deluxe</option>");
                out.println("<option value='Luxury' " + (rs.getString("room_type").equals("Luxury") ? "selected" : "") + ">Luxury</option>");
                out.println("</select>");
                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>CHECK-OUT DATE</label>");
                out.println("<input type='date' name='check_out' value='" + rs.getString("check_out") + "' style='width:100%; padding:10px; margin-bottom:20px;'>");
                out.println("<button type='submit' style='width:100%; padding:15px; background:var(--accent); color:white; border:none; cursor:pointer; font-weight:bold;'>SAVE CHANGES</button>");
                out.println("</form></div></body></html>");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}