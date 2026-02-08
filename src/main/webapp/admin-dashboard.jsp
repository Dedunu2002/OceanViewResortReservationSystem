<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, com.oceanview.resort.DBUtil" %>
<%
    if (session.getAttribute("user") == null || !"ADMIN".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }

    int totalBookings = 0;
    double totalRevenue = 0.0;

    try (Connection conn = DBUtil.getConnection()) {
        Statement st1 = conn.createStatement();
        ResultSet rs1 = st1.executeQuery("SELECT COUNT(*) FROM reservations");
        if (rs1.next()) totalBookings = rs1.getInt(1);

        Statement st2 = conn.createStatement();
        ResultSet rs2 = st2.executeQuery("SELECT SUM(total_bill) FROM reservations");
        if (rs2.next()) totalRevenue = rs2.getDouble(1);
    } catch (Exception e) { e.printStackTrace(); }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Executive Dashboard | Ocean View</title>
    <style>
        :root {
            --primary: #0A2540; --secondary: #1E6F9F; --accent: #C9A24D;
            --bg: #F5F7FA; --text-main: #1C1C1C;
        }
        body { font-family: 'Segoe UI', sans-serif; margin: 0; display: flex; background: var(--bg); color: var(--text-main); }

        .sidebar { width: 280px; background: var(--primary); color: white; height: 100vh; padding: 30px; box-sizing: border-box; }
        .sidebar h2 { font-size: 18px; letter-spacing: 2px; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 20px; }
        .sidebar a { color: #cbd5e1; display: block; padding: 15px 0; text-decoration: none; font-size: 14px; transition: 0.3s; }
        .sidebar a:hover { color: var(--accent); }

        .main-content { flex: 1; padding: 50px; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 40px; }

        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 25px; }
        .card { background: white; padding: 30px; border-radius: 4px; box-shadow: 0 4px 15px rgba(0,0,0,0.03); border-left: 4px solid var(--accent); }
        .card h3 { font-size: 12px; text-transform: uppercase; color: var(--secondary); margin: 0; letter-spacing: 1px; }
        .stat-value { font-size: 32px; font-weight: 600; margin-top: 10px; color: var(--primary); }

        .btn-logout { color: #ef4444 !important; margin-top: 40px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 20px !important; }
    </style>
</head>
<body>
    <div class="sidebar">
        <h2>OCEAN VIEW</h2>
        <p style="font-size: 11px; color: var(--accent);">ADMINISTRATIVE PORTAL</p>
        <a href="view-reservations">System Oversight</a>
        <a href="manage-staff.jsp">Staff Management</a>
        <a href="help.html">Operational Guide</a>
        <a href="logout" class="btn-logout">Exit System</a>
    </div>
    <div class="main-content">
        <div class="header">
            <h1>Executive Overview</h1>
            <span style="color: var(--secondary);">Logged in as: <strong>${user}</strong></span>
        </div>

        <div class="stats-grid">
            <div class="card">
                <h3>Total Gross Revenue</h3>
                <div class="stat-value">Rs. <%= String.format("%.2f", totalRevenue) %></div>
            </div>
            <div class="card">
                <h3>Current Bookings</h3>
                <div class="stat-value"><%= totalBookings %></div>
            </div>
        </div>
    </div>
</body>
</html>