package utility;

import utility.excelReader;
import utility.PersistenceUtils;
import user.User;

import java.io.File;
import java.util.List;

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

        for (File excelFile : excelFiles) {
            System.out.println("Processing: " + excelFile.getName());

            // Step 1: Read users from the current Excel file
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
