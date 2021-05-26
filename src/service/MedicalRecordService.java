package service;

import model.person.MedicalRecord;
import model.person.Nurse;
import model.person.Patient;
import repository.MedicalRecordRepository;
import repository.PatientRepository;

import java.util.Scanner;

public class MedicalRecordService {
    Scanner scanner = new Scanner(System.in);
    PatientRepository patientRepository = new PatientRepository();
    MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();

    public void menu() {
        int option = 0;
        System.out.println("Choose an option.");

        while(option != 5){
            System.out.println("1. Create a new medical record");
            System.out.println("2. Update a medical record");
            System.out.println("3. Delete a medical record");
            System.out.println("4. Get a medical record");
            System.out.println("5. Return to main menu");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    createMedicalRecord();
                    Audit.writeToAudit(1, 5);
                    break;
                case 2:
                    updateMedicalRecord();
                    Audit.writeToAudit(2, 5);
                    break;
                case 3:
                    deleteMedicalRecord();
                    Audit.writeToAudit(3, 5);
                    break;
                case 4:
                    getMedicalRecord();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option!");

            }
        }
    }

    public void createMedicalRecord() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = patientRepository.checkIfPatientExists(cnp);

        if(!exists) {
            System.out.println("There is no patient with the CNP: " + cnp);
        }
        else {
            Patient patient = patientRepository.getPatient(cnp);
            if(patient.getMedicalRecord() != null) {
                System.out.println("This patient already has a medical record.\nPlease update or delete.");
            }
            else {
                MedicalRecord newMedicalRecord = new MedicalRecord();
                System.out.println("Is the patient a smoker? (Y/N)");
                char option = scanner.next().charAt(0);

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
                    System.out.println("Enter each alergy one by one (press enter after each one).");
                    System.out.println("When finished, press enter again.");
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
                    System.out.println("Enter each disease one by one (press enter after each one).");
                    System.out.println("When finished, press enter again.");
                    String disease = scanner.nextLine();

                    while (!disease.equals("")) {
                        newMedicalRecord.addDisease(disease);
                        disease = scanner.nextLine();
                    }
                }
                medicalRecordRepository.insertRecord(patient.getCnp(), newMedicalRecord);
            }
        }
    }

    public void updateMedicalRecord() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = patientRepository.checkIfPatientExists(cnp);

        if (!exists) {
            System.out.println("There is no patient with the CNP: " + cnp);
        }
        else {
            Patient patient = patientRepository.getPatient(cnp);
            if (patient.getMedicalRecord() == null) {
                System.out.println("This patient has no medical record.\nPlease create one.");
            }
            else {
                int option = 0;

                while(option != 6){
                    MedicalRecord medicalRecord = medicalRecordRepository.getRecord(patient.getCnp());
                    System.out.println("1. Update smoker status");
                    System.out.println("2. Add alergy");
                    System.out.println("3. Delete alergy");
                    System.out.println("4. Add disease");
                    System.out.println("5. Delete disease");
                    System.out.println("6. Finish");
                    option = scanner.nextInt();
                    scanner.nextLine();

                    switch(option) {
                        case 1:
                            System.out.println("Is the patient a smoker? (Y/N)");
                            char optionSubmenu = scanner.next().charAt(0);

                            if (optionSubmenu == 'Y') {
                                medicalRecordRepository.updateSmokerStatus(patient.getCnp(), true);
                            }
                            else {
                                medicalRecordRepository.updateSmokerStatus(patient.getCnp(), false);
                            }
                            break;
                        case 2:
                            System.out.println("Enter the alergy name: ");
                            String newAlergy = scanner.nextLine();
                            medicalRecordRepository.addAlergy(medicalRecord.getId(), newAlergy);
                            break;
                        case 3:
                            if(medicalRecord.getAlergies().size() == 0) {
                                System.out.println("This patient has no alergies");
                                break;
                            }

                            System.out.println("Which alergy do you wish to remove?\nEnter the full name");

                            for(String alergyName : medicalRecord.getAlergies()) {
                                System.out.println(alergyName);
                            }

                            String alergyOption = scanner.nextLine();

                            if(medicalRecord.getAlergies().contains(alergyOption)) {
                                medicalRecordRepository.removeAlergy(medicalRecord.getId(), alergyOption);
                            }
                            else {
                                System.out.println("Invalid name");
                            }
                            break;
                        case 4:
                            System.out.println("Enter the disease name: ");
                            String newDisease = scanner.nextLine();
                            medicalRecordRepository.addDisease(medicalRecord.getId(), newDisease);
                            break;
                        case 5:
                            if(medicalRecord.getChronicDiseases().size() == 0) {
                                System.out.println("This patient has no diseases");
                                break;
                            }

                            System.out.println("Which disease do you wish to remove?\nEnter the full name");

                            for(String diseaseName : medicalRecord.getChronicDiseases()) {
                                System.out.println(diseaseName);
                            }

                            String diseaseOption = scanner.nextLine();

                            if(medicalRecord.getChronicDiseases().contains(diseaseOption)) {
                                medicalRecordRepository.removeDisease(medicalRecord.getId(), diseaseOption);
                            }
                            else {
                                System.out.println("Invalid name");
                            }
                            break;
                        case 6:
                            break;
                        default:
                            System.out.println("Invalid option!");

                    }
                }
            }
        }
    }

    public void deleteMedicalRecord() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = patientRepository.checkIfPatientExists(cnp);

        if (!exists) {
            System.out.println("There is no patient with the CNP: " + cnp);
        } else {
            Patient patient = patientRepository.getPatient(cnp);
            if (patient.getMedicalRecord() == null) {
                System.out.println("This patient has no medical record.\nPlease create one.");
            } else {
                medicalRecordRepository.deleteRecord(cnp);
                System.out.println("Record deleted");
            }
        }
    }
    public void getMedicalRecord() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = patientRepository.checkIfPatientExists(cnp);

        if (!exists) {
            System.out.println("There is no patient with the CNP: " + cnp);
        } else {
            Patient patient = patientRepository.getPatient(cnp);
            if (patient.getMedicalRecord() == null) {
                System.out.println("This patient has no medical record.\nPlease create one.");
            } else {
                System.out.println("Patient CNP: " + cnp);
                System.out.println(patient.getMedicalRecord());
            }
        }
    }
}