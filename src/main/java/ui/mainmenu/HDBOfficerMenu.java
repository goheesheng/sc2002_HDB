package ui.mainmenu;

import user.HDBOfficer;
import user.Applicant;
import ui.mainmenu.ApplicantMenu;
import ui.submenu.EnquiryMenu;
import ui.submenu.ProjectMenu;
import ui.submenu.ProjectRegisterMenu;

public class HDBOfficerMenu extends UserMenu {
    private ApplicantMenu applicantMenu;
    private EnquiryMenu enquiryMenu;
    private ProjectMenu projectMenu;
    private ProjectRegisterMenu projectRegisterMenu;

    public HDBOfficerMenu(HDBOfficer officer) {
        super(officer);
        this.projectMenu = new ProjectMenu(officer);
        this.applicantMenu = new ApplicantMenu((Applicant) officer);
        this.enquiryMenu = new EnquiryMenu();
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----HDB Officer Menu-----");
            System.out.println("1. View and Reply to Enquiries");
            System.out.println("2. View Project Details");
            System.out.println("3. Access Applicant Functionalities");
            System.out.println("4. Register for project");
            System.out.println("5. Generate Booking Receipt");
            System.out.println("6: Change password");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    enquiryMenu.displayMenu();
                    break;
                case 2:
                    projectMenu.viewProjects(null);
                    break;
                case 3:
                    applicantMenu.displayMenu();
                    break;
                case 4:
                    projectRegisterMenu.displayMenu();
                    break;
                case 5:
                System.out.println("Generating reciept...");
                    // add functionaities
                    break;
                case 6:
                    System.out.println("New password:");
                    // sd
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }
}