package ui.submenu;

import user.HDBManager;
import project.Project;
import java.util.List;
import java.util.Scanner;


public class HDBManagerProjectMenu extends ProjectMenu{
    private Scanner scanner = new Scanner(System.in);

    public HDBManagerProjectMenu(HDBManager manager) {
        super(manager);
    }

    public void displayMenu(List<Project> projects) {
        int choice;
        do {
            System.out.println("\n----- Manager Project Menu -----");
            System.out.println("1. View All created Projects"); //need to add view own created
            System.out.println("2. Create Project");
            System.out.println("3. Edit Project");
            System.out.println("4. Delete Project");
            System.out.println("5. Toggle Project Visibility");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewProjects(projects);
                    break;
                case 2:
                    System.out.println("Creating new project");
                    // Add functionailities
                    break;
                case 3:
                    System.out.println("Edit existing project");
                    // Add functionailities
                    break;
                case 4:
                    System.out.println("Deleting project");
                    // Add functionailities
                    break;
                case 5:
                    System.out.println("Toggling project visibility");
                    // Add functionailities
                    break;
                case 6:
                    System.out.println("Exiting Project Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }
}

