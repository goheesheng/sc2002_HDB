package project;

import user.User;
import java.util.Date;

/**
 * Represents an enquiry about a BTO project.
 * Users can submit enquiries to get more information about projects.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */
public class Enquiry {
    private String enquiryId;
    private User user;
    private Project project;
    private String enquiryText;
    private Date dateSubmitted;
    private String reply;
    private Date replyDate;
    private User repliedBy;

    /**
     * Creates a new Enquiry with the specified details.
     * 
     * @param enquiryId The unique identifier for this enquiry
     * @param user The user who submitted this enquiry
     * @param project The project this enquiry is about
     * @param enquiryText The text content of the enquiry
     */
    public Enquiry(String enquiryId, User user, Project project, String enquiryText) {
        this.enquiryId = enquiryId;
        this.user = user;
        this.project = project;
        this.enquiryText = enquiryText;
        this.dateSubmitted = new Date();
    }

    /**
     * Updates the text content of this enquiry.
     * 
     * @param newText The new text content
     * @return true if the text was updated successfully
     */
    public boolean editText(String newText) {
        this.enquiryText = newText;
        return true;
    }

    /**
     * Adds a reply to this enquiry.
     * 
     * @param replyText The text content of the reply
     * @param replier The user who is replying
     * @return true if the reply was added successfully
     */
    public boolean addReply(String replyText, User replier) {
        this.reply = replyText;
        this.replyDate = new Date();
        this.repliedBy = replier;
        return true;
    }

    // Getters and setters
    /**
     * Gets the unique identifier of this enquiry.
     * 
     * @return The enquiry ID
     */
    public String getEnquiryId() {
        return enquiryId;
    }

    /**
     * Gets the user who submitted this enquiry.
     * 
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the project this enquiry is about.
     * 
     * @return The project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Gets the text content of this enquiry.
     * 
     * @return The enquiry text
     */
    public String getEnquiryText() {
        return enquiryText;
    }

    /**
     * Gets the date when this enquiry was submitted.
     * 
     * @return The submission date
     */
    public Date getDateSubmitted() {
        return dateSubmitted;
    }
    
    /**
     * Gets the reply to this enquiry, if any.
     * 
     * @return The reply text, or null if no reply exists
     */
    public String getReply() {
        return reply;
    }
    
    /**
     * Gets the date when a reply was added to this enquiry, if any.
     * 
     * @return The reply date, or null if no reply exists
     */
    public Date getReplyDate() {
        return replyDate;
    }
    
    /**
     * Gets the user who replied to this enquiry, if any.
     * 
     * @return The user who replied, or null if no reply exists
     */
    public User getRepliedBy() {
        return repliedBy;
    }
}