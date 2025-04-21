package ui.submenu;

import project.Enquiry;
import utility.BTODataStore;
import user.HDBManager;
import user.HDBOfficer;
import project.Project;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EnquiryMenu {

    private Scanner scanner = new Scanner(System.in);
    private HDBManager manager;  // The manager replying to the enquiry
    private HDBOfficer officer;  // The officer replying to the enquiry
    private BTODataStore dataStore = BTODataStore.getInstance();

    public EnquiryMenu(HDBManager manager) {
        this.manager = manager;
    }

    public EnquiryMenu(HDBOfficer officer) {
        this.officer = officer;
    }
    
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n----- Enquiry Menu -----");
            System.out.println("1. View All Enquiries");
            System.out.println("2. Reply to an Enquiry");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAllEnquiries();
                    break;
                case 2:
                    replyToEnquiry();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (true);
    }

    private void viewAllEnquiries() {
        List<Enquiry> enquiries;
        if (officer != null) {
            // For officers, only show enquiries for their handling project
            Project handlingProject = officer.getHandlingProject();
            if (handlingProject == null) {
                System.out.println("You are not handling any project. Please register for a project first.");
                return;
            }
            enquiries = dataStore.getAllEnquiries().stream()
                .filter(e -> e.getProject().equals(handlingProject))
                .collect(java.util.stream.Collectors.toList());
        } else {
            // For managers, show all enquiries
            enquiries = dataStore.getAllEnquiries();
        }

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        System.out.println("\n--- List of All Enquiries ---");
        for (Enquiry e : enquiries) {
            System.out.println("ID: " + e.getEnquiryId());
            System.out.println("From: " + e.getUser().getNric());
            System.out.println("Project: " + e.getProject().getProjectName());
            System.out.println("Question: " + e.getEnquiryText());
            System.out.println("Reply: " + (e.getReply() != null ? e.getReply() : "Not replied yet"));
            System.out.println("--------------------------------------");
        }
    }

    private void replyToEnquiry() {
        System.out.print("Enter Enquiry ID to reply to: ");
        String enquiryId = scanner.nextLine();

        Optional<Enquiry> enquiryOpt = dataStore.findEnquiryById(enquiryId);
        if (!enquiryOpt.isPresent()) {
            System.out.println("Enquiry not found.");
            return;
        }

        Enquiry enquiry = enquiryOpt.get();

        // For officers, check if the enquiry is for their handling project
        if (officer != null) {
            Project handlingProject = officer.getHandlingProject();
            if (handlingProject == null) {
                System.out.println("You are not handling any project. Please register for a project first.");
                return;
            }
            if (!enquiry.getProject().equals(handlingProject)) {
                System.out.println("You can only reply to enquiries for the project you are handling.");
                return;
            }
        }

        System.out.println("Enquiry from " + enquiry.getUser().getNric() + ": " + enquiry.getEnquiryText());
        System.out.print("Enter your reply: ");
        String replyText = scanner.nextLine();

        if (replyText.trim().isEmpty()) {
            System.out.println("Reply cannot be empty.");
            return;
        }

        if (officer != null) {
            enquiry.addReply(replyText, officer);
        } else {
            enquiry.addReply(replyText, manager);
        }
        System.out.println("Reply saved successfully.");
    }
}
