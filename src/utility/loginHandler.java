package utility;

import user.User;

public class loginHandler extends UserFileHandler {

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

    //Check all three files (Applicant, Manager, Officer)
    public User login(String nric, String password) {
        User user;

        //Check Applicant file
        user = checkLoginInFile(nric, password, APPLICANT_FILE);
        if (user != null) return user;

        //Check Manager file
        user = checkLoginInFile(nric, password, MANAGER_FILE);
        if (user != null) return user;

        //Check Officer file
        user = checkLoginInFile(nric, password, OFFICER_FILE);
        if (user != null) return user;

        return null; 
    }
}

