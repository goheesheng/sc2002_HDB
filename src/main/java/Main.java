// TestCases
import admin.Receipt;
import admin.Report;
import admin.Registration;
import user.User;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import project.Project;
import project.Application;
import project.Enquiry;
import project.FlatType;
import status.ApplicationStatus;
import status.RegistrationStatus;
//import utility.fileReader;
import utility.loginHandler;
import menu.ApplicantMenu;
import menu.HDBManagerMenu;
import menu.HDBOfficerMenu;

import java.util.*;
import java.io.File;

import utility.ExcelReader;
import utility.ExcelWriter;

public class Main {
    // Unit Test Case
    public static void main(String[] args) {
    // menu screen
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
        System.out.println("\nWelcome to Build-To-Order (BTO) Management System");
        System.out.println("What would you like to do");
        System.out.println("1. Login");
        System.out.println("2. view user list (temp)");
        System.out.println("3. run test cases (temp)");
        System.out.println("4. Exit");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); 
        }

        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                String nric;
                while(true){
                System.out.println("Enter NRIC:");
                nric = scanner.nextLine();

                if (User.isValidNRIC(nric)) {
                    break;
                } else {
                    System.out.println("Invalid NRIC format. Please enter a valid NRIC.");
                }
            }
                System.out.println("Enter Password:");
                String password = scanner.nextLine();

                loginHandler loginHandler = new loginHandler();
                User loggedInUser = loginHandler.login(nric, password); 
                if (loggedInUser != null) {
                    System.out.println("Login successful! User type: " + loggedInUser.getClass().getSimpleName());
                    
                    if (loggedInUser instanceof HDBManager){
                        new HDBManagerMenu((HDBManager) loggedInUser).displayMenu();
                    }
                    if (loggedInUser instanceof HDBOfficer){
                        new HDBOfficerMenu((HDBOfficer) loggedInUser).displayMenu();
                    } else if (loggedInUser instanceof Applicant) {
                        new ApplicantMenu ((Applicant) loggedInUser).displayMenu();
                    }
                } else {
                    System.out.println("Invalid NRIC or password.");
                    System.out.println("----------------------------------------\n");
                }
            
                break;

            case 2:
                String excelFilePath = "test/ApplicantsWithPassword.xlsx";  // Example file path
                ExcelReader.readExcel(excelFilePath);
                String excelOutputFilePath = "test/ApplicantsWithPasswordOutput.xlsx";
                ExcelWriter.writeToExcel(excelOutputFilePath);
                return;

            case 3:
                System.out.println("Running test cases...");
                runTestCases();
            break;
            case 4: 
                System.out.println("Exiting...");
                return;
            default:
                System.out.println("Invalid choice.");
                System.out.println("----------------------------------------\n");
            }
        }
        while (choice != 4);
        scanner.close();
        }
    

    public static void runTestCases() {

        System.out.println("Running BTO Management System Test Cases");
        System.out.println("Current Date: March 21, 2025");
        System.out.println("----------------------------------------\n");

        // Test Case 1: Valid User Login
        System.out.println("Test Case 1: Valid User Login");
        Applicant applicant = new Applicant("S1234567A", "password", 35, "SINGLE");
        boolean loginSuccess = applicant.login("S1234567A", "password");
        System.out.println("Result: " + (loginSuccess ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");

        // Test Case 2: Invalid NRIC Format
        System.out.println("Test Case 2: Invalid NRIC Format");
        boolean invalidNRICLogin = applicant.login("A1234567B", "password");
        System.out.println("Result: " + (!invalidNRICLogin ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");

        // Test Case 3: Incorrect Password
        System.out.println("Test Case 3: Incorrect Password");
        boolean incorrectPasswordLogin = applicant.login("S1234567A", "wrongpassword");
        System.out.println("Result: " + (!incorrectPasswordLogin ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");

        // Test Case 4: Password Change Functionality
        System.out.println("Test Case 4: Password Change Functionality");
        boolean passwordChangeSuccess = applicant.changePassword("password", "newpassword");
        boolean newPasswordLogin = applicant.login("S1234567A", "newpassword");
        System.out.println("Result: " + (passwordChangeSuccess && newPasswordLogin ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");

        // Create HDB Manager and Project for further tests
        HDBManager manager = new HDBManager("S9876543B", "password", 45, "MARRIED");
        
        Map<FlatType, Integer> flatTypes = new HashMap<>();
        flatTypes.put(FlatType.TWO_ROOM, 50);
        flatTypes.put(FlatType.THREE_ROOM, 30);
        
        // Set dates for project application period
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MARCH, 1); // March 1, 2025
        Date openingDate = calendar.getTime();
        
        calendar.set(2025, Calendar.APRIL, 30); // April 30, 2025
        Date closingDate = calendar.getTime();
        
        Map<String, Object> projectDetails = new HashMap<>();
        projectDetails.put("projectId", "P001");
        projectDetails.put("projectName", "Tampines GreenView");
        projectDetails.put("neighborhood", "Tampines");
        projectDetails.put("flatTypes", flatTypes);
        projectDetails.put("applicationOpeningDate", openingDate);
        projectDetails.put("applicationClosingDate", closingDate);
        projectDetails.put("availableOfficerSlots", 5);
        
        Project project = manager.createProject(projectDetails);
        manager.toggleProjectVisibility(project, true);
        
        // Test Case 5: Project Visibility Based on User Group
        System.out.println("Test Case 5: Project Visibility Based on User Group");
        Applicant singleApplicant = new Applicant("S1234567C", "password", 36, "SINGLE");
        Applicant marriedApplicant = new Applicant("S7654321D", "password", 25, "MARRIED");
        Applicant youngSingleApplicant = new Applicant("S2345678E", "password", 30, "SINGLE");
        
        boolean singleCanSee = project.isEligibleForUser(singleApplicant);
        boolean marriedCanSee = project.isEligibleForUser(marriedApplicant);
        boolean youngSingleCanSee = project.isEligibleForUser(youngSingleApplicant);
        
        System.out.println("Single (36yo) can see project: " + singleCanSee);
        System.out.println("Married (25yo) can see project: " + marriedCanSee);
        System.out.println("Single (30yo) can see project: " + youngSingleCanSee);
        System.out.println("Result: " + (singleCanSee && marriedCanSee && !youngSingleCanSee ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 6: Project Application
        System.out.println("Test Case 6: Project Application");
        boolean singleApplied = singleApplicant.applyForProject(project);
        boolean marriedApplied = marriedApplicant.applyForProject(project);
        
        // Try to apply for another project
        Map<String, Object> project2Details = new HashMap<>(projectDetails);
        project2Details.put("projectId", "P002");
        project2Details.put("projectName", "Woodlands Harmony");
        project2Details.put("neighborhood", "Woodlands");
        Project project2 = manager.createProject(project2Details);
        manager.toggleProjectVisibility(project2, true);
        
        boolean singleAppliedAgain = singleApplicant.applyForProject(project2);
        
        System.out.println("Single applicant applied successfully: " + singleApplied);
        System.out.println("Married applicant applied successfully: " + marriedApplied);
        System.out.println("Single applicant could apply to second project: " + singleAppliedAgain);
        System.out.println("Result: " + (singleApplied && marriedApplied && !singleAppliedAgain ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 7: Viewing Application Status after Visibility Toggle Off
        System.out.println("Test Case 7: Viewing Application Status after Visibility Toggle Off");
        manager.toggleProjectVisibility(project, false);
        Application singleApplication = singleApplicant.viewApplication();
        boolean canViewAfterToggle = singleApplication != null;
        System.out.println("Can view application after visibility off: " + canViewAfterToggle);
        System.out.println("Result: " + (canViewAfterToggle ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 8: Single Flat Booking per Successful Application
        System.out.println("Test Case 8: Single Flat Booking per Successful Application");
        singleApplicant.setApplicationStatus(ApplicationStatus.SUCCESSFUL);
        boolean bookingSuccess = singleApplicant.bookFlat(project, FlatType.TWO_ROOM);
        boolean secondBookingAttempt = singleApplicant.bookFlat(project, FlatType.THREE_ROOM);
        System.out.println("First booking successful: " + bookingSuccess);
        System.out.println("Second booking prevented: " + !secondBookingAttempt);
        System.out.println("Result: " + (bookingSuccess && !secondBookingAttempt ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 9: Applicant's enquiries management
        System.out.println("Test Case 9: Applicant's enquiries management");
        Enquiry enquiry = singleApplicant.submitEnquiry(project, "What are the amenities nearby?");
        List<Enquiry> enquiries = singleApplicant.viewEnquiries();
        boolean enquirySubmitted = enquiries.size() > 0;
        
        String enquiryId = enquiry != null ? enquiry.getEnquiryId() : "";
        boolean enquiryEdited = singleApplicant.editEnquiry(enquiryId, "What are the schools nearby?");
        boolean enquiryDeleted = singleApplicant.deleteEnquiry(enquiryId);
        
        System.out.println("Enquiry submitted: " + enquirySubmitted);
        System.out.println("Enquiry edited: " + enquiryEdited);
        System.out.println("Enquiry deleted: " + enquiryDeleted);
        System.out.println("Result: " + (enquirySubmitted && enquiryEdited && enquiryDeleted ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 10: HDB Officer Registration Eligibility
        System.out.println("Test Case 10: HDB Officer Registration Eligibility");
        HDBOfficer officer1 = new HDBOfficer("S5555555F", "password", 40, "MARRIED");
        HDBOfficer officer2 = new HDBOfficer("S6666666G", "password", 38, "SINGLE");
        
        // Officer who has applied for the project shouldn't be able to register
        officer2.applyForProject(project);
        
        boolean officer1Registered = officer1.registerForProject(project);
        boolean officer2Registered = officer2.registerForProject(project);
        
        System.out.println("Officer without application registered: " + officer1Registered);
        System.out.println("Officer with application registered: " + officer2Registered);
        System.out.println("Result: " + (officer1Registered && !officer2Registered ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 11: HDB Officer Registration Status
        System.out.println("Test Case 11: HDB Officer Registration Status");
        RegistrationStatus status = officer1.viewRegistrationStatus();
        System.out.println("Officer registration status: " + status);
        System.out.println("Result: " + (status == RegistrationStatus.PENDING ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 12: Project Detail Access for HDB Officer
        System.out.println("Test Case 12: Project Detail Access for HDB Officer");
        Project visibleProject = officer1.viewProjectDetails(project);
        boolean canAccessDetails = visibleProject != null;
        System.out.println("Officer can access project details: " + canAccessDetails);
        System.out.println("Result: " + (canAccessDetails ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 13: Restriction on Editing Project Details
        System.out.println("Test Case 13: Restriction on Editing Project Details");
        // Not possible to do this since I only code on HDBManager able to edit project
        
        // Test Case 14: Response to Project Enquiries
        System.out.println("Test Case 14: Response to Project Enquiries");
        Enquiry newEnquiry = marriedApplicant.submitEnquiry(project, "When is the expected completion date?");
        boolean replySuccess = officer1.replyToEnquiry(newEnquiry, "The expected completion date is December 2028.");
        System.out.println("Officer replied to enquiry: " + replySuccess);
        System.out.println("Result: " + (replySuccess ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 15: Flat Selection and Booking Management
        System.out.println("Test Case 15: Flat Selection and Booking Management");
        boolean updatedFlats = officer1.updateRemainingFlats(project, FlatType.TWO_ROOM);
        Application retrievedApp = officer1.retrieveApplication(marriedApplicant.getNric());
        boolean statusUpdated = false;
        if (retrievedApp != null) { // Saftey Check for current test case
            statusUpdated = officer1.updateApplicationStatus(retrievedApp, ApplicationStatus.SUCCESSFUL);
        }
        boolean profileUpdated = officer1.updateApplicantProfile(marriedApplicant, FlatType.THREE_ROOM);
        
        System.out.println("Updated flat count: " + updatedFlats);
        System.out.println("Retrieved application: " + (retrievedApp != null)); // since not null it returns truee
        System.out.println("Updated status: " + statusUpdated);
        System.out.println("Updated profile: " + profileUpdated);
        System.out.println("Result: " + (updatedFlats && profileUpdated ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");

        // Test Case 16: Receipt Generation for Flat Booking
        System.out.println("Test Case 16: Receipt Generation for Flat Booking");
        marriedApplicant.setApplicationStatus(ApplicationStatus.SUCCESSFUL);
        marriedApplicant.bookFlat(project, FlatType.THREE_ROOM);
        Application bookedApp = marriedApplicant.viewApplication();
        Receipt receipt = null;
        if (bookedApp != null) {
            receipt = officer1.generateBookingReceipt(bookedApp);
        }
        boolean receiptGenerated = receipt != null;
        System.out.println("Receipt generated: " + receiptGenerated);
        System.out.println("Result: " + (receiptGenerated ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 17: Create, Edit, and Delete BTO Project Listings
        System.out.println("Test Case 17: Create, Edit, and Delete BTO Project Listings");
        // Project already created above
        Map<String, Object> updatedDetails = new HashMap<>();
        updatedDetails.put("projectName", "Tampines GreenView (Updated)");
        boolean projectEdited = manager.editProject(project, updatedDetails);
        boolean projectDeleted = manager.deleteProject(project2);
        
        System.out.println("Project created: " + (project != null));
        System.out.println("Project edited: " + projectEdited);
        System.out.println("Project deleted: " + projectDeleted);
        System.out.println("Result: " + (project != null && projectEdited && projectDeleted ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 18: Single Project Management per Application Period
        // This would require more complex testing with date ranges
        System.out.println("Test Case 18: Single Project Management per Application Period");
        System.out.println("This test requires complex date range testing - manual verification required");
        System.out.println("----------------------------------------\n");
        
        // Test Case 19: Toggle Project Visibility
        System.out.println("Test Case 19: Toggle Project Visibility");
        boolean visibilityOn = manager.toggleProjectVisibility(project, true);
        boolean visibilityOff = manager.toggleProjectVisibility(project, false);
        System.out.println("Visibility toggled on: " + visibilityOn);
        System.out.println("Visibility toggled off: " + visibilityOff);
        System.out.println("Result: " + (visibilityOn && visibilityOff ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 20: View All and Filtered Project Listings
        System.out.println("Test Case 20: View All and Filtered Project Listings");
        List<Project> allProjects = manager.viewAllProjects();
        List<Project> createdProjects = manager.viewCreatedProjects();
        System.out.println("Can view all projects: " + (allProjects != null));
        System.out.println("Can view created projects: " + (createdProjects != null));
        System.out.println("Result: " + (allProjects != null && createdProjects != null ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 21: Manage HDB Officer Registrations
        System.out.println("Test Case 21: Manage HDB Officer Registrations");
        List<Registration> registrations = manager.viewOfficerRegistrations(project);
        Registration officerReg = null;
        // In a real scenario, we would get the actual registration
        // For now, we'll create a dummy one for testing
        officerReg = new Registration("REG001", officer1, project);
        
        boolean approved = manager.approveOfficerRegistration(officerReg);
        System.out.println("Can view registrations: " + (registrations != null));
        System.out.println("Can approve registration: " + approved);
        System.out.println("Result: " + (registrations != null && approved ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 22: Approve or Reject BTO Applications and Withdrawals
        System.out.println("Test Case 22: Approve or Reject BTO Applications and Withdrawals");
        Application testApp = singleApplicant.viewApplication();
        boolean appApproved = false;
        boolean withdrawalApproved = false;
        
        if (testApp != null) {
            appApproved = manager.approveApplication(testApp);
            testApp.requestWithdrawal();
            withdrawalApproved = manager.approveWithdrawal(testApp);
        }
        
        System.out.println("Application approved: " + appApproved);
        System.out.println("Withdrawal approved: " + withdrawalApproved);
        System.out.println("Result: " + (appApproved && withdrawalApproved ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 23: Generate and Filter Reports
        System.out.println("Test Case 23: Generate and Filter Reports");
        Map<String, Object> reportFilters = new HashMap<>();
        reportFilters.put("maritalStatus", "MARRIED");
        reportFilters.put("flatType", FlatType.THREE_ROOM);
        
        Report report = manager.generateReport(reportFilters);
        List<Application> filteredApps = report.applyFilters(reportFilters);
        File reportFile = report.generatePDF();
        
        System.out.println("Report generated: " + (report != null));
        System.out.println("Filters applied: " + (filteredApps != null));
        System.out.println("PDF generated: " + (reportFile != null));
        System.out.println("Result: " + (report != null && filteredApps != null && reportFile != null ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        System.out.println("All test cases completed.");
    }
}
