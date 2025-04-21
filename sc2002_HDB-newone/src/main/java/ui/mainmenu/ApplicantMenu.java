package ui.mainmenu;

import status.ApplicationStatus;
import status.WithdrawalStatus;
import user.Applicant;
import project.Project;
import utility.BTODataStore; // Import the data store
import project.FlatType; // Import FlatType
import project.Application; // Import Application
import project.Enquiry;
import user.User;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicantMenu extends UserMenu {
    // Removed projectMenu field, access projects via DataStore

    private BTODataStore dataStore; // Hold a reference

    public ApplicantMenu(Applicant user) {
        super(user);
        this.dataStore = BTODataStore.getInstance(); // Get instance
    }

    @Override
    public void displayMenu() {
        int choice;
        Applicant currentApplicant = (Applicant) user; // Cast for applicant specific methods

        do {
            System.out.println("\n----- Applicant Menu (User: " + user.getNric() + ") -----");
            System.out.println("1. View Available Projects"); // Changed wording slightly
            System.out.println("2. Apply for Project");
            System.out.println("3. View My Application");
            System.out.println("4. Request Application Withdrawal");
            System.out.println("5. Submit Enquiry");
            System.out.println("6. View My Enquiries"); // New option
            System.out.println("7. Change password");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");

            try { // Basic input error handling
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                choice = 0; // Set choice to invalid value to loop again
            }

            switch (choice) {
                case 1:
                    viewAvailableProjects(currentApplicant);
                    break;
                case 2:
                    applyForProject(currentApplicant);
                    break;
                case 3:
                    viewMyApplication(currentApplicant);
                    break;
                case 4:
                    requestWithdrawal(currentApplicant);
                    break;
                case 5:
                    submitEnquiry();
                    break;
                case 6:
                    viewMyEnquiries();
                    break;
                case 7:
                    changePassword();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    // Save is handled by shutdown hook
                    return; // Exit this menu back to the main login loop
                default:
                    if (choice != 0)
                        System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Pause for user

        } while (choice != 8);
    }

    // --- Implementation for Menu Options ---

    private void viewAvailableProjects(Applicant applicant) {
        System.out.println("\n--- Available BTO Projects ---");
        List<Project> allProjects = dataStore.getAllProjects();
        List<Project> availableProjects = allProjects.stream()
                .filter(Project::isVisible) // Check visibility flag
                .filter(p -> !p.getProjectName().toLowerCase().contains("test")) // Filter out test projects
                .distinct() // Ensure each project appears only once
                // Add filtering based on applicant eligibility (Single vs Married)
                .filter(p -> applicant.getMaritalStatus().equals("SINGLE")
                        ? p.getFlatTypes().containsKey(FlatType.TWO_ROOM)
                        : true) // Simplified eligibility check
                .collect(Collectors.toList());

        if (availableProjects.isEmpty()) {
            System.out.println("No projects currently available for application.");
        } else {
            for (int i = 0; i < availableProjects.size(); i++) {
                Project p = availableProjects.get(i);
                System.out.printf("%d. %s (%s) - Flats: %s%n",
                        i + 1,
                        p.getProjectName(),
                        p.getneighborhood(),
                        p.getFlatTypes().entrySet().stream()
                                .map(entry -> entry.getKey() + ": " + entry.getValue())
                                .collect(Collectors.joining(", ")));
            }
        }
    }

    private void applyForProject(Applicant applicant) {
        System.out.println("\n--- Apply for Project ---");
    
        // 1) Block if already applied
        if (applicant.getAppliedProject() != null) {
            System.out.println("You have already applied for project: "
                + applicant.getAppliedProject().getProjectName());
            System.out.println("Your application status: "
                + applicant.getApplicationStatus());
            return;
        }
    
        // 2) Show all VISIBLE projects (no more manual eligibility filter)
        List<Project> visibleProjects = dataStore.getAllProjects().stream()
            .filter(Project::isVisible)
            .collect(Collectors.toList());
    
        // 3) None at all?
        if (visibleProjects.isEmpty()) {
            System.out.println("No projects available for you to apply for at the moment.");
            return;
        }
    
        // 4) Print the menu
        System.out.println("Available projects to apply for:");
        for (int i = 0; i < visibleProjects.size(); i++) {
            Project p = visibleProjects.get(i);
            System.out.printf("%d. %s – TWO_ROOM: %d, THREE_ROOM: %d%n",
                i + 1,
                p.getProjectName(),
                p.getRemainingFlats(FlatType.TWO_ROOM),
                p.getRemainingFlats(FlatType.THREE_ROOM)
            );
        }
    
        // 5) Read user choice
        System.out.print("Enter the number of the project you want to apply for (or 0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
    
        // 6) Handle the choice
        if (choice == 0) {
            System.out.println("Application cancelled.");
            return;
        }
        if (choice < 1 || choice > visibleProjects.size()) {
            System.out.println("Invalid project selection.");
            return;
        }
    
        Project selected = visibleProjects.get(choice - 1);
    
        // 7) Check if application period has ended
        if (!selected.isWithinApplicationPeriod()) {
            System.out.println("This project's application period has ended.");
            return;
        }
    
        // 8) Delegate to your Applicant logic (which does the real eligibility + flat‐type pick)
        boolean success = applicant.applyForProject(selected);
        if (!success) {
            System.out.println("Application failed. You might be ineligible or have already applied.");
        } else {
            // Persist the newly created Application
            Application app = applicant.viewApplication();
            dataStore.addApplication(app);
    
            System.out.println("\nApplication submitted successfully!");
            System.out.println("  Project:   " + selected.getProjectName());
            System.out.println("  Flat Type: " + app.getFlatType());
            System.out.println("  Status:    " + app.getStatus());
        }
    }
    
    

    private void viewMyApplication(Applicant applicant) {
        System.out.println("\n--- My Application Status ---");
        Application myApp = applicant.viewApplication(); // Assumes viewApplication finds the app in the project's list

        if (myApp != null) {
            System.out.println("Project: " + myApp.getProject().getProjectName());
            System.out.println("Flat Type Applied: " + myApp.getFlatType());
            System.out.println("Status: " + myApp.getStatus());
            
            // Check withdrawal status
            WithdrawalStatus withdrawalStatus = myApp.getWithdrawalStatus();
            if (withdrawalStatus == WithdrawalStatus.PENDING) {
                System.out.println("Withdrawal Request: PENDING MANAGER APPROVAL");
            } else if (withdrawalStatus == WithdrawalStatus.APPROVED) {
                System.out.println("Withdrawal Request: APPROVED");
            } else if (withdrawalStatus == WithdrawalStatus.REJECTED) {
                System.out.println("Withdrawal Request: REJECTED");
            }

        } else if (applicant.getAppliedProject() != null) {
            // Data inconsistency? Applicant thinks they applied, but no Application object
            // found?
            System.out.println("Applied Project: " + applicant.getAppliedProject().getProjectName());
            System.out.println("Status (Applicant record): " + applicant.getApplicationStatus());
            System.out.println("Warning: Could not find detailed application object.");
        } else {
            System.out.println("You have not submitted any application.");
        }
    }

    private void requestWithdrawal(Applicant applicant) {
        System.out.println("\n--- Request Application Withdrawal ---");
        Application myApp = applicant.viewApplication();

         if (myApp != null && myApp.getStatus() != ApplicationStatus.BOOKED && myApp.getWithdrawalStatus() != WithdrawalStatus.APPROVED) {
        System.out.print("Are you sure you want to request withdrawal for project "
                    + myApp.getProject().getProjectName() + "? (Y/N): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                if (myApp.requestWithdrawal()) { // This just sets the flag in Application
                    System.out.println("Withdrawal request submitted. Waiting for HDB Manager approval.");
                    // Persistence will save the Application object's new withdrawalRequest state.
                } else {
                    System.out.println("Failed to submit withdrawal request.");
                }
            } else {
                System.out.println("Withdrawal request cancelled.");
            }
        } else if (myApp != null && myApp.getWithdrawalStatus() == WithdrawalStatus.APPROVED) {
            System.out.println("This application has already been withdrawn.");
        } else if (myApp != null && myApp.getStatus() == ApplicationStatus.BOOKED) {
            System.out.println("Cannot request withdrawal after a flat has been booked.");
        } else {
            System.out.println("No active application found to withdraw from.");
        }
    }

    private void submitEnquiry() {
        System.out.println("\n--- Submit Enquiry ---");
        List<Project> projects = dataStore.getAllProjects();
        if (projects.isEmpty()) {
            System.out.println("No projects available to enquire about.");
            return;
        }
        System.out.println("Select a project to enquire about (or 0 for general enquiry):");
        for (int i = 0; i < projects.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, projects.get(i).getProjectName());
        }
        System.out.print("Enter project number: ");
        int projChoice;
        try {
            projChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Project targetProject = null;
        if (projChoice > 0 && projChoice <= projects.size()) {
            targetProject = projects.get(projChoice - 1);
        } else if (projChoice != 0) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter your enquiry text: ");
        String text = scanner.nextLine();

        if (text.trim().isEmpty()) {
            System.out.println("Enquiry cannot be empty.");
            return;
        }

        // Use the method in the User class
        Enquiry newEnquiry = user.submitEnquiry(targetProject, text);
        // The submitEnquiry method should ideally add the enquiry to the DataStore as
        // well
        // For now, let's assume it does or add it manually:
        // dataStore.addEnquiry(newEnquiry); // Ensure no duplicates

        System.out.println("Enquiry submitted successfully (ID: " + newEnquiry.getEnquiryId() + ")");
    }

    private void viewMyEnquiries() {
        System.out.println("\n--- My Enquiries ---");
        List<Enquiry> myEnquiries = user.viewEnquiries();
        
        if (myEnquiries.isEmpty()) {
            System.out.println("You have not submitted any enquiries yet.");
            return;
        }
        
        for (Enquiry enquiry : myEnquiries) {
            System.out.println("----------------------------------------");
            System.out.println("Enquiry ID: " + enquiry.getEnquiryId());
            System.out.println("Project: " + (enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "General Enquiry"));
            System.out.println("Date: " + enquiry.getDateSubmitted());
            System.out.println("Question: " + enquiry.getEnquiryText());
            
            if (enquiry.getReply() != null && !enquiry.getReply().isEmpty()) {
                System.out.println("Reply: " + enquiry.getReply());
                User repliedBy = enquiry.getRepliedBy();
                if (repliedBy != null) {
                    System.out.println("Replied by: " + repliedBy.getName());
                }
                System.out.println("Reply Date: " + enquiry.getReplyDate());
            } else {
                System.out.println("Status: No reply yet");
            }
        }
        System.out.println("----------------------------------------");
    }
}