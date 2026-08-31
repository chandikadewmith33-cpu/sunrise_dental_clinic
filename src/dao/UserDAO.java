package dao;

import db.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for staff login/authentication.
 */
public class UserDAO {

    public User authenticate(String username, String password) {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("role")
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Authentication error: " + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Register a new user.
     */
    public boolean registerUser(
            String username,
            String password,
            String fullName,
            String role) {

        String sql = "INSERT INTO users "
                + "(username, password, full_name, role) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, role);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "User registration error: " + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Get all usernames from the database.
     */
    public List<String> getAllUsernames() {

        List<String> usernames = new ArrayList<>();

        String sql = "SELECT username FROM users ORDER BY username";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                usernames.add(
                        rs.getString("username")
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error loading usernames: " + e.getMessage()
            );
        }

        return usernames;
    }

    /**
     * Delete a user using username.
     */
    public boolean deleteUser(String username) {

        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.err.println(
                    "User deletion error: " + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Check whether a username already exists.
     */
    public boolean usernameExists(String username) {

        String sql = "SELECT username FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {

            System.err.println(
                    "Username check error: " + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Get all doctors from the database.
     *
     * Only users with the DOCTOR role are returned.
     */
    public String[] getAllDoctors() {

        List<String> doctors = new ArrayList<>();

        String sql = "SELECT full_name "
                + "FROM users "
                + "WHERE role = 'DOCTOR' "
                + "ORDER BY full_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                doctors.add(
                        rs.getString("full_name")
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error loading doctors: " + e.getMessage()
            );
        }

        return doctors.toArray(new String[0]);
    }
}
