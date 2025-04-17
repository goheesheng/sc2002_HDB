package utility;

import utility.excelReader;
import utility.PersistenceUtils;
import user.HDBManager;
import user.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.Project;

public class ExcelToCsv {

    public static void main(String[] args) {
        File folder = new File("src/main/resources");

        // select only xlsx files
        File[] excelFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));

        if (excelFiles == null || excelFiles.length == 0) {
            System.out.println("No Excel files found in directory.");
            return;
        }
        BTODataStore store = BTODataStore.getInstance();

        // Step 1: Load all managers first (once)
        Map<String, HDBManager> managerMap = new HashMap<>();
        for (User user : excelReader.readUsersFromExcel("src/main/resources/managers.xlsx")) {
            if (user instanceof HDBManager) {
                managerMap.put(user.getNric(), (HDBManager) user);
            }
        }

        // Step 2: Read and store projects
        List<Project> projects = excelReader.readProjectsFromExcel("src/main/resources/projects.xlsx", managerMap);
        for (Project project : projects) {
            store.addProject(project);
        }

        // Step 3: Process each user Excel file
        for (File excelFile : excelFiles) {
            System.out.println("Processing: " + excelFile.getName());

            List<User> users = excelReader.readUsersFromExcel(excelFile.getPath());
            
            for (User u : users) {
                store.addUser(u);
            }

            // Step 2: Save users to CSV
            PersistenceUtils.saveUsers(users, excelFile.getName());

            System.out.println("Done: " + excelFile.getName() + "\n");
        }

        System.out.println("All files processed successfully!");
    }
}
