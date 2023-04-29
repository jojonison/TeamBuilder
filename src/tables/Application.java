package tables;

public class Application {
    private int applicationID;
    private int studentID;
    private int sportID;
    private int tryoutID;
    private String approvalStatus;

    public Application(int applicationID, int studentID, int sportID, int tryoutID, String approvalStatus) {
        this.applicationID = applicationID;
        this.studentID = studentID;
        this.sportID = sportID;
        this.tryoutID = tryoutID;
        this.approvalStatus = approvalStatus;
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

    public int getSportID() {return sportID;}

    public void setSportID(int sportID) {
        this.sportID = sportID;
    }

    public int getTryoutID() {
        return tryoutID;
    }

    public void setTryoutID(int tryoutID) {
        this.tryoutID = tryoutID;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {this.approvalStatus = approvalStatus;}
}
