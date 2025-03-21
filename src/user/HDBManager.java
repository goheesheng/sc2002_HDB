package user;

import project.Project;
import project.Enquiry;
import project.Application;
import admin.Registration;
import admin.Report;
import status.ApplicationStatus;
import status.RegistrationStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HDBManager extends User {
    private List<Project> createdProjects;

    public HDBManager(String nric, String password, int age, String maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.createdProjects = new ArrayList<>();
    }

    public Project createProject(Map<String, Object> projectDetails) {
        String projectId = (String) projectDetails.get("projectId");
        String projectName = (String) projectDetails.get("projectName");
        String neighborhood = (String) projectDetails.get("neighborhood");
        Map<project.FlatType, Integer> flatTypes = (Map<project.FlatType, Integer>) projectDetails.get("flatTypes");
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

    public boolean editProject(Project project, Map<String, Object> updatedDetails) {
        if (createdProjects.contains(project)) {
            // Implementation would update project details
            return true;
        }
        return false;
    }

    public boolean deleteProject(Project project) {
        return createdProjects.remove(project);
    }

    public boolean toggleProjectVisibility(Project project, boolean visibility) {
        if (createdProjects.contains(project)) {
            project.setVisibility(visibility);
            return true;
        }
        return false;
    }

    public List<Project> viewAllProjects() {
        // Implementation would return all projects in the system
        return new ArrayList<>();
    }

    public List<Project> viewCreatedProjects() {
        return new ArrayList<>(createdProjects);
    }

    public List<Registration> viewOfficerRegistrations(Project project) {
        // Implementation would return registrations for the project
        return new ArrayList<>();
    }

    public boolean approveOfficerRegistration(Registration registration) {
        return registration.approve();
    }

    public boolean rejectOfficerRegistration(Registration registration) {
        return registration.reject();
    }

    public boolean approveApplication(Application application) {
        return application.updateStatus(ApplicationStatus.SUCCESSFUL);
    }

    public boolean rejectApplication(Application application) {
        return application.updateStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    public boolean approveWithdrawal(Application application) {
        if (application.isWithdrawalRequested()) {
            // Implementation would handle withdrawal approval
            return true;
        }
        return false;
    }

    public boolean rejectWithdrawal(Application application) {
        if (application.isWithdrawalRequested()) {
            // Implementation would handle withdrawal rejection
            return false;
        }
        return false;
    }

    public Report generateReport(Map<String, Object> filters) {
        String reportId = "REP" + System.currentTimeMillis();
        return new Report(reportId, this, filters);
    }

    public List<Enquiry> viewAllEnquiries() {
        // Implementation would return all enquiries from all projects
        return new ArrayList<>();
    }
}
