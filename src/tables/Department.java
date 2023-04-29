package tables;

public class Department {
    private String departmentKey;
    private String departmentName;

    public Department(String departmentKey, String departmentName) {
        this.departmentKey = departmentKey;
        this.departmentName = departmentName;
    }

    public String getDepartmentKey() {
        return departmentKey;
    }

    public void setDepartmentKey(String departmentKey) {
        this.departmentKey = departmentKey;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
