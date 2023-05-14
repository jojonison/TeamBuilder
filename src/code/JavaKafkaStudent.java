package code;

import tables.Application;
import tables.Sport;
import tables.Student;
import tables.TryoutDetails;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class JavaKafkaStudent {
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static ArrayList<Sport> sportsList;

    public static ArrayList<TryoutDetails> tryoutsList;

    public static ArrayList<Application> applicationsList;

    public static ArrayList<Student> studentsList;

    public static int studentID;

    public static void readAllApplications(){
        System.out.printf("%-20s %-20s %-15s %-15s \n", "--------------", "--------", "---------", "---------------");
        System.out.printf("%-20s %-20s %-15s %-15s \n", "Application ID", "Sport ID", "Tryout ID", "Approval Status");
        System.out.printf("%-20s %-20s %-15s %-15s \n", "--------------", "--------", "---------", "---------------");
        for (Application application: applicationsList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
        }
    }

    public static void selectApplicationID(int applicationID) throws Exception {
        Application application = DataKafka.selectApplicationByApplicationID(applicationID);
        if (application != null){
            System.out.printf("%-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
            System.out.println("\nCancel Application? (Y/N): ");
            String cancel = bufferedReader.readLine();

            if (cancel.equalsIgnoreCase("y")) {
                DataKafka.deleteApplication(applicationID);
            } else {
                mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortApplicationsByApprovalStatus(String approvalStatus) throws Exception {
        ArrayList<Application> byApprovalStatus = DataKafka.findApplicationsByApprovalStatus(approvalStatus);
        if (!byApprovalStatus.isEmpty()){
            for (Application application : byApprovalStatus) {
                System.out.printf("%-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
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
                System.out.printf("%-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
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
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "Sport ID", "Sport Name", "Sport Type", "Availability");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        for (Sport sport: sportsList){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }
    }

    public static void selectSportId(int sportID) throws Exception {
        Sport sport = DataKafka.selectSportBySportID(sportID);
        if (sport != null){
            System.out.printf("%-20s %-20s %-15s \n", "Sport details: ", sport.getSportName(), sport.getSportType());
            //createApplication()
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void findSportBySportName(String sportName) throws Exception {
        Sport sport = DataKafka.findSportBySportName(sportName);
        if (sport != null){
            System.out.printf("%-20s %-20s %-20s %-15s \n", "Sport details: ", sport.getSportID(), sport.getSportType(), sport.getAvailability());
            System.out.println("\nApply to this sport? (Y/N): ");
            String apply = bufferedReader.readLine();

            if (apply.equalsIgnoreCase("y")) {
                //createApplication()
            } else {
                mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void readAllTryouts(){
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "---------", "--------", "--------", "--------", "--------");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "Tryout ID", "Sport ID", "Schedule", "Location", "Coach ID");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "---------", "--------", "--------", "--------", "--------");
        for (TryoutDetails tryout: tryoutsList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
        }
    }

    public static void sortTryoutsBySportName(String sportName) throws Exception {
        ArrayList<TryoutDetails> bySportName = DataKafka.findTryoutsBySportName(sportName);
        if (!bySportName.isEmpty()){
            for (TryoutDetails tryout : bySportName) {
                System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
            }
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
        while (true) {
            studentsList = DataKafka.getStudents();

            System.out.println("Email Address: ");
            String emailAddress = bufferedReader.readLine();
            System.out.println("Password: ");
            studentID = Integer.parseInt(bufferedReader.readLine());

            mainMenu();
        }
    }

    public static void mainMenu() throws Exception{
        while (true) {
            applicationsList = DataKafka.getOwnApplications(studentID);
            sportsList = DataKafka.getSports();

            System.out.println("Choose what to do: ");
            System.out.println("1. Show Available Sports");
            System.out.println("2. View My Applications");
            System.out.println("3. Exit");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: readAllSports();
                case 2: readAllApplications();
                case 3: System.exit(0);
                default: System.out.println("Invalid input.");
            }
        }
    }

    public void applyAvailableSports() throws Exception {
        Random random = new Random();
        System.out.println("Which sport do you wish to apply");

        readAllSports();
        int choice = Integer.parseInt(bufferedReader.readLine());
        int counter = 1;
        sportsList = DataKafka.getSports();
        tryoutsList = DataKafka.getTryouts();
        for (TryoutDetails tryoutDetails : tryoutsList){
            if (choice == tryoutDetails.getSportID()){
                //After the log in is done get the password (which is the student id)
                //Get the sportsid and tryoutid
                //Afterwards, create an application object
                //Get the newly created application object and push it into the database

            }
            counter++;
        }
    }

}
