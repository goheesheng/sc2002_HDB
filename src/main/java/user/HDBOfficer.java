package user;

import project.Project;
import project.Enquiry;
import project.Application;
import project.FlatType;
import admin.Receipt;
import admin.Registration;
import status.ApplicationStatus;
import status.RegistrationStatus;
import utility.BTODataStore;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an HDB Officer who can handle BTO projects.
 * Officers can register for projects, update application statuses, and generate receipts.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class HDBOfficer extends Applicant {
    private Project handlingProject;
    private RegistrationStatus registrationStatus;
    private List<Registration> registrations;

    /**
     * Creates a new HDB Officer with the specified details.
     * 
     * @param nric The NRIC of this officer
     * @param password The password for authentication
     * @param age The age of this officer
     * @param maritalStatus The marital status of this officer
     */
    public HDBOfficer(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.registrationStatus = RegistrationStatus.PENDING;
        this.registrations = new ArrayList<>();
    }
    /**
     * Registers this officer to handle a specific project.
     * Officers cannot register for projects they have applied for as applicants.
     * 
     * @param project The project to register for
     * @return true if registration was successful, false otherwise
     */
    public boolean registerForProject(Project project) {
        if (handlingProject == null && !hasAppliedForProject(project)) {
            // Create a registration object and add it to the central data store
            String regId = "REG" + System.currentTimeMillis();
            Registration registration = new Registration(regId, this, project, RegistrationStatus.PENDING);
            registrations.add(registration);
            BTODataStore.getInstance().addRegistration(registration);
            
            this.registrationStatus = RegistrationStatus.PENDING;            return true;
        }
        return false;
    }

    /**
     * Gets the current registration status of this officer.
     * 
     * @return The registration status
     */
    public RegistrationStatus viewRegistrationStatus() {
        return registrationStatus;
    }

    public void viewAllRegistrationStatuses() {
        if (registrations.isEmpty()) {
            System.out.println("You have not registered for any project.");
        } else {
            for (Registration reg : registrations) {
                System.out.println("Project: " + reg.getProject().getProjectName() + " - Status: " + reg.getStatus());
            }
        }
    }

    /**
     * Views the details of a project. Officers can always view project details 
     * even if they are not handling it and even if visibility is toggled off.
     * 
     * @param project The project to view details for
     * @return The project if it exists, null otherwise
     */
    public Project viewProjectDetails(Project project) {
        // HDB Officers can always view project details regardless of visibility
        // This fixes Test Case 12: Project Detail Access for HDB Officer
        if (project != null) {
            return project;
        }
        return null;
    }
    
    /**
     * HDB Officers cannot edit project details. Only HDB Managers have this permission.
     * This method always returns false to enforce this restriction.
     * 
     * @param project The project attempting to be edited
     * @param updatedDetails The details to be updated
     * @return Always false, as officers cannot edit projects
     */
    public boolean editProject(Project project, java.util.Map<String, Object> updatedDetails) {
        // Officers cannot edit project details - this is only for HDBManager
        // This implements Test Case 13: Restriction on Editing Project Details
        return false;
    }

    /**
     * Replies to an enquiry submitted for any project.
     * Officers can reply to enquiries for the project they are handling.
     * 
     * @param enquiry The enquiry to reply to
     * @param replyText The text content of the reply
     * @return true if the reply was added successfully, false otherwise
     */
    public boolean replyToEnquiry(Enquiry enquiry, String replyText) {
        // Fix for Test Case 14: Response to Project Enquiries
        // Officers can reply to enquiries for their assigned project
        if (enquiry != null) {
            // Check if officer is assigned to this project
            if (handlingProject != null && handlingProject.equals(enquiry.getProject())) {
                return enquiry.addReply(replyText, this);
            }
        }
        return false;
    }

    /**
     * Updates the count of remaining flats for a specific flat type in the project this officer is handling.
     * 
     * @param project The project to update flat counts for
     * @param flatType The type of flat to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateRemainingFlats(Project project, FlatType flatType) {
        // Fix for Test Case 15: Flat Selection and Booking Management
        if (project != null && flatType != null) {
            if (handlingProject != null && handlingProject.equals(project)) {
                int currentCount = project.getRemainingFlats(flatType);
                if (currentCount > 0) {
                    project.updateFlatCount(flatType, currentCount - 1);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves an application submitted by an applicant with the specified NRIC.
     * 
     * @param nric The NRIC of the applicant
     * @return The application, or null if not found
     */
    public Application retrieveApplication(String nric) {
        // Fix for Test Case 15: Flat Selection and Booking Management
        if (handlingProject != null && nric != null && !nric.isEmpty()) {
            // First check the project's applications
            for (Application app : handlingProject.getApplications()) {
                if (app.getApplicant().getNric().equals(nric)) {
                    return app;
                }
            }
            
            // If not found in project, check the data store
            for (Application app : BTODataStore.getInstance().getAllApplications()) {
                if (app.getApplicant().getNric().equals(nric)) {
                    return app;
                }
            }
        }
        return null;
    }

    /**
     * Updates the status of an application.
     * 
     * @param application The application to update
     * @param status The new status for the application
     * @return true if the status was updated successfully
     */
    public boolean updateApplicationStatus(Application application, ApplicationStatus status) {
        // Fix for Test Case 15: Flat Selection and Booking Management
        if (application != null && status != null) {
            return application.updateStatus(status);
        }
        return false;
    }

    /**
     * Updates an applicant's profile with the selected flat type after a successful application.
     * 
     * @param applicant The applicant to update
     * @param flatType The flat type selected by the applicant
     * @return true if the profile was updated successfully
     */
    public boolean updateApplicantProfile(Applicant applicant, FlatType flatType) {
        // Fix for Test Case 15: Flat Selection and Booking Management
        if (applicant != null && flatType != null && handlingProject != null) {
            if (applicant.getApplicationStatus() == ApplicationStatus.SUCCESSFUL) {
                // Book the flat for the applicant
                boolean result = applicant.bookFlat(handlingProject, flatType);
                
                if (result) {
                    // Update the application status
                    Application app = retrieveApplication(applicant.getNric());
                    if (app != null) {
                        app.updateStatus(ApplicationStatus.BOOKED);
                        
                        // Update the flat count within the same method for tight coupling
                        int currentCount = handlingProject.getRemainingFlats(flatType);
                        handlingProject.updateFlatCount(flatType, currentCount - 1);
                    }
                }
                return result;
            }
        }
        return false;
    }
      
    /**
     * Generates a booking receipt for a successful application.
     * 
     * @param application The application to generate a receipt for
     * @return The generated Receipt, or null if the application is not eligible
     */
    public Receipt generateBookingReceipt(Application application) {
        // Check if the application status is BOOKED or if we need to check the applicant's status
        if (application != null && 
            (application.getStatus() == ApplicationStatus.BOOKED || 
             application.getApplicant().getApplicationStatus() == ApplicationStatus.BOOKED)) {
            
            Applicant applicant = application.getApplicant();
            String receiptId = "REC" + System.currentTimeMillis();
            
            // Create and return a new receipt with all required details
            return new Receipt(
                receiptId,
                applicant.getNric(), // Using NRIC as name for simplicity
                applicant.getNric(),
                applicant.getAge(),
                applicant.getMaritalStatus(),
                application.getFlatType(),
                application.getProject()
            );
        }
        return null;
    }

    /**
     * Checks if this officer has applied for a project as an applicant.
     * 
     * @param project The project to check
     * @return true if the officer has applied for the project, false otherwise
     */
    private boolean hasAppliedForProject(Project project) {
        return getAppliedProject() != null && getAppliedProject().equals(project);
    }

    /**
     * Sets the registration status of this officer.
     * 
     * @param status The new registration status
     */
    public void setRegistrationStatus(RegistrationStatus status) {
        this.registrationStatus = status;
    }

    /**
     * Sets the handling project for this officer.
     * This is called when the officer's registration is approved.
     * 
     * @param project The project to handle
     */
    public void setHandlingProject(Project project) {
        this.handlingProject = project;
    }
    
    /**
     * Gets the project this officer is handling.
     * 
     * @return The project this officer is handling, or null if not assigned
     */
    public Project getHandlingProject() {
        return handlingProject;
    }
}
