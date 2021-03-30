package model.medical_services;

public class Test extends MedicalS{
    private TestType type;


    public Test() {

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
        return type.name() + "Test\n" + super.toString();
    }
}
