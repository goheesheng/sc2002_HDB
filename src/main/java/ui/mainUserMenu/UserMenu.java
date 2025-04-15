package ui.mainUserMenu;
import user.User;
import java.util.Scanner;

/**
 * Abstract class representing a general user menu.
 * Serves as a base class for specific user roles like HDBOfficerMenu, HDBManagerMenu and ApplicantMenu.
 */

public abstract class UserMenu {
    protected User user;
    protected Scanner scanner = new Scanner(System.in);


    /**
     * Constructor to initialize the menu with a user.
     * 
     * @param user The user who is accessing the menu
     */

    public UserMenu(User user) {
        this.user = user;
    }

     /**
     * Displays the menu options and handles user interaction.
     * Must be implemented by all subclasses.
     */
    public abstract void displayMenu();
}
