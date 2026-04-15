package controller;

import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import dao.PayrollDAO;
import dao.PayrollDAOImpl;
import model.Employee;
import model.Payroll;
import util.InputValidator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * EmployeeController.java
 * Team: Cats | CSc3350 SP2026
 *
 * Handles all business logic for employee operations.
 * The UI calls methods here — this class validates inputs,
 * then calls the DAO. The UI never touches the DAO directly.
 *
 * Think of it like a manager at a restaurant:
 * - The waiter (UI) takes the order and hands it to the manager
 * - The manager (Controller) checks the order makes sense
 * - The kitchen (DAO) actually cooks it
 * - The manager brings the result back to the waiter
 *
 * Every method returns a result object or message string
 * so the UI knows what to display.
 */
public class EmployeeController {
    private static final Set<String> ALLOWED_UPDATE_FIELDS = new HashSet<>(Arrays.asList(
        "Fname", "Lname", "email", "mobile", "emergency_contact_name", "emergency_contact_mobile"
    ));

    private EmployeeDAO employeeDAO;
    private PayrollDAO  payrollDAO;

    public EmployeeController() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.payrollDAO  = new PayrollDAOImpl();
    }

    // ----------------------------------------------------------------
    // SEARCH OPERATIONS
    // ----------------------------------------------------------------

    /**
     * Search for one employee by empID.
     * Returns the Employee or null if not found.
     * Used as the first step in edit and delete workflows.
     */
    public Employee searchByID(int empid) {
        if (!InputValidator.isValidID(empid)) {
            System.out.println("Invalid employee ID.");
            return null;
        }
        Employee e = employeeDAO.findByID(empid);
        if (e == null) {
            System.out.println("No employee found with ID: " + empid);
        }
        return e;
    }

    /**
     * Search employees by last name.
     * Returns a list — empty list if nothing found.
     */
    public List<Employee> searchByLastName(String lname) {
        if (!InputValidator.isValidString(lname)) {
            System.out.println("Last name cannot be blank.");
            return List.of();
        }
        List<Employee> results = employeeDAO.findByLastName(lname);
        if (results.isEmpty()) {
            System.out.println("No employees found with last name: " + lname);
        }
        return results;
    }

    /**
     * Search employees by first name.
     */
    public List<Employee> searchByFirstName(String fname) {
        if (!InputValidator.isValidString(fname)) {
            System.out.println("First name cannot be blank.");
            return List.of();
        }
        List<Employee> results = employeeDAO.findByFirstName(fname);
        if (results.isEmpty()) {
            System.out.println("No employees found with first name: " + fname);
        }
        return results;
    }

    /**
     * Search employees by date of birth.
     */
    public List<Employee> searchByDOB(String dob) {
        if (!InputValidator.isValidDate(dob)) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return List.of();
        }
        List<Employee> results = employeeDAO.findByDOB(dob);
        if (results.isEmpty()) {
            System.out.println("No employees found with DOB: " + dob);
        }
        return results;
    }

    /**
     * Search one employee by SSN.
     */
    public Employee searchBySSN(String ssn) {
        if (!InputValidator.isValidSSN(ssn)) {
            System.out.println("Invalid SSN format. Use ###-##-####.");
            return null;
        }
        Employee employee = employeeDAO.findBySSN(ssn);
        if (employee == null) {
            System.out.println("No employee found with SSN: " + ssn);
        }
        return employee;
    }

    /**
     * Get all employees — for listings and reports.
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    // ----------------------------------------------------------------
    // ADD NEW EMPLOYEE (PT-9)
    // ----------------------------------------------------------------

    /**
     * Validates all fields then inserts a new employee.
     * Returns the new empID on success, -1 on failure.
     *
     * The empID is AUTO_INCREMENT in the DB so we never pass one in.
     * MySQL assigns it and we get it back from RETURN_GENERATED_KEYS.
     */
    public int addEmployee(Employee employee) {
        if (!InputValidator.isValidString(employee.getFname())) {
            System.out.println("ERROR: First name is required.");
            return -1;
        }
        if (!InputValidator.isValidString(employee.getLname())) {
            System.out.println("ERROR: Last name is required.");
            return -1;
        }
        if (!InputValidator.isValidString(employee.getEmail())) {
            System.out.println("ERROR: Email is required.");
            return -1;
        }
        if (!InputValidator.isValidEmail(employee.getEmail())) {
            System.out.println("ERROR: Email format is invalid.");
            return -1;
        }
        if (!InputValidator.isValidSSN(employee.getSsn())) {
            System.out.println("ERROR: SSN must be in format ###-##-####");
            return -1;
        }
        if (!InputValidator.isValidSalary(employee.getSalary())) {
            System.out.println("ERROR: Salary must be greater than 0.");
            return -1;
        }
        if (!InputValidator.isValidDate(employee.getHireDate())) {
            System.out.println("ERROR: Hire date must be in format YYYY-MM-DD");
            return -1;
        }
        if (!InputValidator.isValidDate(employee.getDob())) {
            System.out.println("ERROR: Date of birth must be in format YYYY-MM-DD");
            return -1;
        }
        if (!InputValidator.isValidPhone(employee.getMobile())) {
            System.out.println("ERROR: Mobile phone must be in format ###-###-####");
            return -1;
        }
        if (!InputValidator.isValidString(employee.getEmergencyContactName())) {
            System.out.println("ERROR: Emergency contact name is required.");
            return -1;
        }
        if (!InputValidator.isValidPhone(employee.getEmergencyContactMobile())) {
            System.out.println("ERROR: Emergency contact mobile must be in format ###-###-####");
            return -1;
        }
        if (!InputValidator.isValidString(employee.getStreet())) {
            System.out.println("ERROR: Street address is required.");
            return -1;
        }
        if (!InputValidator.isValidID(employee.getCityID())) {
            System.out.println("ERROR: City ID must be a positive integer.");
            return -1;
        }
        if (!InputValidator.isValidID(employee.getStateID())) {
            System.out.println("ERROR: State ID must be a positive integer.");
            return -1;
        }
        if (!InputValidator.isValidZip(employee.getZip())) {
            System.out.println("ERROR: ZIP code must be 5 digits or ZIP+4.");
            return -1;
        }
        if (!InputValidator.isValidID(employee.getDivisionID())) {
            System.out.println("ERROR: Division ID must be a positive integer.");
            return -1;
        }
        if (!InputValidator.isValidID(employee.getJobTitleID())) {
            System.out.println("ERROR: Job Title ID must be a positive integer.");
            return -1;
        }

        int newEmpID = employeeDAO.insertEmployee(employee);
        if (newEmpID > 0) {
            System.out.println("Employee added successfully. New empID: " + newEmpID);
        } else {
            System.out.println(
                "ERROR: Failed to add employee. Check address, division/job title IDs, and duplicate SSN/email."
            );
        }
        return newEmpID;
    }

    // ----------------------------------------------------------------
    // UPDATE EMPLOYEE FIELD (PT-2a)
    // ----------------------------------------------------------------

    /**
     * Update a single field for one employee.
     *
     * Valid fieldNames (must match DB column names exactly):
     * "Fname", "Lname", "email", "mobile",
     * "emergency_contact_name", "emergency_contact_mobile"
     *
     * Salary has its own method (updateSalary) because it's a DECIMAL.
     */
    public boolean updateEmployeeField(int empid, String fieldName, String newValue) {
        Employee existing = employeeDAO.findByID(empid);
        if (existing == null) {
            System.out.println("ERROR: Employee ID " + empid + " not found.");
            return false;
        }

        if (!ALLOWED_UPDATE_FIELDS.contains(fieldName)) {
            System.out.println("ERROR: Field " + fieldName + " is not updateable.");
            return false;
        }

        if (!InputValidator.isValidString(newValue)) {
            System.out.println("ERROR: Field value cannot be blank.");
            return false;
        }

        // Extra validation for specific fields
        if (fieldName.equals("email") && !InputValidator.isValidEmail(newValue)) {
            System.out.println("ERROR: Invalid email format.");
            return false;
        }
        if ((fieldName.equals("mobile") || fieldName.equals("emergency_contact_mobile"))
                && !InputValidator.isValidPhone(newValue)) {
            System.out.println("ERROR: Phone must be in format ###-###-####");
            return false;
        }

        boolean success = employeeDAO.updateField(empid, fieldName, newValue);
        if (success) {
            System.out.println("Employee " + empid + " " + fieldName + " updated successfully.");
        } else {
            System.out.println("ERROR: Update failed.");
        }
        return success;
    }

    public Employee searchOwnRecordByEmpID(int currentEmpID, int searchEmpID) {
        if (!InputValidator.isValidID(searchEmpID)) {
            System.out.println("ERROR: Employee ID must be a positive integer.");
            return null;
        }
        if (currentEmpID != searchEmpID) {
            System.out.println("No employee found for the entered search criteria.");
            return null;
        }
        return searchByID(currentEmpID);
    }

    public Employee searchOwnRecordByName(int currentEmpID, String name) {
        if (!InputValidator.isValidString(name)) {
            System.out.println("ERROR: Name is required.");
            return null;
        }
        Employee employee = searchByID(currentEmpID);
        if (employee == null) return null;

        String normalized = name.trim().toLowerCase();
        String first = employee.getFname().toLowerCase();
        String last = employee.getLname().toLowerCase();
        String full = (employee.getFname() + " " + employee.getLname()).toLowerCase();
        if (normalized.equals(first) || normalized.equals(last) || normalized.equals(full)) {
            return employee;
        }
        System.out.println("No employee found for the entered search criteria.");
        return null;
    }

    public Employee searchOwnRecordByDOB(int currentEmpID, String dob) {
        if (!InputValidator.isValidDate(dob)) {
            System.out.println("ERROR: Date of birth must be in format YYYY-MM-DD.");
            return null;
        }
        Employee employee = searchByID(currentEmpID);
        if (employee != null && dob.equals(employee.getDob())) {
            return employee;
        }
        System.out.println("No employee found for the entered search criteria.");
        return null;
    }

    public Employee searchOwnRecordBySSN(int currentEmpID, String ssn) {
        if (!InputValidator.isValidSSN(ssn)) {
            System.out.println("ERROR: SSN must be in format ###-##-####.");
            return null;
        }
        Employee employee = searchByID(currentEmpID);
        if (employee != null && ssn.equals(employee.getSsn())) {
            return employee;
        }
        System.out.println("No employee found for the entered search criteria.");
        return null;
    }

    /**
     * Update salary for one employee.
     * HR Admin only.
     */
    public boolean updateEmployeeSalary(int empid, double newSalary) {
        if (employeeDAO.findByID(empid) == null) {
            System.out.println("ERROR: Employee ID " + empid + " not found.");
            return false;
        }
        if (!InputValidator.isValidSalary(newSalary)) {
            System.out.println("ERROR: Salary must be greater than 0.");
            return false;
        }
        boolean success = employeeDAO.updateSalary(empid, newSalary);
        if (success) {
            System.out.printf("Employee %d salary updated to $%,.2f%n", empid, newSalary);
        }
        return success;
    }

    // ----------------------------------------------------------------
    // DELETE EMPLOYEE (PT-2b)
    // ----------------------------------------------------------------

    /**
     * Delete an employee by empID.
     * Caller is responsible for showing the confirmation prompt
     * before calling this method.
     *
     * Returns true if deleted, false if empID not found.
     */
    public boolean deleteEmployee(int empid) {
        Employee existing = employeeDAO.findByID(empid);
        if (existing == null) {
            System.out.println("No employee found with ID: " + empid);
            return false;
        }
        boolean success = employeeDAO.deleteEmployee(empid);
        if (success) {
            System.out.println("Employee " + empid + " (" +
                existing.getFname() + " " + existing.getLname() +
                ") deleted successfully.");
        }
        return success;
    }

    // ----------------------------------------------------------------
    // PAY HISTORY (PT-3)
    // ----------------------------------------------------------------

    /**
     * Get pay history for one employee, sorted newest first.
     * Used by both HR Admin (any empID) and general employee (own empID only).
     */
    public List<Payroll> getPayHistory(int empid) {
        if (!InputValidator.isValidID(empid)) {
            System.out.println("Invalid employee ID.");
            return List.of();
        }
        List<Payroll> history = payrollDAO.getPayHistory(empid);
        if (history.isEmpty()) {
            System.out.println("No payroll records found for employee ID: " + empid);
        }
        return history;
    }

    // ----------------------------------------------------------------
    // NEW HIRES REPORT (PT-5)
    // ----------------------------------------------------------------

    /**
     * Get all employees hired within a date range.
     * Both dates must be in YYYY-MM-DD format.
     */
    public List<Employee> getNewHiresReport(String startDate, String endDate) {
        if (!InputValidator.isValidDate(startDate) || !InputValidator.isValidDate(endDate)) {
            System.out.println("ERROR: Dates must be in format YYYY-MM-DD");
            return List.of();
        }
        List<Employee> results = employeeDAO.getEmployeesByHireDateRange(startDate, endDate);
        if (results.isEmpty()) {
            System.out.println("No new hires found between " + startDate + " and " + endDate);
        }
        return results;
    }
}
