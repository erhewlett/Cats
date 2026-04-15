package dao;

import model.User;

/**
 * UserDAO.java
 * Team: Cats | CSc3350 SP2026
 *
 * Interface for the users table — handles login authentication.
 */
public interface UserDAO {

    /**
     * Find a user by username and hashed password.
     * Returns the User object if credentials match, null if not.
     *
     * This is the core login check — like swiping a keycard.
     * The password passed in must already be hashed before calling this.
     * We never compare plaintext passwords to the database.
     */
    User findByCredentials(String username, String hashedPassword);

    /**
     * Find a user by username only.
     * Used to check if a username exists before attempting login.
     */
    User findByUsername(String username);
}
