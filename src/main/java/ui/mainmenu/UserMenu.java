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
}
