package ui.mainmenu;
import status.ApplicationStatus;
import user.Applicant;
import project.Project;
import utility.BTODataStore; // Import the data store
import project.FlatType;    // Import FlatType
import project.Application; // Import Application
import project.Enquiry;

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
            System.out.println("6: Change password");
            System.out.println("7. Logout");
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
                     changePassword();
                     break;
                case 7:
                    System.out.println("Logging out...");
                    // Save is handled by shutdown hook
                    return; // Exit this menu back to the main login loop
                default:
                    if(choice != 0) System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Pause for user

        } while (choice != 7);
    }

    // --- Implementation for Menu Options ---

    private void viewAvailableProjects(Applicant applicant) {
        System.out.println("\n--- Available BTO Projects ---");
        List<Project> allProjects = dataStore.getAllProjects();
        List<Project> availableProjects = allProjects.stream()
                .filter(Project::isVisible) // Check visibility flag
                // Add filtering based on applicant eligibility (Single vs Married)
                .filter(p -> applicant.getMaritalStatus().equals("SINGLE") ? p.getFlatTypes().containsKey(FlatType.TWO_ROOM) : true) // Simplified eligibility check
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
                           .collect(Collectors.joining(", "))
                 );
            }
        }
    }

     private void applyForProject(Applicant applicant) {
        System.out.println("\n--- Apply for Project ---");

        // Check if already applied
        if (applicant.getAppliedProject() != null) {
             System.out.println("You have already applied for project: " + applicant.getAppliedProject().getProjectName());
             System.out.println("Your application status: " + applicant.getApplicationStatus());
             return;
        }

        List<Project> allProjects = dataStore.getAllProjects();
         List<Project> applicableProjects = allProjects.stream()
                .filter(Project::isVisible)
                // Filter based on eligibility - reuse logic from view or Applicant class
                .filter(p -> applicant.getMaritalStatus().equalsIgnoreCase("SINGLE") ? p.getFlatTypes().containsKey(FlatType.TWO_ROOM) && p.getFlatTypes().get(FlatType.TWO_ROOM)>0 && applicant.getAge() >= 35: p.getFlatTypes().values().stream().anyMatch(count -> count > 0) && applicant.getAge()>=21)
                .collect(Collectors.toList());


        if (applicableProjects.isEmpty()) {
            System.out.println("No projects available for you to apply for at the moment.");
            return;
        }

        System.out.println("Available projects to apply for:");
         for (int i = 0; i < applicableProjects.size(); i++) {
             Project p = applicableProjects.get(i);
             System.out.printf("%d. %s (%s)%n", i + 1, p.getProjectName(), p.getneighborhood());
         }
         System.out.print("Enter the number of the project you want to apply for (or 0 to cancel): ");

         int projectChoice;
         try {
            projectChoice = Integer.parseInt(scanner.nextLine());
         } catch (NumberFormatException e) {
             System.out.println("Invalid input.");
             return;
         }


         if (projectChoice > 0 && projectChoice <= applicableProjects.size()) {
             Project selectedProject = applicableProjects.get(projectChoice - 1);

             // Determine Flat Type (Simplified logic - assumes eligibility check passed)
              FlatType chosenFlatType;
              if (applicant.getMaritalStatus().equalsIgnoreCase("SINGLE")) {
                  chosenFlatType = FlatType.TWO_ROOM;
              } else {
                   // Offer choice if both available? For now, default logic
                   if (selectedProject.getFlatTypes().containsKey(FlatType.THREE_ROOM) && selectedProject.getFlatTypes().get(FlatType.THREE_ROOM) > 0) {
                       chosenFlatType = FlatType.THREE_ROOM; // Prioritize? Or ask user?
                   } else {
                       chosenFlatType = FlatType.TWO_ROOM;
                   }
              }
             System.out.println("Applying for " + chosenFlatType + " in " + selectedProject.getProjectName());

              // Call the core logic in Applicant - NEEDS REFINEMENT
              // The applicant.applyForProject needs to:
              // 1. Check eligibility AGAIN maybe (or trust the filtering)
              // 2. Check if already applied (already done above)
              // 3. Create the Application object
              // 4. Add the Application object to the BTODataStore
              // 5. Update the applicant's state (appliedProject, status)

              // --- Revised Application Process ---
              if (applicant.getAppliedProject() == null && selectedProject.isEligibleForUser(applicant)) { // Double check
                    String appId = "APP" + System.currentTimeMillis(); // Replace with better ID gen
                    Application newApplication = new Application(appId, applicant, selectedProject, chosenFlatType);
                    dataStore.addApplication(newApplication); // Add to central store

                    // Update Applicant state
                    applicant.setAppliedProject(selectedProject); // Need a setter or different logic
                    applicant.setApplicationStatus(ApplicationStatus.PENDING);

                    System.out.println("Application submitted successfully for project: " + selectedProject.getProjectName());
              } else {
                   System.out.println("Application failed. You might be ineligible or have already applied.");
              }


         } else if (projectChoice == 0) {
             System.out.println("Application cancelled.");
         } else {
             System.out.println("Invalid project selection.");
         }
    }

    private void viewMyApplication(Applicant applicant) {
         System.out.println("\n--- My Application Status ---");
         Application myApp = applicant.viewApplication(); // Assumes viewApplication finds the app in the project's list

         if (myApp != null) {
              System.out.println("Project: " + myApp.getProject().getProjectName());
              System.out.println("Flat Type Applied: " + myApp.getFlatType());
              System.out.println("Status: " + myApp.getStatus());
              if (myApp.isWithdrawalRequested()) {
                  System.out.println("Withdrawal Request: PENDING MANAGER APPROVAL");
              }
         } else if (applicant.getAppliedProject() != null) {
             // Data inconsistency? Applicant thinks they applied, but no Application object found?
             System.out.println("Applied Project: " + applicant.getAppliedProject().getProjectName());
             System.out.println("Status (Applicant record): " + applicant.getApplicationStatus());
             System.out.println("Warning: Could not find detailed application object.");
         }
         else {
             System.out.println("You have not submitted any application.");
         }
    }

     private void requestWithdrawal(Applicant applicant) {
        System.out.println("\n--- Request Application Withdrawal ---");
        Application myApp = applicant.viewApplication();

        if (myApp != null && myApp.getStatus() != ApplicationStatus.BOOKED && !myApp.isWithdrawalRequested()) {
            System.out.print("Are you sure you want to request withdrawal for project "
                             + myApp.getProject().getProjectName() + "? (Y/N): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                 if(myApp.requestWithdrawal()) { // This just sets the flag in Application
                     System.out.println("Withdrawal request submitted. Waiting for HDB Manager approval.");
                     // Persistence will save the Application object's new withdrawalRequest state.
                 } else {
                      System.out.println("Failed to submit withdrawal request.");
                 }
            } else {
                 System.out.println("Withdrawal request cancelled.");
            }
        } else if (myApp != null && myApp.isWithdrawalRequested()){
             System.out.println("You have already requested withdrawal for this application.");
        } else if (myApp != null && myApp.getStatus() == ApplicationStatus.BOOKED) {
            System.out.println("Cannot request withdrawal after a flat has been booked.");
        }
        else {
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
         // The submitEnquiry method should ideally add the enquiry to the DataStore as well
         // For now, let's assume it does or add it manually:
         // dataStore.addEnquiry(newEnquiry); // Ensure no duplicates

         System.out.println("Enquiry submitted successfully (ID: " + newEnquiry.getEnquiryId() + ")");
     }

    private void changePassword() {
         System.out.println("\n--- Change Password ---");
         System.out.print("Enter current password: ");
         String oldPass = scanner.nextLine();
         System.out.print("Enter new password: ");
         String newPass = scanner.nextLine();
         System.out.print("Confirm new password: ");
         String confirmPass = scanner.nextLine();

         if (!newPass.equals(confirmPass)) {
             System.out.println("New passwords do not match.");
             return;
         }
         if (newPass.trim().isEmpty()) {
             System.out.println("Password cannot be empty.");
             return;
         }

         if (user.changePassword(oldPass, newPass)) {
             System.out.println("Password changed successfully.");
             // The User object in dataStore.allUsers now has the new password.
             // Persistence will save this change.
         } else {
             System.out.println("Password change failed. Incorrect current password.");
         }
     }
}