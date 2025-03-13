<!-- Uncomment below for local VSC usage, else paste to https://www.plantuml.com/ -->
<!-- ```plantuml -->

@startuml
' Abstract base class for all users
abstract class User {
  - nric : String
  - password : String
  - age : int
  - maritalStatus : String
  + login() : boolean
  + changePassword(newPassword : String)
}

' Applicant role with specific functionalities
class Applicant {
  + viewProjects() : void
  + applyProject(project : Project) : Application
  + viewApplicationStatus() : String
  + requestWithdrawal() : void
  + submitEnquiry(enquiry : String) : Enquiry
  + editEnquiry(enquiryID : int, newText : String) : void
  + deleteEnquiry(enquiryID : int) : void
}

' HDB Officer inherits from Applicant (has all Applicant capabilities plus more)
class HDBOfficer {
  + registerProject(project : Project) : Registration
  + viewProjectDetails() : Project
  + replyEnquiry(enquiry : Enquiry) : void
  + updateFlatAvailability(project : Project, flatType : String, number : int) : void
  + bookFlat(application : Application, flatType : FlatType) : void
  + generateReceipt(application : Application) : Receipt
}

' HDB Manager with administration capabilities
class HDBManager {
  + createProject(project : Project) : void
  + editProject(project : Project) : void
  + deleteProject(projectID : int) : void
  + toggleProjectVisibility(project : Project, visible : boolean) : void
  + approveHDBOfficerRegistration(registration : Registration) : void
  + approveBTOApplication(application : Application) : void
  + rejectBTOApplication(application : Application) : void
  + generateReport(filters : Map) : Report
  + replyEnquiry(enquiry : Enquiry) : void
  + viewAllEnquiries() : List<Enquiry>
}

' Project class holding BTO project details
class Project {
  - projectName : String
  - neighborhood : String
  - availableUnits2Room : int
  - availableUnits3Room : int
  - applicationOpeningDate : Date
  - applicationClosingDate : Date
  - visibility : boolean
  - availableHDBOfficerSlots : int
  + viewDetails() : String
}

' Enumeration for flat types
enum FlatType {
  2_ROOM
  3_ROOM
}

' Application for a project with status updates
class Application {
  - status : ApplicationStatus
  - flatType : FlatType
  + updateStatus(newStatus : ApplicationStatus) : void
}

' Enumeration for application statuses
enum ApplicationStatus {
  PENDING
  SUCCESSFUL
  UNSUCCESSFUL
  BOOKED
}

' Enquiry class for applicant queries
class Enquiry {
  - enquiryID : int
  - enquiryText : String
  - timestamp : Date
  + viewEnquiry() : String
  + editEnquiry(newText : String) : void
  + deleteEnquiry() : void
}

' Registration class for HDB Officer’s registration for handling a project
class Registration {
  - status : RegistrationStatus
  + updateStatus(newStatus : RegistrationStatus) : void
}

' Enumeration for registration statuses
enum RegistrationStatus {
  PENDING
  APPROVED
  REJECTED
}

' Receipt class for flat booking details
class Receipt {
  - applicantName : String
  - nric : String
  - age : int
  - maritalStatus : String
  - flatType : FlatType
  - projectDetails : String
  + generate() : String
}

' Report class for generating project reports
class Report {
  - reportData : String
  + generate() : String
}

' Inheritance relationships
User <|-- Applicant
Applicant <|-- HDBOfficer
User <|-- HDBManager

' Associations
Applicant "1" --> "0..1" Application : applies
Project "1" --> "*" Application : contains
Project "1" --> "*" Enquiry : collects
Applicant "1" --> "*" Enquiry : submits
HDBManager "1" --> "*" Project : creates
HDBOfficer "1" --> "*" Registration : registers
Project "1" --> "*" Registration : associated with
HDBOfficer "1" --> "*" Receipt : generates
HDBManager "1" --> "*" Report : generates

@enduml
```
