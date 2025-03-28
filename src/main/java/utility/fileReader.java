package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class fileReader extends UserFileHandler {
    private static final String APPLICANT_FILE = "test/ApplicantList.txt";
    private static final String MANAGER_FILE = "test/ManagerList.txt";
    private static final String OFFICER_FILE = "test/OfficerList.txt";

    public static void UserList() {
        List<String> filePaths = Arrays.asList(APPLICANT_FILE, MANAGER_FILE, OFFICER_FILE);
        
        for (String filePath : filePaths) {
            System.out.println("\n" + filePath);
            viewUserList(filePath);
        }
    }

    private static void viewUserList(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] details = line.split("\t");

                if (details.length < 5) continue;

                String name = details[0];
                String nric = details[1];
                int age = Integer.parseInt(details[2]);
                String maritalStatus = details[3].toUpperCase();

                System.out.println("Name: " + name + ", NRIC: " + nric + ", Age: " + age + ", Marital Status: " + maritalStatus);
                
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " " + e.getMessage());
        }
    }

}
