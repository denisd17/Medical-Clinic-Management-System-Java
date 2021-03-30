package model.medical_services;

public class Radiography extends MedicalS{
    private RadiographyArea area;

    public Radiography() {

    }

    public Radiography(RadiographyArea area) {
        this.area = area;
    }

    public RadiographyArea getArea() {
        return area;
    }

    public void setArea(RadiographyArea area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return area.name() + "Radiography\n" + super.toString();
    }
}
