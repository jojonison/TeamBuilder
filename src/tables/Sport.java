package tables;

public class Sport {
    private int sportID;
    private String sportName;
    private String sportType;
    private String availability;

    public Sport(int sportID, String sportName, String sportType, String availability) {
        this.sportID = sportID;
        this.sportName = sportName;
        this.sportType = sportType;
        this.availability = availability;
    }

    public int getSportID() {
        return sportID;
    }

    public void setSportID(int sportID) {
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

    public String toString() {
        return (sportID + "\t\t" + sportName + "\t\t" +  sportType + "\t\t" +  availability);
    }
}
