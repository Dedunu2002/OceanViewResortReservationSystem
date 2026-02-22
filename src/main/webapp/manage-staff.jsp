<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.oceanview.resort.util.DBUtil" %>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Management | Admin Portal</title>
    <style>
        :root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }
        body { font-family: 'Segoe UI', sans-serif; background: var(--bg); padding: 40px; }
        .container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.05); }
        h2 { color: var(--primary); border-bottom: 2px solid var(--accent); padding-bottom: 10px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th { background: var(--primary); color: white; padding: 12px; text-align: left; font-size: 13px; }
        td { padding: 12px; border-bottom: 1px solid #eee; font-size: 14px; }
        .role-badge { padding: 4px 8px; border-radius: 3px; font-size: 11px; font-weight: bold; background: #e2e8f0; }
        .btn-add { background: var(--primary); color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 13px; }
        .btn-delete { color: #dc3545; text-decoration: none; font-weight: bold; font-size: 13px; }
    </style>
</head>
<body>

<%
    // Security Check: Only Admins can see this
    HttpSession userSession = request.getSession(false);
    if (userSession == null || !"ADMIN".equals(userSession.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }
%>

<div class="container">
    <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2>Staff Management</h2>
        <a href="add-staff.jsp" class="btn-add">+ REGISTER NEW STAFF</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>System Role</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <%
                try (Connection conn = DBUtil.getConnection()) {
                    String sql = "SELECT * FROM staff ORDER BY role ASC";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
            %>
                <tr>
                    <td>#ST-<%= rs.getInt("id") %></td>
                    <td><strong><%= rs.getString("username") %></strong></td>
                    <td><span class="role-badge"><%= rs.getString("role") %></span></td>
                    <td>
                        <% if(!"ADMIN".equals(rs.getString("role"))) { %>
                            <a href="DeleteStaffServlet?id=<%= rs.getInt("id") %>"
                               class="btn-delete"
                               onclick="return confirm('Remove access for this user?')">REMOVE ACCESS</a>
                        <% } else { %>
                            <span style="color: #ccc; font-size: 11px;">PROTECTED</span>
                        <% } %>
                    </td>
                </tr>
            <%
                    }
                } catch (Exception e) { e.printStackTrace(); }
            %>
        </tbody>
    </table>

    <br><br>
    <a href="admin-dashboard.jsp" style="color: var(--primary); text-decoration: none; font-weight: bold;">&larr; Back to Dashboard</a>
</div>

</body>
</html>