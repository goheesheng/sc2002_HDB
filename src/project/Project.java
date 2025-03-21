package project;

import user.HDBManager;
import user.HDBOfficer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        this.visibility = false;
        this.officers = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    public int getRemainingFlats(FlatType flatType) {
        return flatTypes.getOrDefault(flatType, 0);
    }

    public boolean isEligibleForUser(user.User user) {
        if (user.getMaritalStatus().equals("SINGLE") && user.getAge() >= 35) {
            return getRemainingFlats(FlatType.TWO_ROOM) > 0;
        } else if (user.getMaritalStatus().equals("MARRIED") && user.getAge() >= 21) {
            return getRemainingFlats(FlatType.TWO_ROOM) > 0 || 
                   getRemainingFlats(FlatType.THREE_ROOM) > 0;
        }
        return false;
    }

    public boolean isWithinApplicationPeriod() {
        Date now = new Date();
        return now.after(applicationOpeningDate) && now.before(applicationClosingDate);
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public void addApplication(Application application) {
        applications.add(application);
    }

    // Getters and setters
    public List<HDBOfficer> getOfficers() { // need check 
        return new ArrayList<>(officers);
    }
    
    public String getProjectId() {
        return projectId;
    }
    public String getneighborhood() {
        return neighborhood;
    }
    public int getavailableOfficerSlots() {
        return availableOfficerSlots;
    }
    public String getProjectName() {
        return projectName;
    }

    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}
