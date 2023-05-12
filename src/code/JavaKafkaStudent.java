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

    public void mainMenu() throws Exception{
        while (true) {
            applicationsList = DataKafka.getOwnApplications(studentID);
            sportsList = DataKafka.getAvailableSports();

            System.out.println("Choose what to do: ");
            System.out.println("1. Show Available Sports");
            System.out.println("2. View My Applications");
            System.out.println("3. Exit");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: showAvailableSports();
                case 2: viewApplication();
                case 3: System.exit(0);
                default: System.out.println("Invalid input.");
            }
        }
    }

    public void showAvailableSports() throws Exception {
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "Sport ID", "Sport Name", "Sport Type", "Availability");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        for (Sport sport: sportsList){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }

        //apply by selecting a sport id
    }

    public void applyAvailableSports() throws Exception {
        Random random = new Random();
        System.out.println("Which sport do you wish to apply");

        showAvailableSports();
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
    public void viewApplication(){
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------------", "---------", "-----", "---------------");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "Application ID", "Tryout ID", "Sport", "Approval Status");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------------", "---------", "-----", "---------------");
        for (Application application: applicationsList){
            System.out.printf("%-20s %-20s %-20s %-15s \n", application.getApplicationID(), application.getTryoutID(), DataKafka.findSportNameBySportID(application.getSportID()), application.getApprovalStatus());
        }
    }

}
