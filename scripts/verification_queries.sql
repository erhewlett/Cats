USE cats;

SELECT 'Seed counts' AS section;
SELECT COUNT(*) AS employee_count FROM employees;
SELECT COUNT(*) AS user_count FROM users;
SELECT COUNT(*) AS payroll_count FROM payroll;
SELECT COUNT(*) AS employee_division_count FROM employee_division;
SELECT COUNT(*) AS employee_job_titles_count FROM employee_job_titles;

SELECT 'Login users' AS section;
SELECT username, role, empid
FROM users
WHERE username IN ('sbeagle', 'ppatti')
ORDER BY username;

SELECT 'New hire lookup by demo SSN' AS section;
SELECT empid, Fname, Lname, email, SSN, HireDate, addressID
FROM employees
WHERE SSN = '888-88-8888';

SELECT 'Demo address row' AS section;
SELECT a.addressID, a.street, a.cityID, a.stateID, a.zip
FROM addresses a
JOIN employees e ON e.addressID = a.addressID
WHERE e.SSN = '888-88-8888';

SELECT 'Demo employee division' AS section;
SELECT ed.empid, ed.divID
FROM employee_division ed
JOIN employees e ON e.empid = ed.empid
WHERE e.SSN = '888-88-8888';

SELECT 'Demo employee job title' AS section;
SELECT ejt.empid, ejt.job_titleID
FROM employee_job_titles ejt
JOIN employees e ON e.empid = ejt.empid
WHERE e.SSN = '888-88-8888';

SELECT 'Demo employee phone after update' AS section;
SELECT empid, mobile
FROM employees
WHERE SSN = '888-88-8888';

SELECT 'Salary check rows' AS section;
SELECT empid, Fname, Lname, Salary
FROM employees
WHERE empid IN (1, 2, 4, 5, 6)
ORDER BY empid;

SELECT 'New hires report verification' AS section;
SELECT empid, Fname, Lname, HireDate
FROM employees
WHERE HireDate BETWEEN '2022-07-01' AND '2022-10-01'
ORDER BY HireDate;

SELECT 'Pay by job title report verification' AS section;
SELECT jt.job_title, SUM(p.earnings) AS total_pay
FROM payroll p
JOIN employees e ON p.empid = e.empid
JOIN employee_job_titles ejt ON e.empid = ejt.empid
JOIN job_titles jt ON ejt.job_titleID = jt.job_titleID
WHERE MONTH(p.pay_date) = 1 AND YEAR(p.pay_date) = 2026
GROUP BY jt.job_title
ORDER BY total_pay DESC;

SELECT 'Pay by division report verification' AS section;
SELECT d.Name, SUM(p.earnings) AS total_pay
FROM payroll p
JOIN employees e ON p.empid = e.empid
JOIN employee_division ed ON e.empid = ed.empid
JOIN division d ON ed.divID = d.divID
WHERE MONTH(p.pay_date) = 1 AND YEAR(p.pay_date) = 2026
GROUP BY d.Name
ORDER BY total_pay DESC;
