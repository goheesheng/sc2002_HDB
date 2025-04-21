package ui.mainmenu;

import user.HDBOfficer;
import utility.BTODataStore;
import user.Applicant;
import status.ApplicationStatus;
import project.Application;
import project.Project;
import project.Receipt;
import ui.submenu.EnquiryMenu;
import ui.submenu.ProjectMenu;
import ui.submenu.ProjectRegisterMenu;
import project.FlatType;

import java.util.List;

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
        this.projectRegisterMenu = new ProjectRegisterMenu(officer);

    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----HDB Officer Menu-----");
            System.out.println("1. View and Reply to Enquiries");
            System.out.println("2. View Project Details");
            System.out.println("3. Access Applicant Functionalities");
            System.out.println("4. Register for project");
            System.out.println("5. Update Application Status");
            System.out.println("6. Generate Booking Receipt");
            System.out.println("7. Change password");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    enquiryMenu.displayMenu();
                    break;
                case 2:
                    List<Project> projects = BTODataStore.getInstance().getAllProjects();
                    projectMenu.viewProjects(projects);
                    break;
                case 3:
                    applicantMenu.displayMenu();
                    break;
                case 4:
                    projectRegisterMenu.displayMenu();
                    break;
                case 5:
                    System.out.print("Enter NRIC of applicant to book: ");
                    String nric = scanner.nextLine();
                    HDBOfficer officer = (HDBOfficer) user;
                    Application app = officer.retrieveApplication(nric);
                    
                    if (app != null) {
                        if (app.getStatus() != ApplicationStatus.SUCCESSFUL) {
                            System.out.println("Cannot book flat for applicant. Application must be approved (SUCCESSFUL) first.");
                            break;
                        }
                        
                        System.out.println("\nDo you want to book for applicant?");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        System.out.println("3. Return to previous menu");
                        System.out.print("Enter your choice: ");
                        int bookingChoice = scanner.nextInt();
                        scanner.nextLine();
                        
                        if (bookingChoice == 1) {
                            boolean success = officer.updateApplicationStatus(app, ApplicationStatus.BOOKED);
                            if (success) {
                                // Update flat availability
                                FlatType flatType = app.getFlatType();
                                Project project = app.getProject();
                                int currentCount = project.getRemainingFlats(flatType);
                                boolean updated = officer.updateRemainingFlats(flatType, currentCount - 1);
                                System.out.println(updated ? 
                                    "You have successfully booked the flat for the applicant and updated flat availability!" : 
                                    "Flat was booked but failed to update availability.");
                            } else {
                                System.out.println("Failed to book applicant.");
                            }
                        } else if (bookingChoice == 2) {
                            System.out.println("Booking cancelled.");
                        } else if (bookingChoice == 3) {
                            System.out.println("Returning to previous menu...");
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    } else {
                        System.out.println("No application found for the given NRIC.");
                    }
                    break;
                case 6:
                    System.out.print("Enter NRIC of applicant to generate receipt: ");
                    nric = scanner.nextLine();
                    officer = (HDBOfficer) user;
                    app = officer.retrieveApplication(nric);

                    if (app != null) {
                        Receipt receipt = officer.generateBookingReceipt(app);
                        if (receipt != null) {
                            System.out.println("Receipt generated successfully!\n");
                            receipt.generatePDF();
                            System.out.println("PDF file: Receipt_" + receipt.getReceiptId() + ".pdf");
                        } else {
                            System.out.println("Application is not eligible for a booking receipt.");
                            System.out.println("Make sure the application status is BOOKED.");
                        }
                    } else {
                        System.out.println("No application found for the given NRIC.");
                    }
                    break;
                case 7:
                    changePassword();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;  
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
}