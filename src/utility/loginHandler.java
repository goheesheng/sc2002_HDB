package utility;

import user.User;

public class loginHandler extends UserFileHandler {


    public User login(String nric, String password) {
        if (!User.isValidNRIC(nric)) {
            System.out.println("Invalid NRIC format.");
            return null;
        }

    //Check all three files (Applicant, Manager, Officer)
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

