package com.example.ecommerce;

import java.sql.*;

public class CheckDatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/your_db_name"; // Change to your DB URL
        String user = "your_db_user"; // Change to your DB user
        String password = "your_db_password"; // Change to your DB password

        String[] tables = {"users", "categories", "products", "product_images", "carts", "cart_items", "orders", "order_items", "transactions"};

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Database connected!");
            for (String table : tables) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT 1 FROM " + table + " LIMIT 1");
                    System.out.println("Table '" + table + "' exists and is accessible.");
                } catch (SQLException e) {
                    System.out.println("Table '" + table + "' is NOT accessible: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}