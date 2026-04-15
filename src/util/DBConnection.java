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
 * 1. Optionally set EMS_DB_URL if your MySQL host/port is different.
 * 2. Set EMS_DB_USER if your MySQL username is not "root".
 * 3. Set EMS_DB_PASSWORD in your shell before running the app.
 *
 * Example:
 * export EMS_DB_PASSWORD=CHANGE_TO_YOUR_PASSWORD
 *
 * TEAMMATES: keep real passwords out of source control.
 */
public class DBConnection {
    private static final String DEFAULT_DB_URL  = "jdbc:mysql://localhost:3306/cats";
    private static final String DEFAULT_DB_USER = "root";

    // ----------------------------------------------------------------
    // CONNECTION SETTINGS — change these for your local MySQL setup
    // ----------------------------------------------------------------
    private static final String DB_URL =
        System.getenv().getOrDefault("EMS_DB_URL", DEFAULT_DB_URL);
    private static final String DB_USER =
        System.getenv().getOrDefault("EMS_DB_USER", DEFAULT_DB_USER);
    private static final String DB_PASSWORD = System.getenv("EMS_DB_PASSWORD");

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
                if (DB_PASSWORD == null || DB_PASSWORD.isBlank()) {
                    throw new SQLException(
                        "EMS_DB_PASSWORD is not set. Set it first, for example: " +
                        "export EMS_DB_PASSWORD=CHANGE_TO_YOUR_PASSWORD"
                    );
                }
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
