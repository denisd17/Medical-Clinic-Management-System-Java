package model.medical_services;

import java.util.Objects;

public abstract class MedicalService {
    private float price;


    public MedicalService() {

    }

    public MedicalService(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "price = " + price + " RON\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MedicalService medicalS = (MedicalService) o;
        return Float.compare(medicalS.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
