// TestCases.java

import admin.Receipt;
import admin.Report;
import admin.Registration;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import project.Project;
import project.Application;
import project.Enquiry;
import project.FlatType;
import status.ApplicationStatus;
import status.RegistrationStatus;

import java.util.*;
import java.io.File;

public class TestCases {
    public static void main(String[] args) {
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
        
      
        System.out.println("----------------------------------------\n");
        
        System.out.println("All test cases completed.");
    }
}
