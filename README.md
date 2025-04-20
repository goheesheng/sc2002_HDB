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

- Java-based backend
- Object-oriented design following SOLID principles
- Apache POI for Excel file processing
- CSV data storage for persistence

## Setup and Installation

1. Clone the repository
2. Ensure JDK is installed on your system (JDK 8 or later recommended)
3. Download Apache POI from https://poi.apache.org/download.html and add the JAR files to your classpath
4. Compile the project using Javac:
   ```
   javac -d ./bin -sourcepath src/main/java src/main/java/**/*.java
   ```
5. Run the application:
   ```
   java -cp ./bin Main
   ```

## Data Files

The application uses the following data files in the `src/main/resources` directory:
- Excel files (ApplicantList.xlsx, ManagerList.xlsx, OfficerList.xlsx, ProjectList.xlsx) for importing data
- CSV files (applications.csv, applicantlist.csv, managerlist.csv, officerlist.csv, projects.csv, registrations.csv, enquiries.csv) for data persistence

## Test Login Credentials

You can use the following credentials to test the system:

|Name     | NRIC      | Password    | Age | Marital Status | Role        |
|---------|-----------|-------------|-----|----------------|-------------|
|John     | S1234567A | password    | 35  | SINGLE         | Applicant   |
|Jessica  | S5678901G | password    | 26  | MARRIED        | HDBManager  |
|David    | T1234567J | password    | 29  | MARRIED        | HDBOfficer  |

## Documentation

Additional documentation can be found in the `doc/` directory.

## Javadoc Generation

Generate Javadoc for the project using the full path to the javadoc executable:
```
"C:\Program Files\Java\jdk-XX\bin\javadoc.exe" -d ./html -author -private -noqualifier all -version -sourcepath src/main/java utility user ui status project admin
```

Note: Replace `jdk-XX` with your installed JDK version (e.g., `jdk-17`, `jdk-24`). Using the full binary path is necessary if javadoc is not in your system's PATH.

## UML Diagram

The system architecture and class relationships are documented in `UML.md`.

---

https://docs.google.com/document/d/1Hc29UVGmNa8ezRiRDkSvwIY_-3hwTA4Ckpf8pqMoSLE/edit?usp=sharing
