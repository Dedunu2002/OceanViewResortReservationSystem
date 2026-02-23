package com.oceanview.resort.servlet;

import com.oceanview.resort.dao.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StaffApiServlet extends HttpServlet {

    private StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Security Check: Only Admins can fetch this data
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 2. Fetch Data from DAO
        List<StaffDAO.StaffMember> staffList = staffDAO.getAllStaff();

        // 3. Prepare the Response as JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // 4. Manually build the JSON array (e.g., [{"id":1, "username":"admin", "role":"ADMIN"}])
        // Note: In larger enterprise apps, developers use a library like 'Gson' for this,
        // but this manual builder is reliable and requires no new Maven dependencies!
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < staffList.size(); i++) {
            StaffDAO.StaffMember s = staffList.get(i);
            json.append("{\"id\":").append(s.id)
                    .append(",\"username\":\"").append(s.username)
                    .append("\",\"role\":\"").append(s.role).append("\"}");

            // Add a comma if it's not the last item
            if (i < staffList.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        // 5. Send data to the HTML page
        out.print(json.toString());
        out.flush();
    }
}