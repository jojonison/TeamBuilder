package tables;

public class Tryouts {
    public int tryoutNo;
    public int tryoutId;
    public int applicationId;
    public String comments;

    public Tryouts(int tryoutNo, int tryoutId, int applicationId) {
        this.tryoutNo = tryoutNo;
        this.tryoutId = tryoutId;
        this.applicationId = applicationId;
    }

    public Tryouts(int tryoutNo, int tryoutId, int applicationId, String comments){
        this.tryoutNo = tryoutNo;
        this.tryoutId = tryoutId;
        this.applicationId = applicationId;
        this.comments = comments;
    }

    public int getTryoutNo(){
        return this.tryoutNo;
    }

    public int getTryoutId(){
        return this.tryoutId;
    }

    public int getApplicationId(){
        return this.applicationId;
    }

    public String getComments(){
        return this.comments;
    }

    public void setTryoutNo(int tryoutNo){
        this.tryoutNo = tryoutNo;
    }

    public void setTryoutId(int tryoutId){
        this.tryoutId = tryoutId;
    }

    public void setApplicationId(int applicationId){
        this.applicationId = applicationId;
    }

    public void setComments(String comments){
        this.comments = comments;
    }
}
