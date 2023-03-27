package code;

import java.util.ArrayList;

public class Application extends ArrayList<Application> {
    private String applicationId;
    private String studentId;
    private String sportName;
    private String tryOutId;

    public Application() {

    }

    public Application(String appId,String stuId, String spoName, String tryId){
        this.applicationId = appId;
        this.studentId = stuId;
        this.sportName = spoName;
        this.tryOutId = tryId;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getStudentId(){
        return this.studentId;
    }

    public String getSportName(){
        return this.sportName;
    }

    public String getTryOutId(){
        return this.tryOutId;
    }
}
