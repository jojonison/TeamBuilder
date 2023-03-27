package tablesToClasses;

public class TryoutQualification {
    private int tryoutQualificationID;
    private int studentID;
    private String sportName;
    private String approvalStatus;

    public int getTryoutQualificationID() {
        return tryoutQualificationID;
    }

    public void setTryoutQualificationID(int tryoutQualificationID) {
        this.tryoutQualificationID = tryoutQualificationID;
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

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public TryoutQualification(int tryoutQualificationID, int studentID, String sportName, String approvalStatus) {
        this.tryoutQualificationID = tryoutQualificationID;
        this.studentID = studentID;
        this.sportName = sportName;
        this.approvalStatus = approvalStatus;
    }
}
