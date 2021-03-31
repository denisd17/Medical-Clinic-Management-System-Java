package model.person;

import model.medical_services.MedicalS;

import java.util.Comparator;

public class SpendingsSorter implements Comparator<Patient> {
    @Override
    public int compare(Patient p1, Patient p2) {
        float t1 = p1.getTotalSpent();
        float t2 = p2.getTotalSpent();

        if(t1 < t2){
            return -1;
        }
        if(t1 > t2){
            return 1;
        }
        return 0;
    }
}