package ui.submenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import user.HDBManager;
import project.Project;
import project.FlatType;

import java.util.*;


public class HDBManagerProjectMenu extends ProjectMenu{
    private Scanner scanner = new Scanner(System.in);

    public HDBManagerProjectMenu(HDBManager manager) {
        super(manager);
    }

    public void displayMenu(List<Project> projects) {
        int choice;
        do {
            System.out.println("\n----- Manager Project Menu -----");
            System.out.println("1. View All created Projects"); 
            System.out.println("2. View Own created Projects"); 
            System.out.println("3. Create Project");
            System.out.println("4. Edit Project");
            System.out.println("5. Delete Project");
            System.out.println("6. Toggle Project Visibility");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewProjects(projects);
                    break;
                
                case 2:
                    viewOwnProjects(projects);
                    break;

                case 3:
                    System.out.println("Creating new project");
                    createProject(projects);
                    break;

                case 4:
                    System.out.println("Edit existing project");
                    editProject(projects);
                    break;

                case 5:
                    System.out.println("Deleting project");
                    deleteProject(projects);
                    break;

                case 6:
                    System.out.println("Toggling project visibility");
                    toggleProjectVisibility(projects);
                    break;

                case 7:
                    System.out.println("Exiting Project Menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }

    private void viewOwnProjects(List<Project> projects) {
        boolean hasProjects = false;
        for (Project project : projects) {
            if (project.getManagerInCharge().equals(this.user)) {
                hasProjects = true;
                System.out.println("Project ID: " + project.getProjectId());
                System.out.println("Project Name: " + project.getProjectName());
                System.out.println("Neighborhood: " + project.getneighborhood());
                System.out.println("Application Period: " + project.getApplicationOpeningDate() + " to " + project.getApplicationClosingDate());
                System.out.println("Visibility: " + (project.isVisible() ? "Visible" : "Hidden"));
                System.out.println("Flat Types: " + project.getFlatTypes());
                System.out.println("Officer Slots: " + project.getavailableOfficerSlots());
                System.out.println("--------------------------");
            }
            if (!hasProjects) {
                System.out.println("You have not created any projects.");
            }
        }
    }

    private void createProject(List<Project> projects) {
        System.out.print("Enter Project ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Project Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Neighborhood: ");
        String neighborhood = scanner.nextLine();

        Map<FlatType, Integer> flatTypes = new HashMap<>();
        for (FlatType type : FlatType.values()) {
            System.out.print("Enter number of " + type + " flats: ");
            int count = Integer.parseInt(scanner.nextLine());
            flatTypes.put(type, count);
        }
        Date applicationOpeningDate = null;
        Date applicationClosingDate = null;

        try{
        System.out.print("Enter application opening date (yyyy-mm-dd): ");
        String openDateStr = scanner.nextLine();
        applicationOpeningDate = new SimpleDateFormat("yyyy-MM-dd").parse(openDateStr);

        System.out.print("Enter application closing date (yyyy-mm-dd): ");
        String closeDateStr = scanner.nextLine();
        applicationClosingDate = new SimpleDateFormat("yyyy-MM-dd").parse(closeDateStr);

        } catch (ParseException e) {
        System.out.println("Invalid date format. Project creation aborted.");
        return;
        }

        System.out.print("Enter available officer slots: ");
        int officerSlots = Integer.parseInt(scanner.nextLine());

        Project newProject = new Project(id, name, neighborhood,
            flatTypes, applicationOpeningDate, applicationClosingDate,
            (HDBManager) this.user, officerSlots);

            projects.add(newProject);
            System.out.println("Project created successfully!");
    }

    
    
    /**
    * Edits an existing project with updated details.
    * 
    * @param project The project to edit
    * @param updatedDetails Map containing the details to update
    * @return true if the project was updated successfully, false if any validation fails
    */
    public boolean editProject(List<Project> projects) {
        System.out.print("Enter Project ID to edit: ");
        String projectId = scanner.nextLine();

        Project selectedProject = null;

        for (Project project : projects) {
            if (project.getProjectId().equals(projectId) &&
                project.getManagerInCharge().equals(this.user)) {
                selectedProject = project;
                break;
            }
        }

        if (selectedProject == null) {
            System.out.println("Project not found or you are not authorized to edit this project.");
            return false;
        }


        System.out.println("Editing Project: " + selectedProject.getProjectName());
        System.out.println("1. Edit Name");
        System.out.println("2. Edit Neighborhood");
        System.out.println("3. Edit Flat Counts");
        System.out.println("4. Edit Application Dates");
        System.out.print("Choose field to edit: ");
        int EditChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (EditChoice) {
            case 1:
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                selectedProject.setProjectName(newName);
                System.out.println("Project name updated.");
                break;

            case 2:
                System.out.print("Enter new neighborhood: ");
                String newNeighborhood = scanner.nextLine();
                selectedProject.setNeighborhood(newNeighborhood);
                System.out.println("Neighborhood updated.");
                break;

            case 3:
                Map<FlatType, Integer> flatTypes = selectedProject.getFlatTypes();
                for (FlatType type : flatTypes.keySet()) {
                    System.out.print("Enter new count for " + type + ": ");
                    int newCount = scanner.nextInt();
                    selectedProject.updateFlatCount(type, newCount);
                }
                scanner.nextLine(); // consume newline
                System.out.println("Flat counts updated.");
                break;

            case 4:
                try {
                    System.out.print("Enter new opening date (yyyy-MM-dd): ");
                    String openStr = scanner.nextLine();
                    System.out.print("Enter new closing date (yyyy-MM-dd): ");
                    String closeStr = scanner.nextLine();

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date openDate = df.parse(openStr);
                    Date closeDate = df.parse(closeStr);

                    selectedProject.setApplicationOpeningDate(openDate);
                    selectedProject.setApplicationClosingDate(closeDate);
                    System.out.println("Application dates updated.");
                } catch (ParseException e) {
                    System.out.println("Invalid date format.");
                }
                break;

            default:
                System.out.println("Invalid choice.");
                return false;
        }
        return true;
    }

    private boolean deleteProject(List<Project> projects) {
        System.out.println("\n--- Delete Your Project ---");
        System.out.print("Enter Project ID to delete: ");
        String projectId = scanner.nextLine();
    
        Project projectToDelete = null;
    
        for (Project project : projects) {
            if (project.getProjectId().equals(projectId) &&
                project.getManagerInCharge().equals(this.user)) {
                projectToDelete = project;
                break;
            }
        }
    
        if (projectToDelete == null) {
            System.out.println("Project not found or you are not authorized to delete this project.");
            return false;
        }
    
        // Confirm deletion
        System.out.print("Are you sure you want to delete this project? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
    
        if (confirmation.equals("yes")) {
            projects.remove(projectToDelete);
            System.out.println("Project successfully deleted.");
            return true;
        } else {
            System.out.println("Deletion cancelled.");
            return false;
        }
    }

    private void toggleProjectVisibility(List<Project> projects) {
        System.out.println("\n--- Toggle Project Visibility ---");
        System.out.print("Enter Project ID to toggle visibility: ");
        String projectId = scanner.nextLine();
    
        Project projectToToggle = null;
    
        for (Project project : projects) {
            if (project.getProjectId().equals(projectId) && 
                project.getManagerInCharge().equals(this.user)) {
                projectToToggle = project;
                break;
            }
        }
    
        if (projectToToggle == null) {
            System.out.println("Project not found or you are not authorized to modify this project.");
            return;
        }
    
        // Toggle visibility
        boolean currentVisibility = projectToToggle.isVisible();
        projectToToggle.setVisibility(!currentVisibility);
    
        // Print confirmation message
        if (!currentVisibility) {
            System.out.println("The project is now visible to applicants.");
        } else {
            System.out.println("The project is now hidden from applicants.");
        }
    }
}

