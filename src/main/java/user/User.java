package user;

import project.Project;
import project.Enquiry;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all users in the HDB system.
 * Provides common functionality for authentication and enquiry management.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public abstract class User {
    private String nric;
    private String password;
    private int age;
    private String maritalStatus;
    private List<Enquiry> enquiries;

    /**
     * Creates a new User with the specified details.
     * 
     * @param nric The NRIC of this user
     * @param password The password for authentication
     * @param age The age of this user
     * @param maritalStatus The marital status of this user
     */
    public User(String nric, String password, int age, String maritalStatus) {
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.enquiries = new ArrayList<>();
    }

    /**
     * Authenticates a user with the provided credentials.
     * 
     * @param nric The NRIC to check
     * @param password The password to check
     * @return true if authentication is successful
     */
    public boolean login(String nric, String password) {
        return this.nric.equals(nric) && this.password.equals(password);
    }

    // Check if NRIC is valid
    public static boolean isValidNRIC(String nric) {
        if (nric == null || nric.length() != 9) return false;

        char firstChar = nric.charAt(0);
        char lastChar = nric.charAt(8);

        if (firstChar != 'S' && firstChar != 'T') return false;
        if (!Character.isUpperCase(lastChar)) return false;

        for (int i = 1; i <= 7; i++) {
            if (!Character.isDigit(nric.charAt(i))) return false;
        }

        return true;
    }

    /**
     * Changes the user's password.
     * 
     * @param oldPassword The current password
     * @param newPassword The new password
     * @return true if the password was changed successfully
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    /**
     * Submits an enquiry about a project.
     * 
     * @param project The project to enquire about
     * @param enquiryText The text content of the enquiry
     * @return The created Enquiry
     */
    public Enquiry submitEnquiry(Project project, String enquiryText) {
        Enquiry enquiry = new Enquiry(generateEnquiryId(), this, project, enquiryText);
        enquiries.add(enquiry);
        project.addEnquiry(enquiry);
        return enquiry;
    }

    /**
     * Returns a list of all enquiries submitted by this user.
     * 
     * @return A list of enquiries
     */
    public List<Enquiry> viewEnquiries() {
        return new ArrayList<>(enquiries);
    }

    /**
     * Edits the text content of an existing enquiry.
     * 
     * @param enquiryId The ID of the enquiry to edit
     * @param newText The new text content
     * @return true if the text was updated successfully
     */
    public boolean editEnquiry(String enquiryId, String newText) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId().equals(enquiryId)) {
                return enquiry.editText(newText);
            }
        }
        return false;
    }

    /**
     * Deletes an existing enquiry.
     * 
     * @param enquiryId The ID of the enquiry to delete
     * @return true if the enquiry was deleted successfully
     */
    public boolean deleteEnquiry(String enquiryId) {
        return enquiries.removeIf(e -> e.getEnquiryId().equals(enquiryId));
    }

    /**
     * Generates a unique ID for a new enquiry.
     * 
     * @return The generated ID
     */
    private String generateEnquiryId() {
        return "ENQ" + System.currentTimeMillis();
    }

    /**
     * Gets the NRIC of this user.
     * 
     * @return The NRIC
     */
    public String getNric() {
        return nric;
    }

    public String getPassword(){
        return password;
    }
    
    /**
     * Gets the age of this user.
     * 
     * @return The age
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Gets the marital status of this user.
     * 
     * @return The marital status
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }
}
