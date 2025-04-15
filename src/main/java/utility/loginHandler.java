package utility;

import user.User;

/**
 * Handles user login operations including authentication.
 * This class provides static methods for logging in and validating credentials.
 */
public class loginHandler {
    
    /**
     * Validates user credentials and returns the authenticated user object.
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
                return user;
            }
        }
        return null;
    }
}

