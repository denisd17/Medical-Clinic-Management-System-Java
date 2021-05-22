import service.*;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeService employeeService = new EmployeeService();
        PatientService patientService = new PatientService();
        MedicalServiceService medicalServiceService = new MedicalServiceService();
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
                    patientService.menu();
                    break;
                case 2:
                    employeeService.menu();
                    break;
                case 3:
                    medicalServiceService.menu();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
