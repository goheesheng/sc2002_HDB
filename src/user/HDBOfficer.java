package user;

import project.Project;
import project.Enquiry;
import project.Application;
import project.FlatType;
import admin.Receipt;
import status.ApplicationStatus;
import status.RegistrationStatus;

public class HDBOfficer extends Applicant {
    private Project handlingProject;
    private RegistrationStatus registrationStatus;

    public HDBOfficer(String nric, String password, int age, String maritalStatus) {
        super(nric, password, age, maritalStatus);
        this.registrationStatus = RegistrationStatus.PENDING;
    }

    public boolean registerForProject(Project project) {
        if (handlingProject == null && !hasAppliedForProject(project)) {
            handlingProject = project;
            registrationStatus = RegistrationStatus.PENDING;
            return true;
        }
        return false;
    }

    public RegistrationStatus viewRegistrationStatus() {
        return registrationStatus;
    }

    public Project viewProjectDetails(Project project) {
        if (handlingProject == project) {
            return project;
        }
        return null;
    }

    public boolean replyToEnquiry(Enquiry enquiry, String replyText) {
        if (handlingProject == enquiry.getProject()) {
            return enquiry.addReply(replyText, this);
        }
        return false;
    }

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

    public boolean updateApplicationStatus(Application application, ApplicationStatus status) {
        return application.updateStatus(status);
    }

    public boolean updateApplicantProfile(Applicant applicant, FlatType flatType) {
        // Check if the applicant has a successful application
        if (applicant.getApplicationStatus() == ApplicationStatus.SUCCESSFUL) {
            // Update the applicant's profile with the selected flat type
            boolean result = applicant.bookFlat(handlingProject, flatType);
            
            // If booking was successful, also update the application status
            if (result) {
                Application app = retrieveApplication(applicant.getNric());
                if (app != null) {
                    app.updateStatus(ApplicationStatus.BOOKED);
                }
            }
            return result;
        }
        return false;
    }    

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
    
    private boolean hasAppliedForProject(Project project) {
        return getAppliedProject() != null && getAppliedProject().equals(project);
    }

    // Getters and setters
    public void setRegistrationStatus(RegistrationStatus status) {
        this.registrationStatus = status;
    }
}
