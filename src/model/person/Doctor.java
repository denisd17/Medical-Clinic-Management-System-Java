package model.person;

import model.medical_services.Appointment;

import java.util.ArrayList;
import java.util.Objects;

public class Doctor extends Employee {
    private Specialization specializare;


    public Doctor() {

    }

    public Doctor(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule, ArrayList<Appointment> appointments, Specialization specializare) {
        super(firstName, lastName, phoneNumber, email, cnp, schedule, appointments);
        this.specializare = specializare;
    }

    public Doctor(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule, Specialization specializare) {
        super(firstName, lastName, phoneNumber, email, cnp, schedule);
        this.specializare = specializare;
    }

    public Specialization getSpecializare() {
        return specializare;
    }

    public void setSpecializare(Specialization specializare) {
        this.specializare = specializare;
    }

    @Override
    public String toString() {
        return super.toString() +
                specializare + " Doctor\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Doctor doctor = (Doctor) o;
        return specializare.equals(doctor.specializare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specializare);
    }
}
