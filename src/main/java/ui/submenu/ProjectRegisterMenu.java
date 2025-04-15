    package ui.submenu;

    import java.util.Scanner;
    import java.util.List;

    import utility.BTODataStore;
    import project.Project;
    import status.RegistrationStatus;
    import user.HDBOfficer;

    public class ProjectRegisterMenu {
        private Scanner scanner = new Scanner(System.in);
        private HDBOfficer currentOfficer;

        // Constructor takes the currently logged-in officer
        public ProjectRegisterMenu(HDBOfficer currentOfficer) {
            this.currentOfficer = currentOfficer;
        }

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
                        System.out.println("\nFetching available projects to register for");
                        
                        // Get all available BTO projects from the datastore
                        List<Project> projects = BTODataStore.getInstance().getAllProjects();
                        if (projects.isEmpty()) {
                            System.out.println("No available projects.");
                            break;
                        }

                        // Display the list of projects
                        System.out.println("\nAvailable Projects:");
                        for (int i = 0; i < projects.size(); i++) {
                            System.out.println((i + 1) + ". " + projects.get(i).getProjectName());
                        }


                        // Get officer's selection
                        System.out.print("Enter the project number to register: ");
                        int projectIndex = scanner.nextInt() - 1;
                        scanner.nextLine();

                        if (projectIndex >= 0 && projectIndex < projects.size()) {
                            Project selectedProject = projects.get(projectIndex);

                            // Officer registers to handle the selected project
                            boolean registered = currentOfficer.registerForProject(selectedProject);
                            System.out.println(registered ? "Successfully registered! Awaiting approval." : "Registration failed. You may have already registered or applied for this project.");
                        } else {
                            System.out.println("Invalid selection.");
                        }
                        break;

                    case 2:
                        // View officer's registration status
                        RegistrationStatus status = currentOfficer.viewRegistrationStatus();
                        
                        // Check if the applicant has an existing registration
                        if (status != null) {
                            System.out.println("Your current registration status: " + status);
                            Project assignedProject = currentOfficer.getHandlingProject();
                                if (assignedProject != null) {
                                    System.out.println("Assigned to handle project: " + assignedProject.getProjectName());
                                } else {
                                    System.out.println("You have not been assigned a project yet.");

                             }
                        } else {
                            System.out.println("You have not registered for any project.");
                        }
                        break;  

                    case 3:
                        //Exit and return to main menu
                        return;  
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 3);
        }

    }
