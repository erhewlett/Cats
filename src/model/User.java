package model;

/**
 * User.java
 * Team: Cats | CSc3350 SP2026
 *
 * Represents one row from the users table.
 * Holds login credentials and role for one system user.
 *
 * Role is stored as a String matching the DB ENUM: "HR_ADMIN" or "EMPLOYEE"
 *
 * IMPORTANT: The password field stores the HASHED password from the DB.
 * We never store or compare plaintext passwords anywhere in the app.
 * When a user logs in we hash what they typed and compare hashes.
 */
public class User {

    private int    userID;
    private String username;
    private String password;   // always the hashed version
    private String role;       // "HR_ADMIN" or "EMPLOYEE"
    private int    empid;      // links back to employees table

    // ----------------------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------------------

    public User() {}

    public User(int userID, String username, String password,
                String role, int empid) {
        this.userID   = userID;
        this.username = username;
        this.password = password;
        this.role     = role;
        this.empid    = empid;
    }

    // ----------------------------------------------------------------
    // CONVENIENCE METHODS
    // ----------------------------------------------------------------

    // isAdmin() makes role checks readable in the controller:
    // if (user.isAdmin()) { ... } instead of if (user.getRole().equals("HR_ADMIN"))
    public boolean isAdmin() {
        return "HR_ADMIN".equals(role);
    }

    public boolean isEmployee() {
        return "EMPLOYEE".equals(role);
    }

    // ----------------------------------------------------------------
    // GETTERS & SETTERS
    // ----------------------------------------------------------------
    public int    getUserID()               { return userID; }
    public String getUsername()             { return username; }
    public String getPassword()             { return password; }
    public String getRole()                 { return role; }
    public int    getEmpid()               { return empid; }

    public void setUserID(int userID)       { this.userID = userID; }
    public void setUsername(String u)       { this.username = u; }
    public void setPassword(String p)       { this.password = p; }
    public void setRole(String role)        { this.role = role; }
    public void setEmpid(int empid)         { this.empid = empid; }

    @Override
    public String toString() {
        return String.format("User: %-12s | Role: %s | EmpID: %d",
            username, role, empid);
    }
}
