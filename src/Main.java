import model.person.MedicalRecord;
import model.person.Patient;
import service.Database;
import service.EmployeeService;
import service.PatientService;

import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        EmployeeService e = new EmployeeService();
        e.addEmployee();
        //e.addEmployee();
        e.showEmployees();

        System.out.println(Database.getEmployees().size());
    }
}
