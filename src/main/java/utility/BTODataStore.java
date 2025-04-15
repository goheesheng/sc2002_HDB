package utility; 

import user.*;
import project.*;
import admin.*;
import status.*; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Simple singleton pattern to hold application data
public class BtoDataStore {

    private static BtoDataStore instance = null;

    private List<User> allUsers;
    private List<Project> allProjects;
    private List<Application> allApplications;
    private List<Enquiry> allEnquiries;
    private List<Registration> pendingRegistrations; // Or all registrations

    // Private constructor for Singleton
    private BtoDataStore() {
        allUsers = new ArrayList<>();
        allProjects = new ArrayList<>();
        allApplications = new ArrayList<>();
        allEnquiries = new ArrayList<>();
        pendingRegistrations = new ArrayList<>();
    }

    // Public method to get the instance
    public static BtoDataStore getInstance() {
        if (instance == null) {
            instance = new BtoDataStore();
        }
        return instance;
    }

    // --- Getters for the lists (return copies to prevent external modification) ---
    public List<User> getAllUsers() {
        return new ArrayList<>(allUsers);
    }

    public List<Project> getAllProjects() {
        return new ArrayList<>(allProjects);
    }

     public List<Application> getAllApplications() {
        return new ArrayList<>(allApplications);
    }

     public List<Enquiry> getAllEnquiries() {
        return new ArrayList<>(allEnquiries);
    }

     public List<Registration> getPendingRegistrations() {
        return new ArrayList<>(pendingRegistrations);
    }


    // --- Methods to Add data (used during loading or creation) ---
    public void addUser(User user) {
        // Optional: Check for duplicates
        if (findUserByNric(user.getNric()).isEmpty()) {
             this.allUsers.add(user);
        }
    }

    public void addProject(Project project) {
         // Optional: Check for duplicates
        if (findProjectById(project.getProjectId()).isEmpty()) {
            this.allProjects.add(project);
        }
    }

     public void addApplication(Application application) {
        // Optional: Check for duplicates
        if (findApplicationById(application.getApplicationId()).isEmpty()) {
             this.allApplications.add(application);
             // Link it to the project as well
             application.getProject().getApplications().add(application); // Ensure Project's list is mutable or handle differently
             // Link it to the user? User currently doesn't hold Application object directly
        }
    }

     public void addEnquiry(Enquiry enquiry) {
         if (findEnquiryById(enquiry.getEnquiryId()).isEmpty()) {
             this.allEnquiries.add(enquiry);
             // Link it to project/user if needed
             if (enquiry.getProject() != null) {
                 enquiry.getProject().getEnquiries().add(enquiry); // Ensure Project's list is mutable
             }
             enquiry.getUser().viewEnquiries().add(enquiry); // Ensure User's list is mutable
         }
    }

     public void addRegistration(Registration registration) {
         if (findRegistrationById(registration.getRegistrationId()).isEmpty()) {
            this.pendingRegistrations.add(registration);
         }
     }

     // --- Methods to Find data ---
     public Optional<User> findUserByNric(String nric) {
         return allUsers.stream().filter(u -> u.getNric().equalsIgnoreCase(nric)).findFirst();
     }

     public Optional<Project> findProjectById(String projectId) {
         return allProjects.stream().filter(p -> p.getProjectId().equalsIgnoreCase(projectId)).findFirst();
     }

     public Optional<Application> findApplicationById(String applicationId) {
         return allApplications.stream().filter(a -> a.getApplicationId().equalsIgnoreCase(applicationId)).findFirst();
     }

      public Optional<Enquiry> findEnquiryById(String enquiryId) {
         return allEnquiries.stream().filter(e -> e.getEnquiryId().equalsIgnoreCase(enquiryId)).findFirst();
     }

     public Optional<Registration> findRegistrationById(String registrationId) {
         return pendingRegistrations.stream().filter(r -> r.getRegistrationId().equalsIgnoreCase(registrationId)).findFirst();
     }

     // --- Methods to Update data (Example: Change Password) ---
     public boolean updateUserPassword(String nric, String newPassword) {
         Optional<User> userOpt = findUserByNric(nric);
         if (userOpt.isPresent()) {
             // Need a way to set password directly, bypassing the 'changePassword' check
             // Add a setPassword method to User or handle here (less ideal)
             // userOpt.get().setPassword(newPassword); // Assuming setPassword exists
             return true; // Placeholder
         }
         return false;
     }
     public List<Application> getApplications() {
        return new ArrayList<>(allApplications);

    }


    // --- Methods to Remove data (Example: Delete Project) ---
    public boolean removeProject(String projectId) {
        Optional<Project> projOpt = findProjectById(projectId);
        if (projOpt.isPresent()) {
            // Add logic: Check if manager is allowed to delete, check dependencies?
            allProjects.remove(projOpt.get());
            // Remove associated Applications, Enquiries, Registrations? Careful design needed.
            return true;
        }
        return false;
    }

    // --- Load/Save Triggers ---
    public void loadAllData() {
        PersistenceUtils.loadUsers(this); // Pass 'this' store to the loading methods
        PersistenceUtils.loadProjects(this);
        PersistenceUtils.loadApplications(this);
        PersistenceUtils.loadEnquiries(this);
        PersistenceUtils.loadRegistrations(this);
        // IMPORTANT: After loading, you need to re-establish object references
        // e.g., link loaded Applications back to their loaded User and Project objects.
        System.out.println("Data loaded."); // Placeholder
    }

    public void saveAllData() {
        PersistenceUtils.saveUsers(this.allUsers);
        PersistenceUtils.saveProjects(this.allProjects);
        PersistenceUtils.saveApplications(this.allApplications);
        PersistenceUtils.saveEnquiries(this.allEnquiries);
        PersistenceUtils.saveRegistrations(this.pendingRegistrations);
        System.out.println("Data saved."); // Placeholder
    }
}