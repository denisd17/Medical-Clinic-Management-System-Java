package model.person;

import model.medical_services.Appointment;

import java.util.ArrayList;

public class Doctor extends Employee {
    private Specialization specializare;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public Doctor() {

    }

    public Doctor(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule, Specialization specializare, ArrayList<Appointment> appointments) {
        super(firstName, lastName, phoneNumber, email, cnp, schedule);
        this.specializare = specializare;
        this.appointments = appointments;
    }

    public Specialization getSpecializare() {
        return specializare;
    }

    public void setSpecializare(Specialization specializare) {
        this.specializare = specializare;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return super.toString() +
                specializare + " Doctor\n";
    }
}
