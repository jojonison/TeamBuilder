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
            System.out.println("Could not find application " + applicationID + ".");
        }
        return application;
    }

    public static ArrayList<Application> findApplicationsByApprovalStatus(String approvalStatus) {
        ArrayList<Application> applications = new ArrayList<>();
        String query = "SELECT * FROM application WHERE approvalstatus = ? ORDER BY applicationid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, approvalStatus);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5));
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
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5));
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
                Application application = new Application(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5));
                applications.add(application);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find '" + sportName  + "' applications.");
        }
        return applications;
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
            preparedStatement.execute();
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

    public static Coach findCoachByCoachID(int coachID){
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
            System.out.println("Could not find sport " + sportID + ".");
        }
        return sport;
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

    public static Student findStudentByStudentID(int studentID){
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

    public static void updateTryouts(TryoutDetails tryouts, int tryoutID){
        String query = "UPDATE tryoutdetails SET tryoutid=?, sportid=?, schedule=?, location=?, coachid=? WHERE tryoutid=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, tryouts.getTryoutID());
            preparedStatement.setInt(2, tryouts.getSportID());
            preparedStatement.setString(3, tryouts.getSchedule());
            preparedStatement.setString(4, tryouts.getLocation());
            preparedStatement.setInt(5, tryouts.getCoachID());
            preparedStatement.setInt(5, tryoutID);
            preparedStatement.execute();
            System.out.println("Tryout " + tryoutID + " updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update tryout " + tryoutID + ".");
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

    public static TryoutDetails findTryoutByTryoutID(int tryoutID){
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

    public static ArrayList<TryoutDetails> findTryoutsByCoachFullName(String firstname, String lastname){
        ArrayList<TryoutDetails> tryouts = new ArrayList<>();
        String query = "SELECT tryoutdetails.tryoutid, tryoutdetails.sportid, tryoutdetails.schedule, tryoutdetails.location FROM tryoutdetails " +
                "NATURAL JOIN coach WHERE coach.firstname = ? AND coach.lastname = ? " +
                "ORDER BY tryoutid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                TryoutDetails tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                tryouts.add(tryoutDetail);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryouts under coach " + firstname + " " + lastname + ".");
        }
        return tryouts;
    }

    public static ArrayList<TryoutDetails> findTryoutsByCoachFirstName(String firstname){
        ArrayList<TryoutDetails> tryouts = new ArrayList<>();
        String query = "SELECT tryoutdetails.tryoutid, tryoutdetails.sportid, tryoutdetails.schedule, tryoutdetails.location FROM tryoutdetails " +
                "NATURAL JOIN coach WHERE coach.firstname = ? " +
                "ORDER BY tryoutid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, firstname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                TryoutDetails tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                tryouts.add(tryoutDetail);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryouts under coach " + firstname + ".");
        }
        return tryouts;
    }

    public static ArrayList<TryoutDetails> findTryoutsByCoachLastName(String lastname){
        ArrayList<TryoutDetails> tryouts = new ArrayList<>();
        String query = "SELECT tryoutdetails.tryoutid, tryoutdetails.sportid, tryoutdetails.schedule, tryoutdetails.location FROM tryoutdetails " +
                "NATURAL JOIN coach WHERE coach.lastname = ? " +
                "ORDER BY tryoutid";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();
            while (resultSet.next()){
                TryoutDetails tryoutDetail = new TryoutDetails(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5));
                tryouts.add(tryoutDetail);
            }
            resultSet.close();
        }catch (Exception e){
            System.out.println("Could not find tryouts under coach " + lastname + ".");
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
    public static void closeConnection() throws Exception{
        if(connection != null){
            connection.close();
            System.exit(0);
        }
    }
}
