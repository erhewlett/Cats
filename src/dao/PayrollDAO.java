package dao;

import model.Payroll;
import java.util.List;

/**
 * PayrollDAO.java
 * Team: Cats | CSc3350 SP2026
 *
 * Interface for all payroll table operations.
 */
public interface PayrollDAO {

    /**
     * Get all pay records for one employee, sorted newest first.
     * This is what powers the "view my pay history" feature (PT-3).
     * Example: getPayHistory(1) returns Snoopy's pay stubs newest to oldest.
     */
    List<Payroll> getPayHistory(int empid);

    /**
     * Insert a new payroll record for an employee.
     * Returns true on success.
     */
    boolean insertPayRecord(Payroll payroll);

    /**
     * Get total earnings for a given month grouped by job title.
     * Used for the "total pay by job title" report (PT-7).
     * Returns a 2D array: each row is [jobTitle, totalEarnings]
     */
    List<String[]> getTotalPayByJobTitle(int month, int year);

    /**
     * Get total earnings for a given month grouped by division.
     * Used for the "total pay by division" report (PT-8).
     * Returns a 2D array: each row is [divisionName, totalEarnings]
     */
    List<String[]> getTotalPayByDivision(int month, int year);
}
