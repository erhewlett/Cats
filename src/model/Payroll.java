package model;

/**
 * Payroll.java
 * Team: Cats | CSc3350 SP2026
 *
 * Represents one row from the payroll table.
 * One Payroll object = one paycheck for one employee.
 *
 * Example: payID=1, empid=1 (Snoopy), pay_date=2026-01-31,
 *          earnings=865.38, fed_tax=276.92, ...
 */
public class Payroll {

    private int    payID;
    private int    empid;
    private String payDate;
    private double earnings;
    private double fedTax;
    private double fedMed;
    private double fedSS;
    private double stateTax;
    private double retire401k;
    private double healthCare;

    // ----------------------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------------------

    public Payroll() {}

    public Payroll(int payID, int empid, String payDate, double earnings,
                   double fedTax, double fedMed, double fedSS,
                   double stateTax, double retire401k, double healthCare) {
        this.payID      = payID;
        this.empid      = empid;
        this.payDate    = payDate;
        this.earnings   = earnings;
        this.fedTax     = fedTax;
        this.fedMed     = fedMed;
        this.fedSS      = fedSS;
        this.stateTax   = stateTax;
        this.retire401k = retire401k;
        this.healthCare = healthCare;
    }

    // ----------------------------------------------------------------
    // CALCULATED FIELD
    // Net pay = earnings minus all deductions
    // This is calculated on the fly — we don't store it in the DB
    // because it can always be derived from the other columns
    // ----------------------------------------------------------------
    public double getNetPay() {
        return earnings - fedTax - fedMed - fedSS - stateTax - retire401k - healthCare;
    }

    // ----------------------------------------------------------------
    // GETTERS
    // ----------------------------------------------------------------
    public int    getPayID()      { return payID; }
    public int    getEmpid()      { return empid; }
    public String getPayDate()    { return payDate; }
    public double getEarnings()   { return earnings; }
    public double getFedTax()     { return fedTax; }
    public double getFedMed()     { return fedMed; }
    public double getFedSS()      { return fedSS; }
    public double getStateTax()   { return stateTax; }
    public double getRetire401k() { return retire401k; }
    public double getHealthCare() { return healthCare; }

    // ----------------------------------------------------------------
    // SETTERS
    // ----------------------------------------------------------------
    public void setPayID(int payID)           { this.payID = payID; }
    public void setEmpid(int empid)           { this.empid = empid; }
    public void setPayDate(String payDate)    { this.payDate = payDate; }
    public void setEarnings(double earnings)  { this.earnings = earnings; }
    public void setFedTax(double fedTax)      { this.fedTax = fedTax; }
    public void setFedMed(double fedMed)      { this.fedMed = fedMed; }
    public void setFedSS(double fedSS)        { this.fedSS = fedSS; }
    public void setStateTax(double stateTax)  { this.stateTax = stateTax; }
    public void setRetire401k(double r)       { this.retire401k = r; }
    public void setHealthCare(double h)       { this.healthCare = h; }

    // ----------------------------------------------------------------
    // toString — formatted pay stub style for console display
    // ----------------------------------------------------------------
    @Override
    public String toString() {
        return String.format(
            "Pay Date: %s | Gross: $%7.2f | Fed Tax: $%6.2f | " +
            "SS: $%5.2f | Med: $%5.2f | State: $%6.2f | " +
            "401k: $%5.2f | Health: $%5.2f | Net: $%7.2f",
            payDate, earnings, fedTax, fedSS, fedMed,
            stateTax, retire401k, healthCare, getNetPay()
        );
    }
}
