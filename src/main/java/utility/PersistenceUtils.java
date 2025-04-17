// package utility;

// import user.*;
// import project.*;
// import status.*;
// import admin.*;

// import java.io.*;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.*;

// public class PersistenceUtils {

//     // File names for persistence
//     private static final String USER_FILE = "users.csv";
//     private static final String PROJECT_FILE = "projects.csv";
//     private static final String APPLICATION_FILE = "applications.csv";
//     private static final String ENQUIRY_FILE = "enquiries.csv";
//     private static final String REGISTRATION_FILE = "registrations.csv";

//     // Date format used to serialize/deserialize dates.
//     private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

//     // --- User Persistence ---

//     public static void saveUsers(List<User> users) {
//         try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
//             writer.println("NRIC,Password,Age,MaritalStatus,UserType");
//             for (User user : users) {
//                 String userType = "";
//                 if (user instanceof HDBManager) userType = "Manager";
//                 else if (user instanceof HDBOfficer) userType = "Officer";
//                 else if (user instanceof Applicant) userType = "Applicant";
//                 writer.println(
//                     escapeCsv(user.getNric()) + "," +
//                     escapeCsv(user.getPassword()) + "," +
//                     user.getAge() + "," +
//                     escapeCsv(user.getMaritalStatus()) + "," +
//                     userType
//                 );
//             }
//         } catch (IOException e) {
//             System.err.println("Error saving users: " + e.getMessage());
//         }
//     }

//     public static void loadUsers(BTODataStore store) {
//         File file = new File(USER_FILE);
//         if (!file.exists()) return;
//         try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
//             String line;
//             reader.readLine(); // Skip header
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data.length == 5) {
//                     String nric = data[0];
//                     String password = data[1];
//                     int age = Integer.parseInt(data[2]);
//                     String maritalStatus = data[3];
//                     String userType = data[4];

//                     User user = null;
//                     if ("Manager".equals(userType)) {
//                         user = new HDBManager(nric, password, age, maritalStatus);
//                     } else if ("Officer".equals(userType)) {
//                         user = new HDBOfficer(nric, password, age, maritalStatus);
//                     } else if ("Applicant".equals(userType)) {
//                         user = new Applicant(nric, password, age, maritalStatus);
//                     }
//                     if (user != null) {
//                         store.addUser(user);
//                     }
//                 }
//             }
//         } catch (IOException | NumberFormatException e) {
//             System.err.println("Error loading users: " + e.getMessage());
//         }
//     }

//     // --- Project Persistence ---

//     public static void saveProjects(List<Project> projects) {
//         try (PrintWriter writer = new PrintWriter(new FileWriter(PROJECT_FILE))) {
//             // Header: ID,Name,Neighbourhood,OpenDate,CloseDate,ManagerNRIC,OfficerSlots,Visibility,FlatTypes
//             writer.println("ID,Name,Neighbourhood,OpenDate,CloseDate,ManagerNRIC,OfficerSlots,Visibility,FlatTypes");
//             for (Project p : projects) {
//                 writer.print(escapeCsv(p.getProjectId()) + ",");
//                 writer.print(escapeCsv(p.getProjectName()) + ",");
//                 writer.print(escapeCsv(p.getNeighbourhood()) + ",");
//                 writer.print(DATE_FORMAT.format(p.getApplicationOpeningDate()) + ",");
//                 writer.print(DATE_FORMAT.format(p.getApplicationClosingDate()) + ",");
//                 writer.print(escapeCsv(p.getManagerInCharge().getNric()) + ",");
//                 writer.print(p.getAvailableOfficerSlots() + ",");
//                 writer.print(p.isVisible() + ",");
                
//                 // Serialize FlatTypes map (e.g., "TWO_ROOM:50;THREE_ROOM:30")
//                 Map<FlatType, Integer> flatTypes = p.getFlatsAvailable();
//                 if (flatTypes != null && !flatTypes.isEmpty()) {
//                     StringBuilder flatTypesStr = new StringBuilder();
//                     for (Map.Entry<FlatType, Integer> entry : flatTypes.entrySet()) {
//                         flatTypesStr.append(entry.getKey().name())
//                                     .append(":")
//                                     .append(entry.getValue())
//                                     .append(";");
//                     }
//                     // Remove trailing semicolon if present
//                     if (flatTypesStr.length() > 0) {
//                         flatTypesStr.setLength(flatTypesStr.length() - 1);
//                     }
//                     writer.println(flatTypesStr.toString());
//                 } else {
//                     writer.println("");
//                 }
//             }
//         } catch (IOException e) {
//             System.err.println("Error saving projects: " + e.getMessage());
//         }
//     }

//     public static void loadProjects(BTODataStore store) {
//         File file = new File(PROJECT_FILE);
//         if (!file.exists()) return;
//         try (BufferedReader reader = new BufferedReader(new FileReader(PROJECT_FILE))) {
//             String line;
//             reader.readLine(); // Skip header
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data.length >= 9) {
//                     String id = data[0];
//                     String name = data[1];
//                     String neighbourhood = data[2];
//                     Date openDate = null;
//                     Date closeDate = null;
//                     try {
//                         openDate = DATE_FORMAT.parse(data[3]);
//                         closeDate = DATE_FORMAT.parse(data[4]);
//                     } catch (ParseException pe) {
//                         System.err.println("Error parsing dates for project " + id + ": " + pe.getMessage());
//                         continue;
//                     }
//                     String managerNric = data[5];
//                     int slots = Integer.parseInt(data[6]);
//                     boolean visibility = Boolean.parseBoolean(data[7]);

//                     // Deserialize flat types from field 8
//                     Map<FlatType, Integer> flatTypesMap = new HashMap<>();
//                     if (data[8] != null && !data[8].isEmpty()) {
//                         String[] flatTokens = data[8].split(";");
//                         for (String token : flatTokens) {
//                             String[] parts = token.split(":");
//                             if (parts.length == 2) {
//                                 try {
//                                     FlatType type = FlatType.valueOf(parts[0]);
//                                     int count = Integer.parseInt(parts[1]);
//                                     flatTypesMap.put(type, count);
//                                 } catch (IllegalArgumentException e) {
//                                     System.err.println("Error parsing flat type for project " + id + ": " + token);
//                                 }
//                             }
//                         }
//                     }

//                     // Link Manager via BTODataStore
//                     Optional<User> managerOpt = store.findUserByNric(managerNric);
//                     if (managerOpt.isPresent() && managerOpt.get() instanceof HDBManager) {
//                         HDBManager manager = (HDBManager) managerOpt.get();
//                         // Create the project (ensure your Project class has a matching constructor)
//                         Project project = new Project(id, name, neighbourhood, flatTypesMap, openDate, closeDate, manager, slots);
//                         project.setVisibility(visibility);
//                         store.addProject(project);
//                     } else {
//                         System.err.println("Could not find Manager " + managerNric + " for project " + id);
//                     }
//                 }
//             }
//         } catch (IOException | NumberFormatException e) {
//             System.err.println("Error loading projects: " + e.getMessage());
//         }
//     }

//     // --- Application Persistence ---

//     public static void saveApplications(List<Application> applications) {
//         try (PrintWriter writer = new PrintWriter(new FileWriter(APPLICATION_FILE))) {
//             // Header: ID,ApplicantNRIC,ProjectID,ApplicationDate
//             writer.println("ID,ApplicantNRIC,ProjectID,ApplicationDate");
//             for (Application a : applications) {
//                 writer.print(escapeCsv(a.getApplicationId()) + ",");
//                 writer.print(escapeCsv(a.getApplicant().getNric()) + ",");
//                 writer.print(escapeCsv(a.getProject().getProjectId()) + ",");
//                 writer.println(DATE_FORMAT.format(a.getApplicationDate()));
//             }
//         } catch (IOException e) {
//             System.err.println("Error saving applications: " + e.getMessage());
//         }
//     }

//     public static void loadApplications(BTODataStore store) {
//         File file = new File(APPLICATION_FILE);
//         if (!file.exists()) return;
//         try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
//             String line;
//             reader.readLine(); // Skip header
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data.length == 4) {
//                     String id = data[0];
//                     String applicantNric = data[1];
//                     String projectId = data[2];
//                     Date applicationDate = null;
//                     try {
//                         applicationDate = DATE_FORMAT.parse(data[3]);
//                     } catch (ParseException pe) {
//                         System.err.println("Error parsing application date for " + id + ": " + pe.getMessage());
//                         continue;
//                     }
//                     Optional<User> applicantOpt = store.findUserByNric(applicantNric);
//                     Optional<Project> projectOpt = store.findProjectById(projectId);
//                     if (applicantOpt.isPresent() && projectOpt.isPresent() && applicantOpt.get() instanceof Applicant) {
//                         Application application = new Application(id, (Applicant) applicantOpt.get(), projectOpt.get(), applicationDate);
//                         store.addApplication(application);
//                     } else {
//                         System.err.println("Error loading application " + id + ": Applicant or Project not found.");
//                     }
//                 }
//             }
//         } catch (IOException e) {
//             System.err.println("Error loading applications: " + e.getMessage());
//         }
//     }

//     // --- Enquiry Persistence ---

//     public static void saveEnquiries(List<Enquiry> enquiries) {
//         try (PrintWriter writer = new PrintWriter(new FileWriter(ENQUIRY_FILE))) {
//             // Header: ID,SenderNRIC,ProjectID,EnquiryDate,Message
//             writer.println("ID,SenderNRIC,ProjectID,EnquiryDate,Message");
//             for (Enquiry e : enquiries) {
//                 writer.print(escapeCsv(e.getEnquiryId()) + ",");
//                 writer.print(escapeCsv(e.getSender().getNric()) + ",");
//                 writer.print(escapeCsv(e.getProject().getProjectId()) + ",");
//                 writer.print(DATE_FORMAT.format(e.getEnquiryDate()) + ",");
//                 writer.println(escapeCsv(e.getMessage()));
//             }
//         } catch (IOException e) {
//             System.err.println("Error saving enquiries: " + e.getMessage());
//         }
//     }

//     public static void loadEnquiries(BTODataStore store) {
//         File file = new File(ENQUIRY_FILE);
//         if (!file.exists()) return;
//         try (BufferedReader reader = new BufferedReader(new FileReader(ENQUIRY_FILE))) {
//             String line;
//             reader.readLine(); // Skip header
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data.length == 5) {
//                     String id = data[0];
//                     String senderNric = data[1];
//                     String projectId = data[2];
//                     Date enquiryDate = null;
//                     try {
//                         enquiryDate = DATE_FORMAT.parse(data[3]);
//                     } catch (ParseException pe) {
//                         System.err.println("Error parsing enquiry date for " + id + ": " + pe.getMessage());
//                         continue;
//                     }
//                     String message = data[4];
//                     Optional<User> senderOpt = store.findUserByNric(senderNric);
//                     Optional<Project> projectOpt = store.findProjectById(projectId);
//                     if (senderOpt.isPresent() && projectOpt.isPresent()) {
//                         Enquiry enquiry = new Enquiry(id, senderOpt.get(), projectOpt.get(), enquiryDate, message);
//                         store.addEnquiry(enquiry);
//                     } else {
//                         System.err.println("Error loading enquiry " + id + ": Sender or Project not found.");
//                     }
//                 }
//             }
//         } catch (IOException e) {
//             System.err.println("Error loading enquiries: " + e.getMessage());
//         }
//     }

//     // --- Registration Persistence ---

//     public static void saveRegistrations(List<Registration> registrations) {
//         try (PrintWriter writer = new PrintWriter(new FileWriter(REGISTRATION_FILE))) {
//             // Header: ID,ApplicantNRIC,ProjectID,RegistrationDate
//             writer.println("ID,ApplicantNRIC,ProjectID,RegistrationDate");
//             for (Registration r : registrations) {
//                 writer.print(escapeCsv(r.getRegistrationId()) + ",");
//                 writer.print(escapeCsv(r.getApplicant().getNric()) + ",");
//                 writer.print(escapeCsv(r.getProject().getProjectId()) + ",");
//                 writer.println(DATE_FORMAT.format(r.getRegistrationDate()));
//             }
//         } catch (IOException e) {
//             System.err.println("Error saving registrations: " + e.getMessage());
//         }
//     }

//     public static void loadRegistrations(BTODataStore store) {
//         File file = new File(REGISTRATION_FILE);
//         if (!file.exists()) return;
//         try (BufferedReader reader = new BufferedReader(new FileReader(REGISTRATION_FILE))) {
//             String line;
//             reader.readLine(); // Skip header
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data.length == 4) {
//                     String id = data[0];
//                     String applicantNric = data[1];
//                     String projectId = data[2];
//                     Date registrationDate = null;
//                     try {
//                         registrationDate = DATE_FORMAT.parse(data[3]);
//                     } catch (ParseException pe) {
//                         System.err.println("Error parsing registration date for " + id + ": " + pe.getMessage());
//                         continue;
//                     }
//                     Optional<User> applicantOpt = store.findUserByNric(applicantNric);
//                     Optional<Project> projectOpt = store.findProjectById(projectId);
//                     if (applicantOpt.isPresent() && projectOpt.isPresent() && applicantOpt.get() instanceof Applicant) {
//                         Registration registration = new Registration(id, (Applicant) applicantOpt.get(), projectOpt.get(), registrationDate);
//                         store.addRegistration(registration);
//                     } else {
//                         System.err.println("Error loading registration " + id + ": Applicant or Project not found.");
//                     }
//                 }
//             }
//         } catch (IOException e) {
//             System.err.println("Error loading registrations: " + e.getMessage());
//         }
//     }

//     // Helper method for basic CSV escaping.
//     private static String escapeCsv(String value) {
//         if (value == null) return "";
//         String escaped = value.replace("\"", "\"\"");
//         if (escaped.contains(",")) {
//             return "\"" + escaped + "\"";
//         }
//         return escaped;
//     }
// }
package utility;

import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;
import project.Project;
import project.Application;
import project.FlatType;
import admin.Registration;
import project.Enquiry;
import status.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PersistenceUtils {

    // File names for CSV persistence
    private static final String USER_FILE = "src/main/resources/users.csv";
    private static final String PROJECT_FILE = "src/main/resources/projects.csv";
    private static final String APPLICATION_FILE = "src/main/resources/applications.csv";
    private static final String ENQUIRY_FILE = "src/main/resources/enquiries.csv";
    private static final String REGISTRATION_FILE = "src/main/resources/registrations.csv";

    // Date format used for serializing/deserializing dates.
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // --- User Persistence ---

    public static void saveUsers(List<User> users, String ExcelName) {
        String fileName = ExcelName.replace(".xlsx", "").toLowerCase();
        String folderPath = "src/main/resources"; 
        
        File dir = new File(folderPath);
            if (!dir.exists()) {
            dir.mkdirs();  // Creates the directory if it doesn't exist
        }
    
        try (PrintWriter writer = new PrintWriter(fileName)){
            writer.println("Name,NRIC,Password,Age,MaritalStatus,UserType");
    
            for (User user : users) {
                writer.println(user.toCsvRow());
            }

                System.out.println("Data successfully saved to: " + fileName);
                writer.close();
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
            //try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
                // try (PrintWriter writer = new PrintWriter(outputPath)) {
                //     writer.println("NRIC,Password,Age,MaritalStatus,UserType");
                //     for (User user : users) {
                //         //String userType = "";
                //         writer.println(user.toCsvRow());
                //     }
                // if (user instanceof HDBManager) userType = "Manager";
                // else if (user instanceof HDBOfficer) userType = "Officer";
                // else if (user instanceof Applicant) userType = "Applicant";
                // writer.println(
                //     escapeCsv(user.getNric()) + "," +
                //     escapeCsv(user.getPassword()) + "," +
                //     user.getAge() + "," +
                //     escapeCsv(user.getMaritalStatus()) + "," +
                //     userType
                // );

    public static void loadUsers(BTODataStore store) {
        // File file = new File(USER_FILE);
        // if (!file.exists()) return;
        InputStream input = PersistenceUtils.class.getClassLoader().getResourceAsStream("users.csv");
        if (input == null) {
            System.err.println("Error: users.csv not found in resources.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String name = data[0];
                    String nric = data[1];
                    String password = data[2];
                    int age = Integer.parseInt(data[3]);
                    String maritalStatus = data[4];
                    String userType = data[5];

                    User user = null;
                    if ("Manager".equals(userType)) {
                        user = new HDBManager(name,nric, password, age, maritalStatus);
                    } else if ("Officer".equals(userType)) {
                        user = new HDBOfficer(name,nric, password, age, maritalStatus);
                    } else if ("Applicant".equals(userType)) {
                        user = new Applicant(name,nric, password, age, maritalStatus);
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

    // --- Project Persistence ---

    public static void saveProjects(List<Project> projects) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PROJECT_FILE))) {
            // Header
            writer.println("ID,Name,Neighborhood,OpenDate,CloseDate,ManagerNRIC,OfficerSlots,Visibility,FlatTypes");
    
            for (Project p : projects) {
                writer.print(escapeCsv(p.getProjectId()) + ",");
                writer.print(escapeCsv(p.getProjectName()) + ",");
                writer.print(escapeCsv(p.getneighborhood()) + ",");

                String openDateStr = (p.getApplicationOpeningDate() != null) ?
                        DATE_FORMAT.format(p.getApplicationOpeningDate()) : "";
                String closeDateStr = (p.getApplicationClosingDate() != null) ?
                        DATE_FORMAT.format(p.getApplicationClosingDate()) : "";
                writer.print(openDateStr + ",");
                writer.print(closeDateStr + ",");
    
                writer.print(escapeCsv(p.getManagerInCharge().getNric()) + ",");
                writer.print(p.getavailableOfficerSlots() + ",");
                writer.print(p.isVisible() + ",");
    
                // Flat types
                Map<FlatType, Integer> flatTypes = p.getFlatTypes();
                if (flatTypes != null && !flatTypes.isEmpty()) {
                    StringBuilder flatTypesStr = new StringBuilder();
                    for (Map.Entry<FlatType, Integer> entry : flatTypes.entrySet()) {
                        flatTypesStr.append(entry.getKey().name())
                                .append(":")
                                .append(entry.getValue())
                                .append(";");
                    }
                    if (flatTypesStr.length() > 0)
                        flatTypesStr.setLength(flatTypesStr.length() - 1); // remove last ;
                    writer.println(flatTypesStr.toString());
                } else {
                    writer.println(""); // No flat types
                }
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
                String[] data = line.split(",");
                if (data.length >= 9) {
                    String id = data[0];
                    String name = data[1];
                    // Use getneighborhood()—the CSV header here is "Neighborhood" but data matches "neighborhood"
                    String neighborhood = data[2];
                    Date openDate, closeDate;
                    try {
                        openDate = DATE_FORMAT.parse(data[3]);
                        closeDate = DATE_FORMAT.parse(data[4]);
                    } catch (ParseException pe) {
                        System.err.println("Error parsing dates for project " + id + ": " + pe.getMessage());
                        continue;
                    }
                    String managerNric = data[5];
                    // Use availableOfficerSlots method name from Project: getavailableOfficerSlots()
                    int slots = Integer.parseInt(data[6]);
                    boolean visibility = Boolean.parseBoolean(data[7]);
                    
                    // Deserialize flat types from field 8
                    Map<FlatType, Integer> flatTypesMap = new HashMap<>();
                    if (data[8] != null && !data[8].isEmpty()) {
                        String[] flatTokens = data[8].split(";");
                        for (String token : flatTokens) {
                            String[] parts = token.split(":");
                            if (parts.length == 2) {
                                try {
                                    FlatType type = FlatType.valueOf(parts[0]);
                                    int count = Integer.parseInt(parts[1]);
                                    flatTypesMap.put(type, count);
                                } catch (IllegalArgumentException e) {
                                    System.err.println("Error parsing flat type for project " + id + ": " + token);
                                }
                            }
                        }
                    }
                    
                    Optional<User> managerOpt = store.findUserByNric(managerNric);
                    if (managerOpt.isPresent() && managerOpt.get() instanceof HDBManager) {
                        HDBManager manager = (HDBManager) managerOpt.get();
                        Project project = new Project(id, name, neighborhood, flatTypesMap, openDate, closeDate, manager, slots);
                        project.setVisibility(visibility);
                        store.addProject(project);
                    } else {
                        System.err.println("Could not find Manager " + managerNric + " for project " + id);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading projects: " + e.getMessage());
        }
    }

    // --- Application Persistence ---
    // Assumes Application constructor is:
    // Application(String applicationId, Applicant applicant, Project project, FlatType flatType)
    public static void saveApplications(List<Application> applications) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPLICATION_FILE))) {
            // Header: ID,ApplicantNRIC,ProjectID,FlatType
            writer.println("ID,ApplicantNRIC,ProjectID,FlatType");
            for (Application a : applications) {
                writer.print(escapeCsv(a.getApplicationId()) + ",");
                writer.print(escapeCsv(a.getApplicant().getNric()) + ",");
                writer.print(escapeCsv(a.getProject().getProjectId()) + ",");
                writer.println(a.getFlatType().name());
            }
        } catch (IOException e) {
            System.err.println("Error saving applications: " + e.getMessage());
        }
    }

    public static void loadApplications(BTODataStore store) {
        File file = new File(APPLICATION_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String id = data[0];
                    String applicantNric = data[1];
                    String projectId = data[2];
                    String flatTypeStr = data[3];
                    FlatType flatType;
                    try {
                        flatType = FlatType.valueOf(flatTypeStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error parsing flat type for application " + id + ": " + e.getMessage());
                        continue;
                    }
                    Optional<User> applicantOpt = store.findUserByNric(applicantNric);
                    Optional<Project> projectOpt = store.findProjectById(projectId);
                    if (applicantOpt.isPresent() && projectOpt.isPresent() && applicantOpt.get() instanceof Applicant) {
                        Application application = new Application(id, (Applicant) applicantOpt.get(), projectOpt.get(), flatType);
                        store.addApplication(application);
                    } else {
                        System.err.println("Error loading application " + id + ": Applicant or Project not found.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading applications: " + e.getMessage());
        }
    }

    // --- Enquiry Persistence ---
    // Using Enquiry(String enquiryId, User user, Project project, String enquiryText)
    public static void saveEnquiries(List<Enquiry> enquiries) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ENQUIRY_FILE))) {
            // Header: ID,SenderNRIC,ProjectID,EnquiryText
            writer.println("ID,SenderNRIC,ProjectID,EnquiryText");
            for (Enquiry e : enquiries) {
                writer.print(escapeCsv(e.getEnquiryId()) + ",");
                writer.print(escapeCsv(e.getUser().getNric()) + ",");
                writer.print(escapeCsv(e.getProject().getProjectId()) + ",");
                writer.println(escapeCsv(e.getEnquiryText()));
            }
        } catch (IOException e) {
            System.err.println("Error saving enquiries: " + e.getMessage());
        }
    }

    public static void loadEnquiries(BTODataStore store) {
        File file = new File(ENQUIRY_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(ENQUIRY_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String id = data[0];
                    String senderNric = data[1];
                    String projectId = data[2];
                    String enquiryText = data[3];
                    Optional<User> senderOpt = store.findUserByNric(senderNric);
                    Optional<Project> projectOpt = store.findProjectById(projectId);
                    if (senderOpt.isPresent() && projectOpt.isPresent()) {
                        Enquiry enquiry = new Enquiry(id, senderOpt.get(), projectOpt.get(), enquiryText);
                        store.addEnquiry(enquiry);
                    } else {
                        System.err.println("Error loading enquiry " + id + ": Sender or Project not found.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading enquiries: " + e.getMessage());
        }
    }

    // --- Registration Persistence ---
    // Uses Registration(String registrationId, HDBOfficer officer, Project project)
    public static void saveRegistrations(List<Registration> registrations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REGISTRATION_FILE))) {
            // Header: ID,OfficerNRIC,ProjectID
            writer.println("ID,OfficerNRIC,ProjectID");
            for (Registration r : registrations) {
                writer.print(escapeCsv(r.getRegistrationId()) + ",");
                writer.print(escapeCsv(r.getOfficer().getNric()) + ",");
                writer.println(escapeCsv(r.getProject().getProjectId()));
            }
        } catch (IOException e) {
            System.err.println("Error saving registrations: " + e.getMessage());
        }
    }

    public static void loadRegistrations(BTODataStore store) {
        File file = new File(REGISTRATION_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(REGISTRATION_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String id = data[0];
                    String officerNric = data[1];
                    String projectId = data[2];
                    Optional<User> officerOpt = store.findUserByNric(officerNric);
                    Optional<Project> projectOpt = store.findProjectById(projectId);
                    if (officerOpt.isPresent() && projectOpt.isPresent() && officerOpt.get() instanceof HDBOfficer) {
                        Registration registration = new Registration(id, (HDBOfficer) officerOpt.get(), projectOpt.get());
                        store.addRegistration(registration);
                    } else {
                        System.err.println("Error loading registration " + id + ": Officer or Project not found.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading registrations: " + e.getMessage());
        }
    }

    // --- Helper for CSV escaping ---
    private static String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
