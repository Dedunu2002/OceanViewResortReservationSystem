<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("user") == null || !"RECEPTIONIST".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.html");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Dashboard - Ocean View</title>
    <style>
        body { font-family: sans-serif; margin: 0; background: #fdfdfd; }
        .navbar { background: #16a085; color: white; padding: 15px 30px; display: flex; justify-content: space-between; }
        .container { padding: 40px; text-align: center; }
        .menu-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; max-width: 900px; margin: 0 auto; }
        .menu-item { background: white; padding: 30px; border: 1px solid #ddd; border-radius: 10px; text-decoration: none; color: #333; transition: 0.3s; }
        .menu-item:hover { background: #16a085; color: white; transform: translateY(-5px); }
    </style>
</head>
<body>
    <div class="navbar">
        <span>Ocean View Reception</span>
        <span>User: ${user} | <a href="logout" style="color:white;">Logout</a></span>
    </div>
    <div class="container">
        <h1>Guest Management Hub</h1>
        <div class="menu-grid">
            <a href="index.html" class="menu-item"><h3>Register Guest</h3></a>
            <a href="view-reservations" class="menu-item"><h3>View All Booking</h3></a>
            <a href="help.html" class="menu-item"><h3>Quick Start Guide</h3></a>
        </div>
    </div>
</body>
</html>