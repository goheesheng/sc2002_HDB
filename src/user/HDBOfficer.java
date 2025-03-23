package user;

import project.Project;
import project.Enquiry;
import project.Application;
import project.FlatType;
import admin.Receipt;
import status.ApplicationStatus;
import status.RegistrationStatus;
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

    /**
     * Creates a new HDB Officer with the specified details.
     * 
     * @param nric The NRIC of this officer
     * @param password The password for authentication
     * @param age The age of this officer
     * @param maritalStatus The marital status of this officer
     */
    public HDBOfficer(String nric, String password, int age, String maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.registrationStatus = RegistrationStatus.PENDING;
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
            handlingProject = project;
            registrationStatus = RegistrationStatus.PENDING;
            return true;
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

    /**
     * Views the details of a project if this officer is handling it.
     * 
     * @param project The project to view details for
     * @return The project if this officer is handling it, null otherwise
     */
    public Project viewProjectDetails(Project project) {
        if (handlingProject == project) {
            return project;
        }
        return null;
    }

    /**
     * Replies to an enquiry submitted for the project this officer is handling.
     * 
     * @param enquiry The enquiry to reply to
     * @param replyText The text content of the reply
     * @return true if the reply was added successfully, false otherwise
     */
    public boolean replyToEnquiry(Enquiry enquiry, String replyText) {
        if (handlingProject == enquiry.getProject()) {
            return enquiry.addReply(replyText, this);
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
        if (handlingProject == project) {
            int currentCount = project.getRemainingFlats(flatType);
            if (currentCount > 0) {
                project.updateFlatCount(flatType, currentCount - 1);
                return true;
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
        if (handlingProject != null) {
            for (Application app : handlingProject.getApplications()) {
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
        return application.updateStatus(status);
    }

    /**
     * Updates an applicant's profile with the selected flat type after a successful application.
     * 
     * @param applicant The applicant to update
     * @param flatType The flat type selected by the applicant
     * @return true if the profile was updated successfully
     */
    public boolean updateApplicantProfile(Applicant applicant, FlatType flatType) {
        if (applicant.getApplicationStatus() == ApplicationStatus.SUCCESSFUL) {
            boolean result = applicant.bookFlat(handlingProject, flatType);
            if (result) {
                Application app = retrieveApplication(applicant.getNric());
                if (app != null) {
                    app.updateStatus(ApplicationStatus.BOOKED); 
                    handlingProject.updateFlatCount(flatType, handlingProject.getRemainingFlats(flatType) - 1);
                }
            }
            return result;
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
}
