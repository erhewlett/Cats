package dao;

import model.Payroll;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PayrollDAOImpl.java
 * Team: Cats | CSc3350 SP2026
 *
 * Implements PayrollDAO with real SQL against the payroll table.
 */
public class PayrollDAOImpl implements PayrollDAO {

    // ----------------------------------------------------------------
    // HELPER — maps one ResultSet row to one Payroll object
    // ----------------------------------------------------------------
    private Payroll mapRow(ResultSet rs) throws SQLException {
        Payroll p = new Payroll();
        p.setPayID(rs.getInt("payID"));
        p.setEmpid(rs.getInt("empid"));
        p.setPayDate(rs.getString("pay_date"));
        p.setEarnings(rs.getDouble("earnings"));
        p.setFedTax(rs.getDouble("fed_tax"));
        p.setFedMed(rs.getDouble("fed_med"));
        p.setFedSS(rs.getDouble("fed_SS"));
        p.setStateTax(rs.getDouble("state_tax"));
        p.setRetire401k(rs.getDouble("retire_401k"));
        p.setHealthCare(rs.getDouble("health_care"));
        return p;
    }

    // ----------------------------------------------------------------
    // GET PAY HISTORY — sorted newest first (ORDER BY pay_date DESC)
    // ----------------------------------------------------------------
    @Override
    public List<Payroll> getPayHistory(int empid) {
        List<Payroll> results = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE empid = ? ORDER BY pay_date DESC";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, empid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            System.err.println("[PayrollDAO] getPayHistory error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // INSERT PAY RECORD
    // ----------------------------------------------------------------
    @Override
    public boolean insertPayRecord(Payroll p) {
        String sql = "INSERT INTO payroll " +
                     "(pay_date, empid, earnings, fed_tax, fed_med, fed_SS, " +
                     "state_tax, retire_401k, health_care) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getPayDate());
            ps.setInt(2,    p.getEmpid());
            ps.setDouble(3, p.getEarnings());
            ps.setDouble(4, p.getFedTax());
            ps.setDouble(5, p.getFedMed());
            ps.setDouble(6, p.getFedSS());
            ps.setDouble(7, p.getStateTax());
            ps.setDouble(8, p.getRetire401k());
            ps.setDouble(9, p.getHealthCare());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[PayrollDAO] insertPayRecord error: " + ex.getMessage());
        }
        return false;
    }

    // ----------------------------------------------------------------
    // TOTAL PAY BY JOB TITLE
    // Joins payroll -> employees -> employee_job_titles -> job_titles
    // Filters by month and year, groups by job title
    // ----------------------------------------------------------------
    @Override
    public List<String[]> getTotalPayByJobTitle(int month, int year) {
        List<String[]> results = new ArrayList<>();
        String sql =
            "SELECT jt.job_title, SUM(p.earnings) AS total_pay " +
            "FROM payroll p " +
            "JOIN employees e ON p.empid = e.empid " +
            "JOIN employee_job_titles ejt ON e.empid = ejt.empid " +
            "JOIN job_titles jt ON ejt.job_titleID = jt.job_titleID " +
            "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? " +
            "GROUP BY jt.job_title " +
            "ORDER BY total_pay DESC";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Each row is a String array: [jobTitle, totalPay]
                results.add(new String[]{
                    rs.getString("job_title"),
                    String.format("%.2f", rs.getDouble("total_pay"))
                });
            }
        } catch (SQLException ex) {
            System.err.println("[PayrollDAO] getTotalPayByJobTitle error: " + ex.getMessage());
        }
        return results;
    }

    // ----------------------------------------------------------------
    // TOTAL PAY BY DIVISION
    // Joins payroll -> employees -> employee_division -> division
    // ----------------------------------------------------------------
    @Override
    public List<String[]> getTotalPayByDivision(int month, int year) {
        List<String[]> results = new ArrayList<>();
        String sql =
            "SELECT d.Name, SUM(p.earnings) AS total_pay " +
            "FROM payroll p " +
            "JOIN employees e ON p.empid = e.empid " +
            "JOIN employee_division ed ON e.empid = ed.empid " +
            "JOIN division d ON ed.divID = d.divID " +
            "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? " +
            "GROUP BY d.Name " +
            "ORDER BY total_pay DESC";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new String[]{
                    rs.getString("Name"),
                    String.format("%.2f", rs.getDouble("total_pay"))
                });
            }
        } catch (SQLException ex) {
            System.err.println("[PayrollDAO] getTotalPayByDivision error: " + ex.getMessage());
        }
        return results;
    }
}
