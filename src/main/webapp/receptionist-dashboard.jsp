<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, com.oceanview.resort.DBUtil" %>
<%
    if (session.getAttribute("user") == null || !"RECEPTIONIST".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }

    int occupiedRooms = 0;
    int totalResortRooms = 50;

    try (Connection conn = DBUtil.getConnection()) {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM reservations");
        if (rs.next()) occupiedRooms = rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }

    int availableRooms = totalResortRooms - occupiedRooms;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Dashboard</title>
    <style>
        body { font-family: sans-serif; margin: 0; text-align: center; background: #f9f9f9; }
        .nav { background: #16a085; color: white; padding: 15px; display: flex; justify-content: space-around; }
        .btn { display: inline-block; padding: 20px 40px; margin: 20px; background: white; border: 1px solid #ddd; text-decoration: none; color: #333; border-radius: 8px; }
        .status-box { background: #fff; border: 2px solid #16a085; display: inline-block; padding: 20px; border-radius: 10px; margin-top: 30px; }
    </style>
</head>
<body>
    <div class="nav">
        <span>Ocean View Resort</span>
        <span>Welcome, <%= session.getAttribute("user") %></span>
        <a href="logout" style="color:white;">Logout</a>
    </div>

    <h1>Reception Operations</h1>
    <a href="index.html" class="btn">New Guest Registration</a>
    <a href="view-reservations" class="btn">Search & View All</a>
    <a href="help.html" class="btn">Help Module</a>

    <br>
    <div class="status-box">
        <h3>Current Room Availability</h3>
        <h2 style="color: #16a085;"><%= availableRooms %> / <%= totalResortRooms %> Rooms Available</h2>
    </div>
</body>
</html>