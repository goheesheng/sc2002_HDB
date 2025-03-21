package admin;

import project.FlatType;
import project.Project;
import java.io.File;
import java.util.Date;

public class Receipt {
    private String receiptId;
    private String applicantName;
    private String applicantNRIC;
    private int applicantAge;
    private String maritalStatus;
    private FlatType flatTypeBooked;
    private Project project;
    private Date bookingDate;

    public Receipt(String receiptId, String applicantName, String applicantNRIC, 
                  int applicantAge, String maritalStatus, FlatType flatTypeBooked, 
                  Project project) {
        this.receiptId = receiptId;
        this.applicantName = applicantName;
        this.applicantNRIC = applicantNRIC;
        this.applicantAge = applicantAge;
        this.maritalStatus = maritalStatus;
        this.flatTypeBooked = flatTypeBooked;
        this.project = project;
        this.bookingDate = new Date();
    }

    // Getters
    public String getReceiptId() {
        return receiptId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantNRIC() {
        return applicantNRIC;
    }

    public int getApplicantAge() {
        return applicantAge;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public FlatType getFlatTypeBooked() {
        return flatTypeBooked;
    }

    public Project getProject() {
        return project;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public File generatePDF() {
        // Placeholder for PDF generation logic
        // In a real implementation, this would create a PDF with receipt details
        System.out.println("Generating PDF receipt for " + applicantName);
        System.out.println("Receipt ID: " + receiptId);
        System.out.println("NRIC: " + applicantNRIC);
        System.out.println("Age: " + applicantAge);
        System.out.println("Marital Status: " + maritalStatus);
        System.out.println("Flat Type Booked: " + flatTypeBooked);
        System.out.println("Project: " + project.getProjectName());
        System.out.println("Booking Date: " + bookingDate);
        
        // Return a dummy file
        return new File("receipt_" + receiptId + ".pdf");
    }
}
