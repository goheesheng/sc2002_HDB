package utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class excelReader {

    /**
     * Reads users from an Excel file and creates appropriate User objects
     * based on the type of Excel file (applicant, manager, etc.).
     * 
     * @param filePath Path to the Excel file
     * @return List of User objects
     */
    public static List<User> readUsersFromExcel(String filePath) {
        List<User> users = new ArrayList<>();

        String fileType = new File(filePath).getName().toLowerCase();

        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            // Create a workbook object (XSSFWorkbook for .xlsx)
            Workbook workbook = new XSSFWorkbook(fis);

            // Get the first sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);
            // Loop through each row in the sheet
            for (Row row : sheet) {
                // Loop through each cell in the row
                if (row.getRowNum() == 0) continue; // Skip header row

                //String name = row.getCell(0).getStringCellValue();
                String nric = row.getCell(1).getStringCellValue();
                int age = (int) row.getCell(2).getNumericCellValue();
                String maritalStatus = row.getCell(3).getStringCellValue();
                String password = row.getCell(4).getStringCellValue();

                User user = null;

                if (fileType.contains("manager")) {
                    user = new HDBManager(nric, password, age, maritalStatus);
                } else if (fileType.contains("officer")) {
                    user = new HDBOfficer(nric, password, age, maritalStatus);
                } else if (fileType.contains("applicant")) {
                    user = new Applicant(nric, password, age, maritalStatus);
                }

                if (user != null) {
                    users.add(user);
                } else {
                    System.err.println("Unknown user type for NRIC: " + nric);
                }
            }

            //workbook.close();  // Don't forget to close the workbook!

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
        }
        return users;
    }
}