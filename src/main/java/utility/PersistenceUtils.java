package utility;

import user.*;
import project.*;
import status.*;
import admin.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PersistenceUtils {

    private static final String USER_FILE = "users.csv";
    private static final String PROJECT_FILE = "projects.csv";
    // Add file names for applications, enquiries, registrations
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); // Example date format

    // --- User Persistence (Example) ---

    public static void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            writer.println("NRIC,Password,Age,MaritalStatus,UserType"); // Header
            for (User user : users) {
                String userType = "";
                if (user instanceof HDBManager) userType = "Manager";
                else if (user instanceof HDBOfficer) userType = "Officer";
                else if (user instanceof Applicant) userType = "Applicant";
                writer.println(
                        escapeCsv(user.getNric()) + "," +
                        escapeCsv(user.getPassword()) + "," + // Be cautious saving plain passwords
                        user.getAge() + "," +
                        escapeCsv(user.getMaritalStatus()) + "," +
                        userType
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static void loadUsers(BTODataStore store) {
        File file = new File(USER_FILE);
        if (!file.exists()) return; // No file to load

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Simple split, fragile
                if (data.length == 5) {
                    String nric = data[0];
                    String password = data[1];
                    int age = Integer.parseInt(data[2]);
                    String maritalStatus = data[3];
                    String userType = data[4];

                    User user = null;
                    if ("Manager".equals(userType)) {
                        user = new HDBManager(nric, password, age, maritalStatus);
                    } else if ("Officer".equals(userType)) {
                        user = new HDBOfficer(nric, password, age, maritalStatus);
                    } else if ("Applicant".equals(userType)) {
                        user = new Applicant(nric, password, age, maritalStatus);
                    }
                    if (user != null) {
                        store.addUser(user);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    // --- Project Persistence (Example) ---

    public static void saveProjects(List<Project> projects) {
         try (PrintWriter writer = new PrintWriter(new FileWriter(PROJECT_FILE))) {
            // Header: ID,Name,Neighbourhood,OpenDate,CloseDate,ManagerNRIC,OfficerSlots,Visibility,FlatTypes(Type:Count;...)
             writer.println("ID,Name,Neighbourhood,OpenDate,CloseDate,ManagerNRIC,OfficerSlots,Visibility,FlatTypes");
             for(Project p : projects) {
                 writer.print(escapeCsv(p.getProjectId()) + ",");
                 writer.print(escapeCsv(p.getProjectName()) + ",");
                 writer.print(escapeCsv(p.getneighborhood()) + ",");
                 // Dates need formatting
                 // writer.print(DATE_FORMAT.format(p.getApplicationOpeningDate()) + ",");
                 // writer.print(DATE_FORMAT.format(p.getApplicationClosingDate()) + ",");
                 writer.print(escapeCsv(p.getManagerInCharge().getNric()) + ","); // Saving Manager NRIC
                 writer.print(p.getavailableOfficerSlots() + ",");
                 writer.print(p.isVisible() + ",");
                 // FlatTypes need custom serialization (e.g., "TWO_ROOM:50;THREE_ROOM:30")
                 writer.println("PLACEHOLDER_FLATTYPES");
             }
         } catch (IOException e) {
              System.err.println("Error saving projects: " + e.getMessage());
         }
    }

     public static void loadProjects(BTODataStore store) {
         File file = new File(PROJECT_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(PROJECT_FILE))) {
             String line;
             reader.readLine(); // Skip header
             while ((line = reader.readLine()) != null) {
                 String[] data = line.split(","); // Fragile
                 if (data.length >= 9) { // Adjust count based on header
                     String id = data[0];
                     String name = data[1];
                     String neighborhood = data[2];
                     // Parse Dates using DATE_FORMAT
                     // Date openDate = DATE_FORMAT.parse(data[3]);
                     // Date closeDate = DATE_FORMAT.parse(data[4]);
                     String managerNric = data[5];
                     int slots = Integer.parseInt(data[6]);
                     boolean visibility = Boolean.parseBoolean(data[7]);
                     // Parse FlatTypes from data[8]

                     // --- CRITICAL: Link Manager ---
                     Optional<User> managerOpt = store.findUserByNric(managerNric);
                     if (managerOpt.isPresent() && managerOpt.get() instanceof HDBManager) {
                         HDBManager manager = (HDBManager) managerOpt.get();
                         // Create project - constructor needs modification or use setters
                         // Project project = new Project(id, name, neighborhood, /*flatTypesMap*/ null, openDate, closeDate, manager, slots);
                         // project.setVisibility(visibility);
                         // store.addProject(project);
                     } else {
                         System.err.println("Could not find Manager " + managerNric + " for project " + id);
                     }
                 }
             }
        } catch (IOException | NumberFormatException /*| ParseException*/ e) {
              System.err.println("Error loading projects: " + e.getMessage());
        }
     }


    // --- Implement save/load for Applications, Enquiries, Registrations similarly ---
    // Remember to save IDs for related objects (User, Project) and link them back during load.
    public static void saveApplications(List<Application> applications) {/*... Implement ...*/}
    public static void loadApplications(BTODataStore store) {/*... Implement ...*/}
    public static void saveEnquiries(List<Enquiry> enquiries) {/*... Implement ...*/}
    public static void loadEnquiries(BTODataStore store) {/*... Implement ...*/}
    public static void saveRegistrations(List<Registration> registrations) {/*... Implement ...*/}
    public static void loadRegistrations(BTODataStore store) {/*... Implement ...*/}


    // Helper for basic CSV escaping (replace with a library for real use)
    private static String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\""); // Escape quotes
        if (escaped.contains(",")) {
            return "\"" + escaped + "\""; // Quote if contains comma
        }
        return escaped;
    }
}