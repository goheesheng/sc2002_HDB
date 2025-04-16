package ui.mainmenu;

import user.HDBOfficer;
import user.Applicant;

import java.io.File;
import project.Application;

import admin.Receipt;
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
                    System.out.print("Enter NRIC of applicant to generate receipt: ");
                    String nric = scanner.nextLine();

                    HDBOfficer officer = (HDBOfficer) user;
                    Application app = officer.retrieveApplication(nric);

                    if (app != null) {
                        Receipt receipt = officer.generateBookingReceipt(app);
                        if (receipt != null) {
                            System.out.println("Receipt generated successfully!\n");

                            // PDF generation
                            File pdf = receipt.generatePDF();
                            System.out.println("PDF file:" + pdf.getName());
                        } else {
                            System.out.println("Application is not eligible for a booking receipt.");
                        }
                    } else {
                        System.out.println("No application found for the given NRIC.");
                    }
                    break;

                case 6:
                    changePassword();
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