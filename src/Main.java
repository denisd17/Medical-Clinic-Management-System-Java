import config.DataSetup;
import model.medical_services.*;
import model.person.*;
import repository.*;
import service.*;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException{

        DataSetup setUpData = new DataSetup();
        setUpData.setUp();

        Scanner scanner = new Scanner(System.in);
        EmployeeService employeeService = new EmployeeService();
        PatientService patientService = new PatientService();
        MedicalServiceService medicalServiceService = new MedicalServiceService();
        MedicalRecordService medicalRecordService = new MedicalRecordService();
        MedicalServiceRepository test = new MedicalServiceRepository();

        int option = 0;
        //Reader.readAllFromCSV();

        while(option != 4) {
            System.out.println("Choose an action");
            System.out.println("1. Display patients menu");
            System.out.println("2. Display employees menu");
            System.out.println("3. Display medical records menu");
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
                    medicalRecordService.menu();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
