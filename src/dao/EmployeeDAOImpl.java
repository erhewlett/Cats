package dao;

import model.Employee;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAOImpl.java
 * Team: Cats | CSc3350 SP2026
 *
 * Implements EmployeeDAO — this is where actual SQL runs.
 * Every method opens a PreparedStatement, executes it, and maps
 * the ResultSet back into Employee objects.
 *
 * PreparedStatement vs regular Statement:
 * Regular:  "SELECT * FROM employees WHERE empid = " + userInput
 *           DANGEROUS — user could type "1 OR 1=1" and get all rows (SQL injection)
 * Prepared: "SELECT * FROM employees WHERE empid = ?"
 *           Safe — the ? is a placeholder, input is never treated as SQL
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    // ----------------------------------------------------------------
    // HELPER — maps one ResultSet row to one Employee object
    // Used by every SELECT method so we don't repeat this mapping
    // Think of it like a translator: DB row -> Java object
    // ----------------------------------------------------------------
    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setEmpid(rs.getInt("empid"));
        e.setFname(rs.getString("Fname"));
        e.setLname(rs.getString("Lname"));
        e.setEmail(rs.getString("email"));
        e.setHireDate(rs.getString("HireDate"));
        e.setSalary(rs.getDouble("Salary"));
        e.setSsn(rs.getString("SSN"));
        e.setDob(rs.getString("DOB"));
        e.setMobile(rs.getString("mobile"));
        e.setEmergencyContactName(rs.getString("emergency_contact_name"));
        e.setEmergencyContactMobile(rs.getString("emergency_contact_mobile"));
        e.setAddressID(rs.getInt("addressID"));
        return e;
    }

    private int findExistingAddressID(Connection conn, Employee e) throws SQLException {
        String sql =
            "SELECT addressID FROM addresses WHERE street = ? AND cityID = ? AND stateID = ? AND zip = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getStreet());
            ps.setInt(2, e.getCityID());
            ps.setInt(3, e.getStateID());
            ps.setString(4, e.getZip());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("addressID");
                }
            }
        }
        return -1;
    }

    private int insertAddress(Connection conn, Employee e) throws SQLException {
        int existingAddressID = findExistingAddressID(conn, e);
        if (existingAddressID > 0) {
            return existingAddressID;
        }

        String sql = "INSERT INTO addresses (street, cityID, stateID, zip) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getStreet());
            ps.setInt(2, e.getCityID());
            ps.setInt(3, e.getStateID());
            ps.setString(4, e.getZip());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Address insert did not return a generated key.");
    }

    private void insertEmployeeDivision(Connection conn, int empid, int divisionID) throws SQLException {
        String sql = "INSERT INTO employee_division (empid, divID) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empid);
            ps.setInt(2, divisionID);
            ps.executeUpdate();
        }
    }

    private void insertEmployeeJobTitle(Connection conn, int empid, int jobTitleID) throws SQLException {
        String sql = "INSERT INTO employee_job_titles (empid, job_titleID) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empid);
            ps.setInt(2, jobTitleID);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    // ADDRESS HELPERS - for updating employee address
    // ----------------------------------------------------------------
    /**
     * twin helper which is able to take strings as input (Method Overloading)
     * creates a temporary Employee to work with existing insertAddress logic
     */
    private int insertAddress(Connection conn, String street, int cityID, int stateID, String zip) throws SQLException {
        Employee temp = new Employee();
        temp.setStreet(street);
        temp.setCityID(cityID);
        temp.setStateID(stateID);
        temp.setZip(zip);

        return insertAddress(conn, temp);
    }

    /**
     * implement address update
     */
    @Override
    public boolean updateAddress(int empid, String street, int cityID, int stateID, String zip) {
        String sql = "UPDATE employees SET addressID = ? WHERE empid = ?";
        try {
            Connection conn = DBConnection.getInstance();
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);  // begin transaction

            try {
                // get address ID
                int targetAddressID = insertAddress(conn, street, cityID, stateID, zip);

                // link employee to the address
                try (PreparedStatement ps = conn.prepareStatement(sql)){
                    ps.setInt(1, targetAddressID);
                    ps.setInt(2, empid);
                    int rows = ps.executeUpdate();

                    conn.commit();
                    return rows > 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException e) {
            System.err.println("[EmployeeDAO] updateAddress error: " + e.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------------
    // INSERT
    // ----------------------------------------------------------------
    @Override
    public int insertEmployee(Employee e) {
        String sql = "INSERT INTO employees " +
                     "(Fname, Lname, email, HireDate, Salary, SSN, DOB, " +
                     "mobile, emergency_contact_name, emergency_contact_mobile, addressID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getInstance();
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try {
                int addressID = insertAddress(conn, e);
                e.setAddressID(addressID);

                try (PreparedStatement ps =
                         conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1,  e.getFname());
                    ps.setString(2,  e.getLname());
                    ps.setString(3,  e.getEmail());
                    ps.setString(4,  e.getHireDate());
                    ps.setDouble(5,  e.getSalary());
                    ps.setString(6,  e.getSsn());
                    ps.setString(7,  e.getDob());
                    ps.setString(8,  e.getMobile());
                    ps.setString(9,  e.getEmergencyContactName());
                    ps.setString(10, e.getEmergencyContactMobile());
                    ps.setInt(11,    e.getAddressID());

                    int rows = ps.executeUpdate();
                    if (rows <= 0) {
                        conn.rollback();
                        return -1;
                    }

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            int newEmpID = keys.getInt(1);
                            insertEmployeeDivision(conn, newEmpID, e.getDivisionID());
                            insertEmployeeJobTitle(conn, newEmpID, e.getJobTitleID());
                            conn.commit();
                            conn.setAutoCommit(originalAutoCommit);
                            return newEmpID;
                        }
                    }
                }

                conn.rollback();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] insertEmployee error: " + ex.getMessage());
        }
        return -1;
    }

    // ----------------------------------------------------------------
    // FIND BY ID
    // ----------------------------------------------------------------
    @Override
    public Employee findByID(int empid) {
        String sql = "SELECT * FROM employees WHERE empid = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] findByID error: " + ex.getMessage());
        }
        return null; // null means not found — caller checks for this
    }

    // ----------------------------------------------------------------
    // FIND BY LAST NAME (partial match using LIKE)
    // ----------------------------------------------------------------
    @Override
    public List<Employee> findByLastName(String lname) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE Lname LIKE ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            // % is a wildcard — "B%" matches Beagle, Brown, Blake, Bunny
            ps.setString(1, lname + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] findByLastName error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // FIND BY FIRST NAME (partial match)
    // ----------------------------------------------------------------
    @Override
    public List<Employee> findByFirstName(String fname) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE Fname LIKE ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fname + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] findByFirstName error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // FIND BY DOB
    // ----------------------------------------------------------------
    @Override
    public List<Employee> findByDOB(String dob) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE DOB = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dob);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] findByDOB error: " + ex.getMessage());
        }
        return results;
    }

    @Override
    public Employee findBySSN(String ssn) {
        String sql = "SELECT * FROM employees WHERE SSN = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] findBySSN error: " + ex.getMessage());
        }
        return null;
    }

    // ----------------------------------------------------------------
    // GET ALL EMPLOYEES
    // ----------------------------------------------------------------
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY Lname, Fname";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] getAllEmployees error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // GET EMPLOYEES IN SALARY RANGE
    // ----------------------------------------------------------------
    @Override
    public List<Employee> getEmployeesInSalaryRange(double minSalary, double maxSalary) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE Salary BETWEEN ? AND ? ORDER BY Salary";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, minSalary);
            ps.setDouble(2, maxSalary);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] getEmployeesInSalaryRange error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // GET EMPLOYEES BELOW SALARY THRESHOLD
    // ----------------------------------------------------------------
    @Override
    public List<Employee> getEmployeesBelowSalary(double threshold) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE Salary < ? ORDER BY Salary";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, threshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] getEmployeesBelowSalary error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // GET EMPLOYEES BY HIRE DATE RANGE
    // ----------------------------------------------------------------
    @Override
    public List<Employee> getEmployeesByHireDateRange(String startDate, String endDate) {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE HireDate BETWEEN ? AND ? ORDER BY HireDate";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] getEmployeesByHireDateRange error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // UPDATE FIELD (generic — works for any VARCHAR column)
    // ----------------------------------------------------------------
    @Override
    public boolean updateField(int empid, String fieldName, String newValue) {
        // We build the SQL dynamically here using the fieldName.
        // This is the ONE place in the app where we do this —
        // it's safe because fieldName comes from our own menu code,
        // never directly from user typed input.
        String sql = "UPDATE employees SET " + fieldName + " = ? WHERE empid = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newValue);
            ps.setInt(2, empid);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] updateField error: " + ex.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------------
    // UPDATE SALARY
    // ----------------------------------------------------------------
    @Override
    public boolean updateSalary(int empid, double newSalary) {
        String sql = "UPDATE employees SET Salary = ? WHERE empid = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, newSalary);
            ps.setInt(2, empid);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] updateSalary error: " + ex.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------
    @Override
    public boolean deleteEmployee(int empid) {
        String deletePayrollSql = "DELETE FROM payroll WHERE empid = ?";
        String deleteJobSql = "DELETE FROM employee_job_titles WHERE empid = ?";
        String deleteDivisionSql = "DELETE FROM employee_division WHERE empid = ?";
        String deleteUsersSql = "DELETE FROM users WHERE empid = ?";
        String deleteEmployeeSql = "DELETE FROM employees WHERE empid = ?";
        try {
            Connection conn = DBConnection.getInstance();
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(deletePayrollSql)) {
                    ps.setInt(1, empid);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteJobSql)) {
                    ps.setInt(1, empid);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteDivisionSql)) {
                    ps.setInt(1, empid);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteUsersSql)) {
                    ps.setInt(1, empid);
                    ps.executeUpdate();
                }

                int rows;
                try (PreparedStatement ps = conn.prepareStatement(deleteEmployeeSql)) {
                    ps.setInt(1, empid);
                    rows = ps.executeUpdate();
                }

                conn.commit();
                conn.setAutoCommit(originalAutoCommit);
                return rows > 0;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException ex) {
            System.err.println("[EmployeeDAO] deleteEmployee error: " + ex.getMessage());
        }
        return false;
    }
}
