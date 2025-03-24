
package utility;

import user.User;
import user.Applicant;
import user.HDBOfficer;
import user.HDBManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class UserFileHandler {

    // File paths for txt files
    protected static final String APPLICANT_FILE = "sc2002_HDB/test/ApplicantList.txt";
    protected static final String MANAGER_FILE = "sc2002_HDB/test/ManagerList.txt";
    protected static final String OFFICER_FILE = "sc2002_HDB/test/OfficerList.txt";


    protected User checkLoginInFile(String nric, String password, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] userData = line.split("\t");
                if (userData.length < 5) continue;

                String storedNRIC = userData[1].trim();
                String storedPassword = userData[4].trim();
                int storedAge = Integer.parseInt(userData[2].trim());
                String storedMaritalStatus = userData[3].trim();

                if (nric.equals(storedNRIC) && password.equals(storedPassword)) {
                    return matchUser(filePath, storedNRIC, storedPassword, storedAge, storedMaritalStatus);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return null;
    }

    // matches user with existing list
    private User matchUser(String filePath, String nric, String password, int age, String maritalStatus) {
        if (filePath.equals(APPLICANT_FILE)) return new Applicant(nric, password, age, maritalStatus);
        if (filePath.equals(MANAGER_FILE)) return new HDBManager(nric, password, age, maritalStatus);
        if (filePath.equals(OFFICER_FILE)) return new HDBOfficer(nric, password, age, maritalStatus);
        return null;
    }
}

