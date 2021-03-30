package service;

import jdk.swing.interop.SwingInterOpUtils;
import model.person.MedicalRecord;
import model.person.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class PatientService {
    //TO DO: FIX THE SCANNER PROBLEM
    private Scanner scanner = new Scanner(System.in);
    private static ArrayList<Patient> patients = Database.getPatients();

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

    public void addAppointment() {

    }
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



}
