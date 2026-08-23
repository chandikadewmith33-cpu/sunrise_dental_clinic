package db;

import java.sql.Connection;

/**
 * Run this class directly to verify the database connection works
 * before launching the full application.
 */
public class TestConnection {
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
        if (con != null) {
            System.out.println("Database connected successfully!");
        } else {
            System.out.println("Database connection failed. Check DBConnection.java settings.");
        }
        DBConnection.closeConnection();
    }
}
