import model.person.MedicalRecord;
import model.person.Patient;
import service.Database;
import service.EmployeeService;
import service.MedicalSService;
import service.PatientService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Am inclus optiunea "Generare date proiect" pentru o mai usoara verificare a proiectului in aceasta etapa
        //Aceasta optiune va adauga in "baza de date" cativa pacienti, doctori, asistente si va genera si o lista de servicii medicale
        Scanner scanner = new Scanner(System.in);
        EmployeeService e = new EmployeeService();
        PatientService p = new PatientService();
        MedicalSService m = new MedicalSService();
        int option = 0;

        while(option != 5) {
            System.out.println("Choose an action");
            System.out.println("1. Display patients menu");
            System.out.println("2. Display employees menu");
            System.out.println("3. Display services menu");
            //System.out.println("4. Generare date proiect");
            System.out.println("5. Exit");
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
