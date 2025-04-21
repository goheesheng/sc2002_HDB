package ui.mainmenu;

import user.HDBManager;

import ui.submenu.HDBManagerProjectMenu;
import ui.submenu.OfficerRegistrationMenu;
import ui.submenu.ApplicationManagerMenu;
import ui.submenu.EnquiryMenu;

import admin.Report;
import project.FlatType;
import project.Project;
import project.Application;

import java.util.Map;
import java.util.List;
import java.io.File;

public class HDBManagerMenu extends UserMenu{
    private HDBManagerProjectMenu projectMenu;
    private OfficerRegistrationMenu officerRegistrationMenu;
    private EnquiryMenu enquiryMenu;
    private ApplicationManagerMenu applicationManagerMenu;
    private Project selectedProject;

    public HDBManagerMenu (HDBManager manager){
        super(manager);
        this.projectMenu = new HDBManagerProjectMenu(manager);
        this.enquiryMenu = new EnquiryMenu();
        this.applicationManagerMenu = new ApplicationManagerMenu(manager);
        this.selectedProject = null;
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
                    // Ensure a project is selected before accessing Officer Registration
                    if (selectedProject == null) {
                        selectProject();  // Prompt user to select a project
                    }
                    
                    officerRegistrationMenu = new OfficerRegistrationMenu((HDBManager) user, selectedProject);
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

    private void selectProject() {
        List<Project> allProjects = ((HDBManager) user).viewAllProjects(); // Get all projects from the manager

        if (allProjects != null && !allProjects.isEmpty()) {
            System.out.println("\nSelect a Project:");
            for (int i = 0; i < allProjects.size(); i++) {
                System.out.println((i + 1) + ". " + allProjects.get(i).getProjectName());
            }

            System.out.print("Enter the number of the project you want to select: ");
            int projectChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            if (projectChoice > 0 && projectChoice <= allProjects.size()) {
                selectedProject = allProjects.get(projectChoice - 1);  // Set the selected project
                System.out.println("You have selected: " + selectedProject.getProjectName());
            } else {
                System.out.println("Invalid project choice. Please try again.");
                selectProject();  // Prompt again if the choice is invalid
            }
        } else {
            System.out.println("No projects available.");
        }
    }


    private void generateReport(){
        //Let the manager enter filters
        System.out.print("Enter marital status to filter (or press Enter to skip): ");
        String maritalStatus = scanner.nextLine();

        System.out.print("Enter flat type (e.g., TWO_ROOM, THREE_ROOM) or press Enter to skip: ");
        String flatTypeInput = scanner.nextLine();

        System.out.print("Enter project name to filter (or press Enter to skip): ");
        String projectName = scanner.nextLine();

        System.out.print("Enter minimum age to filter (or press Enter to skip): ");
        String minAgeInput = scanner.nextLine();

        System.out.print("Enter maximum age to filter (or press Enter to skip): ");
        String maxAgeInput = scanner.nextLine();

        Map<String, Object> filters = new java.util.HashMap<>();

        if (!maritalStatus.isEmpty()) filters.put("maritalStatus", maritalStatus);
        if (!flatTypeInput.isEmpty()) {
            try {
                FlatType flatType = FlatType.valueOf(flatTypeInput.toUpperCase());
                filters.put("flatType", flatType);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid flat type. Skipping...");
            }
        }
        if (!projectName.isEmpty()) filters.put("projectName", projectName);

        if (!minAgeInput.isEmpty()) {
            try {
                int minAge = Integer.parseInt(minAgeInput);
                filters.put("minAge", minAge);
            } catch (NumberFormatException e) {
                System.out.println("Invalid minimum age. Skipping...");
            }
        }

        if (!maxAgeInput.isEmpty()) {
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

        HDBManager manager = (HDBManager) user;
        Report report = manager.generateReport(filters);

        // Get all applications from the manager
        List<Application> allApplications = manager.getApplications();
        report.setApplications(allApplications);
        
            // Apply filters to the applications
        if (!filters.isEmpty()) {
            List<Application> filtered = report.applyFilters(filters);
            report.setApplications(filtered);
        }

        // Generate PDF
        File reportFile = report.generatePDF();
        if (reportFile != null) {
            System.out.println("Report generated successfully and saved as: " + reportFile.getName());
            System.out.println("Number of applications in report: " + report.getApplications().size());
        } else {
            System.out.println("Failed to generate report.");
        }
    }
}
