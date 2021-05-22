package model.medical_services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Appointment {
    private Date date;
    private int hour;
    private Set<MedicalService> medicalServices = new HashSet<>();
    private String patientNumericCode;
    private String doctorNumericCode;
    private String nurseNumericCode;


    public Appointment() {

    }

    public Appointment(Date date, Set<MedicalService> medicalServices) {
        this.date = date;
        this.medicalServices = medicalServices;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<MedicalService> getMedicalServices() {
        return medicalServices;
    }

    public void setMedicalServices(Set<MedicalService> medicalServices) {
        this.medicalServices = medicalServices;
    }

    public void addMedicalService(MedicalService medicalService) {
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

    //TO DO: returns the price of the appointment
    public float calculateAppointmentPrice() {
        float price = 0;

        for (MedicalService s : medicalServices){
            price += s.getPrice();
        }

        return price;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd/MM/yyyy");
        result.append("Appointment on: ").append(dateFormat.format(date)).append("\n");
        result.append("Appointment hour: ").append(hour).append(":00\n");

        result.append("The following services are included:\n");

        for(MedicalService s : medicalServices) {
            result.append(s.toString());
        }

        result.append("\nTotal price: ").append(calculateAppointmentPrice()).append(" RON\n");
        return result.toString();
    }
}
