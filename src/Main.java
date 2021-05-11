import model.medical_services.Appointment;
import model.medical_services.Consultation;
import model.medical_services.MedicalS;
import model.person.*;
import service.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeService e = new EmployeeService();
        PatientService p = new PatientService();
        MedicalSService m = new MedicalSService();
        int option = 0;
        Reader.readAllFromCSV();

        while(option != 4) {
            System.out.println("Choose an action");
            System.out.println("1. Display patients menu");
            System.out.println("2. Display employees menu");
            System.out.println("3. Display services menu");
            System.out.println("4. Exit");

            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    p.menu();
                    break;
                case 2:
                    e.menu();
                    break;
                case 3:
                    m.menu();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
