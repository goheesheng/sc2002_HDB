package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class fileReader {
    public static void selectUserList(Scanner scanner) {
        System.out.println("\nSelect a file to view:");
        System.out.println("1. Applicant List");
        System.out.println("2. Manager List");
        System.out.println("3. Officer List");
        System.out.println("4. Project List");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 

        String filePath = null;

        switch (choice) {
            case 1:
                filePath = "sc2002_HDB/test/ApplicantList.txt";
                break;
            case 2:
                filePath = "sc2002_HDB/test/ManagerList.txt";
                break;
            case 3:
                filePath = "sc2002_HDB/test/OfficerList.txt";
                break;
            case 4:
                System.out.println("we didnt add this yet");
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        viewUserList(filePath);
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
                String password = details[4];

                System.out.println("Name: " + name + ", NRIC: " + nric + ", Age: " + age + ", Marital Status: " + maritalStatus);
                
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

}
