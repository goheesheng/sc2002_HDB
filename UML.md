<!-- Uncomment below for local VSC usage, else paste to https://www.plantuml.com/ -->
<!-- ```plantuml -->

@startuml

' ------------------------------
' Abstract base class for all users
' ------------------------------
' The User class acts as the superclass for all the users in the system (Applicant, HDB Officer, HDB Manager).
' It contains common properties (NRIC, password, age, marital status) and common methods for login and password change.
abstract class User {
  - nric : String  ' Unique identifier for the user (NRIC)
  - password : String  ' User password
  - age : int  ' User's age
  - maritalStatus : String  ' User's marital status
  + login() : boolean  ' Method to login the user
  + changePassword(newPassword : String)  ' Method to change the user's password
}

' ------------------------
' Applicant Role
' ------------------------
' The Applicant class represents an applicant user and has methods to view projects, apply for projects,
' view application status, and interact with enquiries.
class Applicant {
  + viewProjects() : void  ' Allows applicant to view available projects based on criteria
  + applyProject(project : Project) : Application  ' Allows applicant to apply for a project
  + viewApplicationStatus() : String  ' Allows applicant to view the current status of their application
  + requestWithdrawal() : void  ' Allows applicant to request withdrawal from the project
  + submitEnquiry(enquiry : String) : Enquiry  ' Allows applicant to submit an enquiry
  + editEnquiry(enquiryID : int, newText : String) : void  ' Allows applicant to edit an existing enquiry
  + deleteEnquiry(enquiryID : int) : void  ' Allows applicant to delete an enquiry
}

' ------------------------
' HDB Officer Role
' ------------------------
' The HDB Officer inherits from Applicant, meaning they have all the applicant's capabilities plus additional
' responsibilities for managing projects, booking flats, and replying to enquiries.
class HDBOfficer {
  + registerProject(project : Project) : Registration  ' Allows HDB Officer to register for a project
  + viewProjectDetails() : Project  ' Allows HDB Officer to view details of a project they are handling
  + replyEnquiry(enquiry : Enquiry) : void  ' Allows HDB Officer to reply to an enquiry
  + updateFlatAvailability(project : Project, flatType : String, number : int) : void  ' Updates availability of flats
  + bookFlat(application : Application, flatType : FlatType) : void  ' Books a flat for an applicant
  + generateReceipt(application : Application) : Receipt  ' Generates a receipt for the applicant's booking
}

' ------------------------
' HDB Manager Role
' ------------------------
' The HDB Manager has administration capabilities such as project creation, modification, visibility toggling,
' approving/rejecting BTO applications, and generating reports.
class HDBManager {
  + createProject(project : Project) : void  ' Creates a new BTO project
  + editProject(project : Project) : void  ' Edits an existing BTO project
  + deleteProject(projectID : int) : void  ' Deletes a BTO project
  + toggleProjectVisibility(project : Project, visible : boolean) : void  ' Toggles visibility of the project
  + approveHDBOfficerRegistration(registration : Registration) : void  ' Approves an HDB Officer's registration
  + approveBTOApplication(application : Application) : void  ' Approves an applicant's BTO application
  + rejectBTOApplication(application : Application) : void  ' Rejects an applicant's BTO application
  + generateReport(filters : Map) : Report  ' Generates a report for the project
  + replyEnquiry(enquiry : Enquiry) : void  ' Replies to an enquiry regarding the project
  + viewAllEnquiries() : List<Enquiry>  ' Views all enquiries related to the project
}

' ------------------------
' Project Details
' ------------------------
' The Project class holds details for each BTO project, including the available units, application dates, and visibility.
class Project {
  - projectName : String  ' Name of the BTO project
  - neighborhood : String  ' Neighborhood where the project is located
  - availableUnits2Room : int  ' Number of 2-room flats available
  - availableUnits3Room : int  ' Number of 3-room flats available
  - applicationOpeningDate : Date  ' Date when the application opens
  - applicationClosingDate : Date  ' Date when the application closes
  - visibility : boolean  ' Whether the project is visible to applicants
  - availableHDBOfficerSlots : int  ' Number of available slots for HDB Officers
  + viewDetails() : String  ' Displays the details of the project
}

' ------------------------
' FlatType Enumeration
' ------------------------
' FlatType is an enumeration that defines the different types of flats available for BTO applications.
enum FlatType {
  2_ROOM  ' Represents 2-room flats
  3_ROOM  ' Represents 3-room flats
}

' ------------------------
' Application Status
' ------------------------
' ApplicationStatus defines the possible states of an application.
enum ApplicationStatus {
  PENDING  ' Application is still pending approval
  SUCCESSFUL  ' Application is successful
  UNSUCCESSFUL  ' Application is unsuccessful
  BOOKED  ' Flat has been booked after successful application
}

' ------------------------
' Enquiry Class for Applicant Queries
' ------------------------
' The Enquiry class stores details for enquiries submitted by applicants, such as text and timestamp.
class Enquiry {
  - enquiryID : int  ' Unique identifier for the enquiry
  - enquiryText : String  ' Content of the enquiry
  - timestamp : Date  ' Time when the enquiry was submitted
  + viewEnquiry() : String  ' Displays the enquiry details
  + editEnquiry(newText : String) : void  ' Edits the enquiry content
  + deleteEnquiry() : void  ' Deletes the enquiry
}

' ------------------------
' Registration for HDB Officer
' ------------------------
' The Registration class handles the registration process for HDB Officers who wish to manage a project.
class Registration {
  - status : RegistrationStatus  ' Status of the registration (pending, approved, rejected)
  + updateStatus(newStatus : RegistrationStatus) : void  ' Updates the registration status
}

' ------------------------
' Registration Status Enumeration
' ------------------------
' Defines the possible statuses for a registration to become an HDB Officer.
enum RegistrationStatus {
  PENDING  ' Registration is pending approval
  APPROVED  ' Registration has been approved
  REJECTED  ' Registration has been rejected
}

' ------------------------
' Receipt for Flat Booking
' ------------------------
' The Receipt class generates a receipt for applicants once they have successfully booked a flat.
class Receipt {
  - applicantName : String  ' Name of the applicant
  - nric : String  ' NRIC of the applicant
  - age : int  ' Age of the applicant
  - maritalStatus : String  ' Marital status of the applicant
  - flatType : FlatType  ' Type of flat booked (2-room or 3-room)
  - projectDetails : String  ' Project details for the booking
  + generate() : String  ' Generates the receipt as a string
}

' ------------------------
' Report for Project Data
' ------------------------
' The Report class generates various reports related to the BTO projects.
class Report {
  - reportData : String  ' Data for the report
  + generate() : String  ' Generates the report content
}

' ------------------------
' Inheritance Relationships
' ------------------------
' Shows how classes inherit from one another. The User class is inherited by Applicant, HDB Officer, and HDB Manager.
User <|-- Applicant
Applicant <|-- HDBOfficer
User <|-- HDBManager

' ------------------------
' Associations Between Classes
' ------------------------
' Describes the relationships between the classes in the system.
Applicant "1" --> "0..1" Application : applies  ' One applicant applies for one application
Project "1" --> "*" Application : contains  ' A project contains multiple applications
Project "1" --> "*" Enquiry : collects  ' A project collects multiple enquiries
Applicant "1" --> "*" Enquiry : submits  ' An applicant can submit multiple enquiries
HDBManager "1" --> "*" Project : creates  ' A HDB Manager can create multiple projects
HDBOfficer "1" --> "*" Registration : registers  ' An HDB Officer can register for multiple projects
Project "1" --> "*" Registration : associated with  ' A project has multiple HDB Officers associated
HDBOfficer "1" --> "*" Receipt : generates  ' A HDB Officer generates multiple receipts
HDBManager "1" --> "*" Report : generates  ' A HDB Manager generates multiple reports

@enduml
