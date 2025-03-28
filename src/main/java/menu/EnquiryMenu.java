package menu;

import java.util.Scanner;

public class EnquiryMenu {
    private Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----Enquiry Menu-----");
            System.out.println("1. View All Enquiries");
            System.out.println("2. Reply to an Enquiry");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.println("Fetching all enquiries...");
                    // Add functionaily
                    break;
                case 2:
                    System.out.println("Enter Enquiry ID to reply:");
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
