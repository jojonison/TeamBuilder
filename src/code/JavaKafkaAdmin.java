package code;

import tables.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.*;

public class JavaKafkaAdmin {
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static ArrayList<Application> applicationsList;
    public static ArrayList<Coach> coachesList;
    public static ArrayList<Department> departmentsList;
    public static ArrayList<Sport> sportsList;
    public static ArrayList<Student> studentsList;
    public static ArrayList<TryoutDetails> tryoutsList;

    public static void readAllApplications(){
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "--------------", "----------", "--------", "---------", "---------------");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "Application ID", "Student ID", "Sport ID", "Tryout ID", "Approval Status");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "--------------", "----------", "--------", "---------", "---------------");
        for (Application application: applicationsList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
        }
    }

    public static void searchByApplicationID(int applicationID) throws Exception {
        Application application = DataKafka.findApplicationByApplicationID(applicationID);
        if (application != null){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());

            if (application.getApprovalStatus().equals("Pending")) {
                System.out.println("\nChoose what to do: ");
                System.out.println("1. Accept Application");
                System.out.println("2. Deny Application");
                System.out.println("3. Back to Main Menu");
                System.out.print("Choice: ");
                int choice = Integer.parseInt(bufferedReader.readLine());

                switch (choice) {
                    case 1: DataKafka.updateApplicationStatus(applicationID, "Accepted");
                    case 2: DataKafka.updateApplicationStatus(applicationID, "Denied");
                    case 3: mainMenu();
                }
            } else {
                System.out.println("\nChange Approval Status? (Y/N): ");
                String change = bufferedReader.readLine();

                if (change.equalsIgnoreCase("y")) {
                    System.out.println("Enter New Approval Status: ");
                    String approvalStatus = bufferedReader.readLine();
                    DataKafka.updateApplicationStatus(applicationID, approvalStatus);
                }
            }
        } else {
            System.out.println("Application ID not Found.");
        }
    }

    public static void sortApplicationsByApprovalStatus(String approvalStatus) throws Exception {
        ArrayList<Application> byApprovalStatus = DataKafka.findApplicationsByApprovalStatus(approvalStatus);
        if (!byApprovalStatus.isEmpty()){
            for (Application application : byApprovalStatus) {
                System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
            }
            System.out.println("\nSelect Application ID: ");
            int applicationID = Integer.parseInt(bufferedReader.readLine());

            for (Application application : byApprovalStatus) {
                if (applicationID == application.getApplicationID()) {
                    searchByApplicationID(applicationID);
                    return;
                }
            }
            System.out.println("Invalid Application ID.");
        } else {
            System.out.println("No applications found with approval status: " + approvalStatus);
        }
    }

    public static void sortApplicationsByDepartmentKey(String departmentKey) throws Exception {
        ArrayList<Application> byDepartmentKey = DataKafka.findApplicationsByDepartmentKey(departmentKey);
        if (!byDepartmentKey.isEmpty()){
            for (Application application : byDepartmentKey) {
                System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
            }
            System.out.println("\nSelect Application ID: ");
            int applicationID = Integer.parseInt(bufferedReader.readLine());

            for (Application application : byDepartmentKey) {
                if (applicationID == application.getApplicationID()) {
                    searchByApplicationID(applicationID);
                    return;
                }
            }
            System.out.println("Invalid Application ID.");
        } else {
            System.out.println("No applications found with department key: " + departmentKey);
        }
    }

    public static void sortApplicationsBySportName(String sportName) throws Exception {
        ArrayList<Application> bySportName = DataKafka.findApplicationsBySportName(sportName);
        if (!bySportName.isEmpty()){
            for (Application application : bySportName) {
                System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus());
            }
            System.out.println("\nSelect Application ID: ");
            int applicationID = Integer.parseInt(bufferedReader.readLine());

            for (Application application : bySportName) {
                if (applicationID == application.getApplicationID()) {
                    searchByApplicationID(applicationID);
                    return;
                }
            }
            System.out.println("Invalid Application ID.");
        } else {
            System.out.println("No applications found with sport name: " + sportName);
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

    public static void createCoach() throws Exception {
        while (true) {
            sportsList = DataKafka.getSports();
            departmentsList = DataKafka.getDepartments();

        System.out.println("\nCreate Coach ID: ");
        int coachID = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Create Coach's First Name: ");
        String firstName = bufferedReader.readLine();
        System.out.println("Create Coach's Last Name: ");
        String lastname = bufferedReader.readLine();

        readAllSports();
        System.out.println("Assign Coach to a Sport ID: ");
        int sportID = Integer.parseInt(bufferedReader.readLine());

        readAllDepartments();
        System.out.println("Assign Coach to a Department Key: ");
        String departmentKey = bufferedReader.readLine();

        do {
            Coach coach = new Coach(coachID, firstName, lastname, sportID, departmentKey);
            DataKafka.createCoach(coach);
        } while (searchBySportId(sportID) && searchByDepartmentKey(departmentKey));
        }
    }

    public static void searchByCoachID(int coachID) throws Exception {
        Coach coach = DataKafka.findCoachByCoachID(coachID);
        if (coach != null){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Edit Coach Details");
            System.out.println("2. Remove Coach");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: DataKafka.updateCoach(coach, coachID);
                case 2: DataKafka.deleteCoach(coachID);
                case 3: mainMenu();
            }
        } else {
            System.out.println("Application ID not Found.");
        }
    }

    public static void readAllDepartments(){
        System.out.printf("%-20s %-20s \n", "--------------", "---------------");
        System.out.printf("%-20s %-20s \n", "Department Key", "Department Name");
        System.out.printf("%-20s %-20s \n", "--------------", "---------------");
        for (Department department: departmentsList){
            System.out.printf("%-20s %-20s \n", department.getDepartmentKey(), department.getDepartmentName());
        }
    }

    public static boolean searchByDepartmentKey(String departmentKey) {
        Department department = DataKafka.findDepartmentByDepartmentKey(departmentKey);
        if (department != null) {
            return true;
        } else {
            System.out.println("Department Key " + departmentKey + " not found.");
            return false;
        }
    }

    public static void readAllStudents(){
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "----------", "----------", "---------", "-------------", "--------------");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "Student ID", "First Name", "Last Name", "Email Address", "Department Key");
        System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", "----------", "----------", "---------", "-------------", "--------------");
        for (Student student: studentsList){
            System.out.printf("%-20s %-20s %-20s %-15s %-15s \n", student.getStudentID(), student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getDepartmentKey());
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
        int sportID = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Create Sport Name: ");
        String sportName = bufferedReader.readLine();
        System.out.println("Create Sport Type: ");
        String sportType = bufferedReader.readLine();
        System.out.println("Create Availability: ");
        String availability = bufferedReader.readLine();

        Sport sport = new Sport(sportID, sportName, sportType, availability);
        DataKafka.createSport(sport);
    }

    public static void readAllSports(){
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "Sport ID", "Sport Name", "Sport Type", "Availability");
        System.out.printf("%-20s %-20s %-20s %-15s \n", "--------", "----------", "----------", "------------");
        for (Sport sport: sportsList){
            System.out.printf("%-20s %-20s %-20s %-15s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }
    }

    public static void updateSport(int sportID) throws IOException {
        Sport sports = DataKafka.findSportBySportID(sportID);
        if(sports !=null) {
            System.out.println("Update Sport Name for " + sportID + ": ");
            String sportName = bufferedReader.readLine();
            System.out.println("Update Sport Type for " + sportID + ": ");
            String sportType = bufferedReader.readLine();
            System.out.println("Update Availability for " + sportID + ": ");
            String availability = bufferedReader.readLine();

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

    public static boolean searchBySportId(int sportID) {
        Sport sport = DataKafka.findSportBySportID(sportID);
        if (sport != null) {
            return true;
        } else {
            System.out.println("Sport ID " + sportID + " not found.");
            return false;
        }
    }

    public static void mainMenu() throws Exception {
        System.out.println("\n------MAIN MENU------");
        System.out.println("1. View Applications");
        System.out.println("2. View Coaches");
        System.out.println("3. View Sports");
        System.out.println("4. View Students");
        System.out.println("5. View Tryout Details");
        System.out.println("6. Exit");
        System.out.print("Choice: ");
        int choice = Integer.parseInt(bufferedReader.readLine());

        switch (choice) {
            case 1 : applicationsMenu();
            case 2 : coachesMenu();
            case 3 : sportsMenu();
            case 4 : studentsMenu();
            case 5 : tryoutsMenu();
            case 6 : DataKafka.closeConnection();
            default : System.out.println("Invalid Input.");
        }
    }

    public static void applicationsMenu() throws Exception {
        while (true) {
            applicationsList = DataKafka.getApplications();

            System.out.println("\nView by: ");
            System.out.println("1. All Applications");
            System.out.println("2. Approval Status");
            System.out.println("3. Department");
            System.out.println("4. Sport");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    readAllApplications();
                    System.out.println("\nSelect Application ID: ");
                    int applicationID = Integer.parseInt(bufferedReader.readLine());
                    searchByApplicationID(applicationID);
                }
                case 2 -> {
                    System.out.println("\nEnter Approval Status: ");
                    String approvalStatus = bufferedReader.readLine();
                    sortApplicationsByApprovalStatus(approvalStatus);
                }
                case 3 -> {
                    System.out.println("\nEnter Department Key: ");
                    String departmentKey = bufferedReader.readLine();
                    sortApplicationsByDepartmentKey(departmentKey);
                }
                case 4 -> {
                    System.out.println("\nEnter Sport Name: ");
                    String sportName = bufferedReader.readLine();
                    sortApplicationsBySportName(sportName);
                }
                case 5 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void coachesMenu() throws Exception{
        while (true){
            coachesList = DataKafka.getCoaches();

            readAllCoaches();
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Add a Coach");
            System.out.println("2. Select Coach ID");
            System.out.println("3. View Coaches by Sport");
            System.out.println("4. View Coaches by Department");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createCoach();
                }
                case 2 -> {
                    System.out.println("\nSelect Coach ID: ");
                    int coachID = Integer.parseInt(bufferedReader.readLine());
                    searchByCoachID(coachID);
                }
                case 3 -> {
                    System.out.println("\nEnter Sport Name: ");
                    // coaches under a certain sport name will be displayed
                }
                case 4 -> {
                    System.out.println("\nEnter Department Key: ");
                    // coaches under a certain department key will be displayed
                }
                case 5 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void sportsMenu() throws Exception{
        while (true){
            sportsList = DataKafka.getSports();

            readAllSports();
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Create a Sport");
            System.out.println("2. Select Sport ID");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createSport();
                    System.out.println();
                }
                case 2 -> {
                    System.out.println("\nEnter Sport ID:");
                    // after selecting a sport, admin can edit or remove the sport
                }
                case 3 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void studentsMenu() throws Exception {
        while (true) {
            studentsList = DataKafka.getStudents();

            readAllStudents();
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Add a Student");
            System.out.println("2. Select Student ID");
            System.out.println("3. View Students by Department");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    // call method for creating a new student account
                }
                case 2 -> {
                    System.out.println("\nEnter Student ID: ");
                    // after selecting a student, admin can edit or remove the student
                }
                case 3 -> {
                    System.out.println("\nEnter Department Key: ");
                    // students under a certain department key will be displayed
                }
                case 4 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid Input.");
            }
        }
    }

    public static void tryoutsMenu() throws Exception {
        while (true) {
            tryoutsList = DataKafka.getTryouts();

            System.out.println("\nView By: ");
            System.out.println("1. All Tryouts");
            System.out.println("2. Coach");
            System.out.println("3. Sport");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    readAllTryouts();
                    System.out.println("Select Tryout ID: ");
                    // after selecting a tryout, admin can edit or remove the tryout
                }
                case 2 -> {
                    System.out.println("Enter Coach Name: ");
                    // tryouts under a certain coach name will be displayed
                }
                case 3 -> {
                    System.out.println("Enter Sport Name: ");
                    // tryouts under a certain sport name will be displayed
                }
                case 4 -> {
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
