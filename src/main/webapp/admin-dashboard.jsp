<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, com.oceanview.resort.DBUtil" %>
<%
    // Security Check
    if (session.getAttribute("user") == null || !"ADMIN".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }

    int totalBookings = 0;
    double totalRevenue = 0.0;

    try (Connection conn = DBUtil.getConnection()) {
        // Query 1: Count total reservations
        Statement st1 = conn.createStatement();
        ResultSet rs1 = st1.executeQuery("SELECT COUNT(*) FROM reservations");
        if (rs1.next()) totalBookings = rs1.getInt(1);

        // Query 2: Sum total revenue
        Statement st2 = conn.createStatement();
        ResultSet rs2 = st2.executeQuery("SELECT SUM(total_bill) FROM reservations");
        if (rs2.next()) totalRevenue = rs2.getDouble(1);
    } catch (Exception e) { e.printStackTrace(); }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body { font-family: sans-serif; margin: 0; display: flex; }
        .sidebar { width: 250px; background: #2c3e50; color: white; height: 100vh; padding: 20px; }
        .content { flex: 1; padding: 40px; background: #f4f7f6; }
        .card { background: white; padding: 25px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); display: inline-block; margin-right: 20px; text-align: center; }
        .stat-val { font-size: 32px; font-weight: bold; color: #16a085; }
        a { color: white; display: block; padding: 12px 0; text-decoration: none; border-bottom: 1px solid #34495e; }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>Admin Panel</h2>
        <a href="view-reservations">Manage All Bookings</a>
        <a href="help.html">Help Guide</a>
        <a href="logout" style="color:#e74c3c;">Logout</a>
    </div>
    <div class="content">
        <h1>System Analytics</h1>
        <div class="card">
            <h3>Total Revenue</h3>
            <div class="stat-val">Rs.<%= String.format("%.2f", totalRevenue) %></div>
        </div>
        <div class="card">
            <h3>Total Bookings</h3>
            <div class="stat-val"><%= totalBookings %></div>
        </div>
    </div>
</body>
</html>