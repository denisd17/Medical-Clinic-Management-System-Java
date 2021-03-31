package model.medical_services;

import java.util.Objects;

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

        Radiography that = (Radiography) o;
        return area == that.area;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), area);
    }
}
