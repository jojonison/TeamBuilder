package code;

import tables.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.*;

public class JavaKafkaAdmin {
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static int applicationID;
    public static int appStudentID;
    public static int appSportID;
    public static int appTryoutID;
    public static String appApprovalStatus;
    public static ArrayList<Application> applicationsList;

    public static int coachID;
    public static String coachFirstName;
    public static String coachLastName;
    public static int coachSportID;
    public static String coachDepartmentKey;
    public static ArrayList<Coach> coachesList;

    public static int sportID;
    public static String sportName;
    public static String sportType;
    public static String availability;
    public static ArrayList<Sport> sportsList;

    public static int tryoutID;
    public static int tryoutSportID;
    public static DateFormat tryoutSchedule;
    public static String tryoutLocation;
    public static int tryoutCoachID;
    public static ArrayList<TryoutDetails> tryoutsList;

    public static void readAllApplications(){
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "--------------", "----------", "--------", "---------", "---------------");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "Application ID", "Student ID", "Sport ID", "Tryout ID", "Approval Status");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "--------------", "----------", "--------", "---------", "---------------");
        for (Application application: applicationsList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
        }
    }

    public static void readAllCoaches(){
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "--------", "----------", "---------", "--------", "--------------");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "Coach ID", "First Name", "Last Name", "Sport ID", "Department Key");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "--------", "----------", "---------", "--------", "--------------");
        for (Coach coach: coachesList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
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

    public static void readAllTryouts(){
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "---------", "--------", "--------", "--------", "--------");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "Tryout ID", "Sport ID", "Schedule", "Location", "Coach ID");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "---------", "--------", "--------", "--------", "--------");
        for (TryoutDetails tryout: tryoutsList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
        }
    }

    public static void createSport() throws Exception{
        System.out.println("Create Sport ID: ");
        sportID = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Create Sport Name: ");
        sportName = bufferedReader.readLine();
        System.out.println("Create Sport Type: ");
        sportType = bufferedReader.readLine();
        System.out.println("Create Availability: ");
        availability = bufferedReader.readLine();

        Sport sport = new Sport(sportID, sportName, sportType, availability);
        DataKafka.createSport(sport);
    }

    public static void updateSport(int sportID) throws IOException {
        Sport sports = DataKafka.findSportBySportID(sportID);
        if(sports !=null) {
            System.out.println("Update Sport Name for " + sportID + ": ");
            sportName = bufferedReader.readLine();
            System.out.println("Update Sport Type for " + sportID + ": ");
            sportType = bufferedReader.readLine();
            System.out.println("Update Availability for " + sportID + ": ");
            availability = bufferedReader.readLine();

            Sport sport = new Sport(sportID, sportName, sportType, availability);
            DataKafka.updateSport(sport, sportID);
        } else {
            System.out.println("Sport ID not found.");
        }
    }

    public static void deleteSport(int sportID) throws IOException {
        Sport sport = DataKafka.findSportBySportID(sportID);
        if(sport !=null){
            System.out.printf("%-20s %-20s %-20s %-15s \n", "Sport details: ", sport.getSportName(), sport.getSportType(), sport.getAvailability());

            System.out.println();
            System.out.println("Delete this sport? (y/n): ");
            String delete = bufferedReader.readLine();

            if (delete.equalsIgnoreCase("y")){
                DataKafka.deleteSport(sportID);
            }
        }else {
            System.out.println("Sport ID not found.");
        }
    }

    public static void searchBySportId(int sportID) {
        Sport sport = DataKafka.findSportBySportID(sportID);
        if(sport !=null){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }else {
            System.out.println("Sport ID not Found.");
        }
    }

    public static void mainMenu() throws Exception{
        while (true){
            applicationsList = DataKafka.getApplications();
            coachesList = DataKafka.getCoaches();
            sportsList = DataKafka.getSports();
            tryoutsList = DataKafka.getTryouts();

            System.out.println("Choose what to do: ");
            System.out.println("1. View applications");
            System.out.println("2. View Coaches");
            System.out.println("3. View Sports");
            System.out.println("4. View TryoutDetails");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    readAllApplications();
                    System.out.println();
                }
                case 2 -> {
                    readAllCoaches();
                    System.out.println();
                }
                case 3 -> {
                    readAllSports();
                    System.out.println();
                }
                case 4 -> {
                    readAllTryouts();
                    System.out.println();
                }
                case 5 -> {
                    DataKafka.closeConnection();
                    System.exit(0);
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void sportsMenu() throws Exception{
        while (true){
            sportsList = DataKafka.getSports();

            System.out.println("Choose what to do: ");
            System.out.println("1. Create a sport");
            System.out.println("3. Update a sport");
            System.out.println("4. Delete a sport");
            System.out.println("5. Find a sport");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createSport();
                    System.out.println();
                }
                case 2 -> {
                    System.out.println("Enter Sport ID:");
                    sportID = Integer.parseInt(bufferedReader.readLine());
                    updateSport(sportID);
                    System.out.println();
                }
                case 3 -> {
                    System.out.println("Enter Sport ID:");
                    sportID = Integer.parseInt(bufferedReader.readLine());
                    deleteSport(sportID);
                    System.out.println();
                }
                case 4 -> {
                    System.out.println("Enter Sport ID: ");
                    sportID = Integer.parseInt(bufferedReader.readLine());
                    searchBySportId(sportID);
                    System.out.println();
                }
                case 5 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DataKafka.setConnection();
        mainMenu();
    }
}
