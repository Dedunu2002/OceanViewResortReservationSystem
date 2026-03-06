package com.oceanview.resort.servlet;

import com.oceanview.resort.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

public class ViewReservationsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String role = (String) session.getAttribute("role");
        String user = (String) session.getAttribute("user");
        String searchQuery = request.getParameter("searchQuery");
        String homePage = "ADMIN".equals(role) ? "admin-dashboard.html" : "receptionist-dashboard.html";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Manage Reservations | Ocean View</title>");
        out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css'>");
        out.println("<style>");
        out.println(":root { --primary: #0A2540; --accent: #C9A24D; --text-muted: #64748b; --bg: #f8fafc; --gold-dark: #A57B0A; }");
        out.println("body { font-family: 'Segoe UI', sans-serif; background: var(--bg); margin: 0; display: flex; }");

        // Side Navigation Styling
        out.println(".sidebar {\n" +
                "            width: 300px;\n" +
                "            background: var(--primary);\n" +
                "            color: white;\n" +
                "            height: 100vh;\n" +
                "            position: fixed;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            box-shadow: 10px 0 30px rgba(0,0,0,0.1);\n" +
                "            z-index: 100;\n" +
                "        }");
        out.println(".logo-container {\n" +
                "            padding: 40px 30px;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            gap: 15px;\n" +
                "            border-bottom: 1px solid rgba(255,255,255,0.05);\n" +
                "        }");
        out.println(".logo-icon {\n" +
                "            width: 45px;\n" +
                "            height: 45px;\n" +
                "            background: linear-gradient(135deg, var(--accent), #e2c275);\n" +
                "            border-radius: 10px;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            font-size: 1.5rem;\n" +
                "            color: var(--primary);\n" +
                "            box-shadow: 0 4px 15px var(--accent-glow);\n" +
                "        }");
        out.println(".logo-text {\n" +
                "            font-family: 'Playfair Display', serif; /* Elegant Serif */\n" +
                "            font-weight: 700;\n" +
                "            letter-spacing: 1px;\n" +
                "            font-size: 1.1rem;\n" +
                "            color: var(--accent);\n" +
                "            text-transform: uppercase;\n" +
                "        }");
        out.println(".role-card-container {\n" +
                "            padding: 20px 30px;\n" +
                "        }");
        out.println(".role-card {\n" +
                "            background: var(--secondary);\n" +
                "            padding: 15px;\n" +
                "            border-radius: 12px;\n" +
                "            border: 1px solid rgba(201, 162, 77, 0.2);\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            gap: 12px;\n" +
                "        }");
        out.println(".status-pulse {\n" +
                "            width: 8px;\n" +
                "            height: 8px;\n" +
                "            background: #48bb78;\n" +
                "            border-radius: 50%;\n" +
                "            box-shadow: 0 0 8px #48bb78;\n" +
                "            animation: pulse 2s infinite;\n" +
                "        }");
        out.println(".role-info span {\n" +
                "            display: block;\n" +
                "            font-size: 0.75rem;\n" +
                "            color: var(--accent);\n" +
                "            text-transform: uppercase;\n" +
                "            letter-spacing: 1px;\n" +
                "            font-weight: 600;\n" +
                "        }");
        out.println(".nav-scroll {\n" +
                "            flex-grow: 1;\n" +
                "            overflow-y: auto;\n" +
                "            padding: 10px 20px;\n" +
                "        }");
        out.println(".section-label {\n" +
                "            font-size: 0.7rem;\n" +
                "            color: var(--text-muted);\n" +
                "            text-transform: uppercase;\n" +
                "            letter-spacing: 2px;\n" +
                "            margin: 25px 15px 10px;\n" +
                "            display: block;\n" +
                "        }");
        out.println(".nav-link {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            gap: 15px;\n" +
                "            padding: 12px 15px;\n" +
                "            color: rgba(255,255,255,0.7);\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 8px;\n" +
                "            transition: 0.3s;\n" +
                "            margin-bottom: 5px;\n" +
                "            font-size: 0.9rem;\n" +
                "        }");
        out.println(".nav-link i {\n" +
                "            width: 20px;\n" +
                "            text-align: center;\n" +
                "            font-size: 1.1rem;\n" +
                "            color: var(--accent);\n" +
                "        }");
        out.println(".nav-link:hover, .nav-link.active {\n" +
                "            background: rgba(201, 162, 77, 0.1);\n" +
                "            color: var(--accent);\n" +
                "        }");
        out.println(".nav-link.active {\n" +
                "            background: var(--accent);\n" +
                "            color: var(--primary);\n" +
                "        }");
        out.println(".nav-link.active i { color: var(--primary); }");
        out.println("@keyframes pulse {\n" +
                "            0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(72, 187, 120, 0.7); }\n" +
                "            70% { transform: scale(1); box-shadow: 0 0 0 6px rgba(72, 187, 120, 0); }\n" +
                "            100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(72, 187, 120, 0); }\n" +
                "        }");




        // Main Content Area
        out.println(".main-content { margin-left: 260px; flex: 1; padding: 40px; }");
        out.println(".header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }");
        out.println(".header h2 { margin: 0; color: var(--primary); font-weight: 300; letter-spacing: 1px; }");

        // Luxury Table Styling
        out.println(".table-container { background: white; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.05); overflow: hidden; }");
        out.println("table { width: 100%; border-collapse: collapse; }");
        out.println("th { background: #fdfdfd; color: var(--text-muted); font-size: 11px; text-transform: uppercase; letter-spacing: 1px; padding: 20px; text-align: left; border-bottom: 1px solid #eee; }");
        out.println("td { padding: 20px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #334155; }");
        out.println("tr:hover { background-color: #fcfcfd; }");

        // Search & Filter
        out.println(".search-form { display: flex; gap: 10px; margin-bottom: 25px; }");
        out.println(".search-input { padding: 12px 20px; border: 1px solid #e2e8f0; border-radius: 30px; width: 350px; outline: none; transition: 0.3s; }");
        out.println(".search-input:focus { border-color: var(--accent); }");
        out.println(".btn-filter { background: var(--primary); color: white; border: none; padding: 10px 25px; border-radius: 30px; cursor: pointer; font-weight: 600; }");

        // Action Buttons
        out.println(".action-link { text-decoration: none; font-weight: 700; font-size: 11px; margin-right: 12px; letter-spacing: 0.5px; }");
        out.println(".edit { color: var(--primary); }");
        out.println(".checkout { color: #16a34a; }");
        out.println(".cancel { color: #dc2626; }");
        out.println(".print { color: var(--gold-dark); }");

        // Status Badges
        out.println(".badge { padding: 5px 12px; border-radius: 20px; font-size: 10px; font-weight: bold; text-transform: uppercase; }");
        out.println(".badge-active { background: #ecfdf5; color: #059669; }");
        out.println(".badge-out { background: #f1f5f9; color: #64748b; }");

        out.println("</style></head><body>");

        // SIDE NAVIGATION
        out.println("<div class='sidebar'>");
        out.println("<div class='logo-container'>");
        out.println("<img class='logo-icon' src='images/logo.png' >");
        out.println("<div class='logo-text'>Ocean View<br><span style='font-size: 0.7rem; opacity: 0.8;'>Resort & Spa</span></div>");
        out.println("</div>");

        out.println("<div class='role-card-container'>");
        out.println("<div class='role-card'>");
        out.println("<div class='status-pulse'></div>");
        out.println(" <div class='role-info'>");
        out.println("<span><strong>" + user + " (" + role + ")</strong></span>");

        out.println("</div>");
        out.println("</div>");
        out.println("</div>");

        out.println("<div class='nav-scroll'>");
        out.println("<a href='" + homePage + "' class='nav-link'><i class='fa-solid fa-gauge'></i> Dashboard</a>");

        out.println("<span class='section-label'>System Oversight</span>");


        out.println("<a href='index.html' class='nav-link'><i class='fa-solid fa-calendar-plus'></i> Add New Reservation</a>");

        out.println("<a href='view-reservations' class='nav-link active'><i class='fa-solid fa-rectangle-list'></i> View Reservations </a>");
        if ("ADMIN".equals(role)) {
            out.println("<a href='daily-summary' class='nav-link'><i class='fa-solid fa-file-invoice-dollar'></i> Daily Summary</a>");
        }
        if ("ADMIN".equals(role)) {
            out.println("<a href='update-rates.html' class='nav-link'><i class='fa-solid fa-tags'></i> Room Pricing</a>");
        }


        out.println("<span class='section-label'>Operations</span>");
        if ("ADMIN".equals(role)) {
            out.println("<a href='manage-staff.html' class='nav-link'><i class='fa-solid fa-users-gear'></i> Staff Management</a>");
        }

        out.println("<a href='help.html' class='nav-link'><i class='fa-solid fa-book-open'></i> Operational Guide </a>");
        if ("ADMIN".equals(role)) {
            out.println("<a href='add-room-category.html' class='nav-link'><i class='fa-solid fa-tags'></i>Add Room Category</a> ");
        }
        if ("ADMIN".equals(role)) {
            out.println("<a href='add-room.html' class='nav-link'><i class='fa-solid fa-tags'></i>Room Managment</a> ");
        }


        out.println("<a href='logout' class='nav-link' style='color: #fc8181;'> <i class='fa-solid fa-right-from-bracket' style='color: #fc8181;'></i> Logout </a>");
        out.println("</div>");
        out.println("</div>");





        // MAIN CONTENT
        out.println("<div class='main-content'>");
        out.println("<div class='header'>");
        out.println("<h2>Reservation Directory</h2>");
        out.println("<div style='text-align:right;'><small style='color:var(--text-muted)'>Logged in as</small><br><strong>" + user + " (" + role + ")</strong></div>");
        out.println("</div>");

        // Filter Bar
        out.println("<form action='view-reservations' method='GET' class='search-form'>");
        out.println("<input type='text' name='searchQuery' class='search-input' placeholder='Search guest name or reference ID...' value='" + (searchQuery != null ? searchQuery : "") + "'>");
        out.println("<button type='submit' class='btn-filter'>SEARCH</button>");
        out.println("</form>");

        out.println("<div class='table-container'>");
        out.println("<table><thead><tr><th>Ref ID</th><th>Guest Details</th><th>Stay Period</th><th>Room</th><th>Total Bill</th><th>Status</th><th>Management</th></tr></thead><tbody>");

        try (Connection conn = DBUtil.getConnection()) {
            String sql = (searchQuery != null && !searchQuery.trim().isEmpty())
                    ? "SELECT * FROM reservations WHERE reservation_number = ? OR guest_name LIKE ? ORDER BY reservation_number DESC"
                    : "SELECT * FROM reservations ORDER BY reservation_number DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                int id = 0; try { id = Integer.parseInt(searchQuery); } catch (Exception e) {}
                ps.setInt(1, id);
                ps.setString(2, "%" + searchQuery + "%");
            }

            ResultSet rs = ps.executeQuery();
            LocalDate today = LocalDate.now();

            while (rs.next()) {
                int resId = rs.getInt("reservation_number");
                String checkOutStr = rs.getString("check_out");
                LocalDate checkOutDate = LocalDate.parse(checkOutStr);
                boolean isCheckedOut = today.isAfter(checkOutDate);

                out.println("<tr>");
                out.println("<td style='color:var(--text-muted); font-family:monospace;'>#OR-" + resId + "</td>");
                out.println("<td><strong>" + rs.getString("guest_name") + "</strong><br><small>" + rs.getString("email") + "</strong><br><small>" + rs.getString("contact_number") + "</small></td>");
                out.println("<td>" + rs.getString("check_in") + " <i class='fa-solid fa-arrow-right' style='font-size:10px; color:var(--accent)'></i> " + checkOutStr + "</td>");
                out.println("<td><span style='color:var(--primary); font-weight:600;'>" + rs.getString("room_type") + "</span><br><small>Room: " + rs.getString("room_number") + "</small></td>");
                out.println("<td style='font-weight:bold; color:var(--primary);'>Rs. " + String.format("%.2f", rs.getDouble("total_bill")) + "</td>");

                // Status
                String statusHtml = isCheckedOut
                        ? "<span class='badge badge-out'>Checked Out</span>"
                        : "<span class='badge badge-active'>Active Stay</span>";
                out.println("<td>" + statusHtml + "</td>");

                // Actions
                out.println("<td>");
                out.println("<a href='edit-reservation?id=" + resId + "' class='action-link edit'>EDIT</a>");

                if (!isCheckedOut) {
                    out.println("<a href='CheckOutServlet?id=" + resId + "' class='action-link checkout' onclick='return confirm(\"Confirm Guest Check-out?\")'>CHECK-OUT</a>");
                }

                if ("ADMIN".equals(role)) {
                    out.println("<a href='delete-reservation?id=" + resId + "' class='action-link cancel' onclick='return confirm(\"Delete this record permanently?\")'>CANCEL</a>");
                }

                out.println("<a href='view-bill.html?id=" + resId + "' target='_blank' class='action-link print'>INVOICE</a>");
                out.println("</td>");

                out.println("</tr>");
            }
        } catch (SQLException e) { e.printStackTrace(); }

        out.println("</tbody></table></div></div></body></html>");
    }
}