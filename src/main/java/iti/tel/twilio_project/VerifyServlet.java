/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package iti.tel.twilio_project;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
/**
 *
 * @author eissa
 */

public class VerifyServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String otp = request.getParameter("full_otp");

            Connection conn = DBConnection.getConnection();

            // get latest OTP
            PreparedStatement ps = conn.prepareStatement(
                "SELECT user_id FROM verification WHERE code = ? AND is_used = false ORDER BY id DESC LIMIT 1"
            );

            ps.setString(1, otp);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");

                // verify user
                PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE users SET is_verified = true WHERE customer_id = ?");
                ps2.setInt(1, userId);
                ps2.executeUpdate();

                // mark OTP used
                PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE verification SET is_used = true WHERE code = ?");
                ps3.setString(1, otp);
                ps3.executeUpdate();

                response.getWriter().println("Account Verified Successfully");

            } else {
                response.getWriter().println("Invalid OTP");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}