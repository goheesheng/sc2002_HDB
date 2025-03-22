package user;

import project.Project;
import project.Enquiry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class User {
    private String nric;
    private String password;
    private int age;
    private String maritalStatus;
    private List<Enquiry> enquiries;

    public User(String nric, String password, int age, String maritalStatus) {
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.enquiries = new ArrayList<>();
    }

    public boolean login(String nric, String password) {
        return this.nric.equals(nric) && this.password.equals(password);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    public Enquiry submitEnquiry(Project project, String enquiryText) {
        Enquiry enquiry = new Enquiry(generateEnquiryId(), this, project, enquiryText);
        enquiries.add(enquiry);
        project.addEnquiry(enquiry);
        return enquiry;
    }

    public List<Enquiry> viewEnquiries() {
        return new ArrayList<>(enquiries);
    }

    public boolean editEnquiry(String enquiryId, String newText) {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryId().equals(enquiryId)) {
                return enquiry.editText(newText);
            }
        }
        return false;
    }

    public boolean deleteEnquiry(String enquiryId) {
        return enquiries.removeIf(e -> e.getEnquiryId().equals(enquiryId));
    }

    private String generateEnquiryId() {
        return "ENQ" + System.currentTimeMillis();
    }

    // Getters and setters
    public String getNric() {
        return nric;
    }

    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }
}
