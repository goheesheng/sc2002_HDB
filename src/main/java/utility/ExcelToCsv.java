package utility;

import utility.ExcelReader;
import utility.PersistenceUtils;
import user.HDBManager;
import user.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.Project;

/**
 * Utility class to convert Excel files to CSV format.
 * This helps in data migration and compatibility with other systems.
 * 
 * @author HDB BTO Management System Team
 * @version 1.0
 */
public class ExcelToCsv {
    
    /**
     * Main method that coordinates the conversion of Excel files to CSV.
     * Processes managers, projects, and other user types.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Converting Excel files to CSV...");
        
        // Process managers and build a manager map
        Map<String, HDBManager> managerMap = new HashMap<>();
        for (User user : ExcelReader.readUsersFromExcel("src/main/resources/managers.xlsx")) {
            if (user instanceof HDBManager) {
                HDBManager manager = (HDBManager) user;
                managerMap.put(manager.getName(), manager);
            }
        }
        
        // Process projects using the manager map
        List<Project> projects = ExcelReader.readProjectsFromExcel("src/main/resources/projects.xlsx", managerMap);
        
        // Process other Excel files
        File resourcesDir = new File("src/main/resources");
        for (File excelFile : resourcesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"))) {
            if (excelFile.getName().equalsIgnoreCase("projects.xlsx") || 
                excelFile.getName().equalsIgnoreCase("managers.xlsx")) {
                continue;  // Skip already processed files
            }
            
            List<User> users = ExcelReader.readUsersFromExcel(excelFile.getPath());
            System.out.println("Processed " + users.size() + " users from " + excelFile.getName());
        }
        
        System.out.println("Conversion complete!");
    }
}
