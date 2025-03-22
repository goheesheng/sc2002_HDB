package user;

import project.Project;
import project.Application;
import project.FlatType;
import status.ApplicationStatus;

public class Applicant extends User {
    private Project appliedProject;
    private ApplicationStatus applicationStatus;
    private FlatType bookedFlatType;

    public Applicant(String nric, String password, int age, String maritalStatus) {
        super(nric, password, age, maritalStatus);
    }

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

    public boolean requestWithdrawal() {
        if (appliedProject != null) {
            Application application = viewApplication();
            if (application != null) {
                return application.requestWithdrawal();
            }
        }
        return false;
    }

    public boolean bookFlat(Project project, FlatType flatType) {
        if (applicationStatus == ApplicationStatus.SUCCESSFUL && appliedProject == project) {
            bookedFlatType = flatType;
            applicationStatus = ApplicationStatus.BOOKED;
            return true;
        }
        return false;
    }

    private FlatType getEligibleFlatType() {
        if (getMaritalStatus().equals("SINGLE")) {
            return FlatType.TWO_ROOM;
        } else {
            // For married applicants, default to THREE_ROOM if available
            return FlatType.THREE_ROOM;
        }
    }

    // Getters and setters
    public Project getAppliedProject() {
        return appliedProject;
    }
    //@audit add UML
    public FlatType getbookedFlatType() {
        return bookedFlatType;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus status) {
        this.applicationStatus = status;
    }
}
