package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
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
                out.println("<div class='form-container'>");
                out.println("<h2 style='color:var(--primary); margin-top:0;'>Modify Reservation #OR-" + id + "</h2>");

                out.println("<form action='update-reservation' method='POST'>");
                out.println("<input type='hidden' name='res_id' value='" + id + "'>");

                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>GUEST NAME</label>");
                out.println("<input type='text' value='" + rs.getString("guest_name") + "' disabled style='width:100%; padding:10px; margin-bottom:20px; background:#f9f9f9; border:1px solid #ddd;'>");

                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>CONTACT NUMBER</label>");
                out.println("<input type='text' name='contact_number' value='" + rs.getString("contact_number") + "' style='width:100%; padding:10px; margin-bottom:20px;'>");

                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>EMAIL ADDRESS</label>");
                out.println("<input type='email' name='email' value='" + rs.getString("email") + "' style='width:100%; padding:10px; margin-bottom:20px;'>");

                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>ADDRESS</label>");
                out.println("<input type='text' name='address' value='" + rs.getString("address") + "' style='width:100%; padding:10px; margin-bottom:20px;'>");


                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>ROOM TYPE</label>");
                out.println("<select name='room_type' style='width:100%; padding:10px; margin-bottom:20px;'>");
                String currentType = rs.getString("room_type");
                out.println("<option value='Standard' " + ("Standard".equals(currentType) ? "selected" : "") + ">Standard</option>");
                out.println("<option value='Deluxe' " + ("Deluxe".equals(currentType) ? "selected" : "") + ">Deluxe</option>");
                out.println("<option value='Luxury' " + ("Luxury".equals(currentType) ? "selected" : "") + ">Luxury</option>");
                out.println("</select>");

                // NEW: ROOM NUMBER FIELD
                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>ASSIGNED ROOM NUMBER</label>");
                out.println("<input type='text' name='room_number' value='" + rs.getString("room_number") + "' placeholder='e.g. D-101' style='width:100%; padding:10px; margin-bottom:20px; border:1px solid var(--accent);'>");

                out.println("<label style='display:block; font-size:12px; font-weight:bold; margin-bottom:5px;'>CHECK-OUT DATE</label>");
                out.println("<input type='date' name='check_out' value='" + rs.getString("check_out") + "' style='width:100%; padding:10px; margin-bottom:20px;'>");

                out.println("<button type='submit' style='width:100%; padding:15px; background:var(--primary); color:white; border:none; border-radius:4px; font-weight:bold; cursor:pointer;'>UPDATE RESERVATION</button>");
                out.println("<a href='view-reservations' style='display:block; text-align:center; margin-top:15px; font-size:13px; color:#666; text-decoration:none;'>Cancel</a>");
                out.println("</form>");
                out.println("</div></body></html>");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}