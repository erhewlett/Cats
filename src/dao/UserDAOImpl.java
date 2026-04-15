package dao;

import model.User;
import util.DBConnection;

import java.sql.*;

/**
 * UserDAOImpl.java
 * Team: Cats | CSc3350 SP2026
 *
 * Implements UserDAO for login authentication.
 */
public class UserDAOImpl implements UserDAO {

    // ----------------------------------------------------------------
    // HELPER — maps one ResultSet row to one User object
    // ----------------------------------------------------------------
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserID(rs.getInt("userID"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setEmpid(rs.getInt("empid"));
        return u;
    }

    // ----------------------------------------------------------------
    // FIND BY CREDENTIALS
    // Both username and hashed password must match.
    // One query, one row back, or null.
    //
    // Example flow:
    // User types: username="sbeagle", password="password123"
    // LoginController hashes "password123" -> "ef92b778..."
    // This method runs: SELECT * FROM users WHERE username=? AND password=?
    // with ("sbeagle", "ef92b778...")
    // DB finds a match -> returns User object with role=HR_ADMIN
    // ----------------------------------------------------------------
    @Override
    public User findByCredentials(String username, String hashedPassword) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.err.println("[UserDAO] findByCredentials error: " + ex.getMessage());
        }
        return null; // null = login failed
    }

    // ----------------------------------------------------------------
    // FIND BY USERNAME ONLY
    // ----------------------------------------------------------------
    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.err.println("[UserDAO] findByUsername error: " + ex.getMessage());
        }
        return null;
    }
}
