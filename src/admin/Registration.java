package admin;

import user.HDBOfficer;
import project.Project;
import status.RegistrationStatus;
import java.util.Date;

public class Registration {
    private String registrationId;
    private HDBOfficer officer;
    private Project project;
    private Date registrationDate;
    private RegistrationStatus status;

    public Registration(String registrationId, HDBOfficer officer, Project project) {
        this.registrationId = registrationId;
        this.officer = officer;
        this.project = project;
        this.registrationDate = new Date();
        this.status = RegistrationStatus.PENDING;
    }

    public boolean approve() {
        if (status == RegistrationStatus.PENDING) {
            status = RegistrationStatus.APPROVED;
            return true;
        }
        return false;
    }

    public boolean reject() {
        if (status == RegistrationStatus.PENDING) {
            status = RegistrationStatus.REJECTED;
            return true;
        }
        return false;
    }

    // Getters
    public String getRegistrationId() {
        return registrationId;
    }
    public Date getregistrationDate() {
        return registrationDate;
    }
    public HDBOfficer getOfficer() {
        return officer;
    }

    public Project getProject() {
        return project;
    }

    public RegistrationStatus getStatus() {
        return status;
    }
}
