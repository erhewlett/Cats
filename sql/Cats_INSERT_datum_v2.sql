-- ============================================================
-- Cats_INSERT_datum_v2.sql
-- Team: Cats | CSc3350 SP2026
-- Description: Seed data for all tables in dependency order.
--              Run AFTER Cats_MySQL_create_v2.sql
-- ============================================================

USE cats;
SET FOREIGN_KEY_CHECKS=0;

-- ============================================================
-- STATES (all 50)
-- stateID will auto-increment 1-50
-- ============================================================

INSERT INTO states (abbrev, state_name) VALUES
('AL','Alabama'),('AK','Alaska'),('AZ','Arizona'),('AR','Arkansas'),
('CA','California'),('CO','Colorado'),('CT','Connecticut'),('DE','Delaware'),
('FL','Florida'),('GA','Georgia'),('HI','Hawaii'),('ID','Idaho'),
('IL','Illinois'),('IN','Indiana'),('IA','Iowa'),('KS','Kansas'),
('KY','Kentucky'),('LA','Louisiana'),('ME','Maine'),('MD','Maryland'),
('MA','Massachusetts'),('MI','Michigan'),('MN','Minnesota'),('MS','Mississippi'),
('MO','Missouri'),('MT','Montana'),('NE','Nebraska'),('NV','Nevada'),
('NH','New Hampshire'),('NJ','New Jersey'),('NM','New Mexico'),('NY','New York'),
('NC','North Carolina'),('ND','North Dakota'),('OH','Ohio'),('OK','Oklahoma'),
('OR','Oregon'),('PA','Pennsylvania'),('RI','Rhode Island'),('SC','South Carolina'),
('SD','South Dakota'),('TN','Tennessee'),('TX','Texas'),('UT','Utah'),
('VT','Vermont'),('VA','Virginia'),('WA','Washington'),('WV','West Virginia'),
('WI','Wisconsin'),('WY','Wyoming');

-- ============================================================
-- CITIES
-- cityID auto-increments. We note the IDs in comments so
-- addresses inserts below are readable.
-- ============================================================

-- cityID=1: Atlanta
-- cityID=2: New York
-- cityID=3: Hollywood
-- cityID=4: Burbank
-- cityID=5: Springfield
INSERT INTO cities (city_name) VALUES
('Atlanta'),       -- 1
('New York'),      -- 2
('Hollywood'),     -- 3
('Burbank'),       -- 4
('Springfield');   -- 5

-- ============================================================
-- DIVISION
-- ============================================================

INSERT INTO division (divID, Name, city, addressLine1, addressLine2, state, country, postalCode)
VALUES
(1,   'Technology Engineering', 'Atlanta',  '200 17th Street NW',  '', 'GA', 'USA', '30363'),
(2,   'Marketing',              'Atlanta',  '200 17th Street NW',  '', 'GA', 'USA', '30363'),
(3,   'Human Resources',        'New York', '45 West 57th Street', '', 'NY', 'USA', '10019'),
(999, 'HQ',                     'New York', '45 West 57th Street', '', 'NY', 'USA', '10019');

-- ============================================================
-- JOB TITLES
-- ============================================================

INSERT INTO job_titles (job_titleID, job_title) VALUES
(100, 'software manager'),
(101, 'software architect'),
(102, 'software engineer'),
(103, 'software developer'),
(200, 'marketing manager'),
(201, 'marketing associate'),
(202, 'marketing assistant'),
(900, 'Chief Exec. Officer'),
(901, 'Chief Finn. Officer'),
(902, 'Chief Info. Officer');

-- ============================================================
-- ADDRESSES
-- GA stateID=10 (Georgia is 10th in alphabetical list)
-- NY stateID=32 (New York is 32nd)
-- CA stateID=5  (California is 5th)
-- Atlanta cityID=1, New York cityID=2, Hollywood cityID=3
-- Burbank cityID=4, Springfield cityID=5
--
-- addressID auto-increments 1-9
-- Multiple employees can share an addressID (roommates concept)
-- ============================================================

INSERT INTO addresses (street, cityID, stateID, zip) VALUES
('123 Doghouse Lane',       1, 10, '30301'),   -- addressID=1  (Snoopy, Charlie, Linus share this)
('456 Psychiatric Booth',   1, 10, '30302'),   -- addressID=2  (Lucy)
('789 Baseball Diamond Rd', 1, 10, '30303'),   -- addressID=3  (Peppermint Patti, PigPin)
('100 Mystery Machine Way', 3,  5, '90028'),   -- addressID=4  (Scooby, Daphne)
('200 Shaggy Snacks Blvd',  3,  5, '90029'),   -- addressID=5  (Shaggy, Bugs)
('300 Velma Smart Ave',     4,  5, '91502'),   -- addressID=6  (Velma)
('400 Porky Pig Court',     5, 14, '62701'),   -- addressID=7  (Porky) -- IL stateID=14
('500 Elmer Fudd Rd',       5, 14, '62702'),   -- addressID=8  (Elmer)
('600 Martian Colony Base', 2, 32, '10001');   -- addressID=9  (Marvin)

-- ============================================================
-- EMPLOYEES
-- empid will auto-increment but we set explicit values to match
-- the original dataset so payroll/division/job_title inserts work.
-- DOB values are fictional — kept consistent with cartoon lore.
-- ============================================================

INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN, DOB, mobile, emergency_contact_name, emergency_contact_mobile, addressID)
VALUES
(1,  'Snoopy',     'Beagle',   'Snoopy@example.com',     '2022-08-01', 45000.00, '111-11-1111', '1950-10-04', '404-111-0001', 'Woodstock Bird',    '404-111-9001', 1),
(2,  'Charlie',    'Brown',    'Charlie@example.com',     '2022-07-01', 48000.00, '111-22-1111', '1950-10-02', '404-111-0002', 'Sally Brown',       '404-111-9002', 1),
(3,  'Lucy',       'Doctor',   'Lucy@example.com',        '2022-07-03', 55000.00, '111-33-1111', '1952-03-03', '404-111-0003', 'Linus Blanket',     '404-111-9003', 2),
(4,  'Peppermint', 'Patti',    'Peppermint@example.com',  '2022-08-02', 98000.00, '111-44-1111', '1966-08-22', '404-111-0004', 'Marcie Friend',     '404-111-9004', 3),
(5,  'Linus',      'Blanket',  'Linus@example.com',       '2022-09-01', 43000.00, '111-55-1111', '1952-07-14', '404-111-0005', 'Charlie Brown',     '404-111-9005', 1),
(6,  'PigPin',     'Dusty',    'PigPin@example.com',      '2022-10-01', 33000.00, '111-66-1111', '1954-02-01', '404-111-0006', 'Charlie Brown',     '404-111-9006', 3),
(7,  'Scooby',     'Doo',      'Scooby@example.com',      '1973-07-03', 78000.00, '111-77-1111', '1969-09-13', '323-111-0007', 'Shaggy Rodgers',    '323-111-9007', 4),
(8,  'Shaggy',     'Rodgers',  'Shaggy@example.com',      '1973-07-11', 77000.00, '111-88-1111', '1951-11-01', '323-111-0008', 'Scooby Doo',        '323-111-9008', 5),
(9,  'Velma',      'Dinkley',  'Velma@example.com',       '1973-07-21', 82000.00, '111-99-1111', '1950-04-22', '818-111-0009', 'Daphne Blake',      '818-111-9009', 6),
(10, 'Daphne',     'Blake',    'Daphne@example.com',      '1973-07-30', 59000.00, '111-00-1111', '1949-10-01', '323-111-0010', 'Fred Jones',        '323-111-9010', 4),
(11, 'Bugs',       'Bunny',    'Bugs@example.com',        '1934-07-01', 18000.00, '222-11-1111', '1940-07-27', '323-111-0011', 'Daffy Duck',        '323-111-9011', 5),
(12, 'Daffy',      'Duck',     'Daffy@example.com',       '1935-04-01', 16000.00, '333-11-1111', '1937-04-17', '323-111-0012', 'Bugs Bunny',        '323-111-9012', 2),
(13, 'Porky',      'Pig',      'Porky@example.com',       '1935-08-12', 16550.00, '444-11-1111', '1935-03-02', '217-111-0013', 'Petunia Pig',       '217-111-9013', 7),
(14, 'Elmer',      'Fudd',     'Elmer@example.com',       '1934-08-01', 15500.00, '555-11-1111', '1940-03-02', '217-111-0014', 'Porky Pig',         '217-111-9014', 8),
(15, 'Marvin',     'Martian',  'Marvin@example.com',      '1937-05-01', 28000.00, '777-11-1111', '1948-07-24', '212-111-0015', 'Commander X2',      '212-111-9015', 9);

-- ============================================================
-- USERS
-- Each employee gets a login account.
-- Username = first initial + last name (e.g., sbeagle)
-- Password = 'password123' hashed with SHA2 (256-bit)
-- In real life you'd use bcrypt — SHA2 is fine for a class project
-- HR Admins: empid 1,2,3 get HR_ADMIN role for demo purposes
-- ============================================================

INSERT INTO users (username, password, role, empid) VALUES
('sbeagle',    SHA2('password123', 256), 'HR_ADMIN',  1),
('cbrown',     SHA2('password123', 256), 'HR_ADMIN',  2),
('ldoctor',    SHA2('password123', 256), 'HR_ADMIN',  3),
('ppatti',     SHA2('password123', 256), 'EMPLOYEE',  4),
('lblanket',   SHA2('password123', 256), 'EMPLOYEE',  5),
('pdusty',     SHA2('password123', 256), 'EMPLOYEE',  6),
('sdoo',       SHA2('password123', 256), 'EMPLOYEE',  7),
('srodgers',   SHA2('password123', 256), 'EMPLOYEE',  8),
('vdinkley',   SHA2('password123', 256), 'EMPLOYEE',  9),
('dblake',     SHA2('password123', 256), 'EMPLOYEE',  10),
('bbunny',     SHA2('password123', 256), 'EMPLOYEE',  11),
('dduck',      SHA2('password123', 256), 'EMPLOYEE',  12),
('ppig',       SHA2('password123', 256), 'EMPLOYEE',  13),
('efudd',      SHA2('password123', 256), 'EMPLOYEE',  14),
('mmartian',   SHA2('password123', 256), 'EMPLOYEE',  15);

-- ============================================================
-- PAYROLL
-- Same calculation logic as original:
-- weekly earnings = annual salary / 52
-- Deductions: fed_tax=32%, fed_med=1.45%, fed_SS=6.2%,
--             state_tax=12%, retire_401k=0.4%, health_care=3.1%
-- ============================================================

-- Employee 1 (Snoopy)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 1, '2026-01-31', 1,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=1;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 2, '2025-12-31', 1,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=1;

-- Employee 2 (Charlie Brown)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 3, '2026-01-31', 2,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=2;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 4, '2025-12-31', 2,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=2;

-- Employee 3 (Lucy)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 5, '2026-01-31', 3,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=3;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 6, '2025-12-31', 3,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=3;

-- Employee 4 (Peppermint Patti)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 7, '2026-01-31', 4,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=4;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 8, '2025-12-31', 4,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=4;

-- Employee 5 (Linus)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 9, '2026-01-31', 5,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=5;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 10, '2025-12-31', 5,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=5;

-- Employee 6 (PigPin)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 11, '2026-01-31', 6,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=6;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 12, '2025-12-31', 6,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=6;

-- Employee 7 (Scooby)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 13, '2026-01-31', 7,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=7;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 14, '2025-12-31', 7,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=7;

-- Employee 8 (Shaggy)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 15, '2026-01-31', 8,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=8;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 16, '2025-12-31', 8,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=8;

-- Employee 9 (Velma)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 17, '2026-01-31', 9,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=9;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 18, '2025-12-31', 9,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=9;

-- Employee 10 (Daphne)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 19, '2026-01-31', 10,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=10;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 20, '2025-12-31', 10,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=10;

-- Employee 11 (Bugs)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 21, '2026-01-31', 11,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=11;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 22, '2025-12-31', 11,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=11;

-- Employee 12 (Daffy)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 23, '2026-01-31', 12,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=12;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 24, '2025-12-31', 12,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=12;

-- Employee 13 (Porky)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 25, '2026-01-31', 13,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=13;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 26, '2025-12-31', 13,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=13;

-- Employee 14 (Elmer)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 27, '2026-01-31', 14,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=14;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 28, '2025-12-31', 14,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=14;

-- Employee 15 (Marvin)
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 29, '2026-01-31', 15,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=15;
INSERT INTO payroll (payID, pay_date, empid, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care)
SELECT 30, '2025-12-31', 15,
    ROUND(salary/52.0, 2),
    ROUND(ROUND(salary/52.0, 2)*0.32, 2),
    ROUND(ROUND(salary/52.0, 2)*0.0145, 2),
    ROUND(ROUND(salary/52.0, 2)*0.062, 2),
    ROUND(ROUND(salary/52.0, 2)*0.12, 2),
    ROUND(ROUND(salary/52.0, 2)*0.004, 2),
    ROUND(ROUND(salary/52.0, 2)*0.031, 2)
FROM employees WHERE empid=15;

-- ============================================================
-- EMPLOYEE DIVISION / JOB TITLE LINKS
-- These rows are required for the monthly reports to return data.
-- ============================================================

INSERT INTO employee_division (empid, divID) VALUES
(1, 1), (2, 1), (3, 3), (4, 999), (5, 1),
(6, 2), (7, 999), (8, 2), (9, 1), (10, 2),
(11, 2), (12, 2), (13, 2), (14, 2), (15, 999);

INSERT INTO employee_job_titles (empid, job_titleID) VALUES
(1, 103), (2, 102), (3, 200), (4, 900), (5, 101),
(6, 202), (7, 901), (8, 201), (9, 102), (10, 200),
(11, 202), (12, 201), (13, 202), (14, 201), (15, 902);

SET FOREIGN_KEY_CHECKS=1;
