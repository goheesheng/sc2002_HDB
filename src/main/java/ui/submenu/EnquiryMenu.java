package ui.submenu;

import project.Enquiry;
import utility.BtoDataStore;
import user.HDBManager;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EnquiryMenu {

    private Scanner scanner = new Scanner(System.in);
    private HDBManager manager;  // The user replying to the enquiry
    private BtoDataStore dataStore = BtoDataStore.getInstance();

    public EnquiryMenu(HDBManager manager) {
        this.manager = manager;
    }
    public EnquiryMenu() {
        // No-arg constructor so it can be instantiated like: new EnquiryMenu()
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
        List<Enquiry> enquiries = dataStore.getAllEnquiries();
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

        System.out.println("Enquiry from " + enquiry.getUser().getNric() + ": " + enquiry.getEnquiryText());
        System.out.print("Enter your reply: ");
        String replyText = scanner.nextLine();

        if (replyText.trim().isEmpty()) {
            System.out.println("Reply cannot be empty.");
            return;
        }

        enquiry.addReply(replyText, manager);  // Assuming manager is the one replying
        System.out.println("Reply saved successfully.");
    }
}
