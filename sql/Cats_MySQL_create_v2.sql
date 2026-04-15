-- ============================================================
-- Cats_MySQL_create_v2.sql
-- Team: Cats | CSc3350 SP2026
-- Description: Full schema with 3NF normalization, FK constraints,
--              addresses/cities/states tables, users table for login,
--              and AUTO_INCREMENT on empid
-- ============================================================

CREATE DATABASE IF NOT EXISTS cats;
USE cats;

-- Drop tables in reverse FK dependency order to avoid constraint errors
-- Think of it like unbuilding a house — you remove the roof before the walls
DROP TABLE IF EXISTS payroll;
DROP TABLE IF EXISTS employee_job_titles;
DROP TABLE IF EXISTS employee_division;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS states;
DROP TABLE IF EXISTS job_titles;
DROP TABLE IF EXISTS division;

-- ============================================================
-- LOOKUP TABLES (no dependencies — build these first)
-- ============================================================

-- states: all 50 US states as a lookup
-- Why separate? So 'Georgia' is never misspelled 'Georga' in an address
CREATE TABLE states (
    stateID     INT         NOT NULL AUTO_INCREMENT,
    abbrev      VARCHAR(2)  NOT NULL,
    state_name  VARCHAR(50) NOT NULL,
    PRIMARY KEY (stateID)
);

-- cities: city names as a lookup
-- Why separate? Two employees in 'Atlanta' reference cityID=1, not two copies of the string
CREATE TABLE cities (
    cityID      INT         NOT NULL AUTO_INCREMENT,
    city_name   VARCHAR(25) NOT NULL,
    PRIMARY KEY (cityID)
);

-- job_titles: maps a numeric ID to a job title string
CREATE TABLE job_titles (
    job_titleID     INT          NOT NULL,
    job_title       VARCHAR(125) NOT NULL,
    PRIMARY KEY (job_titleID)
);

-- division: company divisions with their office locations
CREATE TABLE division (
    divID           INT          NOT NULL,
    Name            VARCHAR(100) DEFAULT NULL,
    city            VARCHAR(50)  NOT NULL,
    addressLine1    VARCHAR(50)  NOT NULL,
    addressLine2    VARCHAR(50)  DEFAULT NULL,
    state           VARCHAR(50)  DEFAULT NULL,
    country         VARCHAR(50)  NOT NULL,
    postalCode      VARCHAR(15)  NOT NULL,
    PRIMARY KEY (divID)
);

-- users: login credentials for HR Admins and general employees
-- role = 'HR_ADMIN' or 'EMPLOYEE'
-- Think of this like the bouncer list — you check here before letting anyone in
CREATE TABLE users (
    userID      INT             NOT NULL AUTO_INCREMENT,
    username    VARCHAR(65)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,  -- store hashed password, never plaintext
    role        ENUM('HR_ADMIN', 'EMPLOYEE') NOT NULL DEFAULT 'EMPLOYEE',
    empid       INT             DEFAULT NULL,  -- links to employees table (NULL for admin accounts not tied to an employee)
    PRIMARY KEY (userID)
);

-- ============================================================
-- ADDRESSES (depends on cities and states)
-- ============================================================

-- addresses: normalized address storage
-- Why not just put address in employees? Because two roommates
-- (e.g., Snoopy and Charlie Brown both at addressID=1) would duplicate
-- the same street string twice. This way address is stored once, referenced twice.
CREATE TABLE addresses (
    addressID   INT         NOT NULL AUTO_INCREMENT,
    street      VARCHAR(100) NOT NULL,
    cityID      INT         NOT NULL,
    stateID     INT         NOT NULL,
    zip         VARCHAR(10) NOT NULL,
    PRIMARY KEY (addressID),
    FOREIGN KEY (cityID)    REFERENCES cities(cityID),
    FOREIGN KEY (stateID)   REFERENCES states(stateID)
);

-- ============================================================
-- EMPLOYEES (depends on addresses)
-- ============================================================

CREATE TABLE employees (
    empid                       INT             NOT NULL AUTO_INCREMENT,
    Fname                       VARCHAR(65)     NOT NULL,
    Lname                       VARCHAR(65)     NOT NULL,
    email                       VARCHAR(65)     NOT NULL UNIQUE,
    HireDate                    DATE,
    Salary                      DECIMAL(10,2)   NOT NULL,
    SSN                         VARCHAR(12)     NOT NULL UNIQUE,
    DOB                         DATE            NOT NULL,
    mobile                      VARCHAR(15)     NOT NULL,
    emergency_contact_name      VARCHAR(130)    NOT NULL,
    emergency_contact_mobile    VARCHAR(15)     NOT NULL,
    addressID                   INT             NOT NULL,
    PRIMARY KEY (empid),
    FOREIGN KEY (addressID) REFERENCES addresses(addressID)
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_empid
    FOREIGN KEY (empid) REFERENCES employees(empid)
    ON DELETE CASCADE;

-- ============================================================
-- JUNCTION / BRIDGE TABLES
-- (depend on employees, division, job_titles)
-- Think of junction tables like a Twitter 'follows' table —
-- it links two entities without duplicating either one's data
-- ============================================================

-- payroll: one row per pay period per employee
CREATE TABLE payroll (
    payID           INT             NOT NULL AUTO_INCREMENT,
    pay_date        DATE,
    earnings        DECIMAL(10,2),
    fed_tax         DECIMAL(10,2),
    fed_med         DECIMAL(10,2),
    fed_SS          DECIMAL(10,2),
    state_tax       DECIMAL(10,2),
    retire_401k     DECIMAL(10,2),
    health_care     DECIMAL(10,2),
    empid           INT,
    PRIMARY KEY (payID),
    FOREIGN KEY (empid) REFERENCES employees(empid)
);

-- employee_job_titles: links an employee to their job title
CREATE TABLE employee_job_titles (
    empid           INT NOT NULL,
    job_titleID     INT NOT NULL,
    PRIMARY KEY (empid, job_titleID),
    FOREIGN KEY (empid)        REFERENCES employees(empid),
    FOREIGN KEY (job_titleID)  REFERENCES job_titles(job_titleID)
);

-- employee_division: links an employee to their division
CREATE TABLE employee_division (
    empid   INT NOT NULL,
    divID   INT NOT NULL,
    PRIMARY KEY (empid),
    FOREIGN KEY (empid)    REFERENCES employees(empid),
    FOREIGN KEY (divID)    REFERENCES division(divID)
) COMMENT='links employee to a division';

-- ============================================================
-- INDEXES
-- Indexes speed up searches — like a book index vs reading every page
-- We index the columns we search most: name, SSN, DOB, HireDate
-- ============================================================

CREATE INDEX idx_emp_lname    ON employees(Lname);
CREATE INDEX idx_emp_ssn      ON employees(SSN);
CREATE INDEX idx_emp_dob      ON employees(DOB);
CREATE INDEX idx_emp_hiredate ON employees(HireDate);
CREATE INDEX idx_emp_email    ON employees(email);
CREATE INDEX idx_payroll_date ON payroll(pay_date);
CREATE INDEX idx_emp_division_divid ON employee_division(divID);
CREATE INDEX idx_emp_job_title_id ON employee_job_titles(job_titleID);
CREATE INDEX idx_users_empid ON users(empid);
