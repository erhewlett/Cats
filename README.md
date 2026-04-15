# Employee Management System
**Team: Cats | CSc3350 Software Development | Spring 2026**

Members: Farhiya Afrah, Rose Duarte, Angel Patino, Elijah Hewlett

---

## Project Structure

```
EmployeeManagementSystem/
├── src/
│   ├── model/             # Data objects (Employee, Payroll, etc.)
│   ├── dao/               # Database access (interfaces + implementations)
│   ├── controller/        # Business logic
│   ├── ui/                # Console menus
│   └── util/              # DBConnection, InputValidator
├── sql/
│   ├── Cats_MySQL_create_v2.sql    # Creates all tables
│   └── Cats_INSERT_datum_v2.sql    # Seeds all data
├── lib/
│   └── mysql-connector-j-9.1.0.jar
└── README.md
```

---

## Setup Instructions

### Step 1 — Database
1. Open dBeaver (or MySQL terminal)
2. Run `sql/Cats_MySQL_create_v2.sql` — creates the `cats` database and all tables
3. Run `sql/Cats_INSERT_datum_v2.sql` — loads all seed data

### Step 2 — Configure DB Connection
Set environment variables before running the app:

```bash
export EMS_DB_URL=jdbc:mysql://localhost:3306/cats
export EMS_DB_USER=root
export EMS_DB_PASSWORD=CHANGE_TO_YOUR_PASSWORD
```

If you do not set `EMS_DB_URL`, the app defaults to `jdbc:mysql://localhost:3306/cats`.

### Step 3 — Compile
From the project root directory:

**Mac/Linux:**
```bash
javac -cp .:lib/mysql-connector-j-9.1.0.jar -d out src/util/*.java src/model/*.java src/dao/*.java src/controller/*.java src/ui/*.java
```

**Windows:**
```bash
javac -cp .;lib/mysql-connector-j-9.1.0.jar -d out src\\util\\*.java src\\model\\*.java src\\dao\\*.java src\\controller\\*.java src\\ui\\*.java
```

### Step 4 — Run
**Mac/Linux:**
```bash
java -cp out:lib/mysql-connector-j-9.1.0.jar ui.MainUI
```

**Windows:**
```bash
java -cp out;lib/mysql-connector-j-9.1.0.jar ui.MainUI
```

---

## Test Login Credentials

| Username   | Password    | Role      |
|------------|-------------|-----------|
| sbeagle    | password123 | HR_ADMIN  |
| cbrown     | password123 | HR_ADMIN  |
| ldoctor    | password123 | HR_ADMIN  |
| ppatti     | password123 | EMPLOYEE  |
| sdoo       | password123 | EMPLOYEE  |

---

## Features Implemented

| PT   | Feature                              | Role       |
|------|--------------------------------------|------------|
| PT-1 | Login with role-based access         | Both       |
| PT-2a| Edit employee data                   | HR Admin   |
| PT-2b| Delete employee with confirmation    | HR Admin   |
| PT-2c| Raise salaries below threshold       | HR Admin   |
| PT-3 | View pay history newest first        | Employee   |
| PT-4 | Search by name, DOB, empID           | HR Admin   |
| PT-5 | New hires report by date range       | HR Admin   |
| PT-6 | Raise salaries within a range        | HR Admin   |
| PT-7 | Total pay by job title report        | HR Admin   |
| PT-8 | Total pay by division report         | HR Admin   |
| PT-9 | Add new employee (auto empID)        | HR Admin   |
| PT-10| Employee self-service profile view   | Employee   |
