package model.medical_services;

public abstract class MedicalS {
    private float price;
    private int duration;

    public MedicalS() {

    }

    public MedicalS(float price, int duration) {
        this.price = price;
        this.duration = duration;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "price = " + price + " RON\n";
    }
}
