package tables;

public class Student {
    private int studentID;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String departmentKey;

    public Student(int studentID, String firstName, String lastName, String emailAddress, String departmentKey) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.departmentKey = departmentKey;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey;
    }
}
