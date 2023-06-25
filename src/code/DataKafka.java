package code;

import tables.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public static ArrayList<Tryouts> allTryOuts() throws Exception{
        ArrayList<Tryouts> tryouts = new ArrayList<Tryouts>();
        String query = "SELECT * FROM tryouts";
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            Tryouts tryout = new Tryouts(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getInt(3),
                    resultSet.getString(4)
            );
            tryouts.add(tryout);
        }
        resultSet.close();
        return tryouts;
    }

    public static Student logIn(String emailAdd, int password) throws Exception {
        Student student = null;
        try {
            String query = "SELECT * FROM student WHERE emailaddress = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, emailAdd);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int pw = resultSet.getInt("studentid");
                if (pw != password) {
                    System.out.println("Password is incorrect.");
                } else {
                    student = new Student(
                            resultSet.getInt("studentid"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("emailaddress"),
                            resultSet.getString("departmentkey")
                    );
                }
            }else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("Account not found.");
        }
        return student;
    }

    public static Coach coachLogIn(int coachID) throws Exception {
        Coach coach = null;
        try {
            String query = "select * from coach where coachID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, coachID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                coach = new Coach(
                        resultSet.getInt("coachid"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getInt("sportid"),
                        resultSet.getString("departmentkey")
                );
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("Account not found.");
        }
        return coach;
    }

    public static Application getApplicationByStudentID(int studentID) throws Exception{

        String query = "SELECT * FROM application WHERE studentid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(2) == studentID){
                    System.out.println("Found one");
                    return new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
                }
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("No applications found.");
        }
        System.out.println("Did not find one");
        return null;
    }

    public static ArrayList<Application> getApplications() throws Exception{
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
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            applications.add(application);
        }
        resultSet.close();
        return applications;
    }

    public static ArrayList<Application> getOwnApplications(int studentID) throws Exception {
        ArrayList<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM application WHERE studentid = ? ORDER BY applicationid";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
                applications.add(application);
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("No applications found.");
        }
        return applications;
    }

    public static Application selectApplicationByApplicationID(int applicationID){
        Application application = null;
        String query = "SELECT * FROM application WHERE applicationid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, applicationID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find application " + applicationID + ".");
        }
        return application;
    }

    public static Tryouts selectTryoutByTryoutNo(int tryoutNo){
        Tryouts tryout = null;
        String query = "SELECT * FROM tryouts WHERE tryoutno = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryoutNo);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                tryout = new Tryouts(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4)
                );
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryoutNo " + tryoutNo + ".");
        }
        return tryout;
    }

    public static ArrayList<Application> findApplicationsByApprovalStatus(String approvalStatus) {
        ArrayList<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM application WHERE approvalstatus = ? ORDER BY applicationid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, approvalStatus);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
                applications.add(application);
            }
            resultSet.close();
        } catch (Exception e){
            System.out.println("Could not find '" + approvalStatus  + "' applications.");
        }
        return applications;
    }

    public static ArrayList<Application> findApplicationsByDepartmentKey(String departmentKey){
        ArrayList<Application> applications = new ArrayList<>();
        String query = "SELECT application.applicationid, application.studentid, application.sportid, application.tryoutid, application.approvalstatus " +
                "FROM application " +
                "JOIN student ON application.studentid = student.studentid " +
                "NATURAL JOIN department WHERE department.departmentkey = ? " +
                "ORDER BY applicationid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, departmentKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
                applications.add(application);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find applications under " + departmentKey + " department.");
        }
        return applications;
    }

    public static ArrayList<Application> findApplicationsBySportName(String sportName){
        ArrayList<Application> applications = new ArrayList<>();
        String query = "SELECT application.applicationid, application.studentid, application.sportid, application.tryoutid, application.approvalstatus " +
                "FROM application " +
                "NATURAL JOIN sport WHERE sport.sportname = ? " +
                "ORDER BY applicationid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6));
                applications.add(application);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find '" + sportName  + "' applications.");
        }
        return applications;
    }

    public static int getCoachIDBySportID(int sportID) {
        try {
            String query = "SELECT coachid FROM coach WHERE sportid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coachid");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    public static String getScheduleBySportID(int sportID) {
        try {
            String query = "SELECT schedule FROM tryoutdetails WHERE sportid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("schedule"));
                return resultSet.getString("schedule");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String getLocationBySportID(int sportID) {
        try {
            String query = "SELECT location FROM tryoutdetails WHERE sportid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sportID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("location");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "No location yet.";
    }

    public static void studentApply(int studentID, int tryoutID, int sportID) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        try {
            String appIDQuery = "SELECT MAX(applicationid) FROM application";
            Statement appIDStatement = connection.createStatement();
            ResultSet appIDResultSet = appIDStatement.executeQuery(appIDQuery);
            int applicationID = 0;
            if (appIDResultSet.next()){
                applicationID = appIDResultSet.getInt(1);
            }
            int newAppID = applicationID + 1;

            String applicationQuery = "insert into application(applicationid, studentid, sportid, tryoutid, approvalstatus, applicationdate) values(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(applicationQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, newAppID);
            preparedStatement.setInt(2, studentID);
            preparedStatement.setInt(3, sportID);
            preparedStatement.setInt(4, tryoutID);
            preparedStatement.setString(5, "Pending");
            preparedStatement.setString(6, formattedDate);
            preparedStatement.execute();
            System.out.println("You have applied for sport: " + sportID);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * make this so it needs a tryout detail before applying:
     * a coach needs to put up a tryout detail first.
     * so if there is nothing, then nothing.
     * @param studentID
     * @param sportID
     * @param approvalStatus
     * @param applicationDate
     */

    public static void createApplication(int studentID, int sportID, String approvalStatus, String applicationDate){
        try {
            String appIDQuery = "SELECT MAX(applicationid) FROM application";
            Statement appIDStatement = connection.createStatement();
            ResultSet appIDResultSet = appIDStatement.executeQuery(appIDQuery);
            int applicationID = 0;
            if (appIDResultSet.next()){
                applicationID = appIDResultSet.getInt(1);
            }
            int newAppID = applicationID + 1;

            String tryoutIDQuery = "SELECT MAX(tryoutid) FROM tryoutdetails";
            Statement tryoutIDStatement = connection.createStatement();
            ResultSet tryoutIDResultSet = tryoutIDStatement.executeQuery(tryoutIDQuery);
            int tryoutID = 0;
            if (tryoutIDResultSet.next()){
                tryoutID = tryoutIDResultSet.getInt(1);
            }
            int newTryoutID = tryoutID + 1;

            String tryoutQuery = "INSERT INTO tryoutdetails(tryoutid,sportid,schedule,location,coachid) VALUES(?,?,?,?,?)";
            PreparedStatement tryoutPreparedStatement = connection.prepareStatement(tryoutQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            tryoutPreparedStatement.setInt(1, newTryoutID);
            tryoutPreparedStatement.setInt(2, sportID);
            tryoutPreparedStatement.setString(3, getScheduleBySportID(sportID));
            tryoutPreparedStatement.setString(4, getLocationBySportID(sportID));
            tryoutPreparedStatement.setInt(5, getCoachIDBySportID(sportID));
            tryoutPreparedStatement.execute();

            String appQuery = "INSERT INTO application(applicationid,studentid,sportid,tryoutid,approvalstatus,applicationdate) VALUES(?,?,?,?,?,?)";
            PreparedStatement appPreparedStatement = connection.prepareStatement(appQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            appPreparedStatement.setInt(1, newAppID);
            appPreparedStatement.setInt(2, studentID);
            appPreparedStatement.setInt(3, sportID);
            appPreparedStatement.setInt(4, newTryoutID);
            appPreparedStatement.setString(5, approvalStatus);
            appPreparedStatement.setString(6, applicationDate);
            appPreparedStatement.execute();
            System.out.println("Successfully applied.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to apply.");
        }
    }

    public static void createTryout(Application application){
        try {
            String tryoutQuery = "INSERT INTO tryout(tryoutid,applicationid,comments) VALUES(?,?,?)";
            PreparedStatement tryoutPreparedStatement = connection.prepareStatement(tryoutQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            tryoutPreparedStatement.setInt(1, application.getTryoutID());
            tryoutPreparedStatement.setInt(2, application.getApplicationID());
            tryoutPreparedStatement.setString(3, "Pending");
            tryoutPreparedStatement.execute();
            System.out.println("Tryout Added");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to apply tryouts.");
        }
    }


    public static void updateApplicationStatus(int applicationID, String approvalStatus) {
        String query = "UPDATE application SET approvalstatus=? WHERE applicationid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, approvalStatus);
            preparedStatement.setInt(2, applicationID);
            preparedStatement.execute();
            System.out.println("Application now " + approvalStatus + ".");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update application " + applicationID + ".");
        }
    }

    public static void deleteApplication(int applicationID){
        String query = "DELETE from application where applicationid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, applicationID);
            preparedStatement.execute();
            System.out.println("Application " + applicationID + " removed.");
        } catch (SQLException e) {
            System.out.println("Failed to remove application " + applicationID + ".");
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

    public static Coach selectCoachByCoachID(int coachID){
        Coach coach = null;
        String query = "SELECT * FROM coach WHERE coachid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, coachID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                coach = new Coach(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find coach " + coachID + ".");
        }
        return coach;
    }

    public static ArrayList<Coach> findCoachByCoachName(String firstname, String lastname){
        ArrayList<Coach> coaches = new ArrayList<>();
        String query = "SELECT * FROM coach " +
                "WHERE coach.firstname = ? OR coach.lastname = ? " +
                "ORDER BY coachid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Coach coach = new Coach(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5));
                coaches.add(coach);            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not coach named " + firstname + " " + lastname + ".");
        }
        return coaches;
    }

    public static ArrayList<Coach> findCoachesByDepartmentKey(String departmentKey){
        ArrayList<Coach> coaches = new ArrayList<>();
        String query = "SELECT coach.coachid, coach.firstname, coach.lastname, coach.sportid, coach.departmentkey FROM coach " +
                "NATURAL JOIN department WHERE department.departmentkey = ? " +
                "ORDER BY coachid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, departmentKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Coach coach = new Coach(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5));
                coaches.add(coach);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find coaches under " + departmentKey + " department.");
        }
        return coaches;
    }

    public static ArrayList<Coach> findCoachesBySportName(String sportName){
        ArrayList<Coach> coaches = new ArrayList<>();
        String query = "SELECT coach.coachid, coach.firstname, coach.lastname, coach.sportid, coach.departmentkey FROM coach " +
                "NATURAL JOIN sport WHERE sport.sportname = ? " +
                "ORDER BY coachid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Coach coach = new Coach(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getString(5));
                coaches.add(coach);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find '" + sportName  + "' coaches.");
        }
        return coaches;
    }

    public static void createCoach(Coach coach){
        String query = "INSERT INTO coach(coachid,firstname,lastname,sportid,departmentkey) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, coach.getCoachID());
            preparedStatement.setString(2, coach.getFirstName());
            preparedStatement.setString(3, coach.getLastName());
            preparedStatement.setInt(4, coach.getSportID());
            preparedStatement.setString(5, coach.getDepartmentKey());
            preparedStatement.execute();
            System.out.println("Coach with the following details is added: \n" + coach);

        } catch (SQLException e) {
            System.out.println("Failed to add coach " + coach.getCoachID() + ".");
        }
    }



    public static void updateCoach(Coach coach, int coachID){
        String query = "UPDATE coach SET coachid=?, firstname=?, lastname=?, sportid=?, departmentkey WHERE coachid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, coach.getCoachID());
            preparedStatement.setString(2, coach.getFirstName());
            preparedStatement.setString(3, coach.getLastName());
            preparedStatement.setInt(4,coach.getSportID());
            preparedStatement.setString(5, coach.getDepartmentKey());
            preparedStatement.setInt(5, coachID);
            preparedStatement.execute(query);
            System.out.println("Coach " + coachID + " updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update coach" + coachID + ".");
        }
    }

    public static void deleteCoach(int coachID){
        String query = "DELETE from coach where coachid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, coachID);
            preparedStatement.execute();
            System.out.println("Coach " + coachID + " removed.");
        } catch (SQLException e) {
            System.out.println("Failed to remove coach " + coachID + ".");
        }
    }

    public static ArrayList<Department> getDepartments() throws Exception{
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

    public static Department findDepartmentByDepartmentKey(String departmentKey) {
        Department department = null;
        String query = "SELECT * FROM department WHERE departmentkey = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, departmentKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                department = new Department(resultSet.getString(1), resultSet.getString(2));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find department " + departmentKey + ".");
        }
        return department;
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

    public static Sport selectSportBySportID(int sportID){
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
            System.out.println("Could not find sport " + sportID + ".");
        }
        return sport;
    }

    public static Sport findSportBySportName(String sportName){
        Sport sport = null;
        String query = "SELECT sportname FROM sport WHERE sportname = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                sport = new Sport(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find " + sportName + " sport.");
        }
        return sport;
    }

    public static ArrayList<Sport> findSportsBySportType(String sportType){
        ArrayList<Sport> sports = new ArrayList<>();
        String query = "SELECT sport.sportid, sport.sportname, sport.availability FROM sport " +
                "WHERE sport.sporttype = ? " +
                "ORDER BY sportid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportType);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Sport sport = new Sport(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                sports.add(sport);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find '" + sportType + "' sports.");
        }
        return sports;
    }

    public static ArrayList<Sport> findSportsByAvailability(String availability){
        ArrayList<Sport> sports = new ArrayList<>();
        String query = "SELECT sport.sportid, sport.sportname, sport.sporttype FROM sport " +
                "WHERE sport.availability = ? " +
                "ORDER BY sportid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, availability);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Sport sport = new Sport(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                sports.add(sport);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find '" + availability + "' sports.");
        }
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
            System.out.println("Sport with the following details is added: \n" + sport);

        } catch (SQLException e) {
            System.out.println("Failed to add sport " + sport.getSportID() + ".");
        }
    }

    public static void createTryouts(TryoutDetails tryoutDetails){
        String query = "INSERT INTO tryoutdetails(tryoutid,sportid,schedule,location,coachid) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryoutDetails.getTryoutID());
            preparedStatement.setInt(2, tryoutDetails.getSportID());
            preparedStatement.setString(3, tryoutDetails.getSchedule());
            preparedStatement.setString(4, tryoutDetails.getLocation());
            preparedStatement.setInt(5, tryoutDetails.getCoachID());
            preparedStatement.execute();
            System.out.println("Tryout Details with the following details is added: \n" + tryoutDetails);

        } catch (SQLException e) {
            System.out.println("Failed to add Tryout Details " + tryoutDetails.getSportID() + ".");
        }
    }

    public static void createTryout(TryoutDetails tryoutDetails){
        String query = "INSERT INTO tryoutdetails(tryoutid,sportid,schedule,location,coachid) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryoutDetails.getTryoutID());
            preparedStatement.setInt(2, tryoutDetails.getSportID());
            preparedStatement.setString(3, tryoutDetails.getSchedule());
            preparedStatement.setString(4, tryoutDetails.getLocation());
            preparedStatement.setInt(5, tryoutDetails.getCoachID());
            preparedStatement.execute();
            System.out.println("Tryout Details with the following details is added: \n" + tryoutDetails);

        } catch (SQLException e) {
            System.out.println("Failed to add Tryout Details " + tryoutDetails.getSportID() + ".");
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
            System.out.println("Failed to update sport " + sportID + ".");
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
            System.out.println("Failed to delete sport " + sportID + ".");
        }
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

    public static Student selectStudentByStudentID(int studentID){
        Student student = null;
        String query = "SELECT * FROM student WHERE studentid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                student = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find student " + studentID + " .");
        }
        return student;
    }

    public static ArrayList<Student> findStudentByStudentName(String firstname, String lastname){
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student " +
                "WHERE student.firstname = ? OR student.lastname = ? " +
                "ORDER BY studentid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Student student = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                students.add(student);        }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not student named " + firstname + " " + lastname + ".");
        }
        return students;
    }

    public static ArrayList<Student> findStudentsByDepartmentKey(String departmentKey){
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT student.studentid, student.firstname, student.lastname, student.emailaddress, student.departmentkey FROM student " +
                "NATURAL JOIN department WHERE department.departmentkey = ? " +
                "ORDER BY studentid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, departmentKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                Student student = new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                students.add(student);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find students under " + departmentKey + " department.");
        }
        return students;
    }

    public static void createStudent(Student student){
        String query = "INSERT INTO student(studentid,firstname,lastname,emailaddress,departmentkey) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, student.getStudentID());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.setString(4, student.getEmailAddress());
            preparedStatement.setString(5, student.getDepartmentKey());
            preparedStatement.execute();
            System.out.println("Student with the following details is added: \n" + student);

        } catch (SQLException e) {
            System.out.println("Failed to add student " + student.getStudentID() + ".");
        }
    }

    public static void updateStudent(Student student, int studentID){
        String query = "UPDATE student SET studentid=?, firstname=?, lastname=?, emailaddress=?, departmentkey=? WHERE studentid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, student.getStudentID());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.setString(4, student.getEmailAddress());
            preparedStatement.setString(5, student.getDepartmentKey());
            preparedStatement.setInt(5, studentID);
            preparedStatement.execute();
            System.out.println("Student " + studentID + " updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update student " + studentID + ".");
        }
    }

    public static void deleteStudent(int studentID){
        String query = "DELETE from student where studentid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, studentID);
            preparedStatement.execute();
            System.out.println("Student " + studentID + " removed.");
        } catch (SQLException e) {
            System.out.println("Failed to remove student " + studentID + ".");
        }
    }


    public static ArrayList<TryoutDetails> getCoachCreatedTryoutDetails(int coachid) throws Exception {
        ArrayList<TryoutDetails> tryoutDetails = new ArrayList<>();
        String query = "select * from tryoutdetails where coachid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, coachid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               TryoutDetails tryout = new TryoutDetails(
                       resultSet.getInt(1),
                       resultSet.getInt(2),
                       resultSet.getString(3),
                       resultSet.getString(4),
                       resultSet.getInt(5)
               );
               tryoutDetails.add(tryout);
            }
            resultSet.close();
        }catch (Exception e) {
            System.out.println("An error occurred");
        }
        return tryoutDetails;
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

    public static TryoutDetails selectTryoutByTryoutID(int tryoutID){
        TryoutDetails tryoutDetail = null;
        String query = "SELECT * FROM tryoutdetails WHERE tryoutid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryoutID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryout " + tryoutID + ".");
        }
        return tryoutDetail;
    }

    public static TryoutDetails getDetailsOfSportBySportID(int sportID) {
        TryoutDetails tryoutDetail = null;
        String query = "SELECT * FROM tryoutdetails LEFT OUTER JOIN sport ON tryoutdetails.sportid = sport.sportid WHERE tryoutdetails.sportid = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, sportID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryout details for sport " + sportID + ".");
            System.out.println("Why not have a look at other sport tryouts?");
            System.out.println("You might be interested with: ");
//            e.printStackTrace();
        }
        return tryoutDetail;
    }

    public static ArrayList<TryoutDetails> findTryoutsByDepartmentKey(String departmentKey){
        ArrayList<TryoutDetails> tryouts = new ArrayList<>();
        String query = "SELECT tryoutdetails.tryoutid, tryoutdetails.sportid, tryoutdetails.schedule, tryoutdetails.location, tryoutdetals.coachid " +
                "FROM tryoutdetails " +
                "JOIN coach ON tryoutdetails.coachid = coach.coachid " +
                "NATURAL JOIN department WHERE department.departmentkey = ? " +
                "ORDER BY tryoutid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, departmentKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                TryoutDetails tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                tryouts.add(tryoutDetail);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find applications under " + departmentKey + " department.");
        }
        return tryouts;
    }

    public static ArrayList<TryoutDetails> findTryoutsBySportName(String sportName){
        ArrayList<TryoutDetails> tryouts = new ArrayList<>();
        String query = "SELECT tryoutdetails.tryoutid, tryoutdetails.sportid, tryoutdetails.schedule, tryoutdetails.location, tryoutdetails.coachid FROM tryoutdetails " +
                "NATURAL JOIN sport WHERE sport.sportname = ? " +
                "ORDER BY tryoutid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, sportName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                TryoutDetails tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                tryouts.add(tryoutDetail);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryouts under " + sportName + ".");
        }
        return tryouts;
    }

    public static void updateTryouts(TryoutDetails tryouts, int tryoutID, String schedule, String location){
        String query = "UPDATE tryoutdetails SET tryoutid=?, sportid=?, schedule=?, location=?, coachid=? WHERE tryoutid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryouts.getTryoutID());
            preparedStatement.setInt(2, tryouts.getSportID());
            preparedStatement.setString(3, schedule);
            preparedStatement.setString(4, location);
            preparedStatement.setInt(5, tryouts.getCoachID());
            preparedStatement.setInt(6, tryoutID);
            preparedStatement.execute();
            System.out.println("Tryout " + tryoutID + " updated.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update tryout " + tryoutID + ".");
        }
    }

    public static void updateTryout(Tryouts tryout, int tryoutNo, String comments){
        String query = "UPDATE tryouts SET tryoutno=?, tryoutid=?, applicationid=?, comments=? WHERE tryoutno=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryout.getTryoutNo());
            preparedStatement.setInt(2, tryout.getTryoutId());
            preparedStatement.setInt(3, tryout.getApplicationId());
            preparedStatement.setString(4, comments);
            preparedStatement.setInt(5, tryoutNo);
            preparedStatement.execute();
            System.out.println("Tryout " + tryoutNo + " updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update tryout " + tryoutNo + ".");
        }
    }

    public static void deleteTryout(int tryoutNo){
        String query = "DELETE from tryout where tryoutid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryoutNo);
            preparedStatement.execute();
            System.out.println("Tryout " + tryoutNo + " removed.");
        } catch (SQLException e) {
            System.out.println("--------------------");
        }
    }

    public static void deleteTryouts(int tryoutID){
        String query = "DELETE from tryoutdetails where tryoutid = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryoutID);
            preparedStatement.execute();
            System.out.println("Tryout " + tryoutID + " removed.");
        } catch (SQLException e) {
            System.out.println("Failed to remove tryout " + tryoutID + ".");
        }
    }

    public static void closeConnection() throws Exception{
        if(connection != null){
            connection.close();
            System.exit(0);
        }
    }

    public static int getCoachSport(int coachID) throws Exception {
        String query = "select sportid from coach where coachid = ?";
        int sportID = 0;

        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, coachID);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Coach coach = new Coach(
                    resultSet.getInt(1),
                    resultSet.getString(1),
                    resultSet.getString(1),
                    resultSet.getInt(1),
                    resultSet.getString(1)
            );
            sportID = coach.getSportID();
        }
        resultSet.close();

        return sportID;
    }


    public static void coachAddTryout(TryoutDetails tryoutDetails) throws SQLException {
        String tryoutQuery = "INSERT INTO tryoutdetails(tryoutid,sportid,schedule,location,coachid) VALUES(?,?,?,?,?)";
        PreparedStatement tryoutPreparedStatement = connection.prepareStatement(tryoutQuery,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        tryoutPreparedStatement.setInt(1, tryoutDetails.getTryoutID());
        tryoutPreparedStatement.setInt(2, tryoutDetails.getSportID());
        tryoutPreparedStatement.setString(3, tryoutDetails.getSchedule());
        tryoutPreparedStatement.setString(4, tryoutDetails.getLocation());
        tryoutPreparedStatement.setInt(5, tryoutDetails.getCoachID());
        tryoutPreparedStatement.execute();
    }

    /**
     * SELECT *
     * FROM TRYOUTDETAILS
     * WHERE CoachID IN (
     *     SELECT CoachID
     *     FROM COACH
     *     WHERE departmentkey = 'SEA'
     * );
     */
    public static ArrayList<TryoutDetails> getDetailsOfSportByDepartment(int sportID, String departmentKey) throws SQLException {
        ArrayList<TryoutDetails> tryouts = new ArrayList<>();
        String query = "select * from tryoutdetails where tryoutdetails.sportid = ? and coachid in (select coachid from coach where departmentkey = ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, sportID);
        preparedStatement.setString(2, departmentKey);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.beforeFirst();

        while (resultSet.next()) {
            TryoutDetails tryoutDetails = new TryoutDetails(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5)
            );
            tryouts.add(tryoutDetails);
        }
        resultSet.close();
        return tryouts;
    }

    public static String getDepartmentOfStudent(int studentID) throws SQLException {
        String query = "select departmentkey from student where studentid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, studentID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }else {
            return null;
        }
    }

    public static ArrayList<Application> coachViewApplications(int coachID) throws Exception {
        ArrayList<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM application WHERE tryoutid IN (SELECT tryoutid FROM tryoutdetails WHERE coachid = ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        preparedStatement.setInt(1, coachID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.beforeFirst();

        while (resultSet.next()) {
            Application application = new Application(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            applications.add(application);
        }
        return applications;
    }

}
