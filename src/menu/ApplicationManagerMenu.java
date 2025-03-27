package menu;

import java.util.Scanner;

import user.HDBManager;

public class ApplicationManagerMenu {

    private HDBManager manager;
    private Scanner scanner = new Scanner(System.in);

    public ApplicationManagerMenu(HDBManager manager) {
        this.manager = manager;
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----Application Manager Menu-----");
            System.out.println("1. Approve Apllication");
            System.out.println("2. Reject Application");
            System.out.println("3. Approve withdrawal");
            System.out.println("4. Reject withdrawal");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.println("Which Application to approve");
                    // Add functionaily
                    break;
                case 2:
                    System.out.println("Which Application to reject");
                    // Add functionaily
                    break;
                case 3:
                    System.out.println("Which Withdrawal to appprove");
                    // Add functionaily
                    break;
                case 4:
                    System.out.println("Whch Withdrawal to Reject");
                    // Add functionaily
                    break;
                case 5:
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

}

