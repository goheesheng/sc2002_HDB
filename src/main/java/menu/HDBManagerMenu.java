package menu;

import user.HDBManager;

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
                    // Generate report
                    break;
                case 6:
                    // change password
                    break;
                case 7:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }
}
