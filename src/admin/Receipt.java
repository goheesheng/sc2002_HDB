package admin;

import project.FlatType;
import project.Project;
import java.io.File;
import java.util.Date;

/**
 * Represents a receipt for a successful BTO flat booking.
 * Contains details about the applicant and the booked flat.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class Receipt {
    private String receiptId;
    private String applicantName;
    private String applicantNRIC;
    private int applicantAge;
    private String maritalStatus;
    private FlatType flatTypeBooked;
    private Project project;
    private Date bookingDate;

    /**
     * Creates a new Receipt with the specified details.
     * 
     * @param receiptId The unique identifier for this receipt
     * @param applicantName The name of the applicant
     * @param applicantNRIC The NRIC of the applicant
     * @param applicantAge The age of the applicant
     * @param maritalStatus The marital status of the applicant
     * @param flatTypeBooked The type of flat booked
     * @param project The project where the flat is located
     */
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

    /**
     * Gets the unique identifier of this receipt.
     * 
     * @return The receipt ID
     */
    public String getReceiptId() {
        return receiptId;
    }
    
    /**
     * Gets the name of the applicant.
     * 
     * @return The applicant's name
     */
    public String getApplicantName() {
        return applicantName;
    }
    
    /**
     * Gets the NRIC of the applicant.
     * 
     * @return The applicant's NRIC
     */
    public String getApplicantNRIC() {
        return applicantNRIC;
    }
    
    /**
     * Gets the age of the applicant.
     * 
     * @return The applicant's age
     */
    public int getApplicantAge() {
        return applicantAge;
    }
    
    /**
     * Gets the marital status of the applicant.
     * 
     * @return The applicant's marital status
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }
    
    /**
     * Gets the type of flat booked by the applicant.
     * 
     * @return The flat type booked
     */
    public FlatType getFlatTypeBooked() {
        return flatTypeBooked;
    }
    
    /**
     * Gets the project where the booked flat is located.
     * 
     * @return The project
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Gets the date when the flat was booked.
     * 
     * @return The booking date
     */
    public Date getBookingDate() {
        return bookingDate;
    }
    
    /**
     * Generates a PDF file representing this receipt.
     * This method simulates PDF generation and returns a placeholder file.
     * In a real implementation, it would create a PDF with receipt details.
     * 
     * @return A File object representing the generated PDF
     */
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
