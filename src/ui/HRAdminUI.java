package ui;

import controller.EmployeeController;
import controller.SalaryController;
import model.Employee;
import model.User;
import util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * HRAdminUI.java
 * Team: Cats | CSc3350 SP2026
 *
 * Console UI for HR Admin users.
 * Provides access to all features:
 * - Add, search, edit, delete employees
 * - Salary raise operations
 * - Reports (new hires, pay by job title, pay by division)
 *
 * This class only handles INPUT and OUTPUT.
 * All logic lives in the controllers.
 * All DB work lives in the DAOs.
 */
public class HRAdminUI {

    private Scanner            scanner;
    private EmployeeController empController;
    private SalaryController   salaryController;

    public HRAdminUI(Scanner scanner, User currentUser) {
        this.scanner          = scanner;
        this.empController    = new EmployeeController();
        this.salaryController = new SalaryController();
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
                case "1" -> searchEmployee();
                case "2" -> addEmployee();
                case "3" -> editEmployee();
                case "4" -> deleteEmployee();
                case "5" -> salaryMenu();
                case "6" -> reportsMenu();
                case "7" -> viewAllEmployees();
                case "0" -> running = false;
                default  -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         HR ADMIN MENU                ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Search Employee                  ║");
        System.out.println("║  2. Add New Employee                 ║");
        System.out.println("║  3. Edit Employee                    ║");
        System.out.println("║  4. Delete Employee                  ║");
        System.out.println("║  5. Salary Operations                ║");
        System.out.println("║  6. Reports                          ║");
        System.out.println("║  7. View All Employees               ║");
        System.out.println("║  0. Logout                           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Select: ");
    }

    // ----------------------------------------------------------------
    // SEARCH EMPLOYEE
    // ----------------------------------------------------------------

    private void searchEmployee() {
        System.out.println("\n-- SEARCH EMPLOYEE --");
        System.out.println("1. Search by Employee ID");
        System.out.println("2. Search by Last Name");
        System.out.println("3. Search by First Name");
        System.out.println("4. Search by Date of Birth");
        System.out.println("5. Search by SSN");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.print("Enter Employee ID: ");
                int id = InputValidator.parseID(scanner.nextLine());
                Employee e = empController.searchByID(id);
                if (e != null) printEmployee(e);
            }
            case "2" -> {
                System.out.print("Enter Last Name: ");
                String lname = scanner.nextLine().trim();
                List<Employee> results = empController.searchByLastName(lname);
                printEmployeeList(results);
            }
            case "3" -> {
                System.out.print("Enter First Name: ");
                String fname = scanner.nextLine().trim();
                List<Employee> results = empController.searchByFirstName(fname);
                printEmployeeList(results);
            }
            case "4" -> {
                System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                String dob = scanner.nextLine().trim();
                List<Employee> results = empController.searchByDOB(dob);
                printEmployeeList(results);
            }
            case "5" -> {
                System.out.print("Enter SSN (###-##-####): ");
                Employee e = empController.searchBySSN(scanner.nextLine().trim());
                if (e != null) printEmployee(e);
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ----------------------------------------------------------------
    // ADD NEW EMPLOYEE (PT-9)
    // ----------------------------------------------------------------

    private void addEmployee() {
        System.out.println("\n-- ADD NEW EMPLOYEE --");
        System.out.println("(empID will be auto-generated)");

        Employee e = new Employee();

        System.out.print("First Name: ");
        e.setFname(scanner.nextLine().trim());

        System.out.print("Last Name: ");
        e.setLname(scanner.nextLine().trim());

        System.out.print("Email: ");
        e.setEmail(scanner.nextLine().trim());

        System.out.print("Hire Date (YYYY-MM-DD): ");
        e.setHireDate(scanner.nextLine().trim());

        System.out.print("Date of Birth (YYYY-MM-DD): ");
        e.setDob(scanner.nextLine().trim());

        System.out.print("SSN (###-##-####): ");
        e.setSsn(scanner.nextLine().trim());

        System.out.print("Salary: ");
        double salary = InputValidator.parseDouble(scanner.nextLine());
        e.setSalary(salary);

        System.out.print("Mobile (###-###-####): ");
        e.setMobile(scanner.nextLine().trim());

        System.out.print("Emergency Contact Name: ");
        e.setEmergencyContactName(scanner.nextLine().trim());

        System.out.print("Emergency Contact Mobile (###-###-####): ");
        e.setEmergencyContactMobile(scanner.nextLine().trim());

        System.out.print("Street Address: ");
        e.setStreet(scanner.nextLine().trim());

        System.out.print("City ID: ");
        e.setCityID(InputValidator.parseID(scanner.nextLine()));

        System.out.print("State ID: ");
        e.setStateID(InputValidator.parseID(scanner.nextLine()));

        System.out.print("ZIP Code: ");
        e.setZip(scanner.nextLine().trim());

        System.out.print("Division ID: ");
        e.setDivisionID(InputValidator.parseID(scanner.nextLine()));

        System.out.print("Job Title ID: ");
        e.setJobTitleID(InputValidator.parseID(scanner.nextLine()));

        empController.addEmployee(e);
    }

    // ----------------------------------------------------------------
    // EDIT EMPLOYEE (PT-2a)
    // ----------------------------------------------------------------

    private void editEmployee() {
        System.out.println("\n-- EDIT EMPLOYEE --");
        Employee e = selectEmployeeForAction("edit");
        if (e == null) return;
        int id = e.getEmpid();

        printEmployee(e);

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Mobile");
        System.out.println("5. Emergency Contact Name");
        System.out.println("6. Emergency Contact Mobile");
        System.out.println("7. Salary");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                System.out.print("New First Name: ");
                empController.updateEmployeeField(id, "Fname", scanner.nextLine().trim());
            }
            case "2" -> {
                System.out.print("New Last Name: ");
                empController.updateEmployeeField(id, "Lname", scanner.nextLine().trim());
            }
            case "3" -> {
                System.out.print("New Email: ");
                empController.updateEmployeeField(id, "email", scanner.nextLine().trim());
            }
            case "4" -> {
                System.out.print("New Mobile (###-###-####): ");
                empController.updateEmployeeField(id, "mobile", scanner.nextLine().trim());
            }
            case "5" -> {
                System.out.print("New Emergency Contact Name: ");
                empController.updateEmployeeField(id, "emergency_contact_name",
                    scanner.nextLine().trim());
            }
            case "6" -> {
                System.out.print("New Emergency Contact Mobile (###-###-####): ");
                empController.updateEmployeeField(id, "emergency_contact_mobile",
                    scanner.nextLine().trim());
            }
            case "7" -> {
                System.out.print("New Salary: ");
                double newSalary = InputValidator.parseDouble(scanner.nextLine());
                empController.updateEmployeeSalary(id, newSalary);
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private Employee selectEmployeeForAction(String actionName) {
        System.out.println("Locate employee to " + actionName + ":");
        System.out.println("1. Search by Employee ID");
        System.out.println("2. Search by Last Name");
        System.out.println("3. Search by First Name");
        System.out.println("4. Search by Date of Birth");
        System.out.println("5. Search by SSN");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        Employee selected = null;
        switch (choice) {
            case "1" -> {
                System.out.print("Enter Employee ID: ");
                selected = empController.searchByID(InputValidator.parseID(scanner.nextLine()));
            }
            case "2" -> {
                System.out.print("Enter Last Name: ");
                List<Employee> results = empController.searchByLastName(scanner.nextLine().trim());
                selected = selectFromResults(results);
            }
            case "3" -> {
                System.out.print("Enter First Name: ");
                List<Employee> results = empController.searchByFirstName(scanner.nextLine().trim());
                selected = selectFromResults(results);
            }
            case "4" -> {
                System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                List<Employee> results = empController.searchByDOB(scanner.nextLine().trim());
                selected = selectFromResults(results);
            }
            case "5" -> {
                System.out.print("Enter SSN (###-##-####): ");
                selected = empController.searchBySSN(scanner.nextLine().trim());
            }
            default -> System.out.println("Invalid option.");
        }
        return selected;
    }

    private Employee selectFromResults(List<Employee> results) {
        if (results.isEmpty()) return null;
        printEmployeeList(results);
        System.out.print("Enter Employee ID from the results list: ");
        return empController.searchByID(InputValidator.parseID(scanner.nextLine()));
    }

    // ----------------------------------------------------------------
    // DELETE EMPLOYEE (PT-2b)
    // ----------------------------------------------------------------

    private void deleteEmployee() {
        System.out.println("\n-- DELETE EMPLOYEE --");
        System.out.print("Enter Employee ID to delete: ");
        int id = InputValidator.parseID(scanner.nextLine());

        // Search first — show the record before asking to confirm
        Employee e = empController.searchByID(id);
        if (e == null) return;

        printEmployee(e);

        // Confirmation prompt — must type YES to proceed
        // This is the two-factor check before an irreversible action
        System.out.print("\nAre you sure you want to delete this employee? (YES to confirm): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("YES")) {
            empController.deleteEmployee(id);
        } else {
            System.out.println("Deletion cancelled. No changes made.");
        }
    }

    // ----------------------------------------------------------------
    // SALARY MENU (PT-2c and PT-6)
    // ----------------------------------------------------------------

    private void salaryMenu() {
        System.out.println("\n-- SALARY OPERATIONS --");
        System.out.println("1. Raise salaries BELOW a threshold");
        System.out.println("2. Raise salaries WITHIN a range");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> raiseBelowThreshold();
            case "2" -> raiseInRange();
            default  -> System.out.println("Invalid option.");
        }
    }

    private void raiseBelowThreshold() {
        System.out.println("\n-- RAISE SALARIES BELOW THRESHOLD (PT-2c) --");
        System.out.print("Enter salary threshold (employees BELOW this get the raise): $");
        double threshold = InputValidator.parseDouble(scanner.nextLine());

        System.out.print("Enter raise percentage (e.g. 10 for 10%): ");
        double pct = InputValidator.parseDouble(scanner.nextLine());

        System.out.printf("%nThis will raise all salaries below $%,.2f by %.1f%%.%n",
            threshold, pct);
        System.out.print("Confirm? (YES to proceed): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("YES")) {
            salaryController.raiseSalariesBelowThreshold(threshold, pct);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private void raiseInRange() {
        System.out.println("\n-- RAISE SALARIES IN RANGE (PT-6) --");
        System.out.print("Enter minimum salary: $");
        double min = InputValidator.parseDouble(scanner.nextLine());

        System.out.print("Enter maximum salary: $");
        double max = InputValidator.parseDouble(scanner.nextLine());

        System.out.print("Enter raise percentage (e.g. 5 for 5%): ");
        double pct = InputValidator.parseDouble(scanner.nextLine());

        System.out.printf("%nThis will raise all salaries between $%,.2f and $%,.2f by %.1f%%.%n",
            min, max, pct);
        System.out.print("Confirm? (YES to proceed): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("YES")) {
            salaryController.raiseSalariesInRange(min, max, pct);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // ----------------------------------------------------------------
    // REPORTS MENU
    // ----------------------------------------------------------------

    private void reportsMenu() {
        System.out.println("\n-- REPORTS --");
        System.out.println("1. New Employee Hires by Date Range");
        System.out.println("2. Total Pay by Job Title");
        System.out.println("3. Total Pay by Division");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> newHiresReport();
            case "2" -> payByJobTitleReport();
            case "3" -> payByDivisionReport();
            default  -> System.out.println("Invalid option.");
        }
    }

    private void newHiresReport() {
        System.out.println("\n-- NEW HIRES REPORT (PT-5) --");
        System.out.print("Start Date (YYYY-MM-DD): ");
        String start = scanner.nextLine().trim();
        System.out.print("End Date   (YYYY-MM-DD): ");
        String end = scanner.nextLine().trim();

        List<Employee> results = empController.getNewHiresReport(start, end);
        if (!results.isEmpty()) {
            System.out.println("\nNew Hires from " + start + " to " + end + ":");
            printEmployeeList(results);
        }
    }

    private void payByJobTitleReport() {
        System.out.println("\n-- TOTAL PAY BY JOB TITLE (PT-7) --");
        System.out.print("Enter Month (1-12): ");
        int month = InputValidator.parseID(scanner.nextLine());
        System.out.print("Enter Year (e.g. 2026): ");
        int year  = InputValidator.parseID(scanner.nextLine());
        salaryController.reportPayByJobTitle(month, year);
    }

    private void payByDivisionReport() {
        System.out.println("\n-- TOTAL PAY BY DIVISION (PT-8) --");
        System.out.print("Enter Month (1-12): ");
        int month = InputValidator.parseID(scanner.nextLine());
        System.out.print("Enter Year (e.g. 2026): ");
        int year  = InputValidator.parseID(scanner.nextLine());
        salaryController.reportPayByDivision(month, year);
    }

    // ----------------------------------------------------------------
    // VIEW ALL EMPLOYEES
    // ----------------------------------------------------------------

    private void viewAllEmployees() {
        System.out.println("\n-- ALL EMPLOYEES --");
        List<Employee> all = empController.getAllEmployees();
        printEmployeeList(all);
    }

    // ----------------------------------------------------------------
    // DISPLAY HELPERS
    // ----------------------------------------------------------------

    private void printEmployee(Employee e) {
        System.out.println("\n" + "─".repeat(60));
        System.out.printf("EmpID    : %d%n",      e.getEmpid());
        System.out.printf("Name     : %s %s%n",   e.getFname(), e.getLname());
        System.out.printf("Email    : %s%n",       e.getEmail());
        System.out.printf("Hire Date: %s%n",       e.getHireDate());
        System.out.printf("DOB      : %s%n",       e.getDob());
        System.out.printf("Salary   : $%,.2f%n",   e.getSalary());
        System.out.printf("Mobile   : %s%n",       e.getMobile());
        System.out.printf("Emergency: %s | %s%n",  e.getEmergencyContactName(),
                                                    e.getEmergencyContactMobile());
        System.out.println("─".repeat(60));
    }

    private void printEmployeeList(List<Employee> list) {
        if (list.isEmpty()) return;
        System.out.println("\n" + "─".repeat(65));
        System.out.printf("%-6s %-14s %-14s %-12s %s%n",
            "ID", "Last Name", "First Name", "Salary", "Hire Date");
        System.out.println("─".repeat(65));
        for (Employee e : list) {
            System.out.printf("%-6d %-14s %-14s $%,-10.2f %s%n",
                e.getEmpid(), e.getLname(), e.getFname(),
                e.getSalary(), e.getHireDate());
        }
        System.out.println("─".repeat(65));
        System.out.println("Total: " + list.size() + " employee(s)");
    }
}
