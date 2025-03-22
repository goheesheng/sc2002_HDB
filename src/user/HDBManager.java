package user;

import project.Project;
import project.Enquiry;
import project.Application;
import admin.Registration;
import admin.Report;
import status.ApplicationStatus;
import project.FlatType;

import java.util.ArrayList;
import java.util.Date;
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
        // Implementation to return all projects in the system
        List<Project> allProjects = new ArrayList<>(createdProjects);
        // Add logic to fetch projects from other managers if needed
        return allProjects;
    }

    public List<Project> viewCreatedProjects() {
        return new ArrayList<>(createdProjects);
    }

    public List<Registration> viewOfficerRegistrations(Project project) {
        // Implementation to return registrations for the project
        List<Registration> registrations = new ArrayList<>();
        for (HDBOfficer officer : project.getOfficers()) {
            Registration registration = new Registration("REG" + System.currentTimeMillis(), officer, project);
            registrations.add(registration);
        }
        return registrations;
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
    
    

    public boolean rejectWithdrawal(Application application) {
        if (application.isWithdrawalRequested()) {
            // Reset withdrawal request
            application.setWithdrawalRequest(false);
            return true;
        }
        return false;
    }

    public Report generateReport(Map<String, Object> filters) {
        String reportId = "REP" + System.currentTimeMillis();
        return new Report(reportId, this, filters);
    }

    public List<Enquiry> viewAllEnquiries() {
        List<Enquiry> allEnquiries = new ArrayList<>();
        for (Project project : createdProjects) {
            allEnquiries.addAll(project.getEnquiries());
        }
        return allEnquiries;
    }
}
