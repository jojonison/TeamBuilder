package code;

import tables.*;

import java.sql.*;
import java.util.ArrayList;

public class DataKafka {
    private static Connection connection;
    private DataKafka(){
    }

    public static void setConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kafka?useSSL=false","root","");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Data Connection Failed.");
        }
    }

    public static ArrayList<Application> getApplications() throws  Exception{
        ArrayList<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM application ORDER BY applicationid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Application application = new Application(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getString(5)
            );
            applications.add(application);
        }
        resultSet.close();
        return applications;
    }

    public static Application findApplicationByApplicationID(int applicationID){
        Application application = null;
        String query = "SELECT * FROM application WHERE applicationid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, applicationID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find sport " + applicationID + " .");
        }
        return application;
    }

    public static void changeApplicationStatus(int applicationID, String approvalStatus) {
        String query = "UPDATE application SET approvalstatus=? WHERE applicationid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, approvalStatus);
            preparedStatement.setInt(2, applicationID);
            preparedStatement.execute();
            System.out.println("Application now " + approvalStatus + ".");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update application.");
        }
    }

    public static ArrayList<Coach> getCoaches() throws  Exception{
        ArrayList<Coach> coaches = new ArrayList<Coach>();
        String query = "SELECT * FROM coach ORDER BY coachid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Coach coach = new Coach(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getString(5)
            );
            coaches.add(coach);
        }
        resultSet.close();
        return coaches;
    }

    public static ArrayList<Department> getDepartments() throws  Exception{
        ArrayList<Department> departments = new ArrayList<Department>();
        String query = "SELECT * FROM department ORDER BY departmentkey";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Department department = new Department(
                    resultSet.getString(1),
                    resultSet.getString(2)
            );
            departments.add(department);
        }
        resultSet.close();
        return departments;
    }

    public static ArrayList<Sport> getSports() throws  Exception{
        ArrayList<Sport> sports = new ArrayList<Sport>();
        String query = "SELECT * FROM sport ORDER BY sportid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Sport sport = new Sport(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            sports.add(sport);
        }
        resultSet.close();
        return sports;
    }

    public static ArrayList<Student> getStudents() throws  Exception{
        ArrayList<Student> students = new ArrayList<Student>();
        String query = "SELECT * FROM student ORDER BY studentid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Student student = new Student(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            students.add(student);
        }
        resultSet.close();
        return students;
    }

    public static ArrayList<TryoutDetails> getTryouts() throws  Exception{
        ArrayList<TryoutDetails> tryouts = new ArrayList<TryoutDetails>();
        String query = "SELECT * FROM tryoutdetails ORDER BY tryoutid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            TryoutDetails tryout = new TryoutDetails(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
            );
            tryouts.add(tryout);
        }
        resultSet.close();
        return tryouts;
    }

    public static void createSport(Sport sport){
        String query = "INSERT INTO sport(sportid,sportname,sporttype,availability) VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, sport.getSportID());
            preparedStatement.setString(2, sport.getSportName());
            preparedStatement.setString(3, sport.getSportType());
            preparedStatement.setString(4, sport.getAvailability());
            preparedStatement.execute();
            System.out.println("Sport with the following details is added: \n" + sport.toString());

        } catch (SQLException e) {
            System.out.println("Failed to add sport.");
        }
    }

    public static void updateSport(Sport sport, int sportID){
        String query = "UPDATE sport SET sportid=?, sportname=?, sporttype=?, availability=? WHERE sportid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, sport.getSportID());
            preparedStatement.setString(2, sport.getSportName());
            preparedStatement.setString(3, sport.getSportType());
            preparedStatement.setString(4, sport.getAvailability());
            preparedStatement.setInt(5,sportID);
            preparedStatement.execute();
            System.out.println("Sport " + sportID + " updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update sport.");
        }
    }

    public static void deleteSport(int sportID){
        String query = "DELETE from sport where sportid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, sportID);
            preparedStatement.execute();
            System.out.println("Sport " + sportID + " deleted.");
        } catch (SQLException e) {
            System.out.println("Failed to delete sport.");
        }
    }

    public static Sport findSportBySportID(int sportID){
        Sport sport = null;
        String query = "SELECT * FROM sport WHERE sportid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, sportID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                sport = new Sport(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find sport " + sportID + " .");
        }
        return sport;
    }


    public static void closeConnection() throws Exception{
        if(connection != null){
            connection.close();
        }
    }
}
