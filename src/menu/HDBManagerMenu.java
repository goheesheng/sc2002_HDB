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

        System.out.println("\n-----HDB Manager Menu-----");
        System.out.println("1. Manage Projects");
        System.out.println("2. Manage Officer Registration");
        System.out.println("3. View and reply Enquires");
        System.out.println("4. Manage Applications");
        System.out.println("5. Generate Report");
        System.out.println("6. Logout");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                projectMenu.displayMenu(null); 
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
                System.out.println("Generating report");
                // Add functionailties
                break;
            case 6: 
                System.out.println("Logging out, have a nice day");
                return;
            default:
                System.out.println("Invalid choice.");
        }
        displayMenu();
    }
}
