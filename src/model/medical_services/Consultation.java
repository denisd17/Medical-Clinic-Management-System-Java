package model.medical_services;

import model.person.Specialization;

public class Consultation extends MedicalS{
    Specialization specialization;

    public Consultation(float price, int duration, Specialization specialization) {
        super(price, duration);
        this.specialization = specialization;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return specialization.toString() + " Consultation\n" + super.toString();
    }
}
