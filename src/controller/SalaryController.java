package controller;

import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import dao.PayrollDAO;
import dao.PayrollDAOImpl;
import model.Employee;
import util.DBConnection;
import util.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * SalaryController.java
 * Team: Cats | CSc3350 SP2026
 *
 * Handles all salary update operations.
 * Two features live here:
 *
 * PT-2c: Raise all salaries BELOW a threshold by a percentage
 *        Example: Give everyone under $50k a 10% raise
 *
 * PT-6:  Raise all salaries WITHIN a range by a percentage
 *        Example: Give everyone between $40k-$50k a 5% raise
 *
 * Both operations use a database TRANSACTION.
 * Transaction = all updates succeed together, or none of them do.
 * Think of it like a bank transfer — you don't want $500 to leave
 * your account without $500 arriving in the other account.
 * If any salary update fails midway, we ROLLBACK and nothing changes.
 */
public class SalaryController {

    private EmployeeDAO employeeDAO;
    private PayrollDAO  payrollDAO;

    public SalaryController() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.payrollDAO  = new PayrollDAOImpl();
    }

    // ----------------------------------------------------------------
    // PT-2c: RAISE ALL SALARIES BELOW THRESHOLD
    // ----------------------------------------------------------------

    /**
     * Applies a percentage raise to all employees earning less than threshold.
     *
     * Example: raiseSalariesBelowThreshold(50000, 10)
     *          -> everyone under $50,000 gets a 10% raise
     *          -> Snoopy $45,000 -> $49,500
     *          -> Peppermint Patti $98,000 -> UNCHANGED (above threshold)
     *
     * @param threshold    salary ceiling — employees BELOW this get the raise
     * @param raisePercent percentage to increase (e.g. 10 for 10%)
     * @return number of employees updated, -1 on error
     */
    public int raiseSalariesBelowThreshold(double threshold, double raisePercent) {

        // Validate inputs first
        if (!InputValidator.isValidSalary(threshold)) {
            System.out.println("ERROR: Threshold must be greater than 0.");
            return -1;
        }
        if (!InputValidator.isValidPercentage(raisePercent)) {
            System.out.println("ERROR: Percentage must be between 0 and 100.");
            return -1;
        }

        // Get all employees below threshold
        List<Employee> toUpdate = employeeDAO.getEmployeesBelowSalary(threshold);
        if (toUpdate.isEmpty()) {
            System.out.println("No employees found with salary below $" +
                String.format("%,.2f", threshold));
            return 0;
        }

        // Apply updates inside a transaction
        int updatedCount = 0;
        try {
            Connection conn = DBConnection.getInstance();
            conn.setAutoCommit(false); // START TRANSACTION

            for (Employee e : toUpdate) {
                double newSalary = Math.round(e.getSalary() * (1 + raisePercent / 100) * 100.0) / 100.0;
                boolean success = employeeDAO.updateSalary(e.getEmpid(), newSalary);
                if (!success) {
                    conn.rollback(); // something failed — undo everything
                    System.out.println("ERROR: Update failed for empID " + e.getEmpid() +
                        ". All changes rolled back.");
                    conn.setAutoCommit(true);
                    return -1;
                }
                System.out.printf("  EmpID %-4d | %-12s | $%,10.2f -> $%,10.2f%n",
                    e.getEmpid(), e.getLname(), e.getSalary(), newSalary);
                updatedCount++;
            }

            conn.commit(); // all succeeded — make it permanent
            conn.setAutoCommit(true);
            System.out.println("\n" + updatedCount + " salary/salaries updated successfully.");

        } catch (SQLException ex) {
            System.err.println("[SalaryController] Transaction error: " + ex.getMessage());
            return -1;
        }

        return updatedCount;
    }

    // ----------------------------------------------------------------
    // PT-6: RAISE ALL SALARIES WITHIN A RANGE
    // ----------------------------------------------------------------

    /**
     * Applies a percentage raise to all employees whose salary
     * falls between minSalary and maxSalary (inclusive).
     *
     * Example: raiseSalariesInRange(40000, 50000, 5)
     *          -> Linus $43,000 -> $45,150
     *          -> Snoopy $45,000 -> $47,250
     *          -> Charlie $48,000 -> $50,400
     *          -> PigPin $33,000 -> UNCHANGED (below range)
     *          -> Peppermint $98,000 -> UNCHANGED (above range)
     *
     * @param minSalary    lower bound of the range (inclusive)
     * @param maxSalary    upper bound of the range (inclusive)
     * @param raisePercent percentage to increase
     * @return number of employees updated, -1 on error
     */
    public int raiseSalariesInRange(double minSalary, double maxSalary, double raisePercent) {

        // Validate inputs
        if (!InputValidator.isValidSalaryRange(minSalary, maxSalary)) {
            System.out.println("ERROR: Invalid salary range. " +
                "Min must be greater than 0 and less than max.");
            return -1;
        }
        if (!InputValidator.isValidPercentage(raisePercent)) {
            System.out.println("ERROR: Percentage must be between 0 and 100.");
            return -1;
        }

        // Get all employees in range
        List<Employee> toUpdate = employeeDAO.getEmployeesInSalaryRange(minSalary, maxSalary);
        if (toUpdate.isEmpty()) {
            System.out.printf("No employees found with salary between $%,.2f and $%,.2f%n",
                minSalary, maxSalary);
            return 0;
        }

        // Apply updates inside a transaction
        int updatedCount = 0;
        try {
            Connection conn = DBConnection.getInstance();
            conn.setAutoCommit(false); // START TRANSACTION

            for (Employee e : toUpdate) {
                double newSalary = Math.round(e.getSalary() * (1 + raisePercent / 100) * 100.0) / 100.0;
                boolean success = employeeDAO.updateSalary(e.getEmpid(), newSalary);
                if (!success) {
                    conn.rollback();
                    System.out.println("ERROR: Update failed for empID " + e.getEmpid() +
                        ". All changes rolled back.");
                    conn.setAutoCommit(true);
                    return -1;
                }
                System.out.printf("  EmpID %-4d | %-12s | $%,10.2f -> $%,10.2f%n",
                    e.getEmpid(), e.getLname(), e.getSalary(), newSalary);
                updatedCount++;
            }

            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("\n" + updatedCount + " salary/salaries updated successfully.");

        } catch (SQLException ex) {
            System.err.println("[SalaryController] Transaction error: " + ex.getMessage());
            return -1;
        }

        return updatedCount;
    }

    // ----------------------------------------------------------------
    // PAYROLL REPORTS (PT-7, PT-8)
    // ----------------------------------------------------------------

    /**
     * Print total pay grouped by job title for a given month/year.
     */
    public void reportPayByJobTitle(int month, int year) {
        if (month < 1 || month > 12) {
            System.out.println("ERROR: Month must be between 1 and 12.");
            return;
        }
        if (year < 2000 || year > 2100) {
            System.out.println("ERROR: Invalid year.");
            return;
        }
        List<String[]> results = payrollDAO.getTotalPayByJobTitle(month, year);
        if (results.isEmpty()) {
            System.out.printf("No payroll records found for %02d/%d%n", month, year);
            return;
        }
        System.out.printf("%nTotal Pay by Job Title — %02d/%d%n", month, year);
        System.out.println("─".repeat(45));
        System.out.printf("%-30s %s%n", "Job Title", "Total Earnings");
        System.out.println("─".repeat(45));
        for (String[] row : results) {
            System.out.printf("%-30s $%,12.2f%n", row[0], Double.parseDouble(row[1]));
        }
        System.out.println("─".repeat(45));
    }

    /**
     * Print total pay grouped by division for a given month/year.
     */
    public void reportPayByDivision(int month, int year) {
        if (month < 1 || month > 12) {
            System.out.println("ERROR: Month must be between 1 and 12.");
            return;
        }
        if (year < 2000 || year > 2100) {
            System.out.println("ERROR: Invalid year.");
            return;
        }
        List<String[]> results = payrollDAO.getTotalPayByDivision(month, year);
        if (results.isEmpty()) {
            System.out.printf("No payroll records found for %02d/%d%n", month, year);
            return;
        }
        System.out.printf("%nTotal Pay by Division — %02d/%d%n", month, year);
        System.out.println("─".repeat(45));
        System.out.printf("%-25s %s%n", "Division", "Total Earnings");
        System.out.println("─".repeat(45));
        for (String[] row : results) {
            System.out.printf("%-25s $%,12.2f%n", row[0], Double.parseDouble(row[1]));
        }
        System.out.println("─".repeat(45));
    }
}
