/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hp
 */

import com.contactReport.connection.DBConnection;

public class Main {
    public static void main(String[] args) {
        try {
            if (DBConnection.checkValue("SELECT 1")) {
                System.out.println("Database connected successfully!");
            } else {
                System.out.println("Connection check failed.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

