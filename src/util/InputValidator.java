package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * InputValidator.java
 * Team: Cats | CSc3350 SP2026
 *
 * Static utility class for validating all user inputs before
 * anything touches the database.
 *
 * Think of this like the bouncer at the door — nothing gets
 * into the database unless it passes validation first.
 * Every controller calls these methods before calling a DAO.
 *
 * All methods are static — you never need to instantiate this class.
 * Usage: InputValidator.isValidSSN("111-11-1111")
 */
public class InputValidator {

    // Private constructor — prevents anyone from doing new InputValidator()
    // This class is a toolbox, not an object
    private InputValidator() {}

    // ----------------------------------------------------------------
    // STRING / REQUIRED FIELD VALIDATION
    // ----------------------------------------------------------------

    /**
     * Checks that a string is not null and not blank.
     * Use this for any required text field — name, email, etc.
     *
     * Example: isValidString("") returns false
     *          isValidString("Snoopy") returns true
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Checks that a string does not exceed a maximum length.
     * Matches the VARCHAR limits in the database schema.
     *
     * Example: isValidLength("Snoopy", 65) returns true
     *          isValidLength("a very long name...", 5) returns false
     */
    public static boolean isValidLength(String value, int maxLength) {
        return value != null && value.trim().length() <= maxLength;
    }

    // ----------------------------------------------------------------
    // SSN VALIDATION
    // ----------------------------------------------------------------

    /**
     * Validates SSN format: ###-##-####
     * Must be exactly that pattern — 3 digits, dash, 2 digits, dash, 4 digits.
     *
     * Example: isValidSSN("111-11-1111") returns true
     *          isValidSSN("111111111")   returns false
     *          isValidSSN(null)          returns false
     */
    public static boolean isValidSSN(String ssn) {
        if (ssn == null) return false;
        return ssn.matches("\\d{3}-\\d{2}-\\d{4}");
    }

    // ----------------------------------------------------------------
    // DATE VALIDATION
    // ----------------------------------------------------------------

    /**
     * Validates date format: YYYY-MM-DD
     * This matches MySQL DATE column format.
     *
     * Example: isValidDate("2022-08-01") returns true
     *          isValidDate("08/01/2022") returns false
     *          isValidDate("2022-13-01") returns false (month 13 doesn't exist)
     */
    public static boolean isValidDate(String date) {
        if (date == null) return false;
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        try {
            LocalDate parsed = LocalDate.parse(date);
            return parsed.getYear() >= 1900 && parsed.getYear() <= 2100;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    // ----------------------------------------------------------------
    // SALARY VALIDATION
    // ----------------------------------------------------------------

    /**
     * Validates that salary is a positive number.
     *
     * Example: isValidSalary(45000.00) returns true
     *          isValidSalary(-1000.00) returns false
     *          isValidSalary(0)        returns false
     */
    public static boolean isValidSalary(double salary) {
        return salary > 0;
    }

    /**
     * Validates a salary range — min must be less than max,
     * both must be positive.
     * Used for the salary range increase feature (PT-6).
     *
     * Example: isValidSalaryRange(40000, 50000) returns true
     *          isValidSalaryRange(50000, 40000) returns false (inverted)
     *          isValidSalaryRange(-1000, 50000) returns false (negative min)
     */
    public static boolean isValidSalaryRange(double min, double max) {
        return min > 0 && max > 0 && min < max;
    }

    /**
     * Validates a percentage increase value.
     * Must be greater than 0 and realistically not more than 100%.
     *
     * Example: isValidPercentage(10.0) returns true
     *          isValidPercentage(0)    returns false
     *          isValidPercentage(-5)   returns false
     */
    public static boolean isValidPercentage(double pct) {
        return pct > 0 && pct <= 100;
    }

    // ----------------------------------------------------------------
    // ID VALIDATION
    // ----------------------------------------------------------------

    /**
     * Validates that an ID is a positive integer.
     * Used for empID, divID, jobTitleID lookups.
     *
     * Example: isValidID(1)  returns true
     *          isValidID(0)  returns false
     *          isValidID(-1) returns false
     */
    public static boolean isValidID(int id) {
        return id > 0;
    }

    /**
     * Tries to parse a String as an integer ID.
     * Returns -1 if the string is not a valid integer.
     * Use this when reading empID input from the console.
     *
     * Example: parseID("4")    returns 4
     *          parseID("abc")  returns -1
     *          parseID("")     returns -1
     */
    public static int parseID(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Tries to parse a String as a double (for salary, percentage inputs).
     * Returns -1.0 if the string is not a valid number.
     *
     * Example: parseDouble("45000.00") returns 45000.0
     *          parseDouble("abc")      returns -1.0
     */
    public static double parseDouble(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    // ----------------------------------------------------------------
    // EMAIL VALIDATION
    // ----------------------------------------------------------------

    /**
     * Basic email format check — must contain @ and a dot after it.
     * Not a full RFC email validator — good enough for a class project.
     *
     * Example: isValidEmail("snoopy@example.com") returns true
     *          isValidEmail("notanemail")          returns false
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // ----------------------------------------------------------------
    // PHONE VALIDATION
    // ----------------------------------------------------------------

    /**
     * Validates phone format: ###-###-####
     *
     * Example: isValidPhone("404-111-0001") returns true
     *          isValidPhone("4041110001")   returns false
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("\\d{3}-\\d{3}-\\d{4}");
    }

    public static boolean isValidZip(String zip) {
        if (zip == null) return false;
        return zip.matches("\\d{5}(-\\d{4})?");
    }
}
