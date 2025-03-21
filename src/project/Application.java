package project;

import user.Applicant;
import status.ApplicationStatus;
import java.util.Date;

public class Application {
    private String applicationId;
    private Applicant applicant;
    private Project project;
    private Date applicationDate;
    private ApplicationStatus status;
    private FlatType flatType;
    private boolean withdrawalRequest;

    public Application(String applicationId, Applicant applicant, Project project, FlatType flatType) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.project = project;
        this.applicationDate = new Date();
        this.status = ApplicationStatus.PENDING;
        this.flatType = flatType;
        this.withdrawalRequest = false;
    }

    public boolean updateStatus(ApplicationStatus status) {
        this.status = status;
        return true;
    }

    public boolean requestWithdrawal() {
        this.withdrawalRequest = true;
        return true;
    }

    // Getters and setters
    public String getApplicationId() {
        return applicationId;
    }
    public Date getapplicationDate() {
        return applicationDate;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Project getProject() {
        return project;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public boolean isWithdrawalRequested() {
        return withdrawalRequest;
    }
}
