package code;

import tables.Sport;

import java.util.*;

public class JavaKafka {
    public static Scanner scanner = new Scanner(System.in);
    public static String sportID = null;
    public static String sportName = null;
    public static String sportType = null;
    public static String availability = null;
    public static ArrayList<Sport> list = new ArrayList<>();

    public static void createSport() throws Exception{
        System.out.println("Create Sport ID: ");
        sportID = scanner.next();
        System.out.println("Create Sport Name: ");
        sportName = scanner.next();
        System.out.println("Create Sport Type: ");
        sportType = scanner.next();
        System.out.println("Create Availability: ");
        availability = scanner.next();

        Sport sport = new Sport(sportID, sportName, sportType, availability);
        DataKafka.createSport(sport);
    }

    public static void readAllSports(){
        for (Sport sport: list){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }
    }

    public static void updateSport(String sportID){
        System.out.println("Update Sport Name for " + sportID + " :");
        sportName = scanner.next();
        System.out.println("Update Sport Type for " + sportID + " :");
        sportType = scanner.next();
        System.out.println("Update Availability for " + sportID + " :");
        availability = scanner.next();

        Sport sport = new Sport(sportID,sportName,sportType,availability);
        DataKafka.updateSport(sport, sportID);
    }

    public static void searchBySportId() throws Exception {
        System.out.println("Enter Sport ID: ");
        sportID = scanner.next();
        Sport sport = DataKafka.findSportBySportID(sportID);

        if(sport !=null){
            System.out.printf("%-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());

            System.out.println();
            System.out.println("Update Sport? (y/n): ");
            String update = scanner.next();

            if (update.equalsIgnoreCase("y")){
                updateSport(sportID);
            }
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
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();

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
                    sportID = scanner.next();
                    updateSport(sportID);
                    System.out.println();
                }
                case 4 -> {
                    // TO DO
                }
                case 5 -> {
                    searchBySportId();
                    System.out.println();
                }
                case 6 -> {
                    System.exit(0);
                    DataKafka.closeConnection();
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
