package tables_to_class;

public class Admin {
    private int adminID;
    private String firstName;
    private String lastName;

    public Admin(int adminID, String firstName, String lastName) {
        this.adminID = adminID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
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
}
