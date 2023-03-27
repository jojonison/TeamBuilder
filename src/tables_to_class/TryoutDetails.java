package tables_to_class;

import java.text.DateFormat;

public class TryoutDetails {
    private int tryoutID;
    private String sportName;
    private DateFormat schedule;
    private String location;
    private int coachID;

    public int getTryoutID() {
        return tryoutID;
    }

    public void setTryoutID(int tryoutID) {
        this.tryoutID = tryoutID;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public DateFormat getSchedule() {
        return schedule;
    }

    public void setSchedule(DateFormat schedule) {
        this.schedule = schedule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public TryoutDetails(int tryoutID, String sportName, DateFormat schedule, String location, int coachID) {
        this.tryoutID = tryoutID;
        this.sportName = sportName;
        this.schedule = schedule;
        this.location = location;
        this.coachID = coachID;
    }
}
