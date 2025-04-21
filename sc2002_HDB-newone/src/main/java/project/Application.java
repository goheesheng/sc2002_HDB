package project;

import user.Applicant;
import status.ApplicationStatus;
import status.WithdrawalStatus;

import java.util.Date;

/**
 * Represents an application for a BTO flat.
 * An application is created when an applicant applies for a project.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class Application {
    private String applicationId;
    private Applicant applicant;
    private Project project;
    private Date applicationDate;
    private ApplicationStatus status;
    private FlatType flatType;
    private WithdrawalStatus withdrawalStatus; 


    /**
     * Creates a new Application with the specified details.
     * 
     * @param applicationId The unique identifier for this application
     * @param applicant The applicant who submitted this application
     * @param project The project being applied for
     * @param flatType The type of flat being applied for
     */
    public Application(String applicationId, Applicant applicant, Project project, FlatType flatType) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.project = project;
        this.applicationDate = new Date();
        this.status = ApplicationStatus.PENDING;
        this.flatType = flatType;
        this.withdrawalStatus = WithdrawalStatus.NIL;
    }

    /**
     * Updates the status of this application and synchronizes with the applicant's status.
     * 
     * @param status The new status for this application
     * @return true if the status was updated successfully
     */
    public boolean updateStatus(ApplicationStatus status) {
        this.status = status;
        // Also update the applicant's status
        this.applicant.setApplicationStatus(status);
        return true;
    }
    
    /**
     * Marks this application for withdrawal.
     * 
     * @return true if the withdrawal request was successful
     */
    public boolean requestWithdrawal() {
        this.withdrawalStatus = WithdrawalStatus.PENDING;
        return true;
    }

    // Getters and setters

    /**
     * Gets the unique identifier of this application.
     * 
     * @return The application ID
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Gets the date when this application was submitted.
     * 
     * @return The application date
     */
    public Date getapplicationDate() {
        return applicationDate;
    }

    /**
     * Gets the applicant who submitted this application.
     * 
     * @return The applicant
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Gets the project being applied for.
     * 
     * @return The project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Gets the current status of this application.
     * 
     * @return The application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Gets the type of flat being applied for.
     * 
     * @return The flat type
     */
    public FlatType getFlatType() {
        return flatType;
    }
    
    /**
     * update the withdrawal request status for this application.
     * 
     * @param value The new withdrawal request status
     */
    public boolean updateWithdrawalStatus(WithdrawalStatus status) {
        this.withdrawalStatus = status;
        return true;
    }

    /**
     * Checks if this application has been marked for withdrawal.
     * 
     * @return true if withdrawal is requested, false otherwise
     */
    public WithdrawalStatus getWithdrawalStatus() {
        return withdrawalStatus;
    }
}
