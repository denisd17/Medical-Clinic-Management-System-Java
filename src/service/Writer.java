package service;

import model.medical_services.*;
import model.person.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Writer {
    private static Writer single_instance = null;

    private Writer() {

    }

    public static Writer getInstance() {
        if(single_instance == null)
            single_instance = new Writer();

        return single_instance;
    }

    public static <T> void writeToCSV(ArrayList<T> array, String filename) {

        String fullPath = "src/resources/" + filename;

        try (BufferedWriter br = new BufferedWriter(new FileWriter(fullPath))) {
            StringBuilder toWrite = new StringBuilder();

            switch(filename){
                case "Persons/specializations.csv":
                    br.append("Name\n");

                    for(Object o : array) {
                        Specialization specToWrite = (Specialization) o;
                        toWrite.append(specToWrite.toString()).append("\n");
                        br.append(toWrite);
                        toWrite.setLength(0);
                    }
                    br.flush();
                    br.close();
                    break;
                case "Persons/employees.csv":
                    br.append("First Name,Last Name,Phone Number,eMail,CNP,Schedule,Job\n");

                    for(Object o : array) {
                        //toWrite.append("\n");

                        Employee employeeToWrite = (Employee) o;
                        toWrite.append(employeeToWrite.getFirstName());
                        toWrite.append(",");

                        toWrite.append(employeeToWrite.getLastName());
                        toWrite.append(",");

                        toWrite.append(employeeToWrite.getPhoneNumber());
                        toWrite.append(",");

                        toWrite.append(employeeToWrite.getEmail());
                        toWrite.append(",");

                        toWrite.append(employeeToWrite.getCnp());
                        toWrite.append(",");

                        WorkDay[] schedule = employeeToWrite.getSchedule();
                        toWrite.append('"');
                        for(int i = 0; i < 4; i++){
                            toWrite.append(schedule[i].toString());
                            toWrite.append(":");
                        }
                        toWrite.append(schedule[4].toString());
                        toWrite.append('"');
                        toWrite.append(",");

                        if(employeeToWrite instanceof Nurse){
                            toWrite.append("Nurse");
                        }
                        else {
                            Doctor doctorToWrite = (Doctor) o;
                            toWrite.append(doctorToWrite.getSpecializare().toString());
                        }
                        toWrite.append("\n");
                        br.append(toWrite);
                        toWrite.setLength(0);
                    }

                    br.flush();
                    br.close();
                    break;
                case "Persons/patients.csv":
                    br.append("First Name,Last Name,Phone Number,eMail,CNP,Birthdate,Smoker,Alergies,Diseases\n");

                    for(Object o : array) {
                        Patient patientToWrite = (Patient) o;
                        //toWrite.append("\n");

                        toWrite.append(patientToWrite.getFirstName());
                        toWrite.append(",");

                        toWrite.append(patientToWrite.getLastName());
                        toWrite.append(",");

                        toWrite.append(patientToWrite.getPhoneNumber());
                        toWrite.append(",");

                        toWrite.append(patientToWrite.getEmail());
                        toWrite.append(",");

                        toWrite.append(patientToWrite.getCnp());
                        toWrite.append(",");

                        toWrite.append(new SimpleDateFormat("dd/MM/yyyy").format(patientToWrite.getBirthDate()));
                        toWrite.append(",");

                        //Scriere medical record
                        if(patientToWrite.getMedicalRecord() != null) {
                            toWrite.append(patientToWrite.getMedicalRecord().getSmoker());
                            toWrite.append(",");

                            if(!patientToWrite.getMedicalRecord().getAlergies().isEmpty()) {
                                toWrite.append('"');
                                for(String s : patientToWrite.getMedicalRecord().getAlergies()) {
                                    toWrite.append(s);
                                    toWrite.append(':');
                                }
                                toWrite.append('"');
                            }
                            else {
                                toWrite.append("null");
                            }
                            toWrite.append(",");

                            if(!patientToWrite.getMedicalRecord().getChronicDiseases().isEmpty()) {
                                toWrite.append('"');
                                for(String s : patientToWrite.getMedicalRecord().getChronicDiseases()) {
                                    toWrite.append(s);
                                    toWrite.append(':');
                                }
                                toWrite.append('"');
                            }
                            else {
                                toWrite.append("null");
                            }
                            //toWrite.append(",");

                        }
                        else {
                            toWrite.append("null,null,null");
                        }
                        toWrite.append("\n");
                        br.append(toWrite);
                        toWrite.setLength(0);
                    }

                    br.flush();
                    br.close();
                    break;
                case "Appointments/appointments.csv":
                    br.append("Date,Hour,Consultation,Radiographies,Tests,Patient CNP,Doctor CNP,Nurse CNP\n");

                    for(Object o : array) {
                        Consultation c = null;
                        ArrayList<Radiography> radiographies = new ArrayList<>();
                        ArrayList<Test> tests = new ArrayList<>();

                        Appointment appointmentToWrite = (Appointment) o;

                        //toWrite.append("\n");

                        toWrite.append(new SimpleDateFormat("dd/MM/yyyy").format(appointmentToWrite.getDate()));
                        toWrite.append(",");

                        toWrite.append(appointmentToWrite.getHour());
                        toWrite.append(",");

                        for(MedicalService service : appointmentToWrite.getMedicalServices()) {
                            if(service instanceof Consultation) {
                                c = (Consultation) service;
                            }
                            else if(service instanceof Radiography) {
                                radiographies.add((Radiography) service);
                            }
                            else {
                                tests.add((Test) service);
                            }
                        }

                        if(c == null) {
                            toWrite.append("null");
                        }
                        else {
                            toWrite.append(c.getSpecialization().toString());
                        }
                        toWrite.append(",");

                        if(radiographies.isEmpty()) {
                            toWrite.append("null");
                        }
                        else {
                            toWrite.append('"');
                            for(Radiography r : radiographies) {
                                toWrite.append(r.getArea().toString());
                                toWrite.append(" ");
                            }
                            toWrite.deleteCharAt(toWrite.length()-1);
                            toWrite.append('"');
                        }
                        toWrite.append(",");

                        if(tests.isEmpty()) {
                            toWrite.append("null");
                        }
                        else {
                            toWrite.append('"');
                            for(Test t : tests) {
                                toWrite.append(t.getType().toString());
                                toWrite.append(" ");
                            }
                            toWrite.deleteCharAt(toWrite.length()-1);
                            toWrite.append('"');
                        }
                        toWrite.append(",");

                        toWrite.append(appointmentToWrite.getPatientNumericCode());
                        toWrite.append(",");

                        if(appointmentToWrite.getDoctorNumericCode() != null) {
                            toWrite.append(appointmentToWrite.getDoctorNumericCode());
                        }
                        else {
                            toWrite.append("null");
                        }
                        toWrite.append(",");

                        if(appointmentToWrite.getNurseNumericCode() != null) {
                            toWrite.append(appointmentToWrite.getNurseNumericCode());
                        }
                        else {
                            toWrite.append("null");
                        }
                        toWrite.append("\n");

                        br.append(toWrite);
                        toWrite.setLength(0);

                    }
                    br.flush();
                    br.close();
                    break;
                case "MedicalServices/consultations.csv":
                    br.append("Specialization,Price\n");

                    for(Object o : array) {
                        Consultation consultationToWrite = (Consultation) o;

                        //toWrite.append("\n");

                        toWrite.append(consultationToWrite.getSpecialization().toString());
                        toWrite.append(",");

                        toWrite.append(consultationToWrite.getPrice());
                        toWrite.append("\n");

                        br.append(toWrite);
                        toWrite.setLength(0);
                    }
                    br.flush();
                    br.close();
                    break;
                case "MedicalServices/radiographies.csv":
                    br.append("Area,Price\n");

                    for(Object o : array) {
                        Radiography radiographyToWrite = (Radiography) o;

                        //toWrite.append("\n");

                        toWrite.append(radiographyToWrite.getArea().toString());
                        toWrite.append(",");

                        toWrite.append(radiographyToWrite.getPrice());
                        toWrite.append("\n");

                        br.append(toWrite);
                        toWrite.setLength(0);
                    }
                    br.flush();
                    br.close();
                    break;
                case "MedicalServices/tests.csv":
                    br.append("Type,Price\n");

                    for(Object o : array) {
                        Test testToWrite = (Test) o;

                        //toWrite.append("\n");

                        toWrite.append(testToWrite.getType().toString());
                        toWrite.append(",");

                        toWrite.append(testToWrite.getPrice());
                        toWrite.append("\n");

                        br.append(toWrite);
                        toWrite.setLength(0);
                    }
                    br.flush();
                    br.close();
                    break;
            }


        }

        catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    //Scrie toate informatiile in fisierele CSV
    public static void writeAllToCSV() {
        Writer.writeToCSV(Database.getSpecializations(), "Persons/specializations.csv");
        Writer.writeToCSV(Database.getPatients(), "Persons/patients.csv");
        Writer.writeToCSV(Database.getEmployees(), "Persons/employees.csv");
        Writer.writeToCSV(Database.getAppointments(), "Appointments/appointments.csv");
        Writer.writeToCSV(Database.getTests(), "MedicalServices/tests.csv");
        Writer.writeToCSV(Database.getRadiographies(), "MedicalServices/radiographies.csv");
        Writer.writeToCSV(Database.getConsultations(), "MedicalServices/consultations.csv");
    }
}
