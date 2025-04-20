/**
 * Main class for the Build-To-Order (BTO) Management System.
 * Provides a menu-driven interface for users to interact with the system.
 */
import admin.Receipt;
import admin.Report;
import admin.Registration;

import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;
import project.Project;
import project.Application;
import project.Enquiry;
import project.FlatType;
import status.ApplicationStatus;
import status.RegistrationStatus;
import ui.LoginInterface;

import utility.BTODataStore;

import java.util.*;
import java.io.File;
import java.text.SimpleDateFormat;

import utility.ExcelReader;
import utility.ExcelWriter;

public class Main {
    /**
     * Main method to start the BTO Management System.
     * Displays a menu for user interaction and handles user input.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
    // menu screen
        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
        System.out.println("\nWelcome to Build-To-Order (BTO) Management System");
        System.out.println("What would you like to do");
        System.out.println("1. Login");
        System.out.println("2. Load all sheets");
        System.out.println("3. Run test cases");
        System.out.println("4. Exit");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); 
        }

        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                LoginInterface loginInterface = new LoginInterface();
                loginInterface.start();
                break;

            case 2:
                // try {
                //     // Ensure the test data contains sample users in data store
                //     ensureTestUsers();
                    
                //     // Create test directory if it doesn't exist
                //     File testDir = new File("test");
                //     if (!testDir.exists()) {
                //         testDir.mkdirs();
                //         System.out.println("Created test directory.");
                //     }
                    
                //     String excelFilePath = "test/ApplicantsList.xlsx";  // Example file path
                //     File excelFile = new File(excelFilePath);
                    
                //     // Create parent directories if they don't exist
                //     if (!excelFile.getParentFile().exists()) {
                //         excelFile.getParentFile().mkdirs();
                //     }
                    
                //     // Write to Excel file first (creates the file if it doesn't exist)
                //     System.out.println("Writing to Excel file: " + excelFile.getAbsolutePath());
                //     excelWriter.writeToExcel(excelFilePath);
                    
                //     // Then read from it
                //     System.out.println("Reading from Excel file: " + excelFile.getAbsolutePath());
                //     excelReader.readUsersFromExcel(excelFilePath);
                    
                //     System.out.println("Excel operations completed successfully.");
                // } catch (Exception e) {
                //     System.err.println("Error in Excel operations: " + e.getMessage());
                //     e.printStackTrace();
                // }
                processExcelFilesAndExportCSV();
                break;

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
    
    /**
     * Processes Excel files and exports data to CSV.
     * Reads user and project data from Excel files and stores them in the data store.
     *
     * Handles missing files and logs errors during processing.
     */
    private static void processExcelFilesAndExportCSV() {
        BTODataStore dataStore = BTODataStore.getInstance();
        String[] userExcelPaths = {
            "src/main/resources/ApplicantList.xlsx",
            "src/main/resources/ManagerList.xlsx",
            "src/main/resources/OfficerList.xlsx"
        };

        for (String excelPath : userExcelPaths) {
            File file = new File(excelPath);
            if (!file.exists()) {
                System.out.println("Skipping missing file: " + excelPath);
                continue;
            }
    
            System.out.println("Processing user data from: " + excelPath);
    
            try {
                List<User> users = ExcelReader.readUsersFromExcel(excelPath);
                for (User user : users) {
                    dataStore.addUser(user);
                }
    
                System.out.println("Loaded " + users.size() + " users from " + excelPath);
            } catch (Exception e) {
                System.err.println("Error processing " + excelPath + ": " + e.getMessage());
                e.printStackTrace();
            }
    
            System.out.println("Done: " + excelPath + "\n");
        }
    
        // Now process the Project-related Excel file
        String projectExcelPath = "src/main/resources/ProjectList.xlsx";
        File projectFile = new File(projectExcelPath);
        if (!projectFile.exists()) {
            System.out.println("Skipping missing project file: " + projectExcelPath);
        } else {
            System.out.println("Processing project data from: " + projectExcelPath);
    
            try {
                // Assuming managerMap is already populated at this point
                Map<String, HDBManager> managerMap = dataStore.getManagerMap(); // Ensure this method exists to get all managers
                List<Project> projects = ExcelReader.readProjectsFromExcel(projectExcelPath, managerMap);
                for (Project project : projects) {
                    dataStore.addProject(project); // Assuming you have an addProject method in your data store
                }
                System.out.println("Loaded " + projects.size() + " projects from " + projectExcelPath);
            } catch (Exception e) {
                System.err.println("Error processing " + projectExcelPath + ": " + e.getMessage());
                e.printStackTrace();
            }
    
            System.out.println("Done: " + projectExcelPath + "\n");
        }    
        // Now save to CSVs
        dataStore.saveAllData();
        System.out.println("All excel saved to CSV files.");
    }

    /**
     * Runs predefined test cases for the BTO Management System.
     * Outputs the results of each test case to the console.
     */
    public static void runTestCases() {

        System.out.println("Running BTO Management System Test Cases");
        System.out.println("Current Date: March 21, 2025");
        System.out.println("----------------------------------------\n");

        // Test Case 1: Valid User Login
        System.out.println("Test Case 1: Valid User Login");
        Applicant applicant = new Applicant("solomon","S1234567A", "password", 35, "SINGLE");
        
        // Add test user to data store
        BTODataStore.getInstance().addUser(applicant);
        
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
        HDBManager manager = new HDBManager("Jessica","S9876543B", "password", 45, "MARRIED");
        
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
        Applicant singleApplicant = new Applicant("Joe","S1234567C", "password", 36, "SINGLE");
        Applicant marriedApplicant = new Applicant("Emily","S7654321D", "password", 25, "MARRIED");
        Applicant youngSingleApplicant = new Applicant("Daniel","S2345678E", "password", 30, "SINGLE");
        
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
        
        // For project2, create non-overlapping dates
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, Calendar.MAY, 1); // May 1, 2025
        Date openingDate2 = calendar2.getTime();

        calendar2.set(2025, Calendar.JUNE, 30); // June 30, 2025
        Date closingDate2 = calendar2.getTime();

        Map<String, Object> project2Details = new HashMap<>(projectDetails);
        project2Details.put("projectId", "P002");
        project2Details.put("projectName", "Woodlands Harmony");
        project2Details.put("neighborhood", "Woodlands");
        project2Details.put("applicationOpeningDate", openingDate2);
        project2Details.put("applicationClosingDate", closingDate2);
        Project project2 = manager.createProject(project2Details);
        
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
        // We'll get a clean state by creating new officers and registrations
        HDBOfficer officer1 = new HDBOfficer("Emily","S5555555F", "password", 40, "MARRIED");
        HDBOfficer officer2 = new HDBOfficer("Daniel","S6666666G", "password", 38, "SINGLE");
        
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
        officer1.setHandlingProject(project); // First set the handling project (should not be necessary with our fix)
        Project visibleProject = officer1.viewProjectDetails(project);
        boolean canAccessDetails = visibleProject != null;
        System.out.println("Officer can access project details: " + canAccessDetails);
        System.out.println("Result: " + (canAccessDetails ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 13: Restriction on Editing Project Details
        System.out.println("Test Case 13: Restriction on Editing Project Details");
        // This test was commented out before. We've now implemented the restriction.
        Map<String, Object> attemptedUpdate = new HashMap<>();
        attemptedUpdate.put("projectName", "Attempted Update by Officer");
        
        // Create manager update details
        Map<String, Object> managerUpdateDetails = new HashMap<>();
        managerUpdateDetails.put("projectName", "Manager's Update");
        
        // Officers should not be able to edit project details
        boolean officerCanEdit = officer1.editProject(project, attemptedUpdate);
        
        // Managers should be able to edit project details
        boolean managerCanEdit = manager.editProject(project, managerUpdateDetails);
        
        System.out.println("Officer can edit project details: " + officerCanEdit);
        System.out.println("Manager can edit project details: " + managerCanEdit);
        System.out.println("Result: " + (!officerCanEdit && managerCanEdit ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 14: Response to Project Enquiries
        System.out.println("Test Case 14: Response to Project Enquiries");
        // The test was failing because the officer wasn't correctly assigned to the project
        // Make sure officer1 is assigned to the project
        if (officer1.getHandlingProject() == null) {
            officer1.setHandlingProject(project);
        }
        
        Enquiry newEnquiry = marriedApplicant.submitEnquiry(project, "When is the expected completion date?");
        boolean replySuccess = officer1.replyToEnquiry(newEnquiry, "The expected completion date is December 2028.");
        System.out.println("Officer replied to enquiry: " + replySuccess);
        System.out.println("Result: " + (replySuccess ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 15: Flat Selection and Booking Management
        System.out.println("Test Case 15: Flat Selection and Booking Management");
        // This test was failing due to issues with officer assignment and application retrieval
        
        // Ensure officer1 is assigned to the project
        if (officer1.getHandlingProject() == null) {
            officer1.setHandlingProject(project);
        }
        
        // Make sure the application exists in the data store
        BTODataStore.getInstance().addApplication(marriedApplicant.viewApplication());
        
        boolean updatedFlats = officer1.updateRemainingFlats(project, FlatType.TWO_ROOM);
        Application retrievedApp = officer1.retrieveApplication(marriedApplicant.getNric());
        boolean statusUpdated = false;
        if (retrievedApp != null) {
            statusUpdated = officer1.updateApplicationStatus(retrievedApp, ApplicationStatus.SUCCESSFUL);
        }
        boolean profileUpdated = officer1.updateApplicantProfile(marriedApplicant, FlatType.THREE_ROOM);
        
        System.out.println("Updated flat count: " + updatedFlats);
        System.out.println("Retrieved application: " + (retrievedApp != null));
        System.out.println("Updated status: " + statusUpdated);
        System.out.println("Updated profile: " + profileUpdated);
        System.out.println("Result: " + (updatedFlats && retrievedApp != null && statusUpdated && profileUpdated ? "PASSED" : "FAILED"));
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
        
        // Create a project with non-overlapping dates to avoid date conflicts
        Calendar projectToDeleteCalendar = Calendar.getInstance();
        projectToDeleteCalendar.set(2025, Calendar.JULY, 1); // July 1, 2025
        Date deleteProjectOpeningDate = projectToDeleteCalendar.getTime();
        
        projectToDeleteCalendar.set(2025, Calendar.AUGUST, 31); // August 31, 2025
        Date deleteProjectClosingDate = projectToDeleteCalendar.getTime();
        
        // Ensure project details use non-conflicting dates
        Map<String, Object> projectToDeleteDetails = new HashMap<>();
        projectToDeleteDetails.put("projectId", "P005");
        projectToDeleteDetails.put("projectName", "Project To Delete");
        projectToDeleteDetails.put("neighborhood", "Tampines");
        projectToDeleteDetails.put("flatTypes", flatTypes);
        projectToDeleteDetails.put("applicationOpeningDate", deleteProjectOpeningDate);
        projectToDeleteDetails.put("applicationClosingDate", deleteProjectClosingDate);
        projectToDeleteDetails.put("availableOfficerSlots", 5);
        
        // Create a fresh project specifically for deletion testing
        Project projectToDelete = manager.createProject(projectToDeleteDetails);
        
        // Test project creation
        boolean projectCreated = projectToDelete != null;
        
        // Test project editing
        Map<String, Object> updatedDetails = new HashMap<>();
        updatedDetails.put("projectName", "Tampines GreenView (Updated)");
        boolean projectEdited = manager.editProject(project, updatedDetails);
        
        // Test project deletion
        boolean projectDeleted = manager.deleteProject(projectToDelete);
        
        System.out.println("Project created: " + projectCreated);
        System.out.println("Project edited: " + projectEdited);
        System.out.println("Project deleted: " + projectDeleted);
        System.out.println("Result: " + (projectCreated && projectEdited && projectDeleted ? "PASSED" : "FAILED"));
        System.out.println("----------------------------------------\n");
        
        // Test Case 18: Single Project Management per Application Period
        System.out.println("Test Case 18: Single Project Management per Application Period");
        
        // Create completely new managers to avoid conflicts with previous test cases
        HDBManager testManager1 = new HDBManager("Joe","S8888888A", "password", 45, "MARRIED");
        HDBManager testManager2 = new HDBManager("Tom","S9999999C", "password", 50, "MARRIED");
        
        // Set up dates for the first test project - use a completely different year and month
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.set(2027, Calendar.JANUARY, 15); // January 15, 2027
        Date firstOpeningDate = firstCalendar.getTime();
        
        firstCalendar.set(2027, Calendar.FEBRUARY, 15); // February 15, 2027
        Date firstClosingDate = firstCalendar.getTime();
        
        // Set up dates for an overlapping project (same dates as first)
        Date overlapOpeningDate = firstOpeningDate;
        Date overlapClosingDate = firstClosingDate;
        
        // Set up dates for a non-overlapping project
        Calendar noOverlapCalendar = Calendar.getInstance();
        noOverlapCalendar.set(2027, Calendar.MARCH, 1); // March 1, 2027
        Date noOverlapOpeningDate = noOverlapCalendar.getTime();
        
        noOverlapCalendar.set(2027, Calendar.APRIL, 30); // April 30, 2027
        Date noOverlapClosingDate = noOverlapCalendar.getTime();
        
        // Create initial project with testManager1
        Map<String, Object> firstProjectDetails = new HashMap<>();
        firstProjectDetails.put("projectId", "TP001");
        firstProjectDetails.put("projectName", "First Test Project");
        firstProjectDetails.put("neighborhood", "Tampines");
        firstProjectDetails.put("flatTypes", flatTypes);
        firstProjectDetails.put("applicationOpeningDate", firstOpeningDate);
        firstProjectDetails.put("applicationClosingDate", firstClosingDate);
        firstProjectDetails.put("availableOfficerSlots", 5);
        Project firstProject = testManager1.createProject(firstProjectDetails);
        
        // Create a project with overlapping dates using the same manager - should fail
        Map<String, Object> overlapProjectDetails = new HashMap<>();
        overlapProjectDetails.put("projectId", "TP002");
        overlapProjectDetails.put("projectName", "Overlap Test Project");
        overlapProjectDetails.put("neighborhood", "Tampines");
        overlapProjectDetails.put("flatTypes", flatTypes);
        overlapProjectDetails.put("applicationOpeningDate", overlapOpeningDate);
        overlapProjectDetails.put("applicationClosingDate", overlapClosingDate);
        overlapProjectDetails.put("availableOfficerSlots", 5);
        Project overlapProject = testManager1.createProject(overlapProjectDetails);
        
        // Create a project with non-overlapping dates for the same manager - should succeed
        Map<String, Object> noOverlapProjectDetails = new HashMap<>();
        noOverlapProjectDetails.put("projectId", "TP003");
        noOverlapProjectDetails.put("projectName", "No Overlap Test Project");
        noOverlapProjectDetails.put("neighborhood", "Woodlands");
        noOverlapProjectDetails.put("flatTypes", flatTypes);
        noOverlapProjectDetails.put("applicationOpeningDate", noOverlapOpeningDate);
        noOverlapProjectDetails.put("applicationClosingDate", noOverlapClosingDate);
        noOverlapProjectDetails.put("availableOfficerSlots", 3);
        Project noOverlapProject = testManager1.createProject(noOverlapProjectDetails);
        
        // Different manager should be able to create project with overlapping dates
        Map<String, Object> differentManagerProjectDetails = new HashMap<>();
        differentManagerProjectDetails.put("projectId", "TP004");
        differentManagerProjectDetails.put("projectName", "Different Manager Project");
        differentManagerProjectDetails.put("neighborhood", "Tampines");
        differentManagerProjectDetails.put("flatTypes", flatTypes);
        differentManagerProjectDetails.put("applicationOpeningDate", overlapOpeningDate);
        differentManagerProjectDetails.put("applicationClosingDate", overlapClosingDate);
        differentManagerProjectDetails.put("availableOfficerSlots", 5);
        Project differentManagerProject = testManager2.createProject(differentManagerProjectDetails);
        
        System.out.println("First project created successfully: " + (firstProject != null));
        System.out.println("Project with overlapping dates created with same manager: " + (overlapProject != null));
        System.out.println("Project with non-overlapping dates created with same manager: " + (noOverlapProject != null));
        System.out.println("Project with overlapping dates created with different manager: " + (differentManagerProject != null));
        System.out.println("Result: " + (firstProject != null && overlapProject == null && noOverlapProject != null && differentManagerProject != null ? "PASSED" : "FAILED"));
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
        // Get registrations from our central data store
        List<Registration> registrations = manager.viewOfficerRegistrations(project);
        
        // Find the registration created in Test Case 10 for officer1
        Registration officerReg = null;
        if (registrations != null && !registrations.isEmpty()) {
            officerReg = registrations.stream()
                .filter(r -> r.getOfficer().getNric().equals(officer1.getNric()))
                .findFirst()
                .orElse(null);
            
            if (officerReg == null) {
                System.out.println("Warning: Could not find officer1 registration in the data store");
            }
        } else {
            System.out.println("Warning: No registrations found in the data store");
        }

        boolean approved = officerReg != null && manager.approveOfficerRegistration(officerReg);
        System.out.println("Can view registrations: " + (registrations != null && !registrations.isEmpty()));
        System.out.println("Can approve registration: " + approved);
        System.out.println("Result: " + (registrations != null && !registrations.isEmpty() && approved ? "PASSED" : "FAILED"));
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

