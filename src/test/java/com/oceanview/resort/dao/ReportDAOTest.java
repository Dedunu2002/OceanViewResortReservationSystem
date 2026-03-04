package com.oceanview.resort.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class ReportDAOTest {
    @Test
    void testGetDailyReport() {
        ReportDAO dao = new ReportDAO();
        // Returns a DailySummary object containing revenue and guest lists
        ReportDAO.DailySummary summary = dao.getDailyReport(LocalDate.now());
        assertNotNull(summary, "Daily report summary object should be generated.");
        assertTrue(summary.totalRevenue >= 0, "Revenue should not be a negative value.");
    }
}