package menu;

import user.HDBManager;

public class HDBManagerMenu extends UserMenu{
    private ProjectMenu projectMenu;

    public HDBManagerMenu (HDBManager user){
        super(user);
    }

    public void displayMenu() {

        System.out.println("-----HDB Manager Menu-----");
        System.out.println("1. Create Project");
        System.out.println("2. Edit Project");
        System.out.println("3. Delete Project");
        System.out.println("4. Toggle Project Visibility");

        System.out.println("5. View all Projects");

        System.out.println("5. View created Projects");
        System.out.println("5. View Officer Registration");
        System.out.println("5. Approve Officer Registration");
        System.out.println("5. Reject Officer Registration ");
        System.out.println("5. Approve Apllication");
        System.out.println("5. Reject Application");
        System.out.println("5. Approve withdrawal");
        System.out.println("5. Reject withdrawal");
        System.out.println("5. Generate Report");

        System.out.println("5. View all Enquires");

        System.out.println("6. Logout");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Pojects available:");
                // insert functionailty 
                break;
            case 2:
                System.out.println("Which project do you wish to apply for?");
                // insert functionailty 
                break;
            case 3:
                System.out.println("Application status");
                // insert functionailty 
                break;
            case 4:
                
                break;
            case 5:
                projectMenu.displayMenu();
            case 6: 
                System.out.println("Logging out, have a nice day");
                return;
            default:
                System.out.println("Invalid choice.");
        }
        displayMenu();
    }
}
