package dao;

import model.Employee;
import java.util.List;
import java.sql.Connection;

/**
 * EmployeeDAO.java
 * Team: Cats | CSc3350 SP2026
 *
 * Interface defining all database operations for the employees table.
 * Think of this like a job posting — it says WHAT needs to be done
 * but not HOW. EmployeeDAOImpl is the actual hire who does the work.
 *
 * Every method that touches the employees table is declared here.
 */
public interface EmployeeDAO {

    /**
     * Insert a new employee into the database.
     * empID is AUTO_INCREMENT so we don't pass one in.
     * Returns the generated empID on success, -1 on failure.
     */
    int insertEmployee(Employee employee);

    /**
     * Find one employee by their empID.
     * Returns the Employee object, or null if not found.
     * This is the "search by ID" feature used in edit/delete.
     */
    Employee findByID(int empid);

    /**
     * Search employees by last name (partial match supported).
     * Returns a list — multiple employees can share a last name.
     * Example: findByLastName("B") returns Beagle, Brown, Blake, Bunny
     */
    List<Employee> findByLastName(String lname);

    /**
     * Search employees by date of birth.
     * Returns a list — multiple employees could share a DOB.
     */
    List<Employee> findByDOB(String dob);

    /**
     * Search for one employee by SSN.
     * SSNs should be unique in the database.
     */
    Employee findBySSN(String ssn);

    /**
     * Search employees by first name (partial match supported).
     */
    List<Employee> findByFirstName(String fname);

    /**
     * Get all employees — used for reports and full listings.
     */
    List<Employee> getAllEmployees();

    /**
     * Get all employees whose salary falls within a range.
     * Used for the salary range increase feature (PT-6).
     * Example: getSalaryRange(40000, 50000) returns Snoopy, Charlie, Linus
     */
    List<Employee> getEmployeesInSalaryRange(double minSalary, double maxSalary);

    /**
     * Get all employees whose salary is below a threshold.
     * Used for PT-2c (raise all salaries below X).
     */
    List<Employee> getEmployeesBelowSalary(double threshold);

    /**
     * Get employees hired within a date range.
     * Used for the new hires report (PT-5).
     */
    List<Employee> getEmployeesByHireDateRange(String startDate, String endDate);

    /**
     * Update a single field for one employee.
     * Returns true on success, false on failure.
     *
     * fieldName must match the exact column name in the DB:
     * "Fname", "Lname", "email", "mobile", etc.
     *
     * This is flexible enough to update ANY employee field
     * without needing a separate method for each one.
     */
    boolean updateField(int empid, String fieldName, String newValue);

    /**
     * Update the salary for one employee.
     * Separate from updateField because salary is a DECIMAL not VARCHAR.
     */
    boolean updateSalary(int empid, double newSalary);

    /**
     * Delete one employee by empID.
     * Returns true if a row was deleted, false if empID not found.
     */
    boolean deleteEmployee(int empid);

    /**
     * Update address for one employee
     * Used to allow ADMIN to edit any given employee's address PT-2
     */
    boolean updateAddress(int empid, String street, int cityID, int stateID, String zip);
    boolean updateSalary(Connection conn, int empid, double newSalary);
    boolean updateJobTitle(int empid, int jobTitleID);
    boolean updateDivision(int empid, int divID);
}
