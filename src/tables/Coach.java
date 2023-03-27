package tables;

public class Coach {
    private int coachID;
    private String firstName;
    private String lastName;
    private String department;
    private String sportName;

    public Coach(int coachID, String firstName, String lastName, String department, String sportName) {
        this.coachID = coachID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.sportName = sportName;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
}
