package model.medical_services;

import model.person.Specialization;

import java.util.Objects;

public class Consultation extends MedicalService {
    Specialization specialization;

    public Consultation(float price, Specialization specialization) {
        super(price);
        this.specialization = specialization;
    }

    public Consultation(int id, float price, Specialization specialization) {
        super(id, price);
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
        Consultation that = (Consultation) o;
        return specialization.equals(that.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), specialization);
    }
}
