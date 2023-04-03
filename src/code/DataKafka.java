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
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/kafka?useSSL=false","root","");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Data Connection Failed.");
        }
    }

    public static ArrayList<Sport> getSport() throws  Exception{
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
