package code;

import tables.Coach;
import tables.Department;
import tables.Sport;
import tables.TryoutDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JavaKafkaCoach {
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static ArrayList<Sport> sportsList;
    public static ArrayList<TryoutDetails> tryoutsList;
    public static ArrayList<TryoutDetails> allExistingTryoutsList;
    public static ArrayList<Department> departmentsList;

    public static int coachID;


    public static void readAllTryoutDetails() {
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "--------------", "--------", "---------------", "---------------", "----------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "Tryout ID", "Sport ID", "Schedule", "Location", "Coach ID");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "--------------", "--------", "---------------", "---------------", "----------------");
        for (TryoutDetails tryoutDetails : tryoutsList) {
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n",
                    tryoutDetails.getTryoutID(),
                    tryoutDetails.getSportID(),
                    tryoutDetails.getSchedule(),
                    tryoutDetails.getLocation(),
                    tryoutDetails.getCoachID()
                    );
        }
    }

    public static void selectTryoutID(int tryoutID) throws Exception {
        TryoutDetails tryoutDetails = DataKafka.selectTryoutByTryoutID(tryoutID);
        if (tryoutDetails != null) {
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n",
                    tryoutDetails.getTryoutID(),
                    tryoutDetails.getSportID(),
                    tryoutDetails.getSchedule(),
                    tryoutDetails.getLocation(),
                    tryoutDetails.getCoachID()
            );
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Delete Tryout");
            System.out.println("2. Go Back to Main Menu");
            System.out.print("Choice: ");
            int tryoutDetailChoice = Integer.parseInt(bufferedReader.readLine());

            switch (tryoutDetailChoice) {
                case 1 -> {
                    DataKafka.deleteTryouts(tryoutID);
                }
                case 2 -> {
                    mainMenu();
                }
            }
        }
    }

    public static void tryoutsMenu() throws Exception {
        tryoutsList = DataKafka.getCoachCreatedTryoutDetails(coachID);
        readAllTryoutDetails();
        System.out.println("Select Tryout ID: ");
        int tryoutID = Integer.parseInt(bufferedReader.readLine());
        selectTryoutID(tryoutID);

    }

    public static void createTryout() throws Exception {
        allExistingTryoutsList = DataKafka.getTryouts();
        int maxTryoutID = Integer.MIN_VALUE;
        for (TryoutDetails tryouts : allExistingTryoutsList) {
            if (tryouts.getTryoutID() > maxTryoutID) {
                maxTryoutID = tryouts.getTryoutID();
            }
        }

        System.out.println("Enter the tryout schedule: ");
        System.out.println("FORMAT: ");
        System.out.println("yyyy-mm-dd hh:mm:ss");
        System.out.println("\nExample: ");
        System.out.println("2023-02-02 16:30:00");
        String schedule = bufferedReader.readLine();

        System.out.println("Enter the tryout location: ");
        String location = bufferedReader.readLine();

        TryoutDetails tryoutDetail = new TryoutDetails (
                maxTryoutID + 1,
                DataKafka.getCoachSport(coachID),
                schedule,
                location,
                coachID
        );

        DataKafka.coachAddTryout(tryoutDetail);
        System.out.println("You have added a tryout");
    }

    public static void mainMenu() throws Exception {
        while (true) {

            System.out.println("\nChoose what to do: ");
            System.out.println("1. Show Created Tryouts");
            System.out.println("2. Create a Tryout");
            System.out.println("3. Exit");
            System.out.println("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> tryoutsMenu();
                case 2 -> createTryout();
                case 3 -> {
                    System.out.println("Come Again!");
                    System.exit(0);
                }
            }
        }
    }

    public static void logIn() throws Exception {
        System.out.println("Welcome to TeamBuilder! Please login your account to continue.");
        bufferedReader.readLine();

        System.out.println("Coach ID: ");
        coachID = Integer.parseInt(bufferedReader.readLine());

        while (true) {
            Coach coach = DataKafka.coachLogIn(coachID);
            if (coach != null) {
                mainMenu();
            }else {
                System.out.println("Account not found.");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DataKafka.setConnection();
        logIn();
    }
}
