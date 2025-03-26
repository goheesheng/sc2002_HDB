package menu;

import user.Applicant;

public class ApplicantMenu extends UserMenu{
    private ProjectMenu projectMenu;

    public ApplicantMenu (Applicant user){
        super(user);
    }

    public void displayMenu() {
        int choice; 
        
    do {
        System.out.println("\n-----Applicant Menu-----");
        System.out.println("1. View Projects");
        System.out.println("2. Apply for Project");
        System.out.println("3. View Application");
        System.out.println("4. Request Withdrawal");
        System.out.println("5. Submit Enquiry");
        System.out.println("6: Change password");
        System.out.println("7. Logout");

        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
            projectMenu.viewProjects(null);
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
                System.out.println("Withdrawal request");
                // insert functionailty 
                break;
            case 5:
                System.out.println("What would you like to enquire?");
                // insert functionailty 
            case 6:
                System.out.println("New password:");
                //insert functionality  
                break;
            case 7:
                System.out.println("Logging out, have a nice day");
                return;
            default:
                System.out.println("Invalid choice.");
            }
        } while (choice !=7);
    }
}
