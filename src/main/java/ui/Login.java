package ui;

import java.util.Scanner;

import utility.loginHandler;
import user.User;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import ui.mainUserMenu.HDBManagerMenu;
import ui.mainUserMenu.HDBOfficerMenu;
import ui.mainUserMenu.ApplicantMenu;

/**
 * Handles the login interface logic.
 * Prompts the user to input their credentials and navigates to
 * the appropriate main menu based on the user type.
 */

public class Login{

    private Scanner scanner;

    public Login(Scanner scanner) {
        this.scanner = scanner;
    }


    /**
     * Displays the login menu.
     * Prompts the user to enter their NRIC and password,
     * validates the login, and then directs them to the appropriate
     * user menu (HDBManager, HDBOfficer, or Applicant).
     */
    public void displayMenu() {
        String nric;

        // Loop until a valid NRIC is entered
        while (true) {
            System.out.println("Enter NRIC:");
            nric = scanner.nextLine();

            // Validate the NRIC format using the User class method
            if (User.isValidNRIC(nric)) {
                break;
            } else {
                System.out.println("Invalid NRIC format. Please enter a valid NRIC.");
            }
        }
        // Prompt user to enter password
        System.out.println("Enter Password:");
        String password = scanner.nextLine();

        // Attempt to authenticate the user using loginHandler
        User loggedInUser = loginHandler.login(nric, password); 

        // If login is successful
        if (loggedInUser != null) {
            System.out.println("Login successful! User type: " + loggedInUser.getClass().getSimpleName());
            
            // Navigate to appropriate user menu based on the logged-in user's type
            if (loggedInUser instanceof HDBManager) {
                new HDBManagerMenu((HDBManager) loggedInUser).displayMenu();
            } else if (loggedInUser instanceof HDBOfficer) {
                new HDBOfficerMenu((HDBOfficer) loggedInUser).displayMenu();
            } else if (loggedInUser instanceof Applicant) {
                new ApplicantMenu((Applicant) loggedInUser).displayMenu();
            }
    
        } else {
            // If authentication fails
            System.out.println("Invalid NRIC or password.");
            System.out.println("----------------------------------------");
        }
    }
}
                

                    
