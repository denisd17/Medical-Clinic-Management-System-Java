package service;

import model.medical_services.Appointment;
import model.medical_services.Consultation;
import model.medical_services.Radiography;
import model.medical_services.Test;
import model.person.*;

import java.util.ArrayList;

public class Database {
    private static Database single_instance = null;
    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static ArrayList<Specialization> specializations = new ArrayList<>();
    private static ArrayList<Consultation> consultations = new ArrayList<>();
    private static ArrayList<Radiography> radiographies = new ArrayList<>();
    private static ArrayList<Test> tests = new ArrayList<>();


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

    }
}
