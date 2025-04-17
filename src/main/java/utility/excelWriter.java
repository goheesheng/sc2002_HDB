package utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import user.User;

public class excelWriter {

    public static void writeToExcel(String filePath) {
        // Get all users from the central data store
        BTODataStore dataStore = BTODataStore.getInstance();
        List<User> users = dataStore.getAllUsers();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Applicants");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("NRIC");
        headerRow.createCell(2).setCellValue("Age");
        headerRow.createCell(3).setCellValue("Marital Status");
        headerRow.createCell(4).setCellValue("Password");
        headerRow.createCell(5).setCellValue("User Type");

        // If no users in data store, add a sample user
        if (users.isEmpty()) {
            // Add a default sample user for testing
            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("John Doe");
            row1.createCell(1).setCellValue("S1234567A");
            row1.createCell(2).setCellValue(30);
            row1.createCell(3).setCellValue("Single");
            row1.createCell(4).setCellValue("password123");
            row1.createCell(5).setCellValue("Applicant");
        } else {
            // Write actual users from data store
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getName()); 
                row.createCell(1).setCellValue(user.getNric());
                row.createCell(2).setCellValue(user.getAge());
                row.createCell(3).setCellValue(user.getMaritalStatus());
                row.createCell(4).setCellValue(user.getPassword());
                row.createCell(5).setCellValue(user.getClass().getSimpleName());
            }
        }

        // Auto-size columns for better readability
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // Save the Excel file
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            workbook.write(fos);
            System.out.println("Excel file saved successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to Excel file: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}