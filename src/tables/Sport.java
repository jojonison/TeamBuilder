package tables;

public class Sport {
    private String sportID;
    private String sportName;
    private String sportType;
    private String availability;

    public Sport(String sportID, String sportName, String sportType, String availability) {
        this.sportID = sportID;
        this.sportName = sportName;
        this.sportType = sportType;
        this.availability = availability;
    }

    public String getSportID() {
        return sportID;
    }

    public void setSportID(String sportID) {
        this.sportID = sportID;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
