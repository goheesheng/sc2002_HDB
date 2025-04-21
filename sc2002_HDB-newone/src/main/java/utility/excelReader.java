package utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import project.FlatType;
import project.Project;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class excelReader {
    private static int projectCounter = 10000;

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

                String name = row.getCell(0).getStringCellValue();
                String nric = row.getCell(1).getStringCellValue();
                int age = (int) row.getCell(2).getNumericCellValue();
                String maritalStatus = row.getCell(3).getStringCellValue();
                String password = row.getCell(4).getStringCellValue();

                User user = null;

                if (fileType.contains("manager")) {
                    user = new HDBManager(name, nric, password, age, maritalStatus);
                } else if (fileType.contains("officer")) {
                    user = new HDBOfficer(name, nric, password, age, maritalStatus);
                } else if (fileType.contains("applicant")) {
                    user = new Applicant(name, nric, password, age, maritalStatus);
                }

                if (user != null) {
                    users.add(user);
                } else {
                    System.err.println("Unknown user type for NRIC: " + nric);
                }
            }

            workbook.close(); 

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
        }
        return users;
    }

    public static List<Project> readProjectsFromExcel(String filePath, Map<String, HDBManager> managerMap) {
        List<Project> projects = new ArrayList<>();
    
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
    
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
    
                String projectName = row.getCell(0).getStringCellValue();
                String neighborhood = row.getCell(1).getStringCellValue();
    
                // Use the parseFlatType method instead of valueOf directly
                FlatType type1 = FlatType.parseFlatType(row.getCell(2).getStringCellValue());
                int units1 = 0;
                if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                    units1 = (int) row.getCell(3).getNumericCellValue();
                } else if (row.getCell(3).getCellType() == CellType.STRING) {
                    try {
                        units1 = Integer.parseInt(row.getCell(3).getStringCellValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid units1 value for project: " + projectName + " (Row " + row.getRowNum() + ")");
                        continue;
                    }
                }

                FlatType type2 = FlatType.parseFlatType(row.getCell(5).getStringCellValue());
                int units2 = 0;
                if (row.getCell(6).getCellType() == CellType.NUMERIC) {
                    units2 = (int) row.getCell(6).getNumericCellValue();
                } else if (row.getCell(6).getCellType() == CellType.STRING) {
                    try {
                        units2 = Integer.parseInt(row.getCell(6).getStringCellValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid units2 value for project: " + projectName + " (Row " + row.getRowNum() + ")");
                        continue;
                    }
                }
    
                Date openDate = parseDateCell(row.getCell(8), row.getRowNum(), projectName);
                Date closeDate = parseDateCell(row.getCell(9), row.getRowNum(), projectName);
                
                if (openDate == null || closeDate == null) {
                    System.err.println("Invalid or missing dates for project: " + projectName + " (Row " + row.getRowNum() + ")");
                    continue;
                }

                // Fetch manager (handle missing managers gracefully)
                String managerName = row.getCell(10).getStringCellValue();
                HDBManager manager = managerMap.get(managerName);
                if (manager == null) {
                    System.err.println("Manager not found for project: " + projectName + " (Row " + row.getRowNum() + ")");
                    continue;
                }
    
                // Read officerSlots (with type checking)
                int officerSlots = 0;
                if (row.getCell(11).getCellType() == CellType.NUMERIC) {
                    officerSlots = (int) row.getCell(11).getNumericCellValue();
                } else if (row.getCell(11).getCellType() == CellType.STRING) {
                    try {
                        officerSlots = Integer.parseInt(row.getCell(11).getStringCellValue());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid officerSlots value for project: " + projectName + " (Row " + row.getRowNum() + ")");
                        continue;
                    }
                }
    
                // Create flat type map
                Map<FlatType, Integer> flatMap = new HashMap<>();
                flatMap.put(type1, units1);
                flatMap.put(type2, units2);
    
                String projectId = "P" + projectCounter++;
    
                Project project = new Project(projectId, projectName, neighborhood, flatMap, openDate, closeDate, manager, officerSlots);
                projects.add(project);
            }
    
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return projects;
    }

    private static Date parseDateCell(Cell cell, int rowNum, String projectName) {
        try {
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String dateStr = cell.getStringCellValue().trim();
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
                return sdf.parse(dateStr);
            }
        } catch (Exception e) {
            System.err.println("Error parsing date in row " + rowNum + " for project: " + projectName);
        }
        return null;
    }
    
}