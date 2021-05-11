package service;

import model.medical_services.*;
import model.person.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Reader {
    private static Reader single_instance = null;

    private Reader() {

    }

    public static Reader getInstance() {
        if(single_instance == null)
            single_instance = new Reader();

        return single_instance;
    }

    public static <T> void readCSV(ArrayList<T> array, String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            //PRIMA LINIE, COLOANELE DIN CSV
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.strip().split(",");

                switch(fileName){
                    case "specializations.csv":
                        //System.out.println("123456");
                        Specialization newSpecialization = new Specialization(data[0]);
                        array.add((T) newSpecialization);
                        break;
                    case "employees.csv":
                        Employee newEmployee = (Employee) parsePerson(data, 2);
                        array.add((T) newEmployee);
                        break;
                    case "patients.csv":
                        Patient newPatient = (Patient) parsePerson(data, 1);
                        array.add((T) newPatient);
                        break;
                    case "appointments.csv":
                        Appointment newAppointment = parseAppointment(data);
                        array.add((T) newAppointment);
                        break;
                    case "consultations.csv":
                        Consultation newConsultation = (Consultation) parseMedicalService(data, 1);
                        array.add((T) newConsultation);
                        break;
                    case "radiographies.csv":
                        Radiography newRadiography = (Radiography) parseMedicalService(data, 3);
                        array.add((T) newRadiography);
                        break;
                    case "tests.csv":
                        Test newTest = (Test) parseMedicalService(data, 2);
                        array.add((T) newTest);
                        break;
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static Person parsePerson(String[] data, int type) {
        String fName = data[0];
        String lName = data[1];
        String phoneNumber = data[2];
        String eMail = data[3];
        String cnp = data[4];

        switch(type) {
            //Citire pacient
            case 1:
                String birthDateString = data[5];
                Date birthDate = null;

                try {
                    birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDateString);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                //In cazul in care pacientul nu are o fisa medicala
                //toate cele 3 campuri specifice fisei medicale vor fi "null" (in csv)
                if(data[6].equals("null")) {
                    return new Patient(fName, lName, phoneNumber, eMail, cnp, null, birthDate);
                }
                else {
                    boolean smoker = Boolean.parseBoolean(data[6]);
                    Set<String> alergies;
                    Set<String> diseases;

                    //In cazul in care pacientul nu are alergii
                    //campul ptr alergii este "null" (in csv)
                    if(data[7].equals("null")) {
                        alergies = new HashSet<>();
                    }
                    else {
                        String[] alergiesArray = data[7].substring(1, data[7].length()-1).split(":");
                        alergies = new HashSet<>(Arrays.asList(alergiesArray));
                    }

                    //In cazul in care pacientul nu sufera de boli cronice
                    //campul ptr boli cronice este "null" (in csv)
                    if(data[8].equals("null")) {
                        diseases = new HashSet<>();
                    }
                    else {
                        String[] diseasesArray = data[8].substring(1, data[8].length()-1).split(":");
                        diseases = new HashSet<>(Arrays.asList(diseasesArray));
                    }

                    MedicalRecord newMedicalRecord = new MedicalRecord(smoker, alergies, diseases);
                    return new Patient(fName, lName, phoneNumber, eMail, cnp, newMedicalRecord, birthDate);
                }
                //break;
                //Citire angajati
            default:
                String[] scheduleString = data[5].substring(1, data[5].length()-1).split(":");
                WorkDay[] schedule = new WorkDay[5];

                int i = 0;
                for(String workDay : scheduleString) {
                    String[] hours = workDay.substring(1, workDay.length()-1).split(" ");
                    int startHour = Integer.parseInt(hours[0]);
                    int endHour = Integer.parseInt(hours[1]);
                    schedule[i] = new WorkDay(startHour, endHour);
                    i += 1;
                }

                //Determinam daca citim o asistenta sau un doctor
                if(data[6].equals("Nurse")) {
                    return new Nurse(fName, lName, phoneNumber, eMail, cnp, schedule);
                }
                else {
                    Specialization docSpecialization = null;
                    for(Specialization s : Database.getSpecializations()) {
                        if(s.getSpecializationName().equals(data[6])) {
                            docSpecialization = s;
                            break;
                        }
                    }

                    return new Doctor(fName, lName, phoneNumber, eMail, cnp, schedule, docSpecialization);
                }
        }
    }

    public static MedicalS parseMedicalService(String[] data, int type) {
        Float price = Float.parseFloat(data[1]);

        switch(type){
            case 1:
                Specialization consultationSpec = null;

                for(Specialization s : Database.getSpecializations()) {
                    if(s.getSpecializationName().equals(data[0])) {
                        consultationSpec = s;
                        break;
                    }
                }

                return new Consultation(price, consultationSpec);

            case 2:
                TestType tt = null;

                for(TestType t : TestType.values()) {
                    if(t.toString().equals(data[0])) {
                        tt = t;
                        break;
                    }
                }

                return new Test(price, tt);

            default:
                RadiographyArea area = null;

                for(RadiographyArea ra : RadiographyArea.values()) {
                    if(ra.toString().equals(data[0])) {
                        area = ra;
                        break;
                    }
                }

                return new Radiography(price, area);
        }
    }

    public static Appointment parseAppointment(String[] data) {
        Date appointmentDate = null;
        Set<MedicalS> services = new HashSet<>();
        try {
            appointmentDate = new SimpleDateFormat("dd/MM/yyyy").parse(data[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int hour = Integer.parseInt(data[1]);

        if(!data[2].equals("null")) {
            for(Consultation c : Database.getConsultations()) {
                if(c.getSpecialization().toString().equals(data[2])) {
                    services.add(c);
                    break;
                }
            }
        }

        if(!data[3].equals("null")) {
            String[] radiographies = data[3].substring(1, data[3].length()-1).split(" ");

            for(String radiography : radiographies) {
                for(Radiography clinicRadiography : Database.getRadiographies()) {
                    if(clinicRadiography.getArea().toString().equals(radiography)) {
                        services.add(clinicRadiography);
                        break;
                    }
                }
            }
        }

        if(!data[4].equals("null")) {
            String[] tests = data[4].substring(1, data[4].length()-1).split(" ");

            for(String test : tests) {
                for(Test clinicTest : Database.getTests()) {
                    if(clinicTest.getType().toString().equals(test)) {
                        services.add(clinicTest);
                        break;
                    }
                }
            }
        }

        Appointment newAppointment = new Appointment(appointmentDate, services);
        newAppointment.setHour(hour);
        newAppointment.setPatientNumericCode(data[5]);
        if(!data[6].equals("null")) {
            newAppointment.setDoctorNumericCode(data[6]);
        }

        if(!data[7].equals("null")) {
            newAppointment.setNurseNumericCode(data[7]);
        }

        for(Patient p : Database.getPatients()) {
            if(p.getCnp().equals(newAppointment.getPatientNumericCode())) {
                p.addAppointment(newAppointment);
                break;
            }
        }

        for(Employee e : Database.getEmployees()) {
            if(e.getCnp().equals(newAppointment.getDoctorNumericCode())) {
                e.addAppointment(newAppointment);
                break;
            }
        }

        if(!data[7].equals("null")) {
            for(Employee e : Database.getEmployees()) {
                if(e.getCnp().equals(newAppointment.getNurseNumericCode())) {
                    e.addAppointment(newAppointment);
                    break;
                }
            }
        }

        return newAppointment;
    }

    //Citeste toate informatiile din fisierele CSV
    public static void readAllFromCSV() {
        Reader.readCSV(Database.getSpecializations(), "specializations.csv");
        Reader.readCSV(Database.getConsultations(), "consultations.csv");
        Reader.readCSV(Database.getRadiographies(), "radiographies.csv");
        Reader.readCSV(Database.getTests(), "tests.csv");
        Reader.readCSV(Database.getPatients(), "patients.csv");
        Reader.readCSV(Database.getEmployees(), "employees.csv");
        Reader.readCSV(Database.getAppointments(), "appointments.csv");
    }
}
