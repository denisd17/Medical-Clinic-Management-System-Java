package model.medical_services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Appointment {
    private Date date;
    private int hour;
    private Set<MedicalS> medicalServices = new HashSet<>();
    private String patientNumericCode;
    private String doctorNumericCode;
    private String nurseNumericCode;

    public Appointment() {

    }

    public Appointment(Date date, Set<MedicalS> medicalServices) {
        this.date = date;
        this.medicalServices = medicalServices;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<MedicalS> getMedicalServices() {
        return medicalServices;
    }

    public void setMedicalServices(Set<MedicalS> medicalServices) {
        this.medicalServices = medicalServices;
    }

    public void addMedicalService(MedicalS medicalService) {
        medicalServices.add(medicalService);
    }

    public String getPatientNumericCode() {
        return patientNumericCode;
    }

    public void setPatientNumericCode(String patientNumericCode) {
        this.patientNumericCode = patientNumericCode;
    }

    public String getDoctorNumericCode() {
        return doctorNumericCode;
    }

    public void setDoctorNumericCode(String doctorNumericCode) {
        this.doctorNumericCode = doctorNumericCode;
    }

    public String getNurseNumericCode() {
        return nurseNumericCode;
    }

    public void setNurseNumericCode(String nurseNumericCode) {
        this.nurseNumericCode = nurseNumericCode;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    //TO DO - returns the price of the appointment
    public float calculateAppointmentPrice() {
        return 0;
    }
}
