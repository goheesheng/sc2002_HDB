# HDB BTO Management System

This project implements a Housing and Development Board (HDB) Build-To-Order (BTO) flat application management system. The system provides a platform for managing HDB flat applications, projects, and user interactions.

## Project Overview

The HDB BTO Management System facilitates the following key activities:

- **User Management**: Supports different user roles including Applicants, HDB Officers, and HDB Managers
- **Project Management**: Create, edit, delete, and view BTO housing projects
- **Application Processing**: Apply for BTO flats, manage applications, and handle booking
- **Enquiry Handling**: Submit, view, edit, and respond to project enquiries
- **Report Generation**: Generate reports based on various filters for management insights

## Key Features

- User authentication and role-based access control
- BTO project creation and management
- Application submission and processing workflow
- Flat type selection and booking
- Enquiry submission and management
- Receipt generation for successful bookings
- Officer registration for project handling
- Comprehensive reporting capabilities

## Technologies

- Java-based backend (Maven project)
- Python scripts for data processing and analysis
- Object-oriented design following SOLID principles

## Setup and Installation

1. Clone the repository
2. Install Java dependencies using Maven: `mvn install`
3. Execute "mvn clean compile" To ensure all dependencies are loaded successfully.
4. Execute "mvn compile exec:java -Dexec.mainClass="Main"" to run the application.
5. On seeing the menu Select Option 2(Load all Sheets) to access the database.
6. Your Setup and Installation is complete


## Test Login Credentials

You can use the following credentials to test the system:

|Name     | NRIC      | Password    | Age | Marital Status | Role        |
|---------|-----------|-------------|-----|----------------|-------------|
|John     | S1234567A | password    | 35  | SINGLE         | Applicant   |
|Jessica  | S5678901G | password    | 26  | MARRIED        | HDBManager  |
|David    | T1234567J | password    | 29  | MARRIED        | HDBOfficer  |

## Documentation

Additional documentation can be found in the `doc/` directory.

## UML Diagram

The system architecture and class relationships are documented in `UML.md`.

---

https://docs.google.com/document/d/1Hc29UVGmNa8ezRiRDkSvwIY_-3hwTA4Ckpf8pqMoSLE/edit?usp=sharing
