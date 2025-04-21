package project;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receipt {
    static {
        // Suppress PDFBox font-related warnings
        Logger.getLogger("org.apache.pdfbox").setLevel(Level.SEVERE);
        Logger.getLogger("org.apache.fontbox").setLevel(Level.SEVERE);
        Logger.getLogger("org.apache.fontbox.ttf").setLevel(Level.SEVERE);
        Logger.getLogger("org.apache.fontbox.ttf.TTFParser").setLevel(Level.SEVERE);
    }
    
    private String receiptId;
    private String applicantId;
    private String applicantName;
    private int applicantAge;
    private String applicantMaritalStatus;
    private String projectId;
    private LocalDateTime applicationDate;
    private String flatType;
    private double applicationFee;
    private String status;

    public Receipt(String receiptId, String applicantId, String applicantName, int applicantAge, 
                  String applicantMaritalStatus, String projectId, LocalDateTime applicationDate, 
                  String flatType, double applicationFee, String status) {
        this.receiptId = receiptId;
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.applicantAge = applicantAge;
        this.applicantMaritalStatus = applicantMaritalStatus;
        this.projectId = projectId;
        this.applicationDate = applicationDate;
        this.flatType = flatType;
        this.applicationFee = applicationFee;
        this.status = status;
    }

    public void generatePDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font and size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                
                // Add title
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("HDB BTO Application Receipt");
                contentStream.endText();

                // Set font for content
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                
                // Add receipt details
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 650);
                contentStream.showText("Receipt ID: " + receiptId);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Applicant Name: " + applicantName);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Applicant NRIC: " + applicantId);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Applicant Age: " + applicantAge);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Applicant Marital Status: " + applicantMaritalStatus);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Project ID: " + projectId);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Application Date: " + applicationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Flat Type: " + flatType);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Application Fee: $" + String.format("%.2f", applicationFee));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Status: " + status);
                contentStream.endText();
            }

            // Create receipts directory if it doesn't exist
            File receiptsDir = new File("receipts");
            if (!receiptsDir.exists()) {
                receiptsDir.mkdir();
            }

            // Save the PDF
            String filename = "receipts/Receipt_" + receiptId + ".pdf";
            document.save(filename);
            System.out.println("Receipt generated successfully: " + filename);
        } catch (IOException e) {
            System.err.println("Error generating PDF receipt: " + e.getMessage());
        }
    }

    // Getters and setters
    public String getReceiptId() {
        return receiptId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public String getFlatType() {
        return flatType;
    }

    public double getApplicationFee() {
        return applicationFee;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Receipt ID: " + receiptId + "\n" +
               "Applicant Name: " + applicantName + "\n" +
               "Applicant NRIC: " + applicantId + "\n" +
               "Applicant Age: " + applicantAge + "\n" +
               "Applicant Marital Status: " + applicantMaritalStatus + "\n" +
               "Project ID: " + projectId + "\n" +
               "Application Date: " + applicationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
               "Flat Type: " + flatType + "\n" +
               "Application Fee: $" + String.format("%.2f", applicationFee) + "\n" +
               "Status: " + status;
    }
} 