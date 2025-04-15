package menu;

import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;

import user.HDBManager;
import utility.BtoDataStore;
import project.Application;
import status.ApplicationStatus;

public class ApplicationManagerMenu {

    private HDBManager manager;
    private Scanner scanner = new Scanner(System.in);

    public ApplicationManagerMenu(HDBManager manager) {
        this.manager = manager;
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n----- Application Manager Menu -----");
            System.out.println("1. Approve Application");
            System.out.println("2. Reject Application");
            System.out.println("3. Approve Withdrawal");
            System.out.println("4. Reject Withdrawal");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear the buffer
                continue;
            }
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    approveApplication();
                    break;
                case 2:
                    rejectApplication();
                    break;
                case 3:
                    approveWithdrawal();
                    break;
                case 4:
                    rejectWithdrawal();
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void approveApplication() {
        BtoDataStore dataStore = BtoDataStore.getInstance();
        // Get applications with PENDING status
        List<Application> pendingApps = dataStore.getApplications().stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());
        if (pendingApps.isEmpty()) {
            System.out.println("No pending applications for approval.");
            return;
        }
        System.out.println("\n--- Pending Applications ---");
        for (int i = 0; i < pendingApps.size(); i++) {
            Application app = pendingApps.get(i);
            System.out.printf("%d. %s: Applicant %s, Project %s, Status: %s%n",
                    i + 1,
                    app.getApplicationId(),
                    app.getApplicant().getNric(),
                    app.getProject().getProjectName(),
                    app.getStatus());
        }
        System.out.print("Enter the number of the application to approve (or 0 to cancel): ");
        int index = scanner.nextInt();
        scanner.nextLine(); // consume newline
        if (index == 0) {
            System.out.println("Approval cancelled.");
            return;
        }
        if (index < 1 || index > pendingApps.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Application appToApprove = pendingApps.get(index - 1);
        // Assume the HDBManager's approveApplication method updates the status appropriately
        if (manager.approveApplication(appToApprove)) {
            System.out.println("Application " + appToApprove.getApplicationId() + " approved successfully.");
        } else {
            System.out.println("Failed to approve the application.");
        }
    }

    private void rejectApplication() {
        BtoDataStore dataStore = BtoDataStore.getInstance();
        List<Application> pendingApps = dataStore.getApplications().stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                .collect(Collectors.toList());
        if (pendingApps.isEmpty()) {
            System.out.println("No pending applications for rejection.");
            return;
        }
        System.out.println("\n--- Pending Applications ---");
        for (int i = 0; i < pendingApps.size(); i++) {
            Application app = pendingApps.get(i);
            System.out.printf("%d. %s: Applicant %s, Project %s, Status: %s%n",
                    i + 1,
                    app.getApplicationId(),
                    app.getApplicant().getNric(),
                    app.getProject().getProjectName(),
                    app.getStatus());
        }
        System.out.print("Enter the number of the application to reject (or 0 to cancel): ");
        int index = scanner.nextInt();
        scanner.nextLine(); // consume newline
        if (index == 0) {
            System.out.println("Rejection cancelled.");
            return;
        }
        if (index < 1 || index > pendingApps.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Application appToReject = pendingApps.get(index - 1);
        if (manager.rejectApplication(appToReject)) {
            System.out.println("Application " + appToReject.getApplicationId() + " rejected successfully.");
        } else {
            System.out.println("Failed to reject the application.");
        }
    }

    private void approveWithdrawal() {
        BtoDataStore dataStore = BtoDataStore.getInstance();
        // Get applications with pending withdrawal request
        List<Application> withdrawalApps = dataStore.getApplications().stream()
                .filter(a -> a.isWithdrawalRequested())
                .collect(Collectors.toList());
        if (withdrawalApps.isEmpty()) {
            System.out.println("No withdrawal requests pending approval.");
            return;
        }
        System.out.println("\n--- Pending Withdrawal Requests ---");
        for (int i = 0; i < withdrawalApps.size(); i++) {
            Application app = withdrawalApps.get(i);
            System.out.printf("%d. %s: Applicant %s, Project %s%n",
                    i + 1,
                    app.getApplicationId(),
                    app.getApplicant().getNric(),
                    app.getProject().getProjectName());
        }
        System.out.print("Enter the number of the application to approve withdrawal (or 0 to cancel): ");
        int index = scanner.nextInt();
        scanner.nextLine();
        if (index == 0) {
            System.out.println("Operation cancelled.");
            return;
        }
        if (index < 1 || index > withdrawalApps.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Application appWithdrawal = withdrawalApps.get(index - 1);
        if (manager.approveWithdrawal(appWithdrawal)) {
            System.out.println("Withdrawal approved for application " + appWithdrawal.getApplicationId());
        } else {
            System.out.println("Failed to approve withdrawal.");
        }
    }

    private void rejectWithdrawal() {
        BtoDataStore dataStore = BtoDataStore.getInstance();
        List<Application> withdrawalApps = dataStore.getApplications().stream()
                .filter(a -> a.isWithdrawalRequested())
                .collect(Collectors.toList());
        if (withdrawalApps.isEmpty()) {
            System.out.println("No withdrawal requests pending rejection.");
            return;
        }
        System.out.println("\n--- Pending Withdrawal Requests ---");
        for (int i = 0; i < withdrawalApps.size(); i++) {
            Application app = withdrawalApps.get(i);
            System.out.printf("%d. %s: Applicant %s, Project %s%n",
                    i + 1,
                    app.getApplicationId(),
                    app.getApplicant().getNric(),
                    app.getProject().getProjectName());
        }
        System.out.print("Enter the number of the application to reject withdrawal (or 0 to cancel): ");
        int index = scanner.nextInt();
        scanner.nextLine();
        if (index == 0) {
            System.out.println("Operation cancelled.");
            return;
        }
        if (index < 1 || index > withdrawalApps.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Application appWithdrawal = withdrawalApps.get(index - 1);
        if (manager.rejectWithdrawal(appWithdrawal)) {
            System.out.println("Withdrawal rejected for application " + appWithdrawal.getApplicationId());
        } else {
            System.out.println("Failed to reject withdrawal.");
        }
    }
}
