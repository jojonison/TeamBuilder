import java.sql.*;
import java.util.ArrayList;

public class DataKafka {
    private static Connection connection;
    private DataKafka(){
    }

    public static void setConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kafka","root","");
        }catch (Exception e){
            System.out.println("Data Connection Failed.");
        }
    }

    public static ArrayList<Application> getApplication() throws  Exception{
        ArrayList<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM application ORDER BY applicationid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Application application = new Application(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            applications.add(application);
        }
        resultSet.close();
        return applications;
    }

    public static void addApplication(Application application){
        String query = "INSERT INTO application(applicationid,studentid,sportname.tryoutid) VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, application.getApplicationId());
            preparedStatement.setString(2, application.getStudentId());
            preparedStatement.setString(3, application.getSportName());
            preparedStatement.setString(4, application.getTryOutId());
            preparedStatement.execute();
            System.out.println("New Application Added");
        } catch (SQLException e) {
            System.out.println("Failed to update Application");
        }
    }

    public static void updateApplication(Application application, String num){
        String query = "UPDATE application SET applicationid=?, studentid=?, sportname=?, tryoutid=? WHERE studentid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, application.getApplicationId());
            preparedStatement.setString(2, application.getStudentId());
            preparedStatement.setString(3, application.getSportName());
            preparedStatement.setString(4, application.getTryOutId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Failed to update Application");
        }
    }

    public static Application findApplicationByStudentId(String studentId){
        Application application = null;
        String query = "SELECT * FROM application WHERE studentid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                application = new Application(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not Find Student");
        }
        return application;
    }

    public static Application findApplication(String applicationId){
        Application application = null;
        ArrayList<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM application WHERE applicationid LIKE ? ORDER BY applicationid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, applicationId+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                application = new Application(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not Find Application");
        }
        return application;
    }

    public static ArrayList<Application> getApplicationByTryOutId(String tryoutId) throws Exception{
        ArrayList<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM application WHERE tryoutid=?";
        Application application = null;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, tryoutId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                application = new Application(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                applications.add(application);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not Find Application");
        }
        return application;
    }

    public static void closeConnection() throws Exception{
        if(connection != null){
            connection.close();
        }
    }
}
