package user;

import project.Project;
import project.Application;
import project.FlatType;
import status.ApplicationStatus;
import project.Enquiry;

/**
 * Represents an applicant for BTO housing projects.
 * Applicants can apply for projects, view their applications, and book flats.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class Applicant extends User {
    private Project appliedProject;
    private ApplicationStatus applicationStatus;
    private FlatType bookedFlatType;

    /**
     * Creates a new Applicant with the specified details.
     * 
     * @param nric The NRIC of this applicant
     * @param password The password for authentication
     * @param age The age of this applicant
     * @param maritalStatus The marital status of this applicant
     */
    public Applicant(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
    }

    /**
     * Applies for a BTO project if the applicant is eligible.
     * 
     * @param project The project to apply for
     * @return true if the application was successful, false otherwise
     */
    public boolean applyForProject(Project project) {
        if (appliedProject == null && project.isEligibleForUser(this)) {
            appliedProject = project;
            applicationStatus = ApplicationStatus.PENDING;
            
            // Create and add application to project
            String applicationId = "APP" + System.currentTimeMillis();
            FlatType flatType = getEligibleFlatType();
            Application application = new Application(applicationId, this, project, flatType);
            project.addApplication(application);
            
            return true;
        }
        return false;
    }

    /**
     * Retrieves the application submitted by this applicant.
     * 
     * @return The application, or null if no application exists
     */
    public Application viewApplication() {
        if (appliedProject != null) {
            for (Application app : appliedProject.getApplications()) {
                if (app.getApplicant().equals(this)) {
                    return app;
                }
            }
        }
        return null;
    }

    /**
     * Requests withdrawal of the applicant's application.
     * 
     * @return true if the withdrawal request was successful, false otherwise
     */
    public boolean requestWithdrawal() {
        if (appliedProject != null) {
            Application application = viewApplication();
            if (application != null) {
                return application.requestWithdrawal();
            }
        }
        return false;
    }

    /**
     * Books a flat in a project after a successful application.
     * 
     * @param project The project to book a flat in
     * @param flatType The type of flat to book
     * @return true if the booking was successful, false otherwise
     */
    public boolean bookFlat(Project project, FlatType flatType) {
        if (applicationStatus == ApplicationStatus.SUCCESSFUL && appliedProject == project) {
            bookedFlatType = flatType;
            applicationStatus = ApplicationStatus.BOOKED;
            Application app = viewApplication();
            if (app != null) {
                app.updateStatus(ApplicationStatus.BOOKED);
            }
            return true;
        }
        return false;
    }
    
       
    /**
     * Determines the eligible flat type for this applicant based on marital status.
     * 
     * @return The eligible flat type
     */
    private FlatType getEligibleFlatType() {
        if (getMaritalStatus().equals("SINGLE")) {
            return FlatType.TWO_ROOM;
        } else {
            // For married applicants, default to THREE_ROOM if available
            return FlatType.THREE_ROOM;
        }
    }
    public void setAppliedProject(Project project) {
        this.appliedProject = project;
    }
    

    /**
     * Gets the project this applicant has applied for.
     * 
     * @return The applied project, or null if no application exists
     */
    public Project getAppliedProject() {
        return appliedProject;
    }
    
    /**
     * Gets the type of flat booked by this applicant.
     * 
     * @return The booked flat type, or null if no flat is booked
     */
    public FlatType getbookedFlatType() {
        return bookedFlatType;
    }
    
    /**
     * Gets the current status of this applicant's application.
     * 
     * @return The application status
     */
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
    
    /**
     * Sets the status of this applicant's application.
     * 
     * @param status The new application status
     */
    public void setApplicationStatus(ApplicationStatus status) {
        this.applicationStatus = status;
    }
    
    
}
