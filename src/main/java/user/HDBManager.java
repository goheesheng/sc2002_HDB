package user;

import project.Project;
import project.Enquiry;
import project.Application;
import admin.Registration;
import admin.Report;
import status.ApplicationStatus;
import project.FlatType;
import utility.BTODataStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an HDB Manager who can create and manage BTO projects.
 * Managers have the authority to approve or reject applications and officer registrations.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class HDBManager extends User {
    private List<Project> createdProjects;

    /**
     * Creates a new HDB Manager with the specified details.
     * 
     * @param nric The NRIC of this manager
     * @param password The password for authentication
     * @param age The age of this manager
     * @param maritalStatus The marital status of this manager
     */
    public HDBManager(String nric, String password, int age, String maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.createdProjects = new ArrayList<>();
    }

    /**
     * Creates a new BTO project with the specified details.
     * 
     * @param projectDetails Map containing all project details
     * @return The newly created Project
     */
    public Project createProject(Map<String, Object> projectDetails) {
        String projectId = (String) projectDetails.get("projectId");
        String projectName = (String) projectDetails.get("projectName");
        String neighborhood = (String) projectDetails.get("neighborhood");
        
        // Type-safe retrieval of flatTypes
        @SuppressWarnings("unchecked")
        Map<FlatType, Integer> flatTypes = (Map<FlatType, Integer>) projectDetails.get("flatTypes");
        
        Date applicationOpeningDate = (Date) projectDetails.get("applicationOpeningDate");
        Date applicationClosingDate = (Date) projectDetails.get("applicationClosingDate");
        int availableOfficerSlots = (Integer) projectDetails.get("availableOfficerSlots");
        
        Project project = new Project(
            projectId, projectName, neighborhood, flatTypes,
            applicationOpeningDate, applicationClosingDate,
            this, availableOfficerSlots
        );
        
        createdProjects.add(project);
        return project;
    }

    /**
     * Edits an existing project with updated details.
     * 
     * @param project The project to edit
     * @param updatedDetails Map containing the details to update
     * @return true if the project was updated successfully
     */
    public boolean editProject(Project project, Map<String, Object> updatedDetails) {
        if (createdProjects.contains(project)) {
            // Update project details based on the map
            if (updatedDetails.containsKey("projectName")) {
                project.setProjectName((String) updatedDetails.get("projectName"));
            }
            
            if (updatedDetails.containsKey("neighborhood")) {
                project.setNeighborhood((String) updatedDetails.get("neighborhood"));
            }
            
            if (updatedDetails.containsKey("flatTypes")) {
                @SuppressWarnings("unchecked")
                Map<FlatType, Integer> flatTypes = (Map<FlatType, Integer>) updatedDetails.get("flatTypes");
                project.setFlatTypes(flatTypes);
            }
            
            if (updatedDetails.containsKey("applicationOpeningDate")) {
                project.setApplicationOpeningDate((Date) updatedDetails.get("applicationOpeningDate"));
            }
            
            if (updatedDetails.containsKey("applicationClosingDate")) {
                project.setApplicationClosingDate((Date) updatedDetails.get("applicationClosingDate"));
            }
            
            if (updatedDetails.containsKey("availableOfficerSlots")) {
                project.setAvailableOfficerSlots((Integer) updatedDetails.get("availableOfficerSlots"));
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Deletes a project from the system.
     * 
     * @param project The project to delete
     * @return true if the project was deleted successfully
     */
    public boolean deleteProject(Project project) {
        return createdProjects.remove(project);
    }

    /**
     * Toggles the visibility of a project to applicants.
     * 
     * @param project The project to toggle visibility for
     * @param visibility The new visibility status
     * @return true if the visibility was updated successfully
     */
    public boolean toggleProjectVisibility(Project project, boolean visibility) {
        if (createdProjects.contains(project)) {
            project.setVisibility(visibility);
            return true;
        }
        return false;
    }

    /**
     * Returns a list of all projects in the system.
     * 
     * @return A list of all projects
     */
    public List<Project> viewAllProjects() {
        // Get all projects from the central data store instead of just created ones
        return BTODataStore.getInstance().getAllProjects();
    }

    /**
     * Returns a list of projects created by this manager.
     * 
     * @return A list of created projects
     */
    public List<Project> viewCreatedProjects() {
        return new ArrayList<>(createdProjects);
    }

    /**
     * Returns a list of officer registrations for a specific project.
     * 
     * @param project The project to view registrations for
     * @return A list of officer registrations
     */
    public List<Registration> viewOfficerRegistrations(Project project) {
        // Get the registrations from the central data store
        return BTODataStore.getInstance().getPendingRegistrations().stream()
                .filter(r -> r.getProject().equals(project))
                .collect(Collectors.toList());
    }

    /**
     * Approves an officer's registration for a project.
     * 
     * @param registration The registration to approve
     * @return true if the registration was approved successfully
     */
    public boolean approveOfficerRegistration(Registration registration) {
        if (registration.approve()) {
            // Add the officer to the project's officer list
            Project project = registration.getProject();
            HDBOfficer officer = registration.getOfficer();
            
            // Add officer to the project using proper method instead of directly modifying list
            project.addOfficer(officer);
            
            // Set the handling project for the officer
            officer.setHandlingProject(project);
            
            // Decrement available officer slots
            if (project.getavailableOfficerSlots() > 0) {
                project.setAvailableOfficerSlots(project.getavailableOfficerSlots() - 1);
            }
            
            // Update officer's registration status
            officer.setRegistrationStatus(status.RegistrationStatus.APPROVED);
            
            return true;
        }
        return false;
    }

    /**
     * Rejects an officer's registration for a project.
     * 
     * @param registration The registration to reject
     * @return true if the registration was rejected successfully
     */
    public boolean rejectOfficerRegistration(Registration registration) {
        if (registration.reject()) {
            // Update officer's registration status
            registration.getOfficer().setRegistrationStatus(status.RegistrationStatus.REJECTED);
            return true;
        }
        return false;
    }

    /**
     * Approves an application for a BTO flat.
     * 
     * @param application The application to approve
     * @return true if the application was approved successfully
     */
    public boolean approveApplication(Application application) {
        return application.updateStatus(ApplicationStatus.SUCCESSFUL);
    }

    /**
     * Rejects an application for a BTO flat.
     * 
     * @param application The application to reject
     * @return true if the application was rejected successfully
     */
    public boolean rejectApplication(Application application) {
        return application.updateStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Approves a withdrawal request for an application.
     * This will update the application status and free up the flat.
     * 
     * @param application The application with a withdrawal request
     * @return true if the withdrawal was approved successfully
     */
    public boolean approveWithdrawal(Application application) {
            // @note Check - Effect - Interact design flow
        if (application.isWithdrawalRequested()) { 
            // The applicant is voluntarily withdrawing from the application process.
            // It accurately reflects that the application did not result in a successful flat booking.
            application.updateStatus(ApplicationStatus.UNSUCCESSFUL);
            
            // Free up the flat by incrementing the count in the project
            Project project = application.getProject();
            FlatType flatType = application.getFlatType();
            int currentCount = project.getRemainingFlats(flatType);
            project.updateFlatCount(flatType, currentCount + 1);
            
            // Also update the applicant's status if needed
            Applicant applicant = application.getApplicant();
            if (applicant.getApplicationStatus() == ApplicationStatus.BOOKED || 
                applicant.getApplicationStatus() == ApplicationStatus.SUCCESSFUL) {
                applicant.setApplicationStatus(ApplicationStatus.UNSUCCESSFUL);
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Rejects a withdrawal request for an application.
     * This will reset the withdrawal request status.
     * 
     * @param application The application with a withdrawal request
     * @return true if the withdrawal was rejected successfully
     */
    public boolean rejectWithdrawal(Application application) {
        if (application.isWithdrawalRequested()) {
            // Reset withdrawal request
            application.setWithdrawalRequest(false);
            return true;
        }
        return false;
    }

    /**
     * Generates a report with filtered applications.
     * 
     * @param filters The criteria to filter applications
     * @return The generated Report
     */
    public Report generateReport(Map<String, Object> filters) {
        String reportId = "REP" + System.currentTimeMillis();
        return new Report(reportId, this, filters);
    }
    
    /**
     * Returns a list of all enquiries submitted for projects managed by this manager.
     * 
     * @return A list of all enquiries
     */
    public List<Enquiry> viewAllEnquiries() {
        // Get all enquiries from the central data store
        return BTODataStore.getInstance().getAllEnquiries();
    }
}
