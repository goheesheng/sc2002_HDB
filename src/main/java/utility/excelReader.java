package utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class excelReader {

    public static void readExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            // Create a workbook object (XSSFWorkbook for .xlsx)
            Workbook workbook = new XSSFWorkbook(fis);

            // Get the first sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);

            // Loop through each row in the sheet
            for (Row row : sheet) {
                // Loop through each cell in the row
                // Assuming the columns are Name, NRIC, Age, Marital Status, Password
                if (row.getRowNum() == 0) continue; // Skip header row

                String name = row.getCell(0).getStringCellValue();
                String nric = row.getCell(1).getStringCellValue();
                int age = (int) row.getCell(2).getNumericCellValue();
                String maritalStatus = row.getCell(3).getStringCellValue();
                String password = row.getCell(4).getStringCellValue();

                // Print the row details
                System.out.println("Name: " + name + ", NRIC: " + nric + ", Age: " + age + 
                                   ", Marital Status: " + maritalStatus + ", Password: " + password);
            }

            workbook.close();  // Don't forget to close the workbook!

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
        }
    }
}