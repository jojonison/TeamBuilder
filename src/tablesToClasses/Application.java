package tablesToClasses;

public class Application {
    private int applicationID;
    private int studentID;
    private String sportName;
    private int tryoutID;

    public Application(int applicationID, int studentID, String sportName, int tryoutID) {
        this.applicationID = applicationID;
        this.studentID = studentID;
        this.sportName = sportName;
        this.tryoutID = tryoutID;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getTryoutID() {
        return tryoutID;
    }

    public void setTryoutID(int tryoutID) {
        this.tryoutID = tryoutID;
    }
}
