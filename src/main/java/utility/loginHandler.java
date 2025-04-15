package utility;

import user.User;
import java.io.File;

/**
 * Handles user login operations including authentication.
 * This class provides static methods for logging in and validating credentials.
 */
public class loginHandler {
    
    // Path to the Excel file for user data
    private static final String EXCEL_FILE_PATH = "test/ApplicantsList.xlsx";
    
    /**
     * Validates user credentials and returns the authenticated user object.
     * Also ensures that all user data is saved to Excel file.
     * 
     * @param nric The user's NRIC
     * @param password The user's password
     * @return The authenticated User object if credentials are valid, null otherwise
     */
    public static User login(String nric, String password) {
        BTODataStore dataStore = BTODataStore.getInstance();
        
        java.util.Optional<User> userOpt = dataStore.findUserByNric(nric);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.login(nric, password)) {
                // Save to Excel file after successful login
                saveUserDataToExcel();
                return user;
            }
        }
        return null;
    }
    
    /**
     * Saves all user data to the Excel file.
     * This ensures the Excel file is always up-to-date with the latest user information.
     */
    private static void saveUserDataToExcel() {
        try {
            // Create test directory if it doesn't exist
            File testDir = new File("test");
            if (!testDir.exists()) {
                testDir.mkdirs();
            }
            
            // Write all user data to Excel
            excelWriter.writeToExcel(EXCEL_FILE_PATH);
        } catch (Exception e) {
            System.err.println("Error saving user data to Excel: " + e.getMessage());
        }
    }
}

