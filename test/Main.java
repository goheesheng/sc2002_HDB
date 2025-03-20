import user.Applicant;
import project.Project;
// Incomplete
public class Main {
    public static void main(String[] args) {

        Applicant applicant = new Applicant("S1234567A", "password123", 30, "Single");


        applicant.login();
        applicant.viewProjects();

        Project project = new Project("Punggol Waterway", "Punggol", 50, 30, "2025-01-01", "2025-02-01", true, 5);

        applicant.applyProject(project);

        System.out.println(applicant.viewApplicationStatus());

        applicant.submitEnquiry("Can I apply for a 3-room flat?");
    }
}
