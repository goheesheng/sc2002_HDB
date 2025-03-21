@startuml BTO Management System

' Abstract User class
abstract class User {
  - nric: String
  - password: String
  - age: int
  - maritalStatus: MaritalStatus
  + login(nric: String, password: String): boolean
  + changePassword(oldPassword: String, newPassword: String): boolean
  + viewProjects(filters: Map): List<Project>
  + submitEnquiry(project: Project, enquiryText: String): Enquiry
  + viewEnquiries(): List<Enquiry>
  + editEnquiry(enquiryId: int, newText: String): boolean
  + deleteEnquiry(enquiryId: int): boolean
}

' User types
class Applicant {
  - appliedProject: Project
  - applicationStatus: ApplicationStatus
  - bookedFlatType: FlatType
  + applyForProject(project: Project): boolean
  + viewApplication(): Application
  + requestWithdrawal(): boolean
  + bookFlat(project: Project, flatType: FlatType): boolean
}

class HDBOfficer {
  - handlingProject: Project
  - registrationStatus: RegistrationStatus
  + registerForProject(project: Project): boolean
  + viewRegistrationStatus(): RegistrationStatus
  + viewProjectDetails(project: Project): Project
  + replyToEnquiry(enquiry: Enquiry, replyText: String): boolean
  + updateRemainingFlats(project: Project, flatType: FlatType): boolean
  + retrieveApplication(nric: String): Application
  + updateApplicationStatus(application: Application, status: ApplicationStatus): boolean
  + updateApplicantProfile(applicant: Applicant, flatType: FlatType): boolean
  + generateBookingReceipt(application: Application): Receipt
}

class HDBManager {
  - createdProjects: List<Project>
  + createProject(projectDetails: Map): Project
  + editProject(project: Project, updatedDetails: Map): boolean
  + deleteProject(project: Project): boolean
  + toggleProjectVisibility(project: Project, visibility: boolean): boolean
  + viewAllProjects(): List<Project>
  + viewCreatedProjects(): List<Project>
  + viewOfficerRegistrations(project: Project): List<Registration>
  + approveOfficerRegistration(registration: Registration): boolean
  + rejectOfficerRegistration(registration: Registration): boolean
  + approveApplication(application: Application): boolean
  + rejectApplication(application: Application): boolean
  + approveWithdrawal(withdrawal: Withdrawal): boolean
  + rejectWithdrawal(withdrawal: Withdrawal): boolean
  + generateReport(filters: Map): Report
  + viewAllEnquiries(): List<Enquiry>
}

' Project-related classes
class Project {
  - projectId: String
  - projectName: String
  - neighborhood: String
  - flatTypes: Map<FlatType, Integer>
  - applicationOpeningDate: Date
  - applicationClosingDate: Date
  - managerInCharge: HDBManager
  - availableOfficerSlots: int
  - visibility: boolean
  - officers: List<HDBOfficer>
  - applications: List<Application>
  - enquiries: List<Enquiry>
  + getRemainingFlats(flatType: FlatType): int
  + isEligibleForUser(user: User): boolean
  + isWithinApplicationPeriod(): boolean
}

class Application {
  - applicationId: String
  - applicant: Applicant
  - project: Project
  - applicationDate: Date
  - status: ApplicationStatus
  - flatType: FlatType
  - withdrawalRequest: boolean
  + updateStatus(status: ApplicationStatus): boolean
  + requestWithdrawal(): boolean
}

class Enquiry {
  - enquiryId: String
  - user: User
  - project: Project
  - enquiryText: String
  - dateSubmitted: Date
  - reply: String
  - replyDate: Date
  - repliedBy: User
  + editText(newText: String): boolean
  + addReply(replyText: String, replier: User): boolean
}

class Receipt {
  - receiptId: String
  - applicantName: String
  - applicantNRIC: String
  - applicantAge: int
  - maritalStatus: MaritalStatus
  - flatTypeBooked: FlatType
  - project: Project
  - bookingDate: Date
  + generatePDF(): File
}

class Report {
  - reportId: String
  - generatedBy: HDBManager
  - generatedDate: Date
  - filters: Map
  - applications: List<Application>
  + applyFilters(filters: Map): List<Application>
  + generatePDF(): File
}

class Registration {
  - registrationId: String
  - officer: HDBOfficer
  - project: Project
  - registrationDate: Date
  - status: RegistrationStatus
  + approve(): boolean
  + reject(): boolean
}

' Enumerations
enum FlatType {
  TWO_ROOM
  THREE_ROOM
}

enum MaritalStatus {
  SINGLE
  MARRIED
}

enum ApplicationStatus {
  PENDING
  SUCCESSFUL
  UNSUCCESSFUL
  BOOKED
}

enum RegistrationStatus {
  PENDING
  APPROVED
  REJECTED
}

' Relationships
User <|-- Applicant
User <|-- HDBOfficer
User <|-- HDBManager

Applicant "1" -- "0..1" Application
HDBOfficer "1" -- "0..1" Registration
HDBManager "1" -- "*" Project: creates >

Project "1" -- "*" Application
Project "1" -- "*" Registration
Project "1" -- "*" Enquiry

User "1" -- "*" Enquiry: submits >
HDBOfficer "1" -- "*" Receipt: generates >
HDBManager "1" -- "*" Report: generates >

Application "1" -- "0..1" Receipt
Enquiry "0..1" -- "0..1" User: replied by >

@enduml
