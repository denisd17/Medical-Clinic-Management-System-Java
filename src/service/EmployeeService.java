package service;

import model.medical_services.Appointment;
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


    //Meniu gestionare anagajati
    public void menu() {
        int option = 0;
        System.out.println("Choose an option.");

        while(option != 6){
            System.out.println("1. List all the registered employees");
            System.out.println("2. Search and display employee info");
            System.out.println("3. Add a new employee");
            System.out.println("4. Update employee info");
            System.out.println("5. Remove employee");
            System.out.println("6. Return to main menu");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    showEmployees();
                    break;
                case 2:
                    showEmployee();
                    break;
                case 3:
                    addEmployee();
                    Audit.writeToAudit(1,2);
                    break;
                case 4:
                    updateEmployee();
                    Audit.writeToAudit(2, 2);
                    break;
                case 5:
                    deleteEmployee();
                    Audit.writeToAudit(3, 2);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid option!");

            }
            //Actualizare fisiere CSV dupa fiecare modificare
            if(option != 1 && option != 2 && option != 6)
                Writer.writeAllToCSV();
        }

    }

    //Adaugare nou angajat
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

    //Afisare lista angajati
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

    //Afisare informatii angajat (cautare dupa CNP)
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


    //Actualizare informatii angajat
    //TO DO: Actualizare fisa medicala
    public void updateEmployee() {
        System.out.println("Enter the employee numeric code: ");
        String cnp = scanner.nextLine();
        Employee e = searchEmployee(cnp);
        int option = 0;

        if(e == null){
            System.out.println("There is no employee with the provided numeric code");
        }
        else {
            while(option != 7) {
                System.out.println("What do you wish to update?");
                System.out.println("1. First name");
                System.out.println("2. Last name");
                System.out.println("3. Phone number");
                System.out.println("4. Numeric code");
                System.out.println("5. Schedule");
                if(e instanceof Doctor) {
                    System.out.println("6. Specialization");
                    System.out.println("7. Exit");
                }
                else {
                    System.out.println("6. Exit");
                }
                option = scanner.nextInt();
                scanner.nextLine();

                switch(option) {
                    case 1:
                        enterFirstName(e);
                        break;
                    case 2:
                        enterLastName(e);
                        break;
                    case 3:
                        enterPhoneNumber(e);
                        break;
                    case 4:
                        enterCnp(e);
                        break;
                    case 5:
                        enterSchedule(e);
                    case 6:
                        if(e instanceof Doctor) {
                            Specialization empSpec = ((Doctor) e).getSpecializare();
                            enterSpecialization(e);
                            updateSpecializationAndService(empSpec);
                        }
                        else{
                            option = 7;
                        }
                    case 7:
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        }

    }

    //Stergere angajat
    //TO DO: La stergere angajat, anulare a tuturor programarilor (dupa implementare programari)
    public void deleteEmployee() {
        System.out.println("Please enter the employee's CNP: ");
        String cnp = scanner.nextLine();
        Employee employeeToBeDeleted = searchEmployee(cnp);
        Specialization empSpec = null;


        if(employeeToBeDeleted == null) {
            System.out.println("There is no employee with the CNP: " + cnp);
        }
        else {
            if(employeeToBeDeleted instanceof Doctor){
                empSpec = ((Doctor) employeeToBeDeleted).getSpecializare();
            }

            // Stergerea programarilor aferente
            Iterator<Appointment> it = employeeToBeDeleted.getAppointments().iterator();
            while (it.hasNext()) {
                Appointment a = it.next();
                it.remove();
                Database.deleteAppointment(a);
            }

            employees.remove(employeeToBeDeleted);

            //Stergerea consultatiei aferente din lista de servicii,
            //in cazul in care nu mai exista nici un alt doctor cu aceeasi specializare
            if(empSpec != null)
                updateSpecializationAndService(empSpec);

            System.out.println("The employee has been deleted.");
        }
    }

    //Functie utilizata in cazul updatarii / stergerii unui angajat de tip doctor
    //In cazul in care nu mai exista nici un alt doctor cu acea specializare
    //Va fi stearsa consultatia aferenta, precum si specializarea din lista de specializari
    //TO DO: Actualizare programari (dupa implementare programari)
    private void updateSpecializationAndService(Specialization s){
        boolean gasit = false;

        for(int i = 0; i < employees.size() && !gasit; i++){
            if(employees.get(i) instanceof Doctor) {
                if(s.equals(((Doctor) employees.get(i)).getSpecializare()))
                    gasit = true;
            }
        }

        if(!gasit) {
            for(int i = 0; i < consultations.size(); i++) {
                if(consultations.get(i).getSpecialization().equals(s)){
                    consultations.remove(consultations.get(i));
                    break;
                }
            }
            specializations.remove(s);
        }
    }
    //Cautare angajat dupa CNP
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

    //Functii utilizate pentru creare / updatare angajat
    //Prenume
    private void enterFirstName(Employee e) {
        System.out.print("Employee's first name: ");
        String fName = scanner.nextLine();
        e.setFirstName(fName);
    }

    //Nume
    private void enterLastName(Employee e) {
        System.out.print("Employee's last name: ");
        String lName = scanner.nextLine();
        e.setLastName(lName);
    }

    //Numar de telefon
    private void enterPhoneNumber(Employee e) {
        System.out.print("Employee's phone number: ");
        String pNumber = scanner.nextLine();
        e.setPhoneNumber(pNumber);
    }

    //Email
    private void enterEmail(Employee e) {
        System.out.print("Employee's email: ");
        String pEmail = scanner.nextLine();
        e.setEmail(pEmail);
    }

    //CNP
    private void enterCnp(Employee e) {
        System.out.print("Employee's CNP: ");
        String pCnp = scanner.nextLine();
        e.setCnp(pCnp);
    }

    //Program de lucru
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
            //System.out.println(index);
            System.out.print("Enter the schedule for ");
            System.out.println(weekDays[index]);

            System.out.println("Starting hour: ");
            startHour = scanner.nextInt();
            System.out.println("End hour: ");
            endHour = scanner.nextInt();
            scanner.nextLine();

            schedule[index].setStartHour(startHour);
            schedule[index].setEndHour(endHour);

        }

        e.setSchedule(schedule);
    }

    //Specializare doctor
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
            Consultation newConsultation = new Consultation(100, newSpecialization);
            //System.out.println(newConsultation);
            consultations.add(newConsultation);
            d.setSpecializare(newSpecialization);
        }
        else {
            d.setSpecializare(specializations.get(option - 2));
        }
    }
}