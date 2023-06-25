package tables;

public class tryout {
    int applicationID;
    int tryoutID;
    String comments;

    public tryout(int applicationID, int tryoutID, String comments) {
        this.applicationID = applicationID;
        this.tryoutID = tryoutID;
        this.comments = comments;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getTryoutID() {
        return tryoutID;
    }

    public void setTryoutID(int tryoutID) {
        this.tryoutID = tryoutID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
