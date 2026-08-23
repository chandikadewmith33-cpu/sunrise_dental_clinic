-- =========================================================
-- Sunrise Dental Clinic - Database Setup Script
-- Run this entire script in MySQL before starting the app.
-- =========================================================

CREATE DATABASE IF NOT EXISTS sunrise_dental;
USE sunrise_dental;

-- ---------------------------------------------------------
-- Staff / login accounts
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'STAFF'
);

-- ---------------------------------------------------------
-- Treatment types and their standard cost
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS treatments (
    treatment_type VARCHAR(50) PRIMARY KEY,
    cost DOUBLE NOT NULL
);

-- ---------------------------------------------------------
-- Appointments / patient visit records
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointments (
    appointment_no VARCHAR(20) PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    contact_number VARCHAR(20),
    dentist_name VARCHAR(100),
    treatment_type VARCHAR(50),
    appointment_date DATE,
    appointment_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (treatment_type) REFERENCES treatments(treatment_type)
);

-- ---------------------------------------------------------
-- Generated bills / receipts
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_no VARCHAR(20),
    consultation_fee DOUBLE NOT NULL,
    treatment_cost DOUBLE NOT NULL,
    total_amount DOUBLE NOT NULL,
    bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_no) REFERENCES appointments(appointment_no)
);

-- ---------------------------------------------------------
-- Seed data
-- ---------------------------------------------------------

-- Default staff login (username: admin / password: admin123)
-- Change this password after first login in a real deployment.
INSERT INTO users (username, password, full_name, role) VALUES
    ('admin', 'admin123', 'System Administrator', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;

-- Standard treatment price list (in Rs.), edit to match the clinic's actual prices
INSERT INTO treatments (treatment_type, cost) VALUES
    ('Consultation',        500),
    ('Scaling',            2000),
    ('Filling',            3500),
    ('Root Canal',        15000),
    ('Extraction',         2500),
    ('Whitening',          8000),
    ('Braces Consultation', 1000),
    ('Denture Fitting',   12000)
ON DUPLICATE KEY UPDATE cost = VALUES(cost);
