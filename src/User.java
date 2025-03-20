abstract class User {
    protected String nric;  // Unique identifier for the user (NRIC)
    protected String password;  // User password
    protected int age;  // User's age
    protected String maritalStatus;  // User's marital status

    // Method to login the user
    public abstract boolean login();

    // Method to change the user's password
    public abstract void changePassword(String newPassword);
}
