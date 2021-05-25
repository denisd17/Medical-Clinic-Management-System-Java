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
        AppointmentRepository repo = new AppointmentRepository();
        Set<MedicalService> medicalServices = new HashSet<>();
        medicalServices.add(new Consultation(1, 10, new Specialization("pediatru")));
        medicalServices.add(new Radiography(2, 10, RadiographyArea.Chest));
        medicalServices.add(new Test(3, 10, TestType.Lipid));
        Appointment newAppointment = new Appointment(new Date(), medicalServices);
        newAppointment.setPatientNumericCode("101");
        newAppointment.setHour(10);
        repo.insertAppointment(newAppointment);
        ArrayList<Appointment> apps = new ArrayList<>();
        apps = repo.getAppointmentsByCnp("101", 0);
        System.out.println(apps);
        //repo.deleteAppointment(2);
        System.exit(-1);
        Set<String> alergies = new HashSet<>();
        Set<String> chronicDiseases = new HashSet<>();
        alergies.add("peanuts");
        alergies.add("milk");
        chronicDiseases.add("kidney");
        chronicDiseases.add("heart");
        MedicalRecord mc = new MedicalRecord(true, alergies, chronicDiseases);
        //repo.insertRecord("101", mc);
        System.exit(-1);
        ScheduleRepository repoSchedule = new ScheduleRepository();
        WorkDay[] schedule = new WorkDay[5];
        for(int i = 0; i < 5; i++) {
            schedule[i] = new WorkDay();
            schedule[i].setStartHour(i*2);
            schedule[i].setEndHour(i*2 + 1);
        }
        repoSchedule.deleteSchedule("100");
        repoSchedule.deleteSchedule("101");
        Nurse nurse = new Nurse("prenume", "nume", "telefon", "email", "105", schedule);
        NurseRepository nurseRepository = new NurseRepository();
        nurseRepository.insertNurse(nurse);
        nurseRepository.updateNurseFirstName("105", "updatedname");
        nurseRepository.updateNurseLastName("105", "updatedlastname");
        nurseRepository.updateNurseSchedule("105", "monday", 0, 0);
        nurseRepository.updateNurseEmail("105", "updatedEmial");
        Nurse nurse2 = nurseRepository.getNurse("105");
        System.out.println(nurse2);
        nurseRepository.deleteNurse("105");


        //setUpData.addPatient();
        //setUpData.displayPerson();


        PatientRepository p = new PatientRepository();
        /*
        Patient pa1 = new Patient("nume", "prenume", "123456789", "nume.prenume@gmail.com", "100", null, new Date());

        MedicalRecord mc = new MedicalRecord(true, alergies, chronicDiseases);
        Patient pa2 = new Patient("nume2", "prenume2", "223456789", "nume2.prenume@gmail.com", "101", mc, new Date());
        //p.insertPatient(pa1);
        //p.insertPatient(pa2);

        Patient ptest1 = p.getPatient("100");
        System.out.println(ptest1);

        Patient ptest2 = p.getPatient("101");
        System.out.println(ptest2);

        p.updatePatientEmail("101", "updatedEmail");
        p.updatePatientFirstName("101", "updatedFirstname");
        p.updatePatientLastName("101", "updatedLastName");
        p.updatePatientEmail("101", "updatedEmail");
        p.updatePatientCnp("101", "300");
        java.sql.Date sqlDate = new java.sql.Date(new Date("17/04/1999").getTime());
        System.out.println(new Date("17/04/1999"));
        p.updatePatientBirthDate("300", sqlDate);

        Patient ptest3 = p.getPatient("300");

        System.out.println(ptest3);

         */
        MedicalRecordRepository repoMedical = new MedicalRecordRepository();

        repoMedical.addAlergy(1, "test");
        repoMedical.addDisease(1, "test");
        repoMedical.updateSmokerStatus("101", false);
        MedicalRecord test = repoMedical.getRecord("101");
        System.out.println(test);

        repoMedical.removeAlergy(1,"test");
        repoMedical.removeDisease(1,"test");
        repoMedical.removeDisease(1, "asdasda");
        MedicalRecord test2 = repoMedical.getRecord("101");
        System.out.println(test2);

        repoMedical.deleteRecord("101");


        //System.out.println("test");
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
