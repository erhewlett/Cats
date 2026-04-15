package ui;

import controller.LoginController;
import model.User;
import util.DBConnection;

import java.util.Scanner;

/**
 * MainUI.java
 * Team: Cats | CSc3350 SP2026
 *
 * Entry point for the entire application.
 * Displays the login screen, authenticates the user,
 * then routes to either HRAdminUI or EmployeeUI based on role.
 *
 * This is the front door of the app — everyone comes through here.
 * Think of it like the lobby of a building:
 * - You check in at the front desk (login)
 * - Security gives you a badge (User object with role)
 * - Badge determines which floors you can access (HR Admin vs Employee)
 */
public class MainUI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public static void main(String[] args) {
        printBanner();

        LoginController loginController = new LoginController();
        User currentUser = null;
        int attempts = 0;

        // Give user 3 attempts before locking out
        // Same concept as an ATM — 3 wrong PINs and it ejects the card
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            currentUser = loginController.login(username, password);

            if (currentUser != null) {
                System.out.println("\nLogin successful. Welcome, " +
                    username + "! [" + currentUser.getRole() + "]");
                break;
            }

            attempts++;
            int remaining = MAX_LOGIN_ATTEMPTS - attempts;
            if (remaining > 0) {
                System.out.println("Invalid credentials. " + remaining +
                    " attempt(s) remaining.\n");
            }
        }

        // If still null after 3 attempts, exit
        if (currentUser == null) {
            System.out.println("Too many failed attempts. Exiting.");
            DBConnection.close();
            return;
        }

        // Route to the correct UI based on role
        if (currentUser.isAdmin()) {
            HRAdminUI adminUI = new HRAdminUI(scanner, currentUser);
            adminUI.show();
        } else {
            EmployeeUI employeeUI = new EmployeeUI(scanner, currentUser);
            employeeUI.show();
        }

        // Clean up when user exits
        DBConnection.close();
        System.out.println("Goodbye!");
        scanner.close();
    }

    // ----------------------------------------------------------------
    // HELPERS
    // ----------------------------------------------------------------

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       EMPLOYEE MANAGEMENT SYSTEM             ║");
        System.out.println("║       Team Cats | CSc3350 SP2026             ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Please log in to continue.");
        System.out.println();
    }
}
