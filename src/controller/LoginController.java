package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * LoginController.java
 * Team: Cats | CSc3350 SP2026
 *
 * Handles user authentication.
 * The UI calls login() with raw username and password.
 * This class hashes the password and asks UserDAO if it matches.
 *
 * Think of it like a nightclub:
 * - User shows up with a name and password (ID)
 * - LoginController hashes the password (verifies the ID is real)
 * - UserDAO checks the guest list (DB)
 * - Returns the User object (wristband) or null (turned away)
 *
 * The returned User object is then passed around the app
 * as the "session" — it tells every controller who is logged in
 * and what role they have.
 */
public class LoginController {

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Attempt to log in with the given credentials.
     *
     * @param username  raw username typed by user
     * @param password  raw plaintext password typed by user
     * @return User object if login succeeds, null if credentials are wrong
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;

        String hashed = hashPassword(password);
        if (hashed == null) return null;

        return userDAO.findByCredentials(username.trim(), hashed);
    }

    /**
     * Hashes a plaintext password using SHA-256.
     * Same algorithm used when we inserted users with SHA2() in MySQL.
     *
     * SHA-256 is a one-way function — like a meat grinder.
     * You can turn meat into ground beef but not back into a steak.
     * We store ground beef (the hash), never the steak (plaintext).
     *
     * @param plaintext the raw password
     * @return hex string of the SHA-256 hash, or null on error
     */
    public static String hashPassword(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(plaintext.getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hex string
            // Each byte becomes two hex characters
            // Example: byte 0xEF -> "ef"
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("[LoginController] SHA-256 not available: " + e.getMessage());
            return null;
        }
    }
}
