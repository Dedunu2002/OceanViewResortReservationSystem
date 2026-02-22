<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, com.oceanview.resort.util.DBUtil" %>
<%
    if (session.getAttribute("user") == null || !"RECEPTIONIST".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }

    int occupied = 0;
    int totalRooms = 50;
    try (Connection conn = DBUtil.getConnection()) {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM reservations");
        if (rs.next()) occupied = rs.getInt(1);
    } catch (Exception e) { e.printStackTrace(); }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Reception Desk | Ocean View</title>
    <style>
        :root {
            --primary: #0A2540; --secondary: #1E6F9F; --accent: #C9A24D;
            --bg: #F5F7FA; --text-main: #1C1C1C;
        }
        body { font-family: 'Segoe UI', sans-serif; margin: 0; background: var(--bg); text-align: center; color: var(--text-main); }
        .top-nav { background: var(--primary); color: white; padding: 20px 50px; display: flex; justify-content: space-between; align-items: center; }

        .welcome-section { padding: 60px 20px; }
        .welcome-section h1 { font-weight: 300; font-size: 36px; letter-spacing: 1px; }

        .action-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 30px; max-width: 1000px; margin: 0 auto; padding: 0 20px; }
        .action-card {
            background: white; padding: 40px 20px; border-radius: 4px; text-decoration: none; color: var(--text-main);
            box-shadow: 0 4px 10px rgba(0,0,0,0.02); transition: 0.3s; border-bottom: 3px solid transparent;
        }
        .action-card:hover { transform: translateY(-5px); border-bottom: 3px solid var(--accent); }
        .action-card h3 { color: var(--primary); text-transform: uppercase; font-size: 14px; letter-spacing: 1px; }

        .availability-banner { margin-top: 60px; background: white; display: inline-block; padding: 25px 50px; border-radius: 50px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    </style>
</head>
<body>
    <div class="top-nav">
        <span style="letter-spacing: 2px; font-weight: bold;">OCEAN VIEW</span>
        <a href="logout" style="color: var(--accent); text-decoration: none; font-size: 13px; font-weight: bold;">SIGN OUT</a>
    </div>

    <div class="welcome-section">
        <h1>Welcome, ${user}</h1>
        <p style="color: var(--secondary);">Front Desk Operations Control</p>
    </div>

    <div class="action-grid">
        <a href="index.html" class="action-card">
            <h3>New Guest</h3>
            <p style="font-size: 13px; color: #666;">Registration & Billing</p>
        </a>
        <a href="view-reservations" class="action-card">
            <h3>Reservations</h3>
            <p style="font-size: 13px; color: #666;">Search & Manage</p>
        </a>
        <a href="help.html" class="action-card">
            <h3>Support</h3>
            <p style="font-size: 13px; color: #666;">Staff Quick Guide</p>
        </a>
    </div>

    <div class="availability-banner">
        <span style="color: var(--secondary); font-size: 14px; text-transform: uppercase;">Real-time Inventory:</span>
        <strong style="margin-left: 10px; font-size: 18px; color: var(--primary);"><%= (totalRooms - occupied) %> / <%= totalRooms %> Rooms Available</strong>
    </div>
</body>
</html>