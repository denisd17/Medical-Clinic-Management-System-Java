import model.medical_services.Consultation;
import model.medical_services.MedicalS;
import model.person.*;
import service.Database;
import service.EmployeeService;
import service.MedicalSService;
import service.PatientService;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeService e = new EmployeeService();
        PatientService p = new PatientService();
        MedicalSService m = new MedicalSService();
        int option = 0;

        //Aceasta sectiune va fi scoasa in urmatoarea etapa a proiectului
        //Vor fi adaugati in "baza de date" cativa pacienti, doctori, asistente si va fi generata si o lista de servicii medicale
        //----------GENERARE DATE INITIALE PROIECT----------//
        //----------GENERARE DATE PACIENTI
        Set<String> alergies1 = new HashSet<>();
        Set<String> alergies2 = new HashSet<>();
        alergies1.add("Peanuts");
        alergies2.add("Peanuts");
        alergies1.add("Milk");

        Set<String> chronic1 = new HashSet<>();
        Set<String> chronic2 = new HashSet<>();
        chronic1.add("Diabetes");
        chronic1.add("Heart Disease");
        chronic2.add("Kidney Disease");

        MedicalRecord m1 = new MedicalRecord(true, alergies1, chronic1);
        MedicalRecord m2 = new MedicalRecord(false, alergies2, chronic2);
        MedicalRecord m3 = new MedicalRecord();
        m3.setSmoker(false);

        Date d1 = new Date();
        Date d2 = new Date();
        Date d3 = new Date();

        Patient p1 = new Patient("John", "Smith", "0724000001", "john.smith@gmail.com", "100", m1, d1);
        Patient p2 = new Patient("Michael", "Brown", "0724000002", "michael.brown@gmail.com", "101", m2, d2);
        Patient p3 = new Patient("Tina", "Angelo", "0724000003", "tina@gmail,com", "102", m3, d3);

        ArrayList<Patient> patients = Database.getPatients();
        patients.add(p1);
        patients.add(p2);
        patients.add(p3);

        //----------GENERARE DATE ANGAJATI
        WorkDay day1 = new WorkDay(9, 17);
        WorkDay day2 = new WorkDay();
        WorkDay day3 = new WorkDay(8, 16);
        WorkDay day4 = new WorkDay(8, 16);
        WorkDay day5 = new WorkDay(9, 15);
        WorkDay[] schedule1 = {day1, day2, day3, day4, day5};

        WorkDay day11 = new WorkDay();
        WorkDay day21 = new WorkDay(8, 16);
        WorkDay day31 = new WorkDay(8, 16);
        WorkDay day41 = new WorkDay(8, 16);
        WorkDay day51 = new WorkDay();
        WorkDay[] schedule2 = {day11, day21, day31, day41, day51};

        Specialization s1 = new Specialization("Pediatric");
        ArrayList<Specialization> specializations = Database.getSpecializations();
        specializations.add(s1);
        Consultation c1 = new Consultation(100, s1);
        ArrayList<Consultation> consultations = Database.getConsultations();
        consultations.add(c1);
        Employee e1 = new Doctor("Anthony", "Davis", "0724000004", "anthony@med.com", "103", schedule1, s1);
        Employee e2 = new Nurse("Anna", "Davis", "0724000005", "anna@med.com", "104", schedule2);

        ArrayList<Employee> employees = Database.getEmployees();
        employees.add(e1);
        employees.add(e2);

        //----------GENERARE SERVICII MEDICALE (RADIOGRAFII / INVESTIGATII (TESTE)
        m.generateServicesList();
        //------------------------------


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
