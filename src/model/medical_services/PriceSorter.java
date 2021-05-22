package model.medical_services;

import java.util.Comparator;

public class PriceSorter implements Comparator<MedicalService> {
    @Override
    public int compare(MedicalService s1, MedicalService s2) {
        float p1 = s1.getPrice();
        float p2 = s2.getPrice();
        if(p1 < p2){
            return -1;
        }
        if(p1 > p2){
            return 1;
        }
        return 0;
    }
}
