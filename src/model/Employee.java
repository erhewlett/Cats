package model;

/**
 * Employee.java
 * Team: Cats | CSc3350 SP2026
 *
 * Represents one row from the employees table.
 * Think of this like a digital employee file folder —
 * every field in the folder matches a column in the database.
 *
 * No logic lives here. This class just holds data.
 * The DAO classes are responsible for reading/writing to the DB.
 * The Controller classes are responsible for business rules.
 */
public class Employee {

    // mirrors every column in the employees table
    private int     empid;
    private String  fname;
    private String  lname;
    private String  email;
    private String  hireDate;       // stored as String, formatted YYYY-MM-DD
    private double  salary;
    private String  ssn;
    private String  dob;            // date of birth, YYYY-MM-DD
    private String  mobile;
    private String  emergencyContactName;
    private String  emergencyContactMobile;
    private int     addressID;

    // Convenience fields used during new-employee creation/reporting joins.
    // These are not all stored directly on the employees table.
    private String  street;
    private int     cityID;
    private int     stateID;
    private String  zip;
    private int     divisionID;
    private int     jobTitleID;

    // ----------------------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------------------

    // Empty constructor — used when building an object field by field
    // e.g. Employee e = new Employee(); e.setFname("Snoopy");
    public Employee() {}

    // Full constructor — used when loading a complete row from the DB
    // e.g. Employee e = new Employee(1, "Snoopy", "Beagle", ...);
    public Employee(int empid, String fname, String lname, String email,
                    String hireDate, double salary, String ssn, String dob,
                    String mobile, String emergencyContactName,
                    String emergencyContactMobile, int addressID) {
        this.empid                  = empid;
        this.fname                  = fname;
        this.lname                  = lname;
        this.email                  = email;
        this.hireDate               = hireDate;
        this.salary                 = salary;
        this.ssn                    = ssn;
        this.dob                    = dob;
        this.mobile                 = mobile;
        this.emergencyContactName   = emergencyContactName;
        this.emergencyContactMobile = emergencyContactMobile;
        this.addressID              = addressID;
    }

    // ----------------------------------------------------------------
    // GETTERS
    // ----------------------------------------------------------------
    public int    getEmpid()                  { return empid; }
    public String getFname()                  { return fname; }
    public String getLname()                  { return lname; }
    public String getEmail()                  { return email; }
    public String getHireDate()               { return hireDate; }
    public double getSalary()                 { return salary; }
    public String getSsn()                    { return ssn; }
    public String getDob()                    { return dob; }
    public String getMobile()                 { return mobile; }
    public String getEmergencyContactName()   { return emergencyContactName; }
    public String getEmergencyContactMobile() { return emergencyContactMobile; }
    public int    getAddressID()              { return addressID; }
    public String getStreet()                 { return street; }
    public int    getCityID()                 { return cityID; }
    public int    getStateID()                { return stateID; }
    public String getZip()                    { return zip; }
    public int    getDivisionID()             { return divisionID; }
    public int    getJobTitleID()             { return jobTitleID; }

    // ----------------------------------------------------------------
    // SETTERS
    // ----------------------------------------------------------------
    public void setEmpid(int empid)                               { this.empid = empid; }
    public void setFname(String fname)                            { this.fname = fname; }
    public void setLname(String lname)                            { this.lname = lname; }
    public void setEmail(String email)                            { this.email = email; }
    public void setHireDate(String hireDate)                      { this.hireDate = hireDate; }
    public void setSalary(double salary)                          { this.salary = salary; }
    public void setSsn(String ssn)                                { this.ssn = ssn; }
    public void setDob(String dob)                                { this.dob = dob; }
    public void setMobile(String mobile)                          { this.mobile = mobile; }
    public void setEmergencyContactName(String name)              { this.emergencyContactName = name; }
    public void setEmergencyContactMobile(String mobile)          { this.emergencyContactMobile = mobile; }
    public void setAddressID(int addressID)                       { this.addressID = addressID; }
    public void setStreet(String street)                          { this.street = street; }
    public void setCityID(int cityID)                             { this.cityID = cityID; }
    public void setStateID(int stateID)                           { this.stateID = stateID; }
    public void setZip(String zip)                                { this.zip = zip; }
    public void setDivisionID(int divisionID)                     { this.divisionID = divisionID; }
    public void setJobTitleID(int jobTitleID)                     { this.jobTitleID = jobTitleID; }

    // ----------------------------------------------------------------
    // toString — useful for debugging and console display
    // ----------------------------------------------------------------
    @Override
    public String toString() {
        return String.format(
            "ID: %-4d | %-12s %-12s | Salary: $%,10.2f | Hired: %s",
            empid, fname, lname, salary, hireDate
        );
    }
}
