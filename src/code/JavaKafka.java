package code;

import tables.Sport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class JavaKafka {
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    public static int sportID;
    public static String sportName = null;
    public static String sportType = null;
    public static String availability = null;
    public static ArrayList<Sport> list = new ArrayList<>();

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

    public static void readAllSports(){
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "Sport ID", "Sport Name", "Sport Type", "Availability");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        for (Sport sport: list){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }
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

    public static void searchBySportId() throws Exception {
        System.out.println("Enter Sport ID: ");
        sportID = Integer.parseInt(bufferedReader.readLine());
        Sport sport = DataKafka.findSportBySportID(sportID);

        if(sport !=null){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }else {
            System.out.println("Sport ID not Found.");
        }
    }

    public static void menu() throws Exception{
        while (true){
            list = DataKafka.getSport();

            System.out.println("Choose what to do: ");
            System.out.println("1. Create a sport");
            System.out.println("2. Read all sports");
            System.out.println("3. Update a sport");
            System.out.println("4. Delete a sport");
            System.out.println("5. Find a sport");
            System.out.println("6. Exit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createSport();
                    System.out.println();
                }
                case 2 -> {
                    readAllSports();
                    System.out.println();
                }
                case 3 -> {
                    System.out.println("Enter Sport ID:");
                    sportID = Integer.parseInt(bufferedReader.readLine());
                    updateSport(sportID);
                    System.out.println();
                }
                case 4 -> {
                    System.out.println("Enter Sport ID:");
                    sportID = Integer.parseInt(bufferedReader.readLine());
                    deleteSport(sportID);
                    System.out.println();
                }
                case 5 -> {
                    searchBySportId();
                    System.out.println();
                }
                case 6 -> {
                    DataKafka.closeConnection();
                    System.exit(0);
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DataKafka.setConnection();
        menu();
    }
}
