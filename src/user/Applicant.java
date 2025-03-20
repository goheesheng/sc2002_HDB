package user;

import project.Project;

public class Applicant extends User {
    public Applicant(String nric, String password, int age, String maritalStatus) {
        super(nric, password, age, maritalStatus);
    }

    @Override
    public boolean login() {
        System.out.println("Applicant logged in successfully!");
        return true; 
    }

    @Override
    public void changePassword(String newPassword) {
        setPassword(newPassword);
        System.out.println("Password changed successfully!");
    }

    public void viewProjects() {
        System.out.println("Viewing available projects...");
    }

    public void applyProject(Project project) {
        System.out.println("Applying for project: " + project.getProjectName());
    }

    public String viewApplicationStatus() {
        return "Application Status: PENDING";
    }

    public void requestWithdrawal() {
        System.out.println("Request to withdraw submitted.");
    }

    public void submitEnquiry(String enquiry) {
        System.out.println("Enquiry submitted: " + enquiry);
    }

    public void editEnquiry(int enquiryID, String newText) {
        System.out.println("Enquiry edited: " + newText);
    }

    public void deleteEnquiry(int enquiryID) {
        System.out.println("Enquiry deleted.");
    }
}
