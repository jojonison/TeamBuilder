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
    public static void main(String[] args) throws Exception {
        DataKafka.setConnection();
        JavaKafkaStudent student = new JavaKafkaStudent();
        student.showMenu();
    }

    public void showMenu() throws Exception{
        while (true) {
            System.out.println("Select the number of what you want to do: ");
            System.out.println("1. Show Available Sports");
            System.out.println("2. Apply Available Sports");
            System.out.println("3. View My Applications");
            System.out.println("4. Exit");
            int choice = Integer.parseInt(bufferedReader.readLine());

            switch (choice) {
                case 1:
                    showAvailableSports();
                    break;
                case 2:
                    applyAvailableSports();
                    break;
                case 3:
                    viewApplication();
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Wrong input, system will no exit");
                    System.exit(0);
            }
        }
    }

    public void showAvailableSports() throws Exception {

            sportsList = DataKafka.getSports();
            int count = 1;
            System.out.printf("%-20s %-20s %-20s \n","" ,"--------" , "--------------");
            System.out.printf("%-20s %-20s %-20s \n","" ,"Coach ID" , "Availability");
            System.out.printf("%-20s %-20s %-20s \n","" , "--------" , "--------------");
            for (Sport sport : sportsList){
                System.out.printf("%-20s %-20s %-20s\n",count+".", sport.getSportName(), sport.getAvailability());
                count++;
            }
            System.out.println();
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

    }

}
