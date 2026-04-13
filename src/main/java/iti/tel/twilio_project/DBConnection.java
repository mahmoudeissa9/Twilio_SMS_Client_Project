/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iti.tel.twilio_project;

/**
 *
 * @author eissa
 */
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    
    private static final String URL = "jdbc:postgresql://localhost:5432/iti_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "111";

    public static Connection getConnection() throws Exception {
                Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}