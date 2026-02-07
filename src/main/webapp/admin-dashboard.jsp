<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("user") == null || !"ADMIN".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.html");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Ocean View</title>
    <style>
        body { font-family: sans-serif; margin: 0; display: flex; }
        .sidebar { width: 250px; background: #2c3e50; color: white; height: 100vh; padding: 20px; }
        .content { flex: 1; padding: 40px; background: #ecf0f1; }
        .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); display: inline-block; margin-right: 20px; min-width: 200px; }
        a { color: white; display: block; padding: 10px 0; text-decoration: none; border-bottom: 1px solid #34495e; }
        a:hover { color: #3498db; }
        .logout { color: #e74c3c; margin-top: 50px; font-weight: bold; }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>Admin Panel</h2>
        <p>Welcome, ${user}</p>
        <a href="view-reservations">View All Bookings</a>
        <a href="manage-staff.html">Manage Staff Accounts</a>
        <a href="update-rates.html">Update Room Rates</a>
        <a href="reports.jsp">Revenue Reports</a>
        <a href="logout" class="logout">Exit System</a>
    </div>
    <div class="content">
        <h1>System Overview</h1>
        <div class="card">
            <h3>Daily Revenue</h3>
            <p style="font-size: 24px; color: green;">Rs.1,240.00</p>
        </div>
        <div class="card">
            <h3>Active Bookings</h3>
            <p style="font-size: 24px; color: blue;">14</p>
        </div>
    </div>
</body>
</html>