package service;

import model.medical_services.Appointment;
import model.medical_services.Consultation;
import model.person.*;
import repository.DoctorRepository;
import repository.NurseRepository;
import repository.SpecializationRepository;

import java.util.*;

public class EmployeeService {
    Scanner scanner = new Scanner(System.in);
    private NurseRepository nurseRepository = new NurseRepository();
    private DoctorRepository doctorRepository = new DoctorRepository();
    private SpecializationRepository specializationRepository = new SpecializationRepository();
    private final String weekDays[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static ArrayList<Specialization> specializations = Database.getSpecializations();
    private static ArrayList<Employee> employees = Database.getEmployees();
    private static ArrayList<Consultation> consultations = Database.getConsultations();


    //Meniu gestionare anagajati
    public void menu() {
        int option = 0;
        System.out.println("Choose an option.");

        while(option != 9){
            System.out.println("1. List all the registered nurses");
            System.out.println("2. List all the registered doctors");
            System.out.println("3. Search and display nurse info");
            System.out.println("4. Search and display doctor info");
            System.out.println("5. Add a new employee");
            System.out.println("6. Update employee info");
            System.out.println("7. Remove nurse");
            System.out.println("8. Remove doctor");
            System.out.println("9. Return to main menu");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    showNurses();
                    break;
                case 2:
                    showDoctors();
                    break;
                case 3:
                    showNurse();
                    break;
                case 4:
                    showDoctor();
                    break;
                case 5:
                    addEmployee();
                    Audit.writeToAudit(1,2);
                    break;
                case 6:
                    updateEmployee();
                    Audit.writeToAudit(2, 2);
                    break;
                case 7:
                    deleteNurse();
                    Audit.writeToAudit(3, 2);
                    break;
                case 8:
                    deleteDoctor();
                    Audit.writeToAudit(3, 2);
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Invalid option!");

            }
            //Actualizare fisiere CSV dupa fiecare modificare
            //if(option != 1 && option != 2 && option != 6)
                //Writer.writeAllToCSV();
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

        if(option == 1){
            nurseRepository.insertNurse((Nurse) newEmployee);
        }
        else {
            doctorRepository.insertDoctor((Doctor) newEmployee);
        }
        //employees.add(newEmployee);
    }

    public void showNurses() {
        ArrayList<Nurse> nurses = nurseRepository.getAllNurses();
        if (!nurses.isEmpty()) {
            for(Nurse n : nurses) {
                System.out.println("--------------------");
                System.out.print(n);
            }
        }
        else {
            System.out.println("There are no nurses currently working at this clinic.");
        }
    }

    public void showDoctors() {
        ArrayList<Doctor> doctors = doctorRepository.getAllDoctors();
        if (!doctors.isEmpty()) {
            for(Doctor d : doctors) {
                System.out.println("--------------------");
                System.out.print(d);
            }
        }
        else {
            System.out.println("There are no doctors currently working at this clinic.");
        }
    }

    //Afisare lista angajati - COD ETAPA 2
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


    public void showNurse() {
        System.out.println("Please enter the nurse's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = nurseRepository.checkIfNurseExists(cnp);

        if(!exists) {
            System.out.println("There is no nurse with the CNP: " + cnp);
        }
        else {
            Nurse nurseToBeShown = nurseRepository.getNurse(cnp);
            System.out.println(nurseToBeShown);
        }
    }

    public void showDoctor() {
        System.out.println("Please enter the doctor's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = doctorRepository.checkIfDoctorExists(cnp);

        if(!exists) {
            System.out.println("There is no doctor with the CNP: " + cnp);
        }
        else {
            Doctor doctorToBeShown = doctorRepository.getDoctor(cnp);
            System.out.println(doctorToBeShown);
        }
    }

    //Afisare informatii angajat (cautare dupa CNP) - COD ETAPA 2
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
        int optionType;
        //Employee e = null;
        System.out.println("Select the employee type: ");
        System.out.println("1. Nurse");
        System.out.println("2. Doctor");
        optionType = scanner.nextInt();
        scanner.nextLine();
        boolean valid = true;

        System.out.println("Enter the employee numeric code: ");
        String cnp = scanner.nextLine();

        if(optionType == 1) {
            if(!nurseRepository.checkIfNurseExists(cnp)) {
                valid = false;
            }
        }
        else {
            if(!doctorRepository.checkIfDoctorExists(cnp)) {
                valid = false;
            }
        }

        int option = 0;

        if(!valid){
            System.out.println("There is no employee with the provided numeric code");
        }
        else {
            while (option != 5) {
                System.out.println("What do you wish to update?");
                System.out.println("1. First name");
                System.out.println("2. Last name");
                System.out.println("3. Phone number");
                System.out.println("4. Schedule");
                System.out.println("5. Exit");

                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Employee's first name: ");
                        String fName = scanner.nextLine();
                        if (optionType == 1) {
                            nurseRepository.updateNurseFirstName(cnp, fName);
                        } else {
                            doctorRepository.updateDoctorFirstName(cnp, fName);
                        }
                        break;
                    case 2:
                        System.out.print("Employee's last name: ");
                        String lName = scanner.nextLine();
                        if (optionType == 1) {
                            nurseRepository.updateNurseLastName(cnp, lName);
                        } else {
                            doctorRepository.updateDoctorLastName(cnp, lName);
                        }
                        break;
                    case 3:
                        System.out.print("Employee's phone number: ");
                        String pNumber = scanner.nextLine();
                        if (optionType == 1) {
                            nurseRepository.updateNursePhoneNumber(cnp, pNumber);
                        } else {
                            doctorRepository.updateDoctorPhoneNumber(cnp, pNumber);
                        }
                        break;
                    case 4:
                        WorkDay[] schedule = getUpdatedSchedule();
                        if (optionType == 1) {
                            for (int i = 0; i < 5; i++) {
                                nurseRepository.updateNurseSchedule(cnp, weekDays[i], schedule[i].getStartHour(), schedule[i].getEndHour());
                            }
                        } else {
                            for (int i = 0; i < 5; i++) {
                                doctorRepository.updateDoctorSchedule(cnp, weekDays[i], schedule[i].getStartHour(), schedule[i].getEndHour());
                            }
                        }
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        }
    }
    public void deleteNurse() {
        System.out.println("Please enter the nurse's CNP: ");
        String cnp = scanner.nextLine();

        if(!nurseRepository.checkIfNurseExists(cnp)) {
            System.out.println("There is no nurse with the CNP: " + cnp);
        }
        else {
            nurseRepository.deleteNurse(cnp);
        }
    }

    public void deleteDoctor() {
        System.out.println("Please enter the doctor's CNP: ");
        String cnp = scanner.nextLine();

        if(!doctorRepository.checkIfDoctorExists(cnp)) {
            System.out.println("There is no doctor with the CNP: " + cnp);
        }
        else {
            doctorRepository.deleteDoctor(cnp);
        }

    }
    //Stergere angajat - COD ETAPA 2
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
    //COD ETAPA 2
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
    private WorkDay[] getUpdatedSchedule() {
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
        return schedule;
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
        ArrayList<Specialization> specializations = specializationRepository.getAllSpecializations();
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