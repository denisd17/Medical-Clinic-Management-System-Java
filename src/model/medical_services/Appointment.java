package model.medical_services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Appointment {
    private Date date;
    private Set<MedicalS> medicalServices = new HashSet<>();

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

    //TO DO - returns the price of the appointment
    public float calculateAppointmentPrice() {
        return 0;
    }
}
