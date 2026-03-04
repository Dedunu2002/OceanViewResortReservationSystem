package com.oceanview.resort.util;

public class SMSUtil {
    public static boolean sendBookingSMS(String guestName, String phone, String checkIn, String checkOut, double total) {
        try {
            // Simulate network latency for a "real" feel
            System.out.println("[MOCK SMS] Connecting to Twilio Gateway...");
            Thread.sleep(500);

            System.out.println("[MOCK SMS] Sending to: " + phone);
            System.out.println("[MOCK SMS] Message: Hello " + guestName + ", your booking for " + checkIn + " is confirmed!");

            return true; // Return true to trigger the UI notification
        } catch (Exception e) {
            return false;
        }
    }
}