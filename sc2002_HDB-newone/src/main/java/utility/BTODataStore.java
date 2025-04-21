package utility; 

import user.*;
import project.*;
import admin.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Simple singleton pattern to hold application data
public class BTODataStore {

    private static BTODataStore instance = null;

    private List<User> allUsers;
    private List<Project> allProjects;
    private List<Application> allApplications;
    private List<Enquiry> allEnquiries;
    private List<Registration> allRegistrations;

    // Private constructor for Singleton
    private BTODataStore() {
        allUsers = new ArrayList<>();
        allProjects = new ArrayList<>();
        allApplications = new ArrayList<>();
        allEnquiries = new ArrayList<>();
        allRegistrations = new ArrayList<>();
    }

    // Public method to get the instance
    public static BTODataStore getInstance() {
        if (instance == null) {
            instance = new BTODataStore();
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

    public List<Registration> getAllRegistrations() {
        return new ArrayList<>(allRegistrations);
    }

    public List<Registration> getRegistrationsForOfficer(HDBOfficer officer) {
        List<Registration> officerRegistrations = new ArrayList<>();
        for (Registration registration : allRegistrations) {
            if (registration.getOfficer().equals(officer)) {
                officerRegistrations.add(registration);
            }
        }
        return officerRegistrations;
    }

     // --- Method to get the manager map ---
     public Map<String, HDBManager> getManagerMap() {
        Map<String, HDBManager> managerMap = new HashMap<>();
        for (User user : allUsers) {
            if (user instanceof HDBManager) {
                HDBManager manager = (HDBManager) user;
                managerMap.put(manager.getName(), manager);
            }
        }
        return managerMap;
    }

    // --- Methods to Add data (used during loading or creation) ---
    public void addUser(User user) {
        // Optional: Check for duplicates
        if (!findUserByNric(user.getNric()).isPresent()) {
             this.allUsers.add(user);
        }
    }

    public void addProject(Project project) {
         // Optional: Check for duplicates
        if (!findProjectById(project.getProjectId()).isPresent()) {
            this.allProjects.add(project);
        }
    }

     public void addApplication(Application application) {
        // Optional: Check for duplicates
        if (!findApplicationById(application.getApplicationId()).isPresent()) {
             this.allApplications.add(application);
             // Link it to the project as well
             application.getProject().getApplications().add(application); // Ensure Project's list is mutable or handle differently
             // Link it to the user? User currently doesn't hold Application object directly
        }
    }

     public void addEnquiry(Enquiry enquiry) {
         if (!findEnquiryById(enquiry.getEnquiryId()).isPresent()) {
             this.allEnquiries.add(enquiry);
             // Link it to project/user if needed
             if (enquiry.getProject() != null) {
                 enquiry.getProject().getEnquiries().add(enquiry); // Ensure Project's list is mutable
             }
             enquiry.getUser().viewEnquiries().add(enquiry); // Ensure User's list is mutable
         }
    }

     public void addRegistration(Registration registration) {
         if (!findRegistrationById(registration.getRegistrationId()).isPresent()) {
            this.allRegistrations.add(registration);
            saveAllData();
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
         return allRegistrations.stream().filter(r -> r.getRegistrationId().equalsIgnoreCase(registrationId)).findFirst();
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
    
    /**
     * Removes a project directly from the data store.
     * This method should only be called by HDBManager's deleteProject method.
     * 
     * @param project The project to remove
     * @return true if the project was successfully removed, false otherwise
     */
    public boolean removeProject(Project project) {
        if (project == null) return false;
        
        // Find the project in the list
        Optional<Project> projOpt = findProjectById(project.getProjectId());
        if (projOpt.isPresent()) {
            // Remove the project
            boolean removed = allProjects.remove(projOpt.get());
            
            if (removed) {
                // Also clean up any related applications
                List<Application> relatedApplications = allApplications.stream()
                        .filter(app -> app.getProject().equals(project))
                        .collect(Collectors.toList());
                
                allApplications.removeAll(relatedApplications);
                
                // Clean up any related enquiries
                List<Enquiry> relatedEnquiries = allEnquiries.stream()
                        .filter(enq -> enq.getProject().equals(project))
                        .collect(Collectors.toList());
                
                allEnquiries.removeAll(relatedEnquiries);
                
                // Clean up any related registrations
                List<Registration> relatedRegistrations = allRegistrations.stream()
                        .filter(reg -> reg.getProject().equals(project))
                        .collect(Collectors.toList());
                
                allRegistrations.removeAll(relatedRegistrations);
            }
            
            return removed;
        }
        
        return false;
    }

    // --- Load/Save Triggers ---
    public void loadAllData() {
        // First load users and projects since they are independent
        PersistenceUtils.loadUsers(this);
        PersistenceUtils.loadProjects(this);
        
        // Then load applications since they depend on users and projects
        PersistenceUtils.loadApplications(this);
        
        // Finally load enquiries and registrations since they depend on users, projects, and potentially applications
        PersistenceUtils.loadEnquiries(this);
        PersistenceUtils.loadRegistrations(this);
        
        System.out.println("Data loaded successfully.");
    }

    public void saveAllData() {
        List<User> applicants = allUsers.stream()
            .filter(u -> u instanceof Applicant)
            .collect(Collectors.toList());

        List<User> managers = allUsers.stream()
                .filter(u -> u instanceof HDBManager)
                .collect(Collectors.toList());

        List<User> officers = allUsers.stream()
                .filter(u -> u instanceof HDBOfficer)
                .collect(Collectors.toList());
            
        PersistenceUtils.saveUsers(applicants, "sc2002_HDB-newone/src/main/resources/applicantlist.csv");
        PersistenceUtils.saveUsers(managers, "sc2002_HDB-newone/src/main/resources/managerlist.csv");
        PersistenceUtils.saveUsers(officers, "sc2002_HDB-newone/src/main/resources/officerlist.csv");

        PersistenceUtils.saveProjects(this.allProjects);
        PersistenceUtils.saveApplications(this.allApplications);
        PersistenceUtils.saveEnquiries(this.allEnquiries);
        PersistenceUtils.saveRegistrations(this.allRegistrations);
        System.out.println("Data saved."); // Placeholder
    }
}