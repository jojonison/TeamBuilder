package code;

import tables.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class JavaKafkaStudent {
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static ArrayList<Sport> sportsList;

    public static ArrayList<Application> applicationsList;

    public static int studentID;

    public static String emailAddress;

    public static void readAllApplications(){
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "--------------", "--------", "---------", "---------------", "----------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "Application ID", "Sport ID", "Tryout ID", "Approval Status", "Application Date");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "--------------", "--------", "---------", "---------------", "----------------");
        for (Application application: applicationsList){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
        }
    }

    public static void selectApplicationID(int applicationID) throws Exception {
        Application application = DataKafka.selectApplicationByApplicationID(applicationID);
        if (application != null){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. View Tryout Details");
            System.out.println("2. Cancel Application");
            System.out.println("3. Go Back to Main Menu");
            System.out.print("Choice: ");
            int applicationChoice = Integer.parseInt(bufferedReader.readLine());

            switch (applicationChoice) {
                case 1 -> {
                    TryoutDetails tryout = DataKafka.selectTryoutByTryoutID(application.getTryoutID());
                    System.out.printf("%-20s %-20s %-20s \n", "Schedule", "Location", "Coach ID");
                    System.out.printf("%-20s %-20s %-20s \n", tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
                }
                case 2 -> {
                    DataKafka.deleteApplication(applicationID);
                    DataKafka.deleteTryouts(application.getTryoutID());
                }
                case 3 -> mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortApplicationsByApprovalStatus(String approvalStatus) throws Exception {
//        ArrayList<Application> byApprovalStatus = DataKafka.findApplicationsByApprovalStatus(approvalStatus);
        ArrayList<Application> byApprovalStatus = DataKafka.findApplicationByApprovalStatusAndByID(approvalStatus, studentID);
        if (!byApprovalStatus.isEmpty()){
            for (Application application : byApprovalStatus) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
            }
            System.out.println("\nSelect Application ID: ");
            int applicationID = Integer.parseInt(bufferedReader.readLine());

            for (Application application : byApprovalStatus) {
                if (applicationID == application.getApplicationID()) {
                    selectApplicationID(applicationID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortApplicationsBySportName(String sportName) throws Exception {
        ArrayList<Application> bySportName = DataKafka.findApplicationsBySportName(sportName);
        if (!bySportName.isEmpty()){
            for (Application application : bySportName) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
            }
            System.out.println("\nSelect Application ID: ");
            int applicationID = Integer.parseInt(bufferedReader.readLine());

            for (Application application : bySportName) {
                if (applicationID == application.getApplicationID()) {
                    selectApplicationID(applicationID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void readAllSports(){
        System.out.printf("%-20s %-20s %-20s %-20s \n", "--------", "----------", "----------", "------------");
        System.out.printf("%-20s %-20s %-20s %-20s \n", "Sport ID", "Sport Name", "Sport Type", "Availability");
        System.out.printf("%-20s %-20s %-20s %-20s \n", "--------", "----------", "----------", "------------");
        for (Sport sport: sportsList){
            System.out.printf("%-20s %-20s %-20s %-20s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }
    }

    public static void readSportDetailsByDep(int sportID) throws SQLException {
        ArrayList<TryoutDetails> sportDetailsByDep = DataKafka.getDetailsOfSportByDepartment(sportID,DataKafka.getDepartmentOfStudent(studentID));
        if (sportDetailsByDep.isEmpty()) {
            System.out.println("Sorry, looks like there is no tryout for this sport at the moment...stay tuned!");
            System.out.println("Please restart the program");
            System.exit(1);
        } else {
            System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "--------", "----------", "----------", "------------", "--------");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "Tryout ID", "Sport ID", "Schedule", "Location", "coachid");
            System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "--------", "----------", "----------", "------------", "-------");
            for (TryoutDetails tryoutDetails : sportDetailsByDep) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s\n",
                        tryoutDetails.getTryoutID(),
                        tryoutDetails.getSportID(),
                        tryoutDetails.getSchedule(),
                        tryoutDetails.getLocation(),
                        tryoutDetails.getCoachID()
                );
            }
        }
    }

    /**
     * A student should not be able to apply if the coach has not set up a tryout for the sport
     * @param sportID
     * @throws Exception
     */
    public static void selectSportId(int sportID) throws Exception {
        Sport sport = DataKafka.selectSportBySportID(sportID);
        Student student = DataKafka.selectStudentByStudentID(studentID);
        TryoutDetails sportDetails = DataKafka.getDetailsOfSportBySportID(sportID);

        readSportDetailsByDep(sportID);

        // Work in Progress

        if (sport != null){
            System.out.printf("%-20s %-20s %-20s \n", "Sport details: ", sport.getSportName(), sport.getSportType());

            System.out.println("\nApply to this sport? (Y/N): ");
            String apply = bufferedReader.readLine();

            if (apply.equalsIgnoreCase("y")) {

                System.out.println("Enter tryout ID: ");
                int tryoutID = Integer.parseInt(bufferedReader.readLine());
                DataKafka.studentApply(studentID, tryoutID, sportID);
                DataKafka.createTryout(DataKafka.getApplicationByStudentID(studentID));
            } else {
                mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void findSportBySportName(String sportName) throws Exception {
        Sport sport = DataKafka.findSportBySportName(sportName);
        if (sport != null){
            System.out.printf("%-20s %-20s %-20s %-20s \n", "Sport details: ", sport.getSportID(), sport.getSportType(), sport.getAvailability());
            selectSportId(sport.getSportID());
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void main(String[] args) throws Exception {
        DataKafka.setConnection();
        JavaKafkaStudent student = new JavaKafkaStudent();
        student.logIn();
    }

    public void logIn() throws Exception {
        System.out.println("Welcome to TeamBuilder! Please login your account to continue. (Press Enter on new Line)");
        bufferedReader.readLine();

        System.out.println("Email Address: ");
        emailAddress = bufferedReader.readLine();

        System.out.println("Password: ");
        studentID = Integer.parseInt(bufferedReader.readLine());

        while (true) {
            Student student = DataKafka.logIn(emailAddress, studentID);
            if (student == null) {
                System.out.println("Please try again");
                System.exit(0);
            }
            mainMenu();
        }
    }

    public static void mainMenu() throws Exception{
        while (true) {
            applicationsList = DataKafka.getOwnApplications(studentID);
            sportsList = DataKafka.getSports();

            System.out.println("\nChoose what to do: ");
            System.out.println("1. Show Available Sports");
            System.out.println("2. View My Applications");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: sportsMenu();
                case 2: applicationsMenu();
                case 3: System.exit(0);
                default: System.out.println("Invalid input.");
            }
        }
    }

    public static void sportsMenu() throws Exception {
        while (true) {
            sportsList = DataKafka.getSports();

            System.out.println("\nChoose what to do: ");
            System.out.println("1. View all Sports");
            System.out.println("2. Find a Sport");
            System.out.println("3. Go Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    readAllSports();
                    System.out.println("\nSelect Sport ID: ");
                    int sportID = Integer.parseInt(bufferedReader.readLine());
                    selectSportId(sportID);
                }
                case 2 -> {
                    System.out.println("\nEnter Sport Name: ");
                    String sportName = bufferedReader.readLine();
                    findSportBySportName(sportName);
                }
                case 3 -> mainMenu();
                default -> System.out.println("Invalid input.");
            }
        }
    }

    public static void applicationsMenu() throws Exception{
        while (true) {
            applicationsList = DataKafka.getOwnApplications(studentID);

            System.out.println("\nView By: ");
            System.out.println("1. All Applications");
            System.out.println("2. Sport Name");
            System.out.println("3. Approval Status");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    readAllApplications();
                    System.out.println("\nSelect Application ID: ");
                    int applicationID = Integer.parseInt(bufferedReader.readLine());
                    selectApplicationID(applicationID);

                }
                case 2 -> {
                    System.out.println("\nEnter Sport Name: ");
                    String sportName = bufferedReader.readLine();
                    sortApplicationsBySportName(sportName);
                }
                case 3 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. Approved Applications");
                    System.out.println("b. Pending Applications");
                    System.out.println("c. Denied Applications");
                    String statusChoice = bufferedReader.readLine();

                    switch (statusChoice) {
                        case "a", "A": sortApplicationsByApprovalStatus("Approved");
                        case "b", "B": sortApplicationsByApprovalStatus("Pending");
                        case "c", "C": sortApplicationsByApprovalStatus("Denied");
                        default: System.out.println("Invalid input.");
                    }
                    System.out.println();
                }
                case 4 -> mainMenu();
                default -> System.out.println("Invalid input.");
            }
        }
    }
}
