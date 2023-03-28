package code;

import tables.Sport;

import java.sql.*;
import java.util.ArrayList;

public class DataKafka {
    private static Connection connection;
    private DataKafka(){
    }

    public static void setConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kafka?useSSL=false","root","");
            //as long as wampserver is on, and kafka schema is imported in mysql workbench, this works ???
        }catch (Exception e){
            System.out.println("Data Connection Failed.");
        }
    }

    public static ArrayList<Sport> getSport() throws  Exception{
        ArrayList<Sport> sports = new ArrayList<Sport>();
        String query = "SELECT * FROM application ORDER BY applicationid";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            Sport sport = new Sport(resultSet.getString(1),resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
            sports.add(sport);
        }
        resultSet.close();
        return sports;
    }

    public static void createSport(Sport sport){
        String query = "INSERT INTO sport(sportid,sportname,sporttype,availability) VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sport.getSportID());
            preparedStatement.setString(2, sport.getSportName());
            preparedStatement.setString(3, sport.getSportType());
            preparedStatement.setString(4, sport.getAvailability());
            preparedStatement.execute();
            System.out.println("New sport added.");
        } catch (SQLException e) {
            System.out.println("Failed to add sport.");
        }
    }

    public static void updateSport(Sport sport, String sportID){
        String query = "UPDATE sport SET sportid=?, sportname=?, sporttype=?, availability=? WHERE sportid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sport.getSportID());
            preparedStatement.setString(2, sport.getSportName());
            preparedStatement.setString(3, sport.getSportType());
            preparedStatement.setString(4, sport.getAvailability());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Failed to update sport.");
        }
    }

    public static ArrayList<Sport> getSportBySportId(String sportId) throws Exception{
        ArrayList<Sport> sports = new ArrayList<>();
        String query = "SELECT * FROM sport WHERE sportid=?";
        Sport sport = null;
        try{

            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                sport = new Sport(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                sports.add(sport);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not Find code.Application");
        }
        return sports;
    }

    public static Sport findSportBySportID(String sportID){
        Sport sport = null;
        String query = "SELECT * FROM application WHERE studentid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                sport = new Sport(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not Find Student");
        }
        return sport;
    }


    public static void closeConnection() throws Exception{
        if(connection != null){
            connection.close();
        }
    }
}
