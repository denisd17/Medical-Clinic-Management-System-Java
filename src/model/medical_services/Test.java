package model.medical_services;

import java.util.Objects;

public class Test extends MedicalService {
    private TestType type;


    public Test() {

    }

    public Test(Float price, TestType type) {
        super(price);
        this.type = type;
    }

    public Test(TestType type) {
        this.type = type;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name() + " Test\n" + super.toString();
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
        Test test = (Test) o;
        return type == test.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
