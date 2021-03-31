package model.person;

public class Nurse extends Employee{

    public Nurse() {
    }

    public Nurse(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule) {
        super(firstName, lastName, phoneNumber, email, cnp, schedule);
    }

    @Override
    public String toString() {
        return super.toString() +
                "Nurse" + "\n";
    }
}
