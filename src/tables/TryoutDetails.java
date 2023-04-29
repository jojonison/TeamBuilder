package tables;

import java.text.DateFormat;

public class TryoutDetails {
    private int tryoutID;
    private int sportID;
    private String schedule;
    private String location;
    private int coachID;

    public TryoutDetails(int tryoutID, int sportID, String schedule, String location, int coachID) {
        this.tryoutID = tryoutID;
        this.sportID = sportID;
        this.schedule = schedule;
        this.location = location;
        this.coachID = coachID;
    }

    public int getTryoutID() {
        return tryoutID;
    }

    public void setTryoutID(int tryoutID) {
        this.tryoutID = tryoutID;
    }

    public int getSportID() {return sportID;}

    public void setSportID(int sportID) {
        this.sportID = sportID;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
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
}
