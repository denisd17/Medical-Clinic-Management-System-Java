package service;

import model.medical_services.*;
import model.person.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Database {
    private static Database single_instance = null;
    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static ArrayList<Specialization> specializations = new ArrayList<>();
    private static ArrayList<Consultation> consultations = new ArrayList<>();
    private static ArrayList<Radiography> radiographies = new ArrayList<>();
    private static ArrayList<Test> tests = new ArrayList<>();
    private static ArrayList<Appointment> appointments = new ArrayList<>();


    private Database() {

    }

    public static Database getInstance() {
        if(single_instance == null)
            single_instance = new Database();

        return single_instance;
    }

    public static ArrayList<Patient> getPatients() {
        return patients;
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static ArrayList<Consultation> getConsultations() {
        return consultations;
    }

    public static ArrayList<Radiography> getRadiographies() {
        return radiographies;
    }

    public static ArrayList<Test> getTests() {
        return tests;
    }

    public static ArrayList<Specialization> getSpecializations() {
        return specializations;
    }

    public static ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public static void setAppointments(ArrayList<Appointment> appointments) {
        Database.appointments = appointments;
    }

    // Stergerea unei programari
    public static void deleteAppointment(Appointment a) {
        String patientNumeric = a.getPatientNumericCode();
        String doctorNumeric = a.getDoctorNumericCode();
        String nurseNumeric = a.getNurseNumericCode();

        for (Patient p : patients) {
            if(p.getCnp().equals(patientNumeric)) {
                p.getAppointments().remove(a);
                break;
            }
        }

        if(doctorNumeric != null) {
            for (Employee d : employees) {
                if(d.getCnp().equals(patientNumeric)) {
                    d.getAppointments().remove(a);
                    break;
                }
            }
        }

        if(nurseNumeric != null) {
            for (Employee n : employees) {
                if(n.getCnp().equals(patientNumeric)) {
                    n.getAppointments().remove(a);
                    break;
                }
            }
        }

        appointments.remove(a);

    }
}
