package service;

import model.medical_services.Consultation;
import model.medical_services.MedicalS;
import model.person.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeService {
    Scanner scanner = new Scanner(System.in);
    private final String weekDays[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static ArrayList<Specialization> specializations = Database.getSpecializations();
    private static ArrayList<Employee> employees = Database.getEmployees();
    private static ArrayList<Consultation> consultations = Database.getConsultations();

    public void addEmployee() {
        int option;
        Employee newEmployee;

        System.out.println("Select the employee type: ");
        System.out.println("1. Nurse");
        System.out.println("2. Doctor");
        option = scanner.nextInt();
        scanner.nextLine();
        if (option == 1) {
            newEmployee = new Nurse();
        }
        else {
            newEmployee = new Doctor();
        }

        enterFirstName(newEmployee);
        enterLastName(newEmployee);
        enterPhoneNumber(newEmployee);
        enterEmail(newEmployee);
        enterCnp(newEmployee);
        enterSchedule(newEmployee);

        if(option == 2){
            enterSpecialization(newEmployee);
        }

        employees.add(newEmployee);
    }

    public void showEmployees() {
        if (!employees.isEmpty()) {
            for(Employee e : employees) {
                System.out.println("--------------------");
                System.out.print(e);
            }
        }
        else {
            System.out.println("There are no employees currently working at this clinic.");
        }
    }

    public void showEmployee() {
        System.out.println("Please enter the employee's CNP: ");
        String cnp = scanner.nextLine();
        Employee employeeToBeShown = searchEmployee(cnp);

        if(employeeToBeShown == null) {
            System.out.println("There is no employee with the CNP: " + cnp);
        }
        else {
            System.out.println(employeeToBeShown);
        }
    }

    public void updateEmployee() {

    }

    //TO DO: ANULARE A TUTUROR PROGRAMARILOR PENTRU ACEL ANGAJAT
    //DACA NU MAI EXISTA DOCTORI CU ACEA SPECIALIZARE, STERGEREA SPECIALIZARII
    //HASHCODE SI EQUALS
    public void deleteEmployee() {
        System.out.println("Please enter the employee's CNP: ");
        String cnp = scanner.nextLine();
        Employee employeeToBeDeleted = searchEmployee(cnp);

        if(employeeToBeDeleted == null) {
            System.out.println("There is no employee with the CNP: " + cnp);
        }
        else {
            employees.remove(employeeToBeDeleted);
            System.out.println("The employee has been deleted.");
        }
    }

    private Employee searchEmployee(String cnp) {
        if (!employees.isEmpty()) {
            for(Employee e : employees) {
                if(e.getCnp().equals(cnp)) {
                    return e;
                }
            }
        }
        return null;
    }

    private void enterFirstName(Employee e) {
        System.out.print("Employee's first name: ");
        String fName = scanner.nextLine();
        e.setFirstName(fName);
    }

    private void enterLastName(Employee e) {
        System.out.print("Employee's last name: ");
        String lName = scanner.nextLine();
        e.setLastName(lName);
    }

    private void enterPhoneNumber(Employee e) {
        System.out.print("Employee's phone number: ");
        String pNumber = scanner.nextLine();
        e.setPhoneNumber(pNumber);
    }

    private void enterEmail(Employee e) {
        System.out.print("Employee's email: ");
        String pEmail = scanner.nextLine();
        e.setEmail(pEmail);
    }

    private void enterCnp(Employee e) {
        System.out.print("Employee's CNP: ");
        String pCnp = scanner.nextLine();
        e.setCnp(pCnp);
    }

    private void enterSchedule(Employee e) {
        WorkDay schedule[] = new WorkDay[5];
        for(int i = 0; i < 5; i++) {
            schedule[i] = new WorkDay();
        }
        //Exemplu de input: 135
        //Selecteaza ca zile de munca: Luni, Miercuri, Joi
        System.out.print("Select the employee's work days.");
        System.out.println("Enter each corresponding number and then press enter.");
        for (int i = 0; i < weekDays.length; i++) {
            System.out.print(i+1);
            System.out.print(". ");
            System.out.println(weekDays[i]);
        }

        String workDays = scanner.nextLine();
        Set<Character> workDaysSet = new HashSet<>();

        for (int i = 0; i < workDays.length(); i++) {
            workDaysSet.add(workDays.charAt(i));
        }

        for (Character c : workDaysSet) {
            int startHour;
            int endHour;
            int index = Character.getNumericValue(c) - 1;
            System.out.println(index);
            System.out.print("Enter the schedule for ");
            System.out.println(weekDays[index]);

            startHour = scanner.nextInt();
            endHour = scanner.nextInt();
            scanner.nextLine();

            schedule[index].setStartHour(startHour);
            schedule[index].setEndHour(endHour);

        }

        e.setSchedule(schedule);
    }

    private void enterSpecialization(Employee e) {
        Doctor d = (Doctor) e;
        int option;
        System.out.println("1. Add a new specialization(s)");
        System.out.println("Select from the following specializations:");
        if (specializations.isEmpty()) {
            System.out.println("There are no specializations available!");
            System.out.println("Defaulting to option number 1.");
            option = 1;
        }
        else {
            for(int i = 0; i < specializations.size(); i++) {
                System.out.print(i+2);
                System.out.print(". ");
                System.out.println(specializations.get(i));
            }
            option = scanner.nextInt();
            scanner.nextLine();
        }

        if(option == 1){
            System.out.print("Enter the specialization's name: ");
            String specializationName = scanner.nextLine();
            Specialization newSpecialization = new Specialization(specializationName);
            specializations.add(newSpecialization);
            Consultation newConsultation = new Consultation(100, 0, newSpecialization);
            consultations.add(newConsultation);
            d.setSpecializare(newSpecialization);
        }
        else {
            d.setSpecializare(specializations.get(option - 2));
        }
    }
}

/*






* */
