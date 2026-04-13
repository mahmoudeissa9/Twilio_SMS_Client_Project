package iti.tel.twilio_project;

//import iti.tel.twilio_project.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Get data
            String name = request.getParameter("name");
            String birthday = request.getParameter("birthday");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String job = request.getParameter("job");
            String address = request.getParameter("address");
            String password = request.getParameter("password");
            String sid = request.getParameter("twilio_sid");
            String token = request.getParameter("twilio_token");
            String sender = request.getParameter("sender_id");

            Connection conn = DBConnection.getConnection();

            // 2. Insert user (NOT VERIFIED)
            String sql = "INSERT INTO users " +
                    "(name, email, password, phone_number_msisdn, birthday, job, address, twilio_sid, twilio_token, allowed_sender_id, role, is_verified) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'CUSTOMER', false)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setDate(5, Date.valueOf(birthday));
            ps.setString(6, job);
            ps.setString(7, address);
            ps.setString(8, sid);
            ps.setString(9, token);
            ps.setString(10, sender);

            ps.executeUpdate();

            // 3. Get user id
            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT customer_id FROM users WHERE email = ?");
            ps2.setString(1, email);

            ResultSet rs = ps2.executeQuery();

            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt("customer_id");
            }

            // 4. Generate 6-digit OTP
            int otp = (int)(Math.random() * 900000) + 100000;

            // 5. Save OTP
            String vSql = "INSERT INTO verification (user_id, code, expires_at, is_used) " +
                          "VALUES (?, ?, NOW() + INTERVAL '5 minutes', false)";

            PreparedStatement ps3 = conn.prepareStatement(vSql);
            ps3.setInt(1, userId);
            ps3.setString(2, String.valueOf(otp));
            ps3.executeUpdate();

            // 6. TEMP: print OTP
            System.out.println("OTP = " + otp);

            // 7. Redirect to verify page
            response.sendRedirect("verify.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}