package ui;   

import java.util.Scanner;

import utility.loginHandler;

import user.User;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;

import ui.mainmenu.HDBManagerMenu;
import ui.mainmenu.HDBOfficerMenu;
import ui.mainmenu.ApplicantMenu;

public class LoginInterface {
    private Scanner scanner = new Scanner(System.in);
    private loginHandler loginHandler = new loginHandler(); 

    public void start() {
        String nric;

        while (true) {
            System.out.println("Enter NRIC:");
            nric = scanner.nextLine().trim();

            if (User.isValidNRIC(nric)) {
                break;
            } else {
                System.out.println("Invalid NRIC format. Please enter a valid NRIC.");
            }
        }

        System.out.println("Enter Password:");
        String password = scanner.nextLine();

        User loggedInUser = loginHandler.login(nric, password);

        if (loggedInUser != null) {
            System.out.println("Login successful! User type: " + loggedInUser.getClass().getSimpleName());

            if (loggedInUser instanceof HDBManager) {
                new HDBManagerMenu((HDBManager) loggedInUser).displayMenu();
            } else if (loggedInUser instanceof HDBOfficer) {
                new HDBOfficerMenu((HDBOfficer) loggedInUser).displayMenu();
            } else if (loggedInUser instanceof Applicant) {
                new ApplicantMenu((Applicant) loggedInUser).displayMenu();
            } else {
                System.out.println("Unrecognized user role.");
            }

        } else {
            System.out.println("Invalid NRIC or password.");
            System.out.println("----------------------------------------\n");
        }
    }
}