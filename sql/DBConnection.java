package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * Team: Cats | CSc3350 SP2026
 *
 * Singleton database connection manager.
 *
 * Singleton means only ONE connection object is ever created for the
 * entire session — like having one phone line to the database instead
 * of dialing a new call every time you need data. Every DAO class
 * calls DBConnection.getInstance() to get that single shared connection.
 *
 * HOW TO SET UP:
 * 1. Change DB_USER to your MySQL username (usually "root")
 * 2. Change DB_PASSWORD to YOUR MySQL password
 * 3. If using FreeSQLDatabase.com instead of local MySQL,
 *    change DB_URL to your remote connection string.
 *
 * TEAMMATES: Each person changes only DB_USER and DB_PASSWORD locally.
 * Never commit your real password to GitHub.
 */
public class DBConnection {

    // ----------------------------------------------------------------
    // CONNECTION SETTINGS — change these for your local MySQL setup
    // ----------------------------------------------------------------
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/cats";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "CHANGE_TO_YOUR_PASSWORD"; // ← change this

    // For FreeSQLDatabase.com users, replace DB_URL with something like:
    // "jdbc:mysql://sql.freedb.tech:3306/yourDatabaseName"
    // and update DB_USER and DB_PASSWORD to match your free account credentials

    // ----------------------------------------------------------------
    // SINGLETON IMPLEMENTATION
    // ----------------------------------------------------------------
    private static Connection instance = null;

    // Private constructor — prevents anyone from doing "new DBConnection()"
    // The only way in is through getInstance()
    private DBConnection() {}

    /**
     * Returns the single shared database connection.
     * Creates it on the first call; returns the existing one after that.
     *
     * Think of it like a singleton coffee maker in an office —
     * one machine, everyone uses the same one, nobody buys a second.
     *
     * @return Connection to cats MySQL database
     * @throws SQLException if connection fails (wrong password, MySQL not running, etc.)
     */
    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                // Load the MySQL JDBC driver
                // Java 17 can usually auto-detect this but being explicit is safer
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("[DBConnection] Connected to cats database.");
            } catch (ClassNotFoundException e) {
                // This means mysql-connector.jar is not in your classpath
                // Fix: make sure you compiled with -cp .:lib/mysql-connector.jar
                throw new SQLException("MySQL JDBC Driver not found. " +
                    "Make sure mysql-connector.jar is in the lib/ folder.", e);
            }
        }
        return instance;
    }

    /**
     * Closes the connection when the application shuts down.
     * Call this from MainUI before exiting.
     */
    public static void close() {
        if (instance != null) {
            try {
                instance.close();
                System.out.println("[DBConnection] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DBConnection] Error closing connection: " + e.getMessage());
            }
        }
    }
}