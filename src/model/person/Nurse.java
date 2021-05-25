package model.person;

import model.medical_services.Appointment;

import java.util.ArrayList;

public class Nurse extends Employee{

    public Nurse() {
    }

    public Nurse(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule) {
        super(firstName, lastName, phoneNumber, email, cnp, schedule);
    }

    public Nurse(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule, ArrayList<Appointment> appointments) {
        super(firstName, lastName, phoneNumber, email, cnp, schedule, appointments);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Nurse" + "\n";
    }
}
