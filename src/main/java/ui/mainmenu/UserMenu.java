package ui.mainmenu;
import user.User;
import java.util.Scanner;

public abstract class UserMenu {
    protected User user;
    protected Scanner scanner = new Scanner(System.in);

    public UserMenu(User user) {
        this.user = user;
    }

    public abstract void displayMenu();

    public void changePassword() {
        System.out.println("\n--- Change Password ---");
        System.out.print("Enter current password: ");
        String oldPass = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPass = scanner.nextLine();

        if (!newPass.equals(confirmPass)) {
            System.out.println("New passwords do not match.");
            return;
        }
        if (newPass.trim().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        if (user.changePassword(oldPass, newPass)) {
            System.out.println("Password changed successfully.");
            // The User object in dataStore.allUsers now has the new password.
            // Persistence will save this change.
        } else {
            System.out.println("Password change failed. Incorrect current password.");
        }
    }
}

