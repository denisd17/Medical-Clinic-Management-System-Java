package model.person;

import java.util.Arrays;

public class Employee extends Person {
    private WorkDay schedule[];

    public Employee() {

    }

    public Employee(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule) {
        super(firstName, lastName, phoneNumber, email, cnp);
        this.schedule = schedule;
    }

    public WorkDay[] getSchedule() {
        return schedule;
    }

    public void setSchedule(WorkDay[] schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
