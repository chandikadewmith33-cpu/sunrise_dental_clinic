# Sunrise Dental Clinic - Appointment & Patient Management System

A desktop Java Swing application with a MySQL backend for managing dental
appointments, patient records, and billing.

## Features

1. **User Authentication (Login)** — staff must log in with a username/password.
2. **Register New Appointment** — auto-generates an appointment number and stores
   patient name, address, contact number, dentist name, treatment type, date and time.
3. **Display Appointment Details** — search by appointment number.
4. **Calculate and Print Bill** — computes consultation fee + treatment cost and prints a receipt.
5. **Help** — step-by-step instructions for new staff.
6. **Exit System** — safely closes the application.

## Project Structure

This is set up as a ready-to-open **NetBeans project** (standard `src/` +
`nbproject/` layout):

```
Sunrise_Dental_Clinic
│
├── nbproject/               NetBeans project metadata (auto-managed)
├── build.xml                Ant build script (delegates to NetBeans)
├── manifest.mf               Jar manifest
│
├── src
│   ├── dao                  Data access classes (JDBC queries)
│   │   ├── AppointmentDAO.java
│   │   └── UserDAO.java
│   │
│   ├── db                   Database connection
│   │   ├── DBConnection.java
│   │   └── TestConnection.java
│   │
│   ├── model                Plain data model classes
│   │   ├── Appointment.java
│   │   ├── User.java
│   │   └── Bill.java
│   │
│   ├── view                 Swing UI screens
│   │   ├── Login.java
│   │   ├── MenuForm.java
│   │   ├── RegisterAppointment.java
│   │   ├── AppointmentDetails.java
│   │   ├── BillForm.java
│   │   └── Help.java
│   │
│   └── sunrise/dental        Application entry point
│       └── SunriseDental.java
│
├── sql
│   └── database_setup.sql   Creates the database, tables and seed data
│
└── lib                      Put mysql-connector-j-x.x.x.jar here
```

> Note: the package `sunrise.dental` lives in the nested folders
> `src/sunrise/dental/`, matching normal Java package conventions
> (one folder per dot in the package name).

## Opening in NetBeans

1. Download `mysql-connector-j-x.x.x.jar` from
   https://dev.mysql.com/downloads/connector/j/ and drop it into the
   project's `lib/` folder (rename it to `mysql-connector-j.jar`, or
   update the path in `nbproject/project.properties` under
   `file.reference.mysql-connector-j.jar` to match the exact filename).
2. In NetBeans: **File → Open Project…**, browse to the
   `Sunrise_Dental_Clinic` folder (the one containing `nbproject/`), select
   it, and click **Open Project**. NetBeans recognizes it as a Java SE
   project automatically — no import wizard needed.
3. If NetBeans shows a red "!" on the project or complains about a missing
   library reference, right-click the project → **Properties → Libraries**,
   and re-add the connector jar from `lib/` if needed.
4. Run `sql/database_setup.sql` against your MySQL server (see **Setup**
   below) before running the app.
5. Right-click the project → **Run** (or press F6). It launches
   `sunrise.dental.SunriseDental`, which is already set as the Main Class
   in the project properties.

If you'd rather import the sources into a **new** NetBeans project instead
of opening this one directly: **File → New Project → Java with Existing
Sources**, then when prompted for the source folder, point it at `src/`
(not the project root), and set Main Class to `sunrise.dental.SunriseDental`
in Project Properties → Run.

## Requirements

- JDK 8 or newer
- MySQL Server 5.7+ / 8.0+
- MySQL Connector/J (JDBC driver) — download from
  https://dev.mysql.com/downloads/connector/j/ and place the `.jar` in the `lib/` folder.

## Setup

### 1. Create the database

Open a MySQL client (MySQL Workbench, the `mysql` CLI, phpMyAdmin, etc.) and run:

```
sql/database_setup.sql
```

This creates the `sunrise_dental` database with `users`, `treatments`,
`appointments` and `bills` tables, plus:

- A default login: **username `admin`, password `admin123`**
- A starter price list for common treatments (edit the `treatments` table to
  match your clinic's actual pricing).

### 2. Configure the connection

Edit `db/DBConnection.java` if your MySQL username/password/port differ from
the defaults (`root` / `root` on `localhost:3306`):

```java
private static final String URL = "jdbc:mysql://localhost:3306/sunrise_dental?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "root";
```

### 3. Compile (command line, if not using NetBeans's Run button)

From the project root, with the MySQL connector jar in `lib/`:

```bash
# Linux / macOS
javac -cp "lib/*" -d bin $(find src -name "*.java")

# Windows (PowerShell)
javac -cp "lib\*" -d bin (Get-ChildItem -Path src -Recurse -Filter *.java).FullName
```

### 4. Test the database connection (optional but recommended)

```bash
java -cp "bin;lib/*" db.TestConnection      # Windows
java -cp "bin:lib/*" db.TestConnection      # Linux / macOS
```

You should see `Database connected successfully!`.

### 5. Run the application

```bash
java -cp "bin;lib/*" sunrise.dental.SunriseDental   # Windows
java -cp "bin:lib/*" sunrise.dental.SunriseDental   # Linux / macOS
```

Log in with `admin` / `admin123`, then use the Main Menu to register
appointments, look them up, and generate bills.

## Notes

- Consultation fee is a fixed Rs. 500 added on top of the treatment cost
  (see `BillForm.CONSULTATION_FEE`); adjust it there if the clinic's flat
  fee changes.
- Treatment costs come from the `treatments` table, so pricing can be
  updated directly in the database without recompiling the app.
- Passwords are stored in plain text in this version for simplicity — for
  a production deployment, hash passwords (e.g. with BCrypt) before storing them.
# sunrise_dental_clinic
