package ui.mainmenu;

import user.HDBManager;

import ui.submenu.HDBManagerProjectMenu;
import ui.submenu.OfficerRegistrationMenu;
import ui.submenu.ApplicationManagerMenu;
import ui.submenu.EnquiryMenu;

import admin.Report;
import project.FlatType;
import project.Application;

import java.util.Map;
import java.util.List;

public class HDBManagerMenu extends UserMenu{
    private HDBManagerProjectMenu projectMenu;
    private OfficerRegistrationMenu officerRegistrationMenu;
    private EnquiryMenu enquiryMenu;
    private ApplicationManagerMenu applicationManagerMenu;


    public HDBManagerMenu (HDBManager manager){
        super(manager);
        this.projectMenu = new HDBManagerProjectMenu(manager);
        this.officerRegistrationMenu = new OfficerRegistrationMenu(manager);
        this.enquiryMenu = new EnquiryMenu();
        this.applicationManagerMenu = new ApplicationManagerMenu(manager);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n-----HDB Manager Menu-----");
            System.out.println("1. Manage Projects");
            System.out.println("2. Manage Officer Registration");
            System.out.println("3. View and reply Enquires");
            System.out.println("4. Manage Applications");
            System.out.println("5. Generate Report");
            System.out.println("6: Change password");
            System.out.println("7. Logout");
            
            choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    projectMenu.displayMenu(((HDBManager)user).viewAllProjects());
                    break;
                case 2:
                    officerRegistrationMenu.displayMenu();
                    break;
                case 3:
                    enquiryMenu.displayMenu();
                    break;
                case 4:
                    applicationManagerMenu.displayMenu();
                    break;
                case 5:
                    generateReport();
                    break;

                case 6:
                    changePassword();
                    break;

                case 7:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    private void generateReport(){
        //Let the manager enter filters
        System.out.print("Enter marital status to filter (or press Enter to skip): ");
        String maritalStatus = scanner.nextLine();

        System.out.print("Enter flat type (e.g., 2ROOM, 3ROOM) or press Enter to skip: ");
        String flatTypeInput = scanner.nextLine();

        System.out.print("Enter project name to filter (or press Enter to skip): ");
        String projectName = scanner.nextLine();

        System.out.print("Enter minimum age to filter (or press Enter to skip): ");
        String minAgeInput = scanner.nextLine();

        System.out.print("Enter maximum age to filter (or press Enter to skip): ");
        String maxAgeInput = scanner.nextLine();

        Map<String, Object> filters = new java.util.HashMap<>();

        if (!maritalStatus.isBlank()) filters.put("maritalStatus", maritalStatus);
        if (!flatTypeInput.isBlank()) {
            try {
                FlatType flatType = FlatType.valueOf(flatTypeInput.toUpperCase());
                filters.put("flatType", flatType);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid flat type. Skipping...");
            }
        }
        if (!projectName.isBlank()) filters.put("projectName", projectName);

        if (!minAgeInput.isBlank()) {
            try {
                int minAge = Integer.parseInt(minAgeInput);
                filters.put("minAge", minAge);
            } catch (NumberFormatException e) {
                System.out.println("Invalid minimum age. Skipping...");
            }
        }

        if (!maxAgeInput.isBlank()) {
            try {
                int maxAge = Integer.parseInt(maxAgeInput);
                filters.put("maxAge", maxAge);
            } catch (NumberFormatException e) {
                System.out.println("Invalid maximum age. Skipping...");
            }
        }
        
        if (filters.containsKey("minAge") && filters.containsKey("maxAge")) {
            int minAge = (int) filters.get("minAge");
            int maxAge = (int) filters.get("maxAge");
            if (minAge > maxAge) {
                System.out.println("Minimum age cannot be greater than maximum age. Skipping report generation.");
                return;
            }
        }

        if (!maxAgeInput.isBlank()) filters.put("maxAge", Integer.parseInt(maxAgeInput));
        
        HDBManager manager = (HDBManager) user;
        Report report = manager.generateReport(filters);

        // Set applications from manager (assuming it has access to all applications)
        report.setApplications(manager.getApplications());

        // Apply filters
        List<Application> filtered = report.applyFilters(filters);
        report.setApplications(filtered);

        // Generate PDF
        report.generatePDF();
        System.out.println("Report generated successfully.");
        }
}
