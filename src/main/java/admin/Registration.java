package admin;

import user.HDBOfficer;
import project.Project;
import status.RegistrationStatus;
import java.util.Date;

/**
 * Represents a registration request by an HDB Officer for a project.
 * Tracks the status of the registration and provides methods to approve or reject it.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class Registration {
    private String registrationId;
    private HDBOfficer officer;
    private Project project;
    private Date registrationDate;
    private RegistrationStatus status;

    /**
     * Creates a new Registration with the specified details.
     * 
     * @param registrationId The unique identifier for this registration
     * @param officer The HDB Officer requesting registration
     * @param project The project the officer is registering for
     */
    public Registration(String registrationId, HDBOfficer officer, Project project) {
        this.registrationId = registrationId;
        this.officer = officer;
        this.project = project;
        this.registrationDate = new Date();
        this.status = RegistrationStatus.PENDING;
    }

    /**
     * Approves this registration request if it is currently pending.
     * 
     * @return true if the registration was approved successfully, false otherwise
     */
    public boolean approve() {
        if (status == RegistrationStatus.PENDING) {
            status = RegistrationStatus.APPROVED;
            return true;
        }
        return false;
    }

    /**
     * Rejects this registration request if it is currently pending.
     * 
     * @return true if the registration was rejected successfully, false otherwise
     */
    public boolean reject() {
        if (status == RegistrationStatus.PENDING) {
            status = RegistrationStatus.REJECTED;
            return true;
        }
        return false;
    }

    // Getters
    /**
     * Gets the unique identifier of this registration.
     * 
     * @return The registration ID
     */
    public String getRegistrationId() {
        return registrationId;
    }
    
    /**
     * Gets the date when this registration was submitted.
     * 
     * @return The registration date
     */
    public Date getregistrationDate() {
        return registrationDate;
    }
    
    /**
     * Gets the HDB Officer who submitted this registration.
     * 
     * @return The officer
     */
    public HDBOfficer getOfficer() {
        return officer;
    }
    
    /**
     * Gets the project this registration is for.
     * 
     * @return The project
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Gets the current status of this registration.
     * 
     * @return The registration status
     */
    public RegistrationStatus getStatus() {
        return status;
    }
}
