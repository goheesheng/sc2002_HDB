package menu;

import java.util.Scanner;

public class ProjectMenu {
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----Project Menu-----");
            System.out.println("1. View All Projects");
            System.out.println("2. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                System.out.println("Fetching all projects...");
                // Add functionaity
                    break;
                case 2:
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 2);
    }
}

