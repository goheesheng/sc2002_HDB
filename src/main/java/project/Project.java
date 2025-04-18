package project;

import user.HDBManager;
import user.HDBOfficer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a BTO housing project managed by HDB.
 * A project contains information about available flats, application periods,
 * and is managed by an HDB Manager.
 * 
 * @author SC2002 G Team
 * @version 1.0
 * @since 2025-03-16
 */

public class Project {
    private String projectId;
    private String projectName;
    private String neighborhood;
    private Map<FlatType, Integer> flatTypes;
    private Date applicationOpeningDate;
    private Date applicationClosingDate;
    private HDBManager managerInCharge;
    private int availableOfficerSlots;
    private boolean visibility;
    private List<HDBOfficer> officers;
    private List<Application> applications;
    private List<Enquiry> enquiries;

    /**
     * Creates a new Project with the specified details.
     * 
     * @param projectId The unique identifier for this project
     * @param projectName The name of this project
     * @param neighborhood The neighborhood where this project is located
     * @param flatTypes Map of flat types and their quantities
     * @param applicationOpeningDate The date when applications open
     * @param applicationClosingDate The date when applications close
     * @param managerInCharge The HDB Manager in charge of this project
     * @param availableOfficerSlots Number of HDB Officers that can handle this project
     */

    public Project(String projectId, String projectName, String neighborhood, 
                  Map<FlatType, Integer> flatTypes, Date applicationOpeningDate, 
                  Date applicationClosingDate, HDBManager managerInCharge, 
                  int availableOfficerSlots) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.flatTypes = flatTypes;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.managerInCharge = managerInCharge;
        this.availableOfficerSlots = availableOfficerSlots;
        this.visibility = true;
        this.officers = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    /**
     * Gets the number of remaining flats of a specific type.
     * 
     * @param flatType The type of flat to check
     * @return The number of remaining flats of the specified type
     */

    public int getRemainingFlats(FlatType flatType) {
        return flatTypes.getOrDefault(flatType, 0);
    }

    /**
     * Checks if a user is eligible to apply for this project based on age and marital status.
     * 
     * @param user The user to check eligibility for
     * @return true if the user is eligible, false otherwise
     */
    public boolean isEligibleForUser(user.User user) {
        
        if (user.getMaritalStatus().equalsIgnoreCase("SINGLE") && user.getAge() >= 35) {
            System.out.println("Eligible for TWO_ROOM");
            return getRemainingFlats(FlatType.TWO_ROOM) > 0;
        } else if (user.getMaritalStatus().equalsIgnoreCase("MARRIED") && user.getAge() >= 21) {
            System.out.println("Eligible for TWO_ROOM or THREE_ROOM");
            return getRemainingFlats(FlatType.TWO_ROOM) > 0 || getRemainingFlats(FlatType.THREE_ROOM) > 0;
        }
        return false;
    }

    /**
     * Checks if the current date is within the application period for this project.
     * 
     * @return true if current date is within application period, false otherwise
     */
    public boolean isWithinApplicationPeriod() {
        Date now = new Date();
        return now.after(applicationOpeningDate) && now.before(applicationClosingDate);
    }

    /**
     * Adds an enquiry to this project.
     * 
     * @param enquiry The enquiry to add
     */
    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    /**
     * Adds an application to this project.
     * 
     * @param application The application to add
     */
    public void addApplication(Application application) {
        applications.add(application);
    }

    /**
     * Updates the count of available flats for a specific flat type.
     * 
     * @param flatType The type of flat to update
     * @param newCount The new count of available flats
     * @throws IllegalArgumentException if the flat type is not found in the project
     */
    public void updateFlatCount(FlatType flatType, int newCount) {
        if (flatTypes.containsKey(flatType)) {
            flatTypes.put(flatType, newCount);
        } else {
            throw new IllegalArgumentException("Flat type not found in the project.");
        }
    }
    
    // Getters and setters
    /**
     * Gets a copy of the list of officers assigned to this project.
     * Returns a new ArrayList to prevent modification of the internal list.
     * 
     * @return A copy of the list of HDB Officers assigned to this project
     */
    public List<HDBOfficer> getOfficers() { 
        return new ArrayList<>(officers);
    }
    
    /**
     * Gets direct access to the internal officers list.
     * Used by HDBManager to manage officer assignments.
     * 
     * @return The internal list of officers assigned to this project
     */
    public List<HDBOfficer> getOfficersList() {
        return officers;
    }
    
    /**
     * Adds an officer to this project.
     * 
     * @param officer The officer to add
     * @return true if the officer was added successfully, false otherwise
     */
    public boolean addOfficer(HDBOfficer officer) {
        if (availableOfficerSlots > 0) {
            officers.add(officer);
            return true;
        }
        return false;
    }

    /**
     * Gets a copy of the list of applications submitted for this project.
     * Returns a new ArrayList to prevent modification of the internal list.
     * 
     * @return A copy of the list of applications for this project
     */
    public List<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    /**
     * Gets a copy of the list of enquiries submitted for this project.
     * Returns a new ArrayList to prevent modification of the internal list.
     * 
     * @return A copy of the list of enquiries for this project
     */
    public List<Enquiry> getEnquiries() {
        return new ArrayList<>(enquiries);
    }

    /**
     * Gets the unique identifier of this project.
     * 
     * @return The project ID
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Gets the neighborhood where this project is located.
     * 
     * @return The project's neighborhood
     */
    public String getneighborhood() {
        return neighborhood;
    }

    /**
     * Gets the number of available slots for HDB Officers in this project.
     * 
     * @return The number of available officer slots
     */
    public int getavailableOfficerSlots() {
        return availableOfficerSlots;
    }

    /**
     * Gets the name of this project.
     * 
     * @return The project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the HDB Manager in charge of this project.
     * 
     * @return The manager in charge
     */
    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    /**
     * Gets the Flat Types of this project.
     * 
     * @return The Flat Types
     */

    public Map<FlatType, Integer> getFlatTypes(){
        return flatTypes;
    }


    /**
     * Checks if this project is visible to applicants.
     * 
     * @return true if the project is visible, false otherwise
     */
    public boolean isVisible() {
        return visibility;
    }

    /**
     * Sets the visibility of this project.
     * 
     * @param visibility The new visibility
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
    
    /**
     * Gets the application opening date for this project.
     * 
     * @return The application opening date
     */
    public Date getApplicationOpeningDate() {
        return applicationOpeningDate;
    }
    
    /**
     * Gets the application closing date for this project.
     * 
     * @return The application closing date
     */
    public Date getApplicationClosingDate() {
        return applicationClosingDate;
    }

    /**
     * Sets the name of this project.
     * 
     * @param projectName The new project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Sets the neighborhood where this project is located.
     * 
     * @param neighborhood The new neighborhood
     */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * Sets the map of flat types and their quantities for this project.
     * 
     * @param flatTypes The new map of flat types and quantities
     */
    public void setFlatTypes(Map<FlatType, Integer> flatTypes) {
        this.flatTypes = flatTypes;
    }

    /**
     * Sets the date when applications for this project will open.
     * 
     * @param applicationOpeningDate The new application opening date
     */
    public void setApplicationOpeningDate(Date applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    /**
     * Sets the date when applications for this project will close.
     * 
     * @param applicationClosingDate The new application closing date
     */
    public void setApplicationClosingDate(Date applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    /**
     * Sets the number of available slots for HDB Officers in this project.
     * 
     * @param availableOfficerSlots The new number of available officer slots
     */
    public void setAvailableOfficerSlots(int availableOfficerSlots) {
        this.availableOfficerSlots = availableOfficerSlots;
    }

    public String toString() {
        StringBuilder flatTypesString = new StringBuilder();
        for (Map.Entry<FlatType, Integer> entry : flatTypes.entrySet()) {
            flatTypesString.append(entry.getKey()).append(": ").append(entry.getValue()).append(" flats\n");
        }
    
        return 
                "Project ID: " + projectId + "\n" +
                "Project Name: " + projectName + "\n" +
                "Neighborhood: " + neighborhood + "\n" +
                "Flat Types: \n" + flatTypesString.toString() + 
                "Application Opening Date: " + applicationOpeningDate + "\n" +
                "Application Closing Date: " + applicationClosingDate + "\n" +
                "Manager In Charge: " + managerInCharge.getName() + "\n" + 
                "Available Officer Slots: " + availableOfficerSlots + "\n" +
                "Visibility: " + (visibility ? "Visible" : "Not Visible" +
                "\n----------------------------------------\n");
    }
}
