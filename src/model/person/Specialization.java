package model.person;

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
}
