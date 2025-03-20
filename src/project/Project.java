package project;

public class Project {
    private String projectName;
    private String neighborhood;
    private int availableUnits2Room;
    private int availableUnits3Room;
    private String applicationOpeningDate;
    private String applicationClosingDate;
    private boolean visibility;
    private int availableHDBOfficerSlots;

    public Project(String projectName, String neighborhood, int availableUnits2Room, 
                   int availableUnits3Room, String applicationOpeningDate, 
                   String applicationClosingDate, boolean visibility, 
                   int availableHDBOfficerSlots) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.availableUnits2Room = availableUnits2Room;
        this.availableUnits3Room = availableUnits3Room;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.visibility = visibility;
        this.availableHDBOfficerSlots = availableHDBOfficerSlots;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getAvailableUnits2Room() {
        return availableUnits2Room;
    }

    public void setAvailableUnits2Room(int availableUnits2Room) {
        this.availableUnits2Room = availableUnits2Room;
    }

    public int getAvailableUnits3Room() {
        return availableUnits3Room;
    }

    public void setAvailableUnits3Room(int availableUnits3Room) {
        this.availableUnits3Room = availableUnits3Room;
    }

    public String getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public void setApplicationOpeningDate(String applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    public String getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(String applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getAvailableHDBOfficerSlots() {
        return availableHDBOfficerSlots;
    }

    public void setAvailableHDBOfficerSlots(int availableHDBOfficerSlots) {
        this.availableHDBOfficerSlots = availableHDBOfficerSlots;
    }

    public String viewDetails() {
        return "Project: " + projectName + " in " + neighborhood + "\n" +
               "Available 2-room flats: " + availableUnits2Room + "\n" +
               "Available 3-room flats: " + availableUnits3Room;
    }
}
