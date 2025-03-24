package menu;

import user.HDBOfficer;
import user.Applicant;
import java.util.Scanner;

public class HDBOfficerMenu extends UserMenu {
    private ApplicantMenu applicantMenu;
    private EnquiryMenu enquiryMenu;
    private ProjectMenu projectMenu;

    public HDBOfficerMenu(HDBOfficer user) {
        super(user);
        this.applicantMenu = new ApplicantMenu(new Applicant(user.getNric(), user.getPassword(), user.getAge(), user.getMaritalStatus()));
        this.enquiryMenu = new EnquiryMenu();
        this.projectMenu = new ProjectMenu();
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----HDB Officer Menu-----");
            System.out.println("1. View and Reply to Enquiries");
            System.out.println("2. View Project Details");
            System.out.println("3. Access Applicant Functionalities");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    enquiryMenu.displayMenu();
                    break;
                case 2:
                    projectMenu.displayMenu();
                    break;
                case 3:
                    applicantMenu.displayMenu();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }
}
