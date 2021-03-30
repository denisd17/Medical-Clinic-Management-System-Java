package service;

import jdk.swing.interop.SwingInterOpUtils;
import model.medical_services.Appointment;
import model.medical_services.Consultation;
import model.medical_services.Radiography;
import model.medical_services.Test;
import model.person.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientService {
    //TO DO: FIX THE SCANNER PROBLEM
    private Scanner scanner = new Scanner(System.in);
    private static ArrayList<Patient> patients = Database.getPatients();
    private static ArrayList<Consultation> consultations = Database.getConsultations();
    private static ArrayList<Radiography> radiographies = Database.getRadiographies();
    private static ArrayList<Employee> employees = Database.getEmployees();
    private static ArrayList<Test> tests = Database.getTests();

    public void menu() {
        int option = 0;
        System.out.println("Choose an option.");

        while(option != 7){
            System.out.println("1. List all the registered patients");
            System.out.println("2. Search and display patient info");
            System.out.println("3. Add a new patient");
            System.out.println("4. Update patient info");
            System.out.println("5. Remove patient");
            System.out.println("6. Add a new appointment");
            System.out.println("7. Return to main menu");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    showPatients();
                    break;
                case 2:
                    showPatient();
                    break;
                case 3:
                    addPatient();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    //WORK IN PROGRESS
                    System.out.println("WORK IN PROGRESS");
                    break;
                case 7:
                    break;
                default:
                    System.out.println("Invalid option!");

            }
        }

    }

    //TO DO: FIGURE OUT HOW SCANNER REALLY WORKS (CLEARING BUFFER)
    //TO DO: TREAT PARSE EXCEPTION
    //TO DO: MOVE READING INPUT IN PRIVATE FUNCTIONS THAT TREAT EXCEPTIONS
    public void addPatient(){
        Patient newPatient = new Patient();

        System.out.println("Patient's first name: ");
        String fName = scanner.nextLine();
        newPatient.setFirstName(fName);

        System.out.println("Patient's last name: ");
        String lName = scanner.nextLine();
        newPatient.setLastName(lName);

        System.out.println("Patient's phone number: ");
        String pNumber = scanner.nextLine();
        newPatient.setPhoneNumber(pNumber);

        System.out.println("Patient's email: ");
        String pEmail = scanner.nextLine();
        newPatient.setEmail(pEmail);

        System.out.println("Patient's CNP: ");
        String pCnp = scanner.nextLine();
        newPatient.setCnp(pCnp);

        System.out.println("Patient's birthdate: ");
        String pBirthDate = scanner.nextLine();
        try {
            Date pDate = new SimpleDateFormat("dd/MM/yyyy").parse(pBirthDate);
            newPatient.setBirthDate(pDate);
        }
        catch (Exception e) {

        }

        System.out.println("Do you wish to also create the patient's medical record? (Y/N)");
        char option = scanner.next().charAt(0);
        scanner.nextLine();
        if (option == 'Y') {
            MedicalRecord newMedicalRecord = new MedicalRecord();
            System.out.println("Is the patient a smoker? (Y/N)");
            option = scanner.next().charAt(0);
            if (option == 'Y') {
                newMedicalRecord.setSmoker(true);
            }
            else {
                newMedicalRecord.setSmoker(false);
            }

            System.out.println("Does the patient have any alergies? (Y/N)");
            option = scanner.next().charAt(0);
            scanner.nextLine();
            if (option == 'Y') {
                String alergy = scanner.nextLine();

                while (!alergy.equals("")) {
                    newMedicalRecord.addAlergy(alergy);
                    alergy = scanner.nextLine();
                }
            }

            System.out.println("Does the patient have any chronic illnesses? (Y/N)");
            option = scanner.next().charAt(0);
            scanner.nextLine();
            if (option == 'Y') {
                String disease = scanner.nextLine();

                while (!disease.equals("")) {
                    newMedicalRecord.addDisease(disease);
                    disease = scanner.nextLine();
                }
            }
            newPatient.setMedicalRecord(newMedicalRecord);
        }

        patients.add(newPatient);
    }
    //DONE
    public void showPatients() {
        if (!patients.isEmpty()) {
            for(Patient p : patients) {
                System.out.println("--------------------");
                System.out.print(p);
            }
        }
        else {
            System.out.println("There are no recorded patients.");
        }
    }

    //DONE
    public void showPatient() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        Patient patientToBeShown = searchPatient(cnp);

        if(patientToBeShown == null) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            System.out.println(patientToBeShown);
        }
    }

    //TO DO: MOVE READING INPUT IN PRIVATE FUNCTIONS THAT TREAT EXCEPTIONS, SHARED WITH ADDPATIENT
    //TO DO: UPDATING MEDICAL RECORDS
    public void updatePatient() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        Patient patientToBeUpdated = searchPatient(cnp);

        if(patientToBeUpdated == null) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            int option = 0;
            while(option != 8){
                System.out.println("What info do you wish to update?");
                System.out.println("1. First name");
                System.out.println("2. Last name");
                System.out.println("3. Phone number");
                System.out.println("4. Email");
                System.out.println("5. CNP");
                System.out.println("6. Birth date");
                System.out.println("7. Medical records");
                System.out.println("8. Exit");
                option = scanner.nextInt();
                scanner.nextLine();

                switch(option) {
                    case 1:
                        System.out.println("Patient's first name: ");
                        String fName = scanner.nextLine();
                        patientToBeUpdated.setFirstName(fName);
                        break;
                    case 2:
                        System.out.println("Patient's last name: ");
                        String lName = scanner.nextLine();
                        patientToBeUpdated.setLastName(lName);
                        break;
                    case 3:
                        System.out.println("Patient's phone number: ");
                        String pNumber = scanner.nextLine();
                        patientToBeUpdated.setPhoneNumber(pNumber);
                        break;
                    case 4:
                        System.out.println("Patient's email: ");
                        String pEmail = scanner.nextLine();
                        patientToBeUpdated.setEmail(pEmail);
                        break;
                    case 5:
                        System.out.println("Patient's CNP: ");
                        String pCnp = scanner.nextLine();
                        patientToBeUpdated.setCnp(pCnp);
                        break;
                    case 6:
                        System.out.println("Patient's birthdate: ");
                        String pBirthDate = scanner.nextLine();
                        try {
                            Date pDate = new SimpleDateFormat("dd/MM/yyyy").parse(pBirthDate);
                            patientToBeUpdated.setBirthDate(pDate);
                        }
                        catch (Exception e) {

                        }
                    case 7:
                        System.out.println("COMING SOON");
                        break;
                    case 8:
                        break;
                }
            }

        }
    }

    //DONE
    public void deletePatient() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        Patient patientToBeDeleted = searchPatient(cnp);

        if(patientToBeDeleted == null) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            patients.remove(patientToBeDeleted);
            System.out.println("The patient has been deleted.");
        }
    }
    /*
    public void addAppointment() {
        Appointment newAppointment = new Appointment();
        boolean consultation = false;
        boolean investigation = false;
        int option = 0;
        int index;

        while(option != 4){
            System.out.println("Please select the type of service you want:");
            if(consultation){
                System.out.println("1. Consultation");
            }
            else {
                System.out.println("1. Consultation already added to the appointment");
            }
            System.out.println("2. Test");
            System.out.println("3. Radiography");
            System.out.println("4. Finish and choose appointment date");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option){
                case 1:
                    if(consultation){
                        System.out.println("Choose the type of consultation:");
                        for(int i = 0; i < consultations.size(); i++){
                            System.out.print(i+1);
                            System.out.println(consultations.get(i));
                        }

                        index = scanner.nextInt();
                        scanner.nextLine();

                        newAppointment.addMedicalService(consultations.get(index));
                        consultation = true;
                    }
                    else {
                        System.out.println("Consultation has already been added to the appointment!");
                    }
                    break;
                case 2:
                    System.out.println("Choose the type of test:");
                    for(int i = 0; i < tests.size(); i++){
                        System.out.print(i+1);
                        System.out.println(tests.get(i));
                    }

                    index = scanner.nextInt();
                    scanner.nextLine();

                    newAppointment.addMedicalService(tests.get(index));
                    investigation = true;
                    break;
                case 3:
                    System.out.println("Choose the type of radiography:");
                    for(int i = 0; i < radiographies.size(); i++){
                        System.out.print(i+1);
                        System.out.println(radiographies.get(i));
                    }

                    index = scanner.nextInt();
                    scanner.nextLine();

                    newAppointment.addMedicalService(radiographies.get(index));
                    investigation = true;
                case 4:
                    break;
            }

            System.out.println("Select the day of the consultation: ");
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat ("E dd/MM");
            c.setTime(currentDate);

            for(int i = 1; i <= 7; i++){
                c.add(Calendar.DATE, 1);
                System.out.print(i);
                System.out.println(dateFormat.format(c.getTime()));
            }

            option = scanner.nextInt();
            scanner.nextLine();

            c.add(Calendar.DATE, (7 - option));
            Date appointmentDate = c.getTime();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;






        }




    }
    */
    //DONE
    private Patient searchPatient(String cnp) {
        if (!patients.isEmpty()) {
            for(Patient p : patients) {
                if(p.getCnp().equals(cnp)) {
                    return p;
                }
            }
        }
        return null;
    }
    /*
    private void determineAppointmentHour(Specialization spec, boolean consultation, boolean investigation, Date appDate, int day) {
        int hour = 0;
        boolean found = false;
        boolean searching = true;
        ArrayList<Doctor> doctors = null;
        ArrayList<Nurse> nurses = null;

        if(consultation) {
            doctors = searchDoctor(spec, day);
        }
        if(investigation) {
            nurses = searchNurse(day);
        }

        while(!found && searching) {
            if(consultation) {
                for(Doctor d : doctors) {
                    HashSet<Integer> doctorHours = new HashSet<>();
                    int startHour = d.getSchedule()[day].getStartHour();
                    int endHour = d.getSchedule()[day].getEndHour();

                    for(int i = startHour; i < endHour; i++){
                        doctorHours.add(i);
                    }

                    for(Appointment a : d.getAppointments()) {
                        if(equalDate(a.getDate(), appDate)) {
                            doctorHours.remove(a.getHour());
                        }
                    }
                    if(investigation) {
                        for(Nurse n : nurses) {
                            HashSet<Integer> nurseHours = new HashSet<>();
                            int startHour = n.getSchedule()[day].getStartHour();
                            int endHour = n.getSchedule()[day].getEndHour();

                            for(int i = startHour; i < endHour; i++){
                                nurseHours.add(i);
                            }

                            for(Appointment a : n.getAppointments()) {
                                if(equalDate(a.getDate(), appDate)) {
                                    doctorHours.remove(a.getHour());
                                }
                            }

                        }

                    }
                    else {
                        if(!doctorHours.isEmpty()) {
                            hour = Collections.min(doctorHours);
                            found = true;
                            searching = false;
                        }
                    }
                }



            }
            else {

            }
        }
    }

    private ArrayList<Doctor> searchDoctor(Specialization spec, int day) {
        ArrayList<Doctor> doctors = new ArrayList<>();

        for(Employee e : employees) {
            if (e instanceof Doctor) {
                Doctor d = (Doctor) e;
                if(d.getSpecializare().equals(spec) && d.getSchedule()[day].getEndHour() != 0) {
                    doctors.add(d);
                }
            }
        }

        if(doctors.isEmpty())
            return null;
        return doctors;
    }

    private ArrayList<Nurse> searchNurse(int day) {
        ArrayList<Nurse> nurses = new ArrayList<>();

        for(Employee e : employees) {
            if (e instanceof Nurse) {
                Nurse n = (Nurse) e;
                if(n.getSchedule()[day].getEndHour() != 0) {
                    nurses.add(n);
                }
            }
        }

        if(nurses.isEmpty())
            return null;

        return nurses;
    }

    private boolean equalDate(Date date1, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String d1 = dateFormat.format(date1);
        String d2 = dateFormat.format(date2);

        return d1.equals(d2);
    }

     */



}
