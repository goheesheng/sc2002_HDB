package ui.submenu;

import java.util.List;
import java.util.Scanner;

import admin.Registration;
import project.Project;
import status.RegistrationStatus;
import user.HDBManager;
import utility.BTODataStore;

public class OfficerRegistrationMenu {
    private HDBManager manager;
    private Scanner scanner = new Scanner(System.in);
    private Project project;

    /**
    * Constructs the menu with the given HDB Manager.
    *
    * @param manager The HDB Manager using this menu
    */
    public OfficerRegistrationMenu(HDBManager manager, Project project) {
        this.manager = manager;
        this.project = project;
    }

    /**
    * Displays the officer registration menu to the user and handles input selection.
    */
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----Officer Registration Menu-----");
            System.out.println("1. View Officer Registration");
            System.out.println("2. Approve Officer Registration");
            System.out.println("3. Reject Officer Registration ");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.println("All Officer Registrations");
                    viewOfficerRegistrations();
                    break;

                case 2:
                    System.out.println("Approve Officer Registration");
                    approveOfficerRegistration();
                    break;

                case 3:
                    System.out.println("Reject Officer Registration");
                    rejectOfficerRegistration();
                    break;

                case 4:
                    return;  //Back to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    
    private void viewOfficerRegistrations(){
        List<Registration> registrations = manager.viewOfficerRegistrations(project);
        if (registrations != null && !registrations.isEmpty()) {
            for (Registration reg : registrations) {
                System.out.println("Registration ID: " + reg.getRegistrationId());
                System.out.println("Officer: " + reg.getOfficer().getNric());
                System.out.println("Project: " + reg.getProject().getProjectId());  // Display project ID
                System.out.println("Status: " + reg.getStatus());
                System.out.println("Date: " + reg.getregistrationDate());
                System.out.println();
            }
        } else {
            System.out.println("No officer registrations found for this project.");
        }
    }

    /**
    * Prompts the manager to enter a registration ID, then attempts to approve the corresponding
    * officer registration. If approved and officer slots are available in the project, the officer
    * is added to the project. Updates the available officer slots accordingly.
    */
    private void approveOfficerRegistration(){
        System.out.print("Enter Registration ID to approve: ");
        String registrationId = scanner.nextLine();

        Registration registration = findRegistrationById(registrationId);
        if (registration != null) {

            // Only proceed if the registration is still pending
            if (registration.getStatus() == RegistrationStatus.PENDING) {

                // Approve the registration using the manager's method
                if (manager.approveOfficerRegistration(registration)) {
                    BTODataStore.getInstance().saveAllData();
                    System.out.println("Officer registration approved and officer assigned to project.");
                } else {
                    System.out.println("Failed to approve officer registration.");
                }
            } else {
                System.out.println("Registration is not in pending status, it cannot be approved.");
            }
        } else {
            System.out.println("Registration not found.");
        }
    }

    /**
    * Prompts the manager to enter a registration ID, then attempts to reject the corresponding
    * officer registration. Only pending registrations can be rejected.
    */
    private void rejectOfficerRegistration(){
        System.out.print("Enter Registration ID to reject: ");
        String registrationId = scanner.nextLine();

        Registration registration = findRegistrationById(registrationId);
        if (registration != null) {

            // Ensure the registration is still pending before rejecting
            if (registration.getStatus() == RegistrationStatus.PENDING) {
                if (registration.reject()) {
                    System.out.println("Officer registration rejected successfully.");
                } else {
                    System.out.println("Failed to reject officer registration.");
                }
            } else {
                System.out.println("Registration is not in pending status, it cannot be rejected.");
            }
        } else {
            System.out.println("Registration not found.");
        }
    }

    /**
    * Finds and returns a registration with the given ID for the current project.
    *
    * @param registrationId The registration ID to search for
    * @return The Registration object if found, null otherwise
    */
    private Registration findRegistrationById(String registrationId) {
        List<Registration> registrations = manager.viewOfficerRegistrations(project);
        for (Registration reg : registrations) {
            if (reg.getRegistrationId().equals(registrationId)) {
                return reg;
            }
        }
        return null;
    }
}
