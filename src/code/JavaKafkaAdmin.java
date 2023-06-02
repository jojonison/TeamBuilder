package code;

import tables.*;

import javax.xml.crypto.*;
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
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", "--------------", "----------", "--------", "---------", "---------------", "----------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", "Application ID", "Student ID", "Sport ID", "Tryout ID", "Approval Status", "Application Date");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", "--------------", "----------", "--------", "---------", "---------------", "----------------");
        for (Application application: applicationsList){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
        }
    }

    public static void selectApplicationID(int applicationID) throws Exception {
        Application application = DataKafka.selectApplicationByApplicationID(applicationID);
        if (application != null){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());

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
            System.out.println("Invalid input.");
        }
    }

    public static void sortApplicationsByApprovalStatus(String approvalStatus) throws Exception {
        ArrayList<Application> byApprovalStatus = DataKafka.findApplicationsByApprovalStatus(approvalStatus);
        if (!byApprovalStatus.isEmpty()){
            for (Application application : byApprovalStatus) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
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

    public static void sortApplicationsByDepartmentKey(String departmentKey) throws Exception {
        ArrayList<Application> byDepartmentKey = DataKafka.findApplicationsByDepartmentKey(departmentKey);
        if (!byDepartmentKey.isEmpty()){
            for (Application application : byDepartmentKey) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
            }
            System.out.println("\nSelect Application ID: ");
            int applicationID = Integer.parseInt(bufferedReader.readLine());

            for (Application application : byDepartmentKey) {
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
                System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s \n", application.getApplicationID(), application.getStudentID(), application.getSportID(), application.getTryoutID(), application.getApprovalStatus(), application.getApplicationDate());
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

    public static void readAllCoaches(){
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "--------", "----------", "---------", "--------", "--------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "Coach ID", "First Name", "Last Name", "Sport ID", "Department Key");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "--------", "----------", "---------", "--------", "--------------");
        for (Coach coach: coachesList){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
        }
    }

    public static void createCoach() throws Exception {
        System.out.println("\nEnter Coach ID: ");
        int coachID = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Enter Coach's First Name: ");
        String firstName = bufferedReader.readLine();
        System.out.println("Enter Coach's Last Name: ");
        String lastname = bufferedReader.readLine();

        readAllSports();
        System.out.println("Assign Coach to a Sport ID: ");
        int sportID = Integer.parseInt(bufferedReader.readLine());

        readAllDepartments();
        System.out.println("Assign Coach to a Department Key: ");
        String departmentKey = bufferedReader.readLine();

        if (findSportId(sportID) && findDepartmentKey(departmentKey)) {
            Coach coach = new Coach(coachID, firstName, lastname, sportID, departmentKey);
            DataKafka.createCoach(coach);
        } else {
            System.out.println("Invalid input.");
            coachesMenu();
        }
    }

    public static void selectCoachID(int coachID) throws Exception {
        Coach coach = DataKafka.selectCoachByCoachID(coachID);
        if (coach != null){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
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
            System.out.println("Invalid input.");
        }
    }

    public static void findCoachByName(String firstname, String lastname) throws Exception {
        ArrayList<Coach> byCoachName = DataKafka.findCoachByCoachName(firstname, lastname);
        if (!byCoachName.isEmpty()){
            for (Coach coach : byCoachName) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
            }
            System.out.println("\nSelect Coach ID: ");
            int coachID = Integer.parseInt(bufferedReader.readLine());

            for (Coach coach : byCoachName) {
                if (coachID == coach.getCoachID()) {
                    selectCoachID(coachID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortCoachesByDepartmentKey(String departmentKey) throws Exception {
        ArrayList<Coach> byDepartmentKey = DataKafka.findCoachesByDepartmentKey(departmentKey);
        if (!byDepartmentKey.isEmpty()){
            for (Coach coach : byDepartmentKey) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
            }
            System.out.println("\nSelect Coach ID: ");
            int coachID = Integer.parseInt(bufferedReader.readLine());

            for (Coach coach : byDepartmentKey) {
                if (coachID == coach.getCoachID()) {
                    selectCoachID(coachID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortCoachesBySportName(String sportName) throws Exception {
        ArrayList<Coach> bySportName = DataKafka.findCoachesBySportName(sportName);
        if (!bySportName.isEmpty()){
            for (Coach coach : bySportName) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", coach.getCoachID(), coach.getFirstName(), coach.getLastName(), coach.getSportID(), coach.getDepartmentKey());
            }
            System.out.println("\nSelect Coach ID: ");
            int coachID = Integer.parseInt(bufferedReader.readLine());

            for (Coach coach : bySportName) {
                if (coachID == coach.getCoachID()) {
                    selectCoachID(coachID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
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

    public static boolean findDepartmentKey(String departmentKey) {
        Department department = DataKafka.findDepartmentByDepartmentKey(departmentKey);
        return department != null;
    }

    public static void readAllSports(){
        System.out.printf("%-20s %-20s %-20s %-20s \n", "--------", "----------", "----------", "------------");
        System.out.printf("%-20s %-20s %-20s %-20s \n", "Sport ID", "Sport Name", "Sport Type", "Availability");
        System.out.printf("%-20s %-20s %-20s %-20s \n", "--------", "----------", "----------", "------------");
        for (Sport sport: sportsList){
            System.out.printf("%-20s %-20s %-20s %-20s \n", sport.getSportID(), sport.getSportName(), sport.getSportType(), sport.getAvailability());
        }
    }

    public static void selectSportId(int sportID) throws Exception {
        Sport sport = DataKafka.selectSportBySportID(sportID);
        if (sport != null){
            System.out.printf("%-20s %-20s %-20s %-20s \n", "Sport details: ", sport.getSportName(), sport.getSportType(), sport.getAvailability());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Edit Sport Details");
            System.out.println("2. Delete Sport");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: DataKafka.updateSport(sport, sportID);
                case 2: DataKafka.deleteSport(sportID);
                case 3: mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static boolean findSportId(int sportID) {
        Sport sport = DataKafka.selectSportBySportID(sportID);
        return sport != null;
    }

    public static void findSportBySportName(String sportName) throws Exception {
        Sport sport = DataKafka.findSportBySportName(sportName);
        if (sport != null){
            System.out.printf("%-20s %-20s %-20s %-20s \n", "Sport details: ", sport.getSportID(), sport.getSportType(), sport.getAvailability());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Edit Sport Details");
            System.out.println("2. Delete Sport");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: DataKafka.updateSport(sport, sport.getSportID());
                case 2: DataKafka.deleteSport(sport.getSportID());
                case 3: mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortSportsBySportType(String sportType) throws Exception {
        ArrayList<Sport> bySportType = DataKafka.findSportsBySportType(sportType);
        if (!bySportType.isEmpty()){
            for (Sport sport : bySportType) {
                System.out.printf("%-20s %-20s %-20s \n", sport.getSportID(), sport.getSportName(), sport.getAvailability());
            }
            System.out.println("\nSelect Sport ID: ");
            int sportID = Integer.parseInt(bufferedReader.readLine());

            for (Sport sport : bySportType) {
                if (sportID == sport.getSportID()) {
                    selectSportId(sportID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortSportsByAvailability(String availability) throws Exception {
        ArrayList<Sport> byAvailability = DataKafka.findSportsByAvailability(availability);
        if (!byAvailability.isEmpty()){
            for (Sport sport : byAvailability) {
                System.out.printf("%-20s %-20s %-20s \n", sport.getSportID(), sport.getSportName(), sport.getSportType());
            }
            System.out.println("\nSelect Sport ID: ");
            int sportID = Integer.parseInt(bufferedReader.readLine());

            for (Sport sport : byAvailability) {
                if (sportID == sport.getSportID()) {
                    selectSportId(sportID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void createTryoutDetails() throws Exception{
        while (true){
            sportsList = DataKafka.getSports();
            readAllSports();
            System.out.println("Select Sport to create Tryout Details");
            int sportsID = Integer.parseInt(bufferedReader.readLine());
            int coachID = DataKafka.getCoachIDBySportID(sportsID);
            if (coachID == -1){
                System.out.println("System will now Exit. No Coach for this sport");
                System.exit(0);
            }
            System.out.println("Create Tryout ID: ");
            int tryoutID = Integer.parseInt(bufferedReader.readLine());
            System.out.println("Create schedule. Please follow this format. (yy-mm-day) (hr:mm:ss): ");
            String schedule = bufferedReader.readLine();
            System.out.println("Create Location: ");
            String location = bufferedReader.readLine();


            TryoutDetails newTryoutDetails = new TryoutDetails(tryoutID, sportsID, schedule, location, coachID);
            DataKafka.createTryouts(newTryoutDetails);
            break;
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

    public static void readAllStudents(){
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "----------", "----------", "---------", "-------------", "--------------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "Student ID", "First Name", "Last Name", "Email Address", "Department Key");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "----------", "----------", "---------", "-------------", "--------------");
        for (Student student: studentsList){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", student.getStudentID(), student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getDepartmentKey());
        }
    }

    public static void selectStudentId(int studentID) throws Exception {
        Student student = DataKafka.selectStudentByStudentID(studentID);
        if (student != null){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", student.getStudentID(), student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getDepartmentKey());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Edit Student Details");
            System.out.println("2. Remove Student");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1: DataKafka.updateStudent(student, studentID);
                case 2: DataKafka.deleteStudent(studentID);
                case 3: mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void findStudentByName(String firstname, String lastname) throws Exception {
        ArrayList<Student> byStudentName = DataKafka.findStudentByStudentName(firstname, lastname);
        if (!byStudentName.isEmpty()){
            for (Student student : byStudentName) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", student.getStudentID(), student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getDepartmentKey());
            }
            System.out.println("\nSelect Student ID: ");
            int studentID = Integer.parseInt(bufferedReader.readLine());

            for (Student student : byStudentName) {
                if (studentID == student.getStudentID()) {
                    selectStudentId(studentID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortStudentsByDepartmentKey(String departmentKey) throws Exception {
        ArrayList<Student> byDepartmentKey = DataKafka.findStudentsByDepartmentKey(departmentKey);
        if (!byDepartmentKey.isEmpty()){
            for (Student student : byDepartmentKey) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", student.getStudentID(), student.getFirstName(), student.getLastName(), student.getEmailAddress(), student.getDepartmentKey());
            }
            System.out.println("\nSelect Student ID: ");
            int studentID = Integer.parseInt(bufferedReader.readLine());

            for (Student student : byDepartmentKey) {
                if (studentID == student.getStudentID()) {
                    selectStudentId(studentID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void createStudent() throws Exception{
        System.out.println("Enter Student ID: ");
        int studentID = Integer.parseInt(bufferedReader.readLine());
        System.out.println("Enter Student's First Name: ");
        String firstName = bufferedReader.readLine();
        System.out.println("Enter Student's Last Name: ");
        String lastName = bufferedReader.readLine();
        System.out.println("Enter Student's Email Address: ");
        String emailAdd = bufferedReader.readLine();

        readAllDepartments();
        System.out.println("Assign Student to Department Key: ");
        String departmentKey = bufferedReader.readLine();

        if (findDepartmentKey(departmentKey)) {
            Student student = new Student(studentID, firstName, lastName, emailAdd, departmentKey);
            DataKafka.createStudent(student);
        } else {
            System.out.println("Invalid input.");
            studentsMenu();
        }
    }

    public static void readAllTryouts(){
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "---------", "--------", "--------", "--------", "--------");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "Tryout ID", "Sport ID", "Schedule", "Location", "Coach ID");
        System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", "---------", "--------", "--------", "--------", "--------");
        for (TryoutDetails tryout: tryoutsList){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
        }
    }

    public static void selectTryoutID(int tryoutID) throws Exception {
        TryoutDetails tryout = DataKafka.selectTryoutByTryoutID(tryoutID);
        if (tryout != null){
            System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Edit Tryout Details");
            System.out.println("2. Delete Tryout Details");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1:
                    System.out.println("Input the new schedule using this format: (yy-mm-day) (hr:mm:ss)");
                    System.out.println("E.g : 2023-02-13 17:31:22");
                    System.out.println("Enter new schedule: ");
                    String newSchedule = bufferedReader.readLine();
                    System.out.println("Enter new Location");
                    String newLocation = bufferedReader.readLine();
                    DataKafka.updateTryouts(tryout, tryoutID, newSchedule, newLocation);
                    System.exit(0);
                case 2:
                    DataKafka.deleteTryouts(tryoutID);
                case 3:
                    mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void selectTryoutNo(int tryoutNo) throws Exception {
        Tryouts tryout = DataKafka.selectTryoutByTryoutNo(tryoutNo);
        if (tryout != null){
            System.out.printf("%-20s %-20s %-20s %-20s \n", tryout.getTryoutNo(), tryout.getTryoutId(), tryout.getApplicationId(), tryout.getComments());
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Edit Comments");
            System.out.println("2. Delete Tryout");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1:
                System.out.println("Format of the comments should be: (Status[e.g Qualified/Pending/Denied]).(CommentsWithNoSpaces)");
                System.out.println("What is the comment inputted? : ");
                String newComment = bufferedReader.readLine();
                DataKafka.updateTryout(tryout, tryoutNo, newComment);
                String[] words = newComment.split("\\.");
                if (words[0].equals("Qualified")){
                    DataKafka.updateApplicationStatus(tryout.getApplicationId(),"Qualified");
                } else if (words[0].equals("Pending")){
                    DataKafka.updateApplicationStatus(tryout.getApplicationId(),"Pending");
                } else if (words[0].equals("Denied"))
                    DataKafka.updateApplicationStatus(tryout.getApplicationId(),"Denied");

                System.exit(0);
                case 2: DataKafka.deleteTryout(tryoutNo);

                case 3: mainMenu();
            }
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortTryoutsByDepartmentKey(String departmentKey) throws Exception {
        ArrayList<TryoutDetails> byDepartmentKey = DataKafka.findTryoutsByDepartmentKey(departmentKey);
        if (!byDepartmentKey .isEmpty()){
            for (TryoutDetails tryout: byDepartmentKey ) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
            }
            System.out.println("\nSelect Tryout ID: ");
            int tryoutID = Integer.parseInt(bufferedReader.readLine());

            for (TryoutDetails tryoutDetails : byDepartmentKey) {
                if (tryoutID == tryoutDetails.getTryoutID()) {
                    selectTryoutID(tryoutID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static void sortTryoutsBySportName(String sportName) throws Exception {
        ArrayList<TryoutDetails> bySportName = DataKafka.findTryoutsBySportName(sportName);
        if (!bySportName.isEmpty()){
            for (TryoutDetails tryout : bySportName) {
                System.out.printf("%-20s %-20s %-20s %-20s %-20s \n", tryout.getTryoutID(), tryout.getSportID(), tryout.getSchedule(), tryout.getLocation(), tryout.getCoachID());
            }
            System.out.println("\nSelect Tryout ID: ");
            int tryoutID = Integer.parseInt(bufferedReader.readLine());

            for (TryoutDetails tryoutDetails : bySportName) {
                if (tryoutID == tryoutDetails.getTryoutID()) {
                    selectTryoutID(tryoutID);
                    return;
                }
            }
            System.out.println("Invalid input.");
        } else {
            System.out.println("Invalid input.");
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
            default : System.out.println("Invalid input.");
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
                    selectApplicationID(applicationID);
                }
                case 2 -> {
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
                }
                case 3 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. SAMCIS Applications");
                    System.out.println("b. SEA Applications");
                    System.out.println("c. STELA Applications");
                    System.out.println("d. SOL Applications");
                    System.out.println("e. SOM Applications");
                    String departmentChoice = bufferedReader.readLine();

                    switch (departmentChoice) {
                        case "a", "A": sortApplicationsByDepartmentKey("SAMCIS");
                        case "b", "B": sortApplicationsByDepartmentKey("SEA");
                        case "c", "C": sortApplicationsByDepartmentKey("STELA");
                        case "d", "D": sortApplicationsByDepartmentKey("SOL");
                        case "e", "E": sortApplicationsByDepartmentKey("SOM");
                        default: System.out.println("Invalid input.");
                    }
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
                default -> System.out.println("Invalid input.");
            }
        }
    }

    public static void coachesMenu() throws Exception{
        while (true){
            coachesList = DataKafka.getCoaches();
            sportsList = DataKafka.getSports();
            departmentsList = DataKafka.getDepartments();

            readAllCoaches();
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Add a Coach");
            System.out.println("2. Select Coach ID");
            System.out.println("3. Find a Coach");
            System.out.println("4. View Coaches by Department");
            System.out.println("5. View Coaches by Sport");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createCoach();
                }
                case 2 -> {
                    System.out.println("\nSelect Coach ID: ");
                    int coachID = Integer.parseInt(bufferedReader.readLine());
                    selectCoachID(coachID);
                }
                case 3 -> {
                    System.out.println("Enter Coach's First Name: ");
                    String firstname = bufferedReader.readLine();
                    System.out.println("Enter Coach's Last Name: ");
                    String lastname = bufferedReader.readLine();
                    findCoachByName(firstname, lastname);
                }
                case 4 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. SAMCIS Coaches");
                    System.out.println("b. SEA Coaches");
                    System.out.println("c. STELA Coaches");
                    System.out.println("d. SOL Coaches");
                    System.out.println("e. SOM Coaches");
                    String departmentChoice = bufferedReader.readLine();

                    switch (departmentChoice) {
                        case "a", "A": sortCoachesByDepartmentKey("SAMCIS");
                        case "b", "B": sortCoachesByDepartmentKey("SEA");
                        case "c", "C": sortCoachesByDepartmentKey("STELA");
                        case "d", "D": sortCoachesByDepartmentKey("SOL");
                        case "e", "E": sortCoachesByDepartmentKey("SOM");
                        default: System.out.println("Invalid input.");
                    }
                }
                case 5 -> {
                    System.out.println("\nEnter Sport Name: ");
                    String sportName = bufferedReader.readLine();
                    sortCoachesBySportName(sportName);
                }
                case 6 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid input.");
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
            System.out.println("3. Find a Sport");
            System.out.println("4. View Sports by Availability");
            System.out.println("5. View Sports by Sport Type");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createSport();
                    System.out.println();
                }
                case 2 -> {
                    System.out.println("\nSelect Sport ID:");
                    int sportID = Integer.parseInt(bufferedReader.readLine());
                    selectSportId(sportID);
                }
                case 3 -> {
                    System.out.println("\nEnter Sport Name: ");
                    String sportName = bufferedReader.readLine();
                    findSportBySportName(sportName);
                }
                case 4 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. Available Sports");
                    System.out.println("b. Unavailable Sports");
                    String availChoice = bufferedReader.readLine();

                    switch (availChoice) {
                        case "a", "A": sortSportsByAvailability("Available");
                        case "b", "B": sortSportsByAvailability("Not Available");
                        default: System.out.println("Invalid input.");
                    }
                }
                case 5 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. Single Player Sports");
                    System.out.println("b. Team Sports");
                    String typeChoice = bufferedReader.readLine();

                    switch (typeChoice) {
                        case "a", "A": sortSportsBySportType("Single Player");
                        case "b", "B": sortSportsBySportType("Team");
                        default: System.out.println("Invalid input.");
                    }
                }
                case 6 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    public static void studentsMenu() throws Exception {
        while (true) {
            studentsList = DataKafka.getStudents();
            departmentsList = DataKafka.getDepartments();

            readAllStudents();
            System.out.println("\nChoose what to do: ");
            System.out.println("1. Add a Student");
            System.out.println("2. Select Student ID");
            System.out.println("3. Find a Student");
            System.out.println("4. View Students by Department");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    createStudent();
                }
                case 2 -> {
                    System.out.println("\nEnter Student ID: ");
                    int studentID = Integer.parseInt(bufferedReader.readLine());
                    selectStudentId(studentID);
                }
                case 3 -> {
                    System.out.println("Enter Student's First Name: ");
                    String firstname = bufferedReader.readLine();
                    System.out.println("Enter Student's Last Name: ");
                    String lastname = bufferedReader.readLine();
                    findStudentByName(firstname, lastname);
                }
                case 4 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. SAMCIS Students");
                    System.out.println("b. SEA Students");
                    System.out.println("c. STELA Students");
                    System.out.println("d. SOL Students");
                    System.out.println("e. SOM Students");
                    String departmentChoice = bufferedReader.readLine();

                    switch (departmentChoice) {
                        case "a", "A": sortStudentsByDepartmentKey("SAMCIS");
                        case "b", "B": sortStudentsByDepartmentKey("SEA");
                        case "c", "C": sortStudentsByDepartmentKey("STELA");
                        case "d", "D": sortStudentsByDepartmentKey("SOL");
                        case "e", "E": sortStudentsByDepartmentKey("SOM");
                        default: System.out.println("Invalid input.");
                    }
                }
                case 5 -> {
                    mainMenu();
                    System.out.println();
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    public static void readAllTryout() throws Exception {
        ArrayList<Tryouts> tryoutList = DataKafka.allTryOuts();
        System.out.printf("%-20s %-20s %-20s %-20s  \n", "--------------", "----------", "-------------", "---------");
        System.out.printf("%-20s %-20s %-20s %-20s  \n", "Tryout No", "TryoutId", "Application Id", "Comments");
        System.out.printf("%-20s %-20s %-20s %-20s  \n", "--------------", "----------", "-------------", "---------");
        for (Tryouts tryouts: tryoutList){
            System.out.printf("%-20s %-20s %-20s %-20s \n", tryouts.getTryoutNo(), tryouts.getTryoutId(), tryouts.getApplicationId(), tryouts.getComments());
        }
    }
    public static void tryoutsMenu() throws Exception {
        while (true) {
            tryoutsList = DataKafka.getTryouts();

            System.out.println("\nView By: ");
            System.out.println("1. Tryouts Venue");
            System.out.println("2. Coach");
            System.out.println("3. Sport");
            System.out.println("4. All tryouts");
            System.out.println("5. Or Create Tryouts Venue");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1 -> {
                    readAllTryouts();
                    System.out.println("Select Tryout ID: ");
                    int tryoutID = Integer.parseInt(bufferedReader.readLine());
                    selectTryoutID(tryoutID);
                }
                case 2 -> {
                    System.out.println("\nChoose what to view: ");
                    System.out.println("a. SAMCIS Tryouts");
                    System.out.println("b. SEA Tryouts");
                    System.out.println("c. STELA Tryouts");
                    System.out.println("d. SOL Tryouts");
                    System.out.println("e. SOM Tryouts");
                    String departmentChoice = bufferedReader.readLine();

                    switch (departmentChoice) {
                        case "a", "A": sortTryoutsByDepartmentKey("SAMCIS");
                        case "b", "B": sortTryoutsByDepartmentKey("SEA");
                        case "c", "C": sortTryoutsByDepartmentKey("STELA");
                        case "d", "D": sortTryoutsByDepartmentKey("SOL");
                        case "e", "E": sortTryoutsByDepartmentKey("SOM");
                        default: System.out.println("Invalid input.");
                    }
                }
                case 3 -> {
                    System.out.println("Enter Sport Name: ");
                    String sportName = bufferedReader.readLine();
                    sortTryoutsBySportName(sportName);
                }
                case 4 -> {
                    readAllTryout();
                    System.out.println("Select Tryout No: ");
                    int tryoutNo = Integer.parseInt(bufferedReader.readLine());
                    selectTryoutNo(tryoutNo);
                    mainMenu();
                    System.out.println();
                }
                case 5 -> {
                    createTryoutDetails();
                    System.out.println();
                }
                case 6 -> {
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
