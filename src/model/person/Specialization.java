package model.person;

import java.util.Objects;

public class Specialization {
    private String specializationName;


    public Specialization(String specializationName) {
        this.specializationName = specializationName;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    @Override
    public String toString() {
        return specializationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Specialization that = (Specialization) o;
        return specializationName.equals(that.specializationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specializationName);
    }
}
