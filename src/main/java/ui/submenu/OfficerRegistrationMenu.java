package ui.submenu;

import java.util.Scanner;

import user.HDBManager;

public class OfficerRegistrationMenu {
    private HDBManager manager;
    private Scanner scanner = new Scanner(System.in);

    public OfficerRegistrationMenu(HDBManager manager) {
        this.manager = manager;
    }

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
                    // Add functionaily
                    break;
                case 2:
                    System.out.println("Approve Officer Registration");
                    // Add functionaily
                    break;
                case 3:
                    System.out.println("Reject Officer Registration");
                    // Add functionaily
                    break;
                case 4:
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
}
