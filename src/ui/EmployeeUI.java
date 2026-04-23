package ui;

import controller.EmployeeController;
import model.Employee;
import model.Payroll;
import model.User;

import java.util.List;
import java.util.Scanner;

/**
 * EmployeeUI.java
 * Team: Cats | CSc3350 SP2026
 *
 * Console UI for general employee users.
 * Limited to read-only self-service features:
 * - Search/view their own employee profile using personal identifiers
 * - View their own pay history sorted newest first
 * - Update their own contact info (mobile, emergency contact)
 *
 * The empID is pulled from the session (currentUser.getEmpid())
 * so employees can ONLY see their own data.
 */
public class EmployeeUI {

    private Scanner            scanner;
    private User               currentUser;
    private EmployeeController empController;

    public EmployeeUI(Scanner scanner, User currentUser) {
        this.scanner       = scanner;
        this.currentUser   = currentUser;
        this.empController = new EmployeeController();
    }

    // ----------------------------------------------------------------
    // MAIN MENU
    // ----------------------------------------------------------------

    public void show() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> searchMyPersonalData();
                case "2" -> viewMyPayHistory();
                case "3" -> updateMyContactInfo();
                case "0" -> running = false;
                default  -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         EMPLOYEE SELF SERVICE        ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Search/View My Data              ║");
        System.out.println("║  2. View My Pay History              ║");
        System.out.println("║  3. Update My Contact Info           ║");
        System.out.println("║  0. Logout                           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Select: ");
    }

    private void searchMyPersonalData() {
        int empid = currentUser.getEmpid();
        System.out.println("\n-- SEARCH / VIEW MY PERSONAL DATA --");
        System.out.println("1. Search by Employee ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Date of Birth");
        System.out.println("4. Search by SSN");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        Employee result = null;
        switch (choice) {
            case "1" -> {
                System.out.print("Enter your Employee ID: ");
                result = empController.searchOwnRecordByEmpID(empid, parseID(scanner.nextLine()));
            }
            case "2" -> {
                System.out.print("Enter your first name, last name, or full name: ");
                result = empController.searchOwnRecordByName(empid, scanner.nextLine().trim());
            }
            case "3" -> {
                System.out.print("Enter your Date of Birth (YYYY-MM-DD): ");
                result = empController.searchOwnRecordByDOB(empid, scanner.nextLine().trim());
            }
            case "4" -> {
                System.out.print("Enter your SSN (###-##-####): ");
                result = empController.searchOwnRecordBySSN(empid, scanner.nextLine().trim());
            }
            default -> System.out.println("Invalid option.");
        }

        if (result != null) {
            printProfile(result);
        }
    }

    private void printProfile(Employee e) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("           YOUR EMPLOYEE PROFILE");
        System.out.println("─".repeat(60));
        System.out.printf("Employee ID : %d%n",       e.getEmpid());
        System.out.printf("Name        : %s %s%n",    e.getFname(), e.getLname());
        System.out.printf("Email       : %s%n",        e.getEmail());
        System.out.printf("Hire Date   : %s%n",        e.getHireDate());
        System.out.printf("Date of Birth: %s%n",       e.getDob());
        System.out.printf("Mobile      : %s%n",        e.getMobile());
        System.out.printf("Emergency Contact: %s | %s%n",
            e.getEmergencyContactName(), e.getEmergencyContactMobile());
        System.out.println("─".repeat(60));
        // Note: salary is intentionally not shown to general employees
        // They can see pay history but not the annual salary figure
    }

    private int parseID(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    // ----------------------------------------------------------------
    // VIEW MY PAY HISTORY (PT-3)
    // ----------------------------------------------------------------

    private void viewMyPayHistory() {
        int empid = currentUser.getEmpid();
        List<Payroll> history = empController.getPayHistory(empid);
        if (history.isEmpty()) return;

        System.out.println("\n" + "─".repeat(90));
        System.out.println("                         YOUR PAY HISTORY (Most Recent First)");
        System.out.println("─".repeat(90));
        System.out.printf("%-12s %-10s %-10s %-9s %-9s %-10s %-9s %-9s %-10s%n",
            "Pay Date", "Gross", "Fed Tax", "SS", "Medicare",
            "State Tax", "401k", "Health", "Net Pay");
        System.out.println("─".repeat(90));

        for (Payroll p : history) {
            System.out.printf("%-12s $%-9.2f $%-9.2f $%-8.2f $%-8.2f $%-9.2f $%-8.2f $%-8.2f $%-9.2f%n",
                p.getPayDate(),
                p.getEarnings(),
                p.getFedTax(),
                p.getFedSS(),
                p.getFedMed(),
                p.getStateTax(),
                p.getRetire401k(),
                p.getHealthCare(),
                p.getNetPay()
            );
        }
        System.out.println("─".repeat(90));
        System.out.println("Total records: " + history.size());
    }

    // ----------------------------------------------------------------
    // UPDATE MY CONTACT INFO
    // Employees can update their own mobile and emergency contact
    // but cannot change their name, salary, SSN, or hire date
    // ----------------------------------------------------------------

    private void updateMyContactInfo() {
        int empid = currentUser.getEmpid();
        System.out.println("\n-- UPDATE MY CONTACT INFO --");
        System.out.println("1. Update Mobile Number");
        System.out.println("2. Update Emergency Contact Name");
        System.out.println("3. Update Emergency Contact Mobile");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.print("New Mobile (###-###-####): ");
                String mobile = scanner.nextLine().trim();
                empController.updateEmployeeField(empid, "mobile", mobile);
            }
            case "2" -> {
                System.out.print("New Emergency Contact Name: ");
                String name = scanner.nextLine().trim();
                empController.updateEmployeeField(empid, "emergency_contact_name", name);
            }
            case "3" -> {
                System.out.print("New Emergency Contact Mobile (###-###-####): ");
                String mobile = scanner.nextLine().trim();
                empController.updateEmployeeField(empid, "emergency_contact_mobile", mobile);
            }
            case "4" -> {
                System.out.print("New Email: ");
                String email = scanner.nextLine().trim();
                empController.updateEmployeeField(empid, "email", email);
            }
            default -> System.out.println("Invalid option.");
        }
    }
}
