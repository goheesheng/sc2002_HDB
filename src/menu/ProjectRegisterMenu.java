package menu;

import java.util.Scanner;

public class ProjectRegisterMenu {
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----Registration Menu-----");
            System.out.println("1. Register for project");
            System.out.println("2. View Registration Status");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.println("Projects to register");
                    // Add functionaily
                    break;
                case 2:
                    System.out.println("Registration Status:");
                    // Add functionaily
                    break;
                case 3:
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

}
