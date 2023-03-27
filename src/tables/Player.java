package tables;

public class Player {
    private int playerID;
    private int studentID;
    private String sportName;
    private int tryoutQualificationID;
    private int rank;

    public Player(int playerID, int studentID, String sportName, int tryoutQualificationID, int rank) {
        this.playerID = playerID;
        this.studentID = studentID;
        this.sportName = sportName;
        this.tryoutQualificationID = tryoutQualificationID;
        this.rank = rank;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
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

    public int getTryoutQualificationID() {
        return tryoutQualificationID;
    }

    public void setTryoutQualificationID(int tryoutQualificationID) {
        this.tryoutQualificationID = tryoutQualificationID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
