package project;

import user.User;
import java.util.Date;

public class Enquiry {
    private String enquiryId;
    private User user;
    private Project project;
    private String enquiryText;
    private Date dateSubmitted;
    private String reply;
    private Date replyDate;
    private User repliedBy;

    public Enquiry(String enquiryId, User user, Project project, String enquiryText) {
        this.enquiryId = enquiryId;
        this.user = user;
        this.project = project;
        this.enquiryText = enquiryText;
        this.dateSubmitted = new Date();
    }

    public boolean editText(String newText) {
        this.enquiryText = newText;
        return true;
    }

    public boolean addReply(String replyText, User replier) {
        this.reply = replyText;
        this.replyDate = new Date();
        this.repliedBy = replier;
        return true;
    }

    // Getters and setters
    public String getEnquiryId() {
        return enquiryId;
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }

    public String getEnquiryText() {
        return enquiryText;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public String getReply() {
        return reply;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public User getRepliedBy() {
        return repliedBy;
    }
}