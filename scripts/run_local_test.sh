#!/bin/zsh

set -euo pipefail

PROJECT_ROOT="/Users/elijahhewlett/Downloads/Soft Dev Project"
MYSQL_BIN="/usr/local/mysql/bin/mysql"
MYSQLADMIN_BIN="/usr/local/mysql/bin/mysqladmin"
SCHEMA_SQL="$PROJECT_ROOT/sql/Cats_MySQL_create_v2.sql"
SEED_SQL="$PROJECT_ROOT/sql/Cats_INSERT_datum_v2.sql"

cd "$PROJECT_ROOT"

echo "== Employee Management System Local Test Walkthrough =="
echo

export EMS_DB_URL="${EMS_DB_URL:-jdbc:mysql://localhost:3306/cats}"
export EMS_DB_USER="${EMS_DB_USER:-root}"

if [[ -z "${EMS_DB_PASSWORD:-}" ]]; then
  echo "EMS_DB_PASSWORD is not set."
  echo "Run this first in the same terminal:"
  echo "  export EMS_DB_PASSWORD='CHANGE_TO_YOUR_PASSWORD'"
  exit 1
fi

echo "Environment check:"
echo "  EMS_DB_URL=$EMS_DB_URL"
echo "  EMS_DB_USER=$EMS_DB_USER"
if [[ -n "$EMS_DB_PASSWORD" ]]; then
  echo "  EMS_DB_PASSWORD is set"
fi
echo

echo "Checking whether MySQL is running..."
if ! "$MYSQLADMIN_BIN" -u "$EMS_DB_USER" -p"$EMS_DB_PASSWORD" ping >/dev/null 2>&1; then
  echo "MySQL is not responding with the current credentials."
  echo "If needed, start it with:"
  echo "  sudo /usr/local/mysql/support-files/mysql.server start"
  exit 1
fi
echo "MySQL is running."
echo

echo "Testing direct MySQL login..."
"$MYSQL_BIN" -u "$EMS_DB_USER" -p"$EMS_DB_PASSWORD" -e "SELECT USER() AS mysql_user, CURRENT_DATE() AS today;"
echo

echo "Rebuilding cats database from project SQL scripts..."
"$MYSQL_BIN" -u "$EMS_DB_USER" -p"$EMS_DB_PASSWORD" < "$SCHEMA_SQL"
"$MYSQL_BIN" -u "$EMS_DB_USER" -p"$EMS_DB_PASSWORD" < "$SEED_SQL"
echo "Database rebuild complete."
echo

echo "Seed verification:"
"$MYSQL_BIN" -u "$EMS_DB_USER" -p"$EMS_DB_PASSWORD" -e "
USE cats;
SELECT COUNT(*) AS employee_count FROM employees;
SELECT COUNT(*) AS user_count FROM users;
SELECT COUNT(*) AS payroll_count FROM payroll;
SELECT COUNT(*) AS employee_division_count FROM employee_division;
SELECT COUNT(*) AS employee_job_titles_count FROM employee_job_titles;
"
echo

echo "Compiling Java source..."
mkdir -p out
javac -cp .:lib/mysql-connector-j-9.1.0.jar -d out src/util/*.java src/model/*.java src/dao/*.java src/controller/*.java src/ui/*.java
echo "Compilation complete."
echo

echo "Launching app..."
echo "Suggested login tests:"
echo "  HR Admin   -> sbeagle / password123"
echo "  Employee   -> ppatti / password123"
echo
echo "After testing the app, run verification queries with:"
echo "  /usr/local/mysql/bin/mysql -u \"$EMS_DB_USER\" -p\"$EMS_DB_PASSWORD\" < '$PROJECT_ROOT/scripts/verification_queries.sql'"
echo

java -cp 'out:lib/mysql-connector-j-9.1.0.jar' ui.MainUI
