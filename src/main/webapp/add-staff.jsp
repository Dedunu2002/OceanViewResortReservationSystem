<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register Staff | Ocean View</title>
    <style>
        :root { --primary: #0A2540; --accent: #C9A24D; --bg: #F5F7FA; }
        body { font-family: 'Segoe UI', sans-serif; background: var(--bg); display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .form-container { background: white; padding: 40px; border-radius: 4px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); border-top: 5px solid var(--primary); width: 100%; max-width: 400px; }
        h2 { color: var(--primary); text-align: center; margin-bottom: 25px; letter-spacing: 1px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; font-size: 12px; font-weight: bold; margin-bottom: 5px; color: var(--primary); }
        input, select { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 3px; box-sizing: border-box; }
        button { width: 100%; padding: 15px; background: var(--primary); color: white; border: none; border-radius: 3px; cursor: pointer; font-weight: bold; margin-top: 10px; }
        button:hover { background: #1E6F9F; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>New Staff Account</h2>
        <form action="AddStaffServlet" method="POST">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label>System Role</label>
                <select name="role">
                    <option value="RECEPTIONIST">Receptionist</option>
                    <option value="ADMIN">Administrator</option>
                </select>
            </div>
            <button type="submit">CREATE ACCOUNT</button>
        </form>
        <p style="text-align: center;"><a href="manage-staff.jsp" style="font-size: 12px; color: #666; text-decoration: none;">Cancel</a></p>
    </div>
</body>
</html>