package service;

import model.medical_services.Consultation;
import model.medical_services.Radiography;
import model.medical_services.Test;
import model.person.Employee;
import model.person.Patient;
import model.person.Specialization;

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
}
