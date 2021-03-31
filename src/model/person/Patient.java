package model.person;

import model.medical_services.Appointment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Patient extends Person{
    private MedicalRecord medicalRecord;
    private Date birthDate;
    private ArrayList<Appointment> appointments = new ArrayList<>();


    public Patient() {

    }

    public Patient(String firstName, String lastName, String phoneNumber, String email, String cnp, MedicalRecord medicalRecord, Date birthDate, ArrayList<Appointment> appointments) {
        super(firstName, lastName, phoneNumber, email, cnp);
        this.medicalRecord = medicalRecord;
        this.birthDate = birthDate;
        this.appointments = appointments;
    }

    public Patient(String firstName, String lastName, String phoneNumber, String email, String cnp, MedicalRecord medicalRecord, Date birthDate) {
        super(firstName, lastName, phoneNumber, email, cnp);
        this.medicalRecord = medicalRecord;
        this.birthDate = birthDate;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment a) {
        appointments.add(a);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());

        String birthDateString = new SimpleDateFormat("dd/MM/yyyy").format(birthDate);
        result.append("Birthdate: ").append(birthDateString).append("\n");

        if(medicalRecord == null) {
            result.append("This patient has no medical record available\n");
        }
        else{
            result.append(medicalRecord.toString()).append("\n");
        }

        return result.toString();
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
        Patient patient = (Patient) o;

        return Objects.equals(medicalRecord, patient.medicalRecord)
                && birthDate.equals(patient.birthDate)
                && appointments.equals(patient.appointments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), medicalRecord, birthDate, appointments);
    }
}
