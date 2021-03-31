package model.person;

import model.medical_services.Appointment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Employee extends Person {
    private WorkDay schedule[];
    private ArrayList<Appointment> appointments = new ArrayList<>();


    public Employee() {

    }

    public Employee(String firstName, String lastName, String phoneNumber, String email, String cnp, WorkDay[] schedule, ArrayList<Appointment> appointments) {
        super(firstName, lastName, phoneNumber, email, cnp);
        this.schedule = schedule;
        this.appointments = appointments;
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

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment a){
        appointments.add(a);
    }

    @Override
    public String toString() {
        return super.toString();
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
        Employee employee = (Employee) o;
        return Arrays.equals(schedule, employee.schedule) && appointments.equals(employee.appointments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), appointments);
        result = 31 * result + Arrays.hashCode(schedule);
        return result;
    }
}
