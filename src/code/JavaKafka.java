package code;

import java.util.*;

public class JavaKafka {
    public static Scanner scanner = new Scanner(System.in);
    public static String applicationId = null;
    public static String studentId = null;
    public static String sportName = null;
    public static String tryOutId = null;
    public static ArrayList<Application> list = new ArrayList<>();

    public static void displayAllApplication(){
        for (Application application: list){
            System.out.printf("%-20s %-20s %-20s %-15s \n", application.getApplicationId(),application.getStudentId(),application.getSportName(),application.getTryOutId());
        }
    }

    public static void searchByApplicationId(){
        System.out.println("Enter code.Application Id: ");
        applicationId = scanner.next();
        list.clear();
        list = DataKafka.findApplication(applicationId);
        for (Application application: list){
            System.out.printf("%-20s %-20s %-15s \n", application.getApplicationId(),application.getStudentId(),application.getSportName(),application.getTryOutId());
        }

        if(list.isEmpty()){
            System.out.println("code.Application Id not Found");
        }
    }

    public static void updateApplication(String num){
        System.out.println("Enter code.Application Id: ");
        applicationId = scanner.next();
        System.out.println("Enter Student Id: ");
        studentId = scanner.next();
        System.out.println("Enter Sport Name: ");
        sportName = scanner.next();
        System.out.println("Enter TryOut Id: ");
        tryOutId = scanner.next();

        Application application = new Application(applicationId,studentId,sportName,tryOutId);
        DataKafka.updateApplication(application, num);
    }

    public static void searchByStudentId() throws Exception {
        System.out.println("Enter Student Id: ");
        studentId = scanner.next();
        Application application = DataKafka.findApplicationByStudentId(studentId);

        if(application !=null){
            System.out.printf("%-20s %-20s %-15s \n", application.getApplicationId(),application.getStudentId(),application.getSportName(),application.getTryOutId());

            System.out.println();
            System.out.println("Update code.Application? (y/n): ");
             String update = scanner.next();

             if (update.equalsIgnoreCase("y")){
                 updateApplication(studentId);
             }
        }else {
            System.out.println("code.Application data not Found.");
        }
    }

    public static void searchByTryOutId() throws Exception{
        System.out.println("Enter TryOut Id: ");
        tryOutId = scanner.next();
        list.clear();
        list = DataKafka.getApplicationByTryOutId(tryOutId);

        for(Application application: list){
            System.out.printf("%-20s %-20s %-15s \n", application.getApplicationId(),application.getStudentId(),application.getSportName(),application.getTryOutId());
        }
        if (list.isEmpty()){
            System.out.println("code.Application data not Found");
        }
    }

    public static void addApplication() throws Exception{
        System.out.println("Enter ApplicationId: ");
        applicationId = scanner.next();
        System.out.println("Enter StudentId: ");
        studentId = scanner.next();
        System.out.println("Enter SportName: ");
        sportName = scanner.next();
        System.out.println("Enter TryOutId: ");
        tryOutId = scanner.next();

        Application application = new Application(applicationId,studentId,sportName,tryOutId);
        DataKafka.addApplication(application);
    }

    public static void menu() throws Exception{
        while (true){
            list = DataKafka.getApplication();

            System.out.println("Choose what to do");
            System.out.println("1. Display");
            System.out.println("2. Search by code.Application Id");
            System.out.println("3. Search by Student Id");
            System.out.println("4. Search by tryOut Id");
            System.out.println("5. add application data");
            System.out.println("6. Exit");
            System.out.print("choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    displayAllApplication();
                    System.out.println();
                }
                case 2 -> {
                    searchByApplicationId();
                    System.out.println();
                }
                case 3 -> {
                    searchByStudentId();
                    System.out.println();
                }
                case 4 -> {
                    searchByTryOutId();
                    System.out.println();
                }
                case 5 -> {
                    addApplication();
                    System.out.println();
                }
                case 6 -> System.exit(0);
                default -> System.out.println("Invalid Input");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DataKafka.setConnection();
        menu();
    }
}
