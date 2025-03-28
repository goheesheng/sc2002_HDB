package utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {

    public static void writeToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Applicants");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("NRIC");
        headerRow.createCell(2).setCellValue("Age");
        headerRow.createCell(3).setCellValue("Marital Status");
        headerRow.createCell(4).setCellValue("Password");

        // Write some data to the Excel file (Including Password)
        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("John Doe");
        row1.createCell(1).setCellValue("S1234567A");
        row1.createCell(2).setCellValue(30);
        row1.createCell(3).setCellValue("Single");
        row1.createCell(4).setCellValue("password123");

        // Save the Excel file
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            workbook.write(fos);
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