package service;

import model.medical_services.*;
import model.person.*;
import repository.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientService {
    private Scanner scanner = new Scanner(System.in);
    private PatientRepository patientRepository = new PatientRepository();
    private MedicalServiceRepository medicalServiceRepository = new MedicalServiceRepository();
    private DoctorRepository doctorRepository = new DoctorRepository();
    private NurseRepository nurseRepository = new NurseRepository();
    private AppointmentRepository appointmentRepository = new AppointmentRepository();
    private static ArrayList<Patient> patients = Database.getPatients();
    private static ArrayList<Consultation> consultations = Database.getConsultations();
    private static ArrayList<Radiography> radiographies = Database.getRadiographies();
    private static ArrayList<Employee> employees = Database.getEmployees();
    private static ArrayList<Test> tests = Database.getTests();
    private static ArrayList<Appointment> appointments = Database.getAppointments();

    //Meniu gestionare pacienti
    public void menu() {
        int option = 0;
        System.out.println("Choose an option.");

        while(option != 12){
            System.out.println("1. List all the registered patients");
            System.out.println("2. List patients ordered by total money spent");
            System.out.println("3. Search and display patient info");
            System.out.println("4. Display patient appointments");
            System.out.println("5. Add a new patient");
            System.out.println("6. Update patient info");
            System.out.println("7. Remove patient");
            System.out.println("8. Schedule a new appointment");
            System.out.println("9. Edit appointment info");
            System.out.println("10. Show appointment info");
            System.out.println("11. Delete appointment");
            System.out.println("12. Return to main menu");
            option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1:
                    showPatients();
                    break;
                case 2:
                    System.out.println("Ascending or descending (0/1)");
                    int sortOption = scanner.nextInt();
                    scanner.nextLine();
                    showTopPatients(sortOption);
                    break;
                case 3:
                    showPatient();
                    break;
                case 4:
                    showPatientAppointments();
                    break;
                case 5:
                    addPatient();
                    Audit.writeToAudit(1,1);
                    break;
                case 6:
                    updatePatient();
                    Audit.writeToAudit(2, 1);
                    break;
                case 7:
                    deletePatient();
                    Audit.writeToAudit(3, 1);
                    break;
                case 8:
                    schedulePatientAppointment();
                    Audit.writeToAudit(1, 4);
                    break;
                case 9:
                    editPatientAppointment();
                    Audit.writeToAudit(2, 4);
                    break;
                case 10:
                    getPatientAppointment();
                    break;
                case 11:
                    deletePatientAppointment();
                    Audit.writeToAudit(3, 4);
                    break;
                case 12:
                    break;
                default:
                    System.out.println("Invalid option!");

            }
            //Update al fisierelor CSV dupa fiecare modificare
            //if(option != 1 && option != 2 && option != 3 && option != 4 && option != 9)
                //Writer.writeAllToCSV();
        }

    }

    //TO DO: Mutare citire detalii in functii helper, asemanator cu EmployeeService
    //Adaugare pacient nou
    private void addPatient(){
        Patient newPatient = new Patient();

        System.out.println("Patient's first name: ");
        String fName = scanner.nextLine();
        newPatient.setFirstName(fName);

        System.out.println("Patient's last name: ");
        String lName = scanner.nextLine();
        newPatient.setLastName(lName);

        System.out.println("Patient's phone number: ");
        String pNumber = scanner.nextLine();
        newPatient.setPhoneNumber(pNumber);

        System.out.println("Patient's email: ");
        String pEmail = scanner.nextLine();
        newPatient.setEmail(pEmail);

        System.out.println("Patient's CNP: ");
        String pCnp = scanner.nextLine();
        newPatient.setCnp(pCnp);

        System.out.println("Patient's birthdate: ");
        String pBirthDate = scanner.nextLine();
        try {
            Date pDate = new SimpleDateFormat("dd/MM/yyyy").parse(pBirthDate);
            newPatient.setBirthDate(pDate);
        }
        catch (Exception e) {

        }

        System.out.println("Do you wish to also create the patient's medical record? (Y/N)");
        char option = scanner.next().charAt(0);
        scanner.nextLine();
        if (option == 'Y') {
            MedicalRecord newMedicalRecord = new MedicalRecord();
            System.out.println("Is the patient a smoker? (Y/N)");
            option = scanner.next().charAt(0);
            if (option == 'Y') {
                newMedicalRecord.setSmoker(true);
            }
            else {
                newMedicalRecord.setSmoker(false);
            }

            System.out.println("Does the patient have any alergies? (Y/N)");
            option = scanner.next().charAt(0);
            scanner.nextLine();
            if (option == 'Y') {
                System.out.println("Enter each alergy one by one (press enter after each one).");
                System.out.println("When finished, press enter again.");
                String alergy = scanner.nextLine();

                while (!alergy.equals("")) {
                    newMedicalRecord.addAlergy(alergy);
                    alergy = scanner.nextLine();
                }
            }

            System.out.println("Does the patient have any chronic illnesses? (Y/N)");
            option = scanner.next().charAt(0);
            scanner.nextLine();
            if (option == 'Y') {
                System.out.println("Enter each disease one by one (press enter after each one).");
                System.out.println("When finished, press enter again.");
                String disease = scanner.nextLine();

                while (!disease.equals("")) {
                    newMedicalRecord.addDisease(disease);
                    disease = scanner.nextLine();
                }
            }
            newPatient.setMedicalRecord(newMedicalRecord);
        }
        patientRepository.insertPatient(newPatient);
        //patients.add(newPatient);
    }

    //Afisare lista pacienti
    private void showPatients() {
        ArrayList<Patient> patients = patientRepository.getAllPatients();

        if (!patients.isEmpty()) {
            for(Patient p : patients) {
                System.out.println("--------------------");
                System.out.print(p);
            }
        }
        else {
            System.out.println("There are no recorded patients.");
        }
    }

    //Afisare pacient (cautare dupa CNP)
    private void showPatient() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        Patient patientToBeShown = patientRepository.getPatient(cnp);

        if(patientToBeShown == null) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            System.out.println(patientToBeShown);
        }
    }

    private void showTopPatients(int sort) {
        ArrayList<Patient> sortedPatients = patientRepository.getAllPatients();

        if(sortedPatients.isEmpty()) {
            System.out.println("There are no registered patients!");
        }
        else {
            sortedPatients.sort(new SpendingsSorter());

            if(sort == 1){
                Collections.reverse(sortedPatients);
            }

            System.out.print("Patients sorted by total spendings ");
            if(sort == 1){
                System.out.println("[ASCENDING]");
            }
            else {
                System.out.println("[DESCENDING]");
            }

            for(Patient p : sortedPatients){
                System.out.println("--------------------");
                System.out.print(p);
                System.out.print("Total spendings: ");
                System.out.print(p.getTotalSpent());
                System.out.println("RON");
            }
        }
    }

    //Actualizare informatii pacient
    private void updatePatient() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        Patient patientToBeUpdated = patientRepository.getPatient(cnp);

        if(patientToBeUpdated == null) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            int option = 0;
            while(option != 6){
                System.out.println("What info do you wish to update?");
                System.out.println("1. First name");
                System.out.println("2. Last name");
                System.out.println("3. Phone number");
                System.out.println("4. Email");
                System.out.println("5. Birth date");
                System.out.println("6. Exit");
                option = scanner.nextInt();
                scanner.nextLine();

                switch(option) {
                    case 1:
                        System.out.println("Patient's first name: ");
                        String fName = scanner.nextLine();
                        patientRepository.updatePatientFirstName(cnp, fName);
                        //patientToBeUpdated.setFirstName(fName);
                        break;
                    case 2:
                        System.out.println("Patient's last name: ");
                        String lName = scanner.nextLine();
                        patientRepository.updatePatientLastName(cnp, lName);
                        //patientToBeUpdated.setLastName(lName);
                        break;
                    case 3:
                        System.out.println("Patient's phone number: ");
                        String pNumber = scanner.nextLine();
                        patientRepository.updatePatientPhoneNumber(cnp, pNumber);
                        //patientToBeUpdated.setPhoneNumber(pNumber);
                        break;
                    case 4:
                        System.out.println("Patient's email: ");
                        String pEmail = scanner.nextLine();
                        patientRepository.updatePatientEmail(cnp, pEmail);
                        //patientToBeUpdated.setEmail(pEmail);
                        break;
                    case 5:
                        System.out.println("Patient's birthdate: ");
                        String pBirthDate = scanner.nextLine();
                        try {
                            Date pDate = new SimpleDateFormat("dd/MM/yyyy").parse(pBirthDate);
                            java.sql.Date sqlDate = new java.sql.Date(pDate.getTime());
                            patientRepository.updatePatientBirthDate(cnp, sqlDate);
                            //patientToBeUpdated.setBirthDate(pDate);
                        }
                        catch (Exception e) {

                        }
                    case 6:
                        break;
                }
            }

        }
    }

    //Stergere pacient
    private void deletePatient() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = patientRepository.checkIfPatientExists(cnp);

        if(!exists) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            patientRepository.deletePatient(cnp);
            System.out.println("The patient has been deleted.");

            /* COD ETAPA 2
            // Stergerea programarilor aferente
            Iterator<Appointment> it = patientToBeDeleted.getAppointments().iterator();
            while (it.hasNext()) {
                Appointment a = it.next();
                it.remove();
                Database.deleteAppointment(a);
            }
            patients.remove(patientToBeDeleted);

             */
        }
    }

    //Cautare pacient dupa CNP (metoda helper)
    private Patient searchPatient(String cnp) {
        if (!patients.isEmpty()) {
            for(Patient p : patients) {
                if(p.getCnp().equals(cnp)) {
                    return p;
                }
            }
        }
        return null;
    }

    private void schedulePatientAppointment() {
        ArrayList<Patient> patients = patientRepository.getAllPatients();

        if(patients.isEmpty()){
            System.out.println("There are no registered patients");
            return;
        }

        Patient p = null;
        String cnp;

        while(p == null) {
            System.out.println("Enter the patient's numeric code");
            cnp = scanner.nextLine();
            p = patientRepository.getPatient(cnp);
            addAppointment(p);
        }
    }

    private void showPatientAppointments() {
        Patient p = null;
        String cnp;


        System.out.println("Enter the patient's numeric code");
        cnp = scanner.nextLine();
        p = patientRepository.getPatient(cnp);

        if(p == null) {
            System.out.print("There is no patient with the numeric code ");
            System.out.println(cnp);
        }
        else {
            ArrayList<Appointment> patientAppointments = p.getAppointments();

            if(patientAppointments.isEmpty()) {
                System.out.println("This patient has no appointments!");
            }
            else {
                for(Appointment a : patientAppointments){
                    System.out.println("-------------------");
                    System.out.println(a);
                }
                System.out.println();
            }
        }
    }

    private void addAppointment(Patient p) {
        ArrayList<Consultation> consultations = medicalServiceRepository.getConsultations();
        ArrayList<Radiography> radiographies = medicalServiceRepository.getRadiographies();
        ArrayList<Test> tests = medicalServiceRepository.getTests();
        Appointment newAppointment = new Appointment();
        newAppointment.setPatientNumericCode(p.getCnp());
        boolean consultation = false;
        boolean investigation = false;
        Specialization spec = null;
        int option = 0;
        int index;
        int result;
        boolean scheduled = false;

        while(option != 4){
            System.out.println("Please select the type of service you want:");
            if(!consultation){
                System.out.println("1. Consultation");
            }
            else {
                System.out.println("1. Consultation already added to the appointment");
            }
            System.out.println("2. Test");
            System.out.println("3. Radiography");
            if(consultation || investigation) {
                System.out.println("4. Finish and choose appointment date");
            }

            option = scanner.nextInt();
            scanner.nextLine();

            // Adaugare servicii medicale consultatie
            switch(option){
                case 1:
                    if(!consultation){
                        System.out.println("Choose the type of consultation:");
                        for(int i = 0; i < consultations.size(); i++){
                            System.out.print(i+1);
                            System.out.print(" ");
                            System.out.println(consultations.get(i));
                        }

                        index = scanner.nextInt() - 1;
                        scanner.nextLine();

                        newAppointment.addMedicalService(consultations.get(index));
                        spec = consultations.get(index).getSpecialization();
                        consultation = true;
                    }
                    else {
                        System.out.println("Consultation has already been added to the appointment!");
                    }
                    break;
                case 2:
                    System.out.println("Choose the type of test:");
                    for(int i = 0; i < tests.size(); i++){
                        System.out.print(i+1);
                        System.out.print(" ");
                        System.out.println(tests.get(i));
                    }

                    index = scanner.nextInt() - 1;
                    scanner.nextLine();

                    newAppointment.addMedicalService(tests.get(index));
                    investigation = true;
                    break;
                case 3:
                    System.out.println("Choose the type of radiography:");
                    for(int i = 0; i < radiographies.size(); i++){
                        System.out.print(i+1);
                        System.out.print(" ");
                        System.out.println(radiographies.get(i));
                    }

                    index = scanner.nextInt() - 1;
                    scanner.nextLine();

                    newAppointment.addMedicalService(radiographies.get(index));
                    investigation = true;
                case 4:
                    break;
            }
        }
        // Selectare data consultatie

        while(!scheduled) {
            System.out.println("Select the day of the appointment: ");
            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat (" E dd/MM");
            c.setTime(currentDate);

            for(int i = 1; i <= 7; i++){
                c.add(Calendar.DATE, 1);
                if(!isWeekEnd(c.getTime())) {
                    System.out.print(i);
                    System.out.println(dateFormat.format(c.getTime()));
                }
            }

            option = scanner.nextInt();
            scanner.nextLine();

            c.add(Calendar.DATE, (option - 7));
            Date appointmentDate = c.getTime();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;

            result = scheduleAppointment(spec, consultation, investigation, dayOfWeek, newAppointment, appointmentDate);

            if(result == 0) {
                newAppointment.setDate(appointmentDate);
                //p.addAppointment(newAppointment);
                appointmentRepository.insertAppointment(newAppointment);
                System.out.println("Appointment scheduled successfully!");
                //System.out.println(newAppointment.getHour());
                scheduled = true;
            }
            else {
                System.out.println("No available hours.");
                System.out.println("Please choose another date");
            }
        }
    }

    //CAZ 1: Pacientul necesita consultatie
    //CAZ 2: Pacientul necesita consultatie si investigatii
    //CAZ 3: Pacientul necesita investigatii
    //RETURN CODE
    // 0 - a fost gasita o ora libera pentru programare
    //-1 - nu a fost gasita o ora libera pentru programare
    private int scheduleAppointment(Specialization spec, boolean consultation, boolean investigation, int day, Appointment newAppointment, Date appDate) {
        ArrayList<Doctor> doctors = null;
        ArrayList<Nurse> nurses = null;

        HashSet<Integer> doctorHours = new HashSet<>();
        HashSet<Integer> nurseHours = new HashSet<>();

        //Daca pacientul doreste o consultatie, se cauta doctorii cu specializarea potrivita
        //si care muncesc in ziua specificata
        if (consultation) {
            doctors = searchDoctor(spec, day);
        }
        //Daca pacientul doreste o investigatie (radiografie / teste)
        //Se cauta asistentele ce muncesc in ziua specificata
        if (investigation) {
            nurses = searchNurse(day);
        }

        //Cazul 1: Pacientul doreste atat consultatie cat si investigatii medicale
        if (consultation && investigation) {
            // Cautam doctor pana cand gasim doctor liber / epuizam optiunile
            for(Doctor d : doctors) {
                // Cream o lista cu orele in care doctorul lucreaza
                // La final dorim sa avem doar orele libere
                doctorHours.clear();
                int startHourDoc = d.getSchedule()[day].getStartHour();
                int endHourDoc = d.getSchedule()[day].getEndHour();

                for (int i = startHourDoc; i < endHourDoc; i++) {
                    doctorHours.add(i);
                }

                // Verificam daca doctorul mai are alte programari in ziua respectiva
                // In cazul in care gasim programari, eliminam din lista de ore, orele ocupate
                for (Appointment a : d.getAppointments()) {
                    if (equalDate(a.getDate(), appDate)) {
                        doctorHours.remove(a.getHour());
                    }
                }
                //Cautam asistenta pana cand gasim asistenta libera / epuizam optiunile
                for(Nurse n : nurses) {
                    // Cream o lista cu orele in care asistenta lucreaza
                    // La final dorim sa avem doar orele libere
                    nurseHours.clear();
                    int startHourNurse = n.getSchedule()[day].getStartHour();
                    int endHourNurse = n.getSchedule()[day].getEndHour();

                    for (int i = startHourNurse; i < endHourNurse; i++) {
                        nurseHours.add(i);
                    }

                    // Verificam daca asistenta mai are alte programari in ziua respectiva
                    // In cazul in care gasim programari, eliminam din lista de ore, orele ocupate
                    for (Appointment a : n.getAppointments()) {
                        if (equalDate(a.getDate(), appDate)) {
                            nurseHours.remove(a.getHour());
                        }
                    }

                    // Incercam sa gasim prima ora libera din programul doctorului cat si al asistentei
                    HashSet<Integer> commonHours = new HashSet<>(doctorHours);
                    commonHours.retainAll(nurseHours);

                    // Cazul in care s-a gasit o ora comuna
                    if (!commonHours.isEmpty()) {
                        //found = true;
                        // Completare informatii programare
                        int appHour = Collections.min(commonHours);
                        newAppointment.setHour(appHour);
                        newAppointment.setDoctorNumericCode(d.getCnp());
                        newAppointment.setNurseNumericCode(n.getCnp());

                        //appointments.add(newAppointment);
                        // Adaugare programare in listele specifice
                        // Pentru doctor si asistenta
                        //d.addAppointment(newAppointment);
                        //n.addAppointment(newAppointment);

                        return 0;
                    }
                }
            }
        }
        //Cazul 2: Pacientul doreste doar consultatie
        else if (consultation) {
            for (Doctor d : doctors) {
                // Cream o lista cu orele in care doctorul lucreaza
                // La final dorim sa avem doar orele libere
                doctorHours.clear();
                int startHourDoc = d.getSchedule()[day].getStartHour();
                int endHourDoc = d.getSchedule()[day].getEndHour();

                for (int i = startHourDoc; i < endHourDoc; i++) {
                    doctorHours.add(i);
                }

                // Verificam daca doctorul mai are alte programari in ziua respectiva
                // In cazul in care gasim programari, eliminam din lista de ore, orele ocupate
                for (Appointment a : d.getAppointments()) {

                    if (equalDate(a.getDate(), appDate)) {
                        doctorHours.remove(a.getHour());
                    }
                }

                if (!doctorHours.isEmpty()) {
                    int appHour = Collections.min(doctorHours);
                    newAppointment.setHour(appHour);
                    newAppointment.setDoctorNumericCode(d.getCnp());

                    //appointments.add(newAppointment);
                    // Adaugare in lista de programari a doctorului
                    //d.addAppointment(newAppointment);

                    return 0;
                }
            }
        }
        //Cazul 3: Pacientul doreste doar investigatii medicale
        else {
            for (Nurse n : nurses) {
                // Cream o lista cu orele in care doctorul lucreaza
                // La final dorim sa avem doar orele libere
                nurseHours.clear();
                int startHourNurse = n.getSchedule()[day].getStartHour();
                int endHourNurse = n.getSchedule()[day].getEndHour();

                for (int i = startHourNurse; i < endHourNurse; i++) {
                    nurseHours.add(i);
                }

                // Verificam daca doctorul mai are alte programari in ziua respectiva
                // In cazul in care gasim programari, eliminam din lista de ore, orele ocupate
                for (Appointment a : n.getAppointments()) {
                    if (equalDate(a.getDate(), appDate)) {
                        nurseHours.remove(a.getHour());
                    }
                }

                if (!nurseHours.isEmpty()) {
                    int appHour = Collections.min(nurseHours);
                    newAppointment.setHour(appHour);
                    newAppointment.setNurseNumericCode(n.getCnp());
                    //appointments.add(newAppointment);
                    // Adaugare in lista de programari a asistentei
                    //n.addAppointment(newAppointment);

                    return 0;
                }
            }
        }
        // Daca am ajuns in acest punct, inseamna ca nu am reusit sa efectuam programarea
        return -1;
    }

    //Cauta doctori cu specializarea spec care sa lucreze in ziua specificata
    private ArrayList<Doctor> searchDoctor(Specialization spec, int day) {
        ArrayList<Doctor> doctors = new ArrayList<>();
        ArrayList<Doctor> allDoctors = doctorRepository.getAllDoctors();

        for(Doctor d : allDoctors) {
            if(d.getSpecializare().equals(spec) && d.getSchedule()[day].getEndHour() != 0) {
                doctors.add(d);
            }
        }

        return doctors;
    }

    //Cauta asistentele ce muncesc in ziua respectiva
    private ArrayList<Nurse> searchNurse(int day) {
        ArrayList<Nurse> nurses = new ArrayList<>();
        ArrayList<Nurse> allNurses = nurseRepository.getAllNurses();

        for(Nurse n : allNurses) {
            if(n.getSchedule()[day].getEndHour() != 0) {
                nurses.add(n);
            }

        }

        return nurses;
    }

    public void getPatientAppointment() {
        System.out.println("Please enter the appointment id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Appointment appointment = appointmentRepository.getAppointmentById(id);

        if(appointment == null) {
            System.out.println("There is no appointment with the id: " + id);
        }
        else {
            System.out.println(appointment);
        }
    }

    public void editPatientAppointment() {
        System.out.println("Please enter the appointment id: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Appointment appointment = appointmentRepository.getAppointmentById(id);
        ArrayList<Radiography> radiographies = medicalServiceRepository.getRadiographies();
        ArrayList<Test> tests = medicalServiceRepository.getTests();

        if(appointment == null) {
            System.out.println("There is no appointment with the id: " + id);
        }
        else {
            int option = 0;

            while(option != 6){
                System.out.println("1. Add radiographies");
                System.out.println("2. Add tests");
                System.out.println("3. Change nurse");
                System.out.println("4. Change doctor");
                System.out.println("5. Change date and hour");
                System.out.println("6. Finish edit");

                option = scanner.nextInt();
                scanner.nextLine();

                switch(option){
                    case 1:
                        int i = 0;
                        for(Radiography r : radiographies){
                            System.out.print(i + " ");
                            System.out.println(r);
                            i += 1;
                        }
                        System.out.println("Enter radiography number");

                        int radiographyOption = scanner.nextInt();
                        scanner.nextLine();

                        if(radiographyOption < 0 || radiographyOption >= radiographies.size()){
                            System.out.println("Invalid option");
                        }
                        else {
                            if(!appointment.getMedicalServices().contains(radiographies.get(radiographyOption))){
                                appointmentRepository.insertMedicalService(appointment.getId(), radiographies.get(radiographyOption));
                            }
                            else {
                                System.out.println("Radiography already added");
                            }
                        }
                        break;
                    case 2:
                        int j = 0;
                        for(Test t : tests){
                            System.out.print(j + " ");
                            System.out.println(t);
                            j += 1;
                        }
                        System.out.println("Enter test number");

                        int testOption = scanner.nextInt();
                        scanner.nextLine();

                        if(testOption < 0 || testOption >= radiographies.size()){
                            System.out.println("Invalid option");
                        }
                        else {
                            if(!appointment.getMedicalServices().contains(tests.get(testOption))){
                                appointmentRepository.insertMedicalService(appointment.getId(), tests.get(testOption));
                            }
                            else {
                                System.out.println("Radiography already added");
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Enter nurse CNP: ");
                        String nurseCnp = scanner.nextLine();

                        if(nurseRepository.checkIfNurseExists(nurseCnp)){
                            appointmentRepository.changeNurse(appointment.getId(), nurseCnp);
                        }
                        else{
                            System.out.println("There is no nurse with the CNP = " + nurseCnp);
                        }
                        break;
                    case 4:
                        System.out.println("Enter doctor CNP: ");
                        String doctorCnp = scanner.nextLine();

                        if(doctorRepository.checkIfDoctorExists(doctorCnp)){
                            appointmentRepository.changeDoctor(appointment.getId(), doctorCnp);
                        }
                        else{
                            System.out.println("There is no doctor with the CNP = " + doctorCnp);
                        }
                        break;
                    case 5:
                        System.out.println("Appointment date: ");
                        String appDateString = scanner.nextLine();
                        System.out.println("Appointment hour: ");
                        int hour = scanner.nextInt();
                        scanner.nextLine();

                        try {
                            Date appDate = new SimpleDateFormat("dd/MM/yyyy").parse(appDateString);
                            java.sql.Date sqlDate = new java.sql.Date(appDate.getTime());
                            appointmentRepository.changeDate(appointment.getId(), sqlDate);
                            appointmentRepository.changeHour(appointment.getId(), hour);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        }
    }

    public void deletePatientAppointment() {
        System.out.println("Please enter the patient's CNP: ");
        String cnp = scanner.nextLine();
        boolean exists = patientRepository.checkIfPatientExists(cnp);
        int i = 0;


        if(!exists) {
            System.out.println("There is no patient with the cnp: " + cnp);
        }
        else {
            Patient patient = patientRepository.getPatient(cnp);

            for(Appointment a : patient.getAppointments()) {
                System.out.println("Appointment number: " + i);
                System.out.println(a);
                i += 1;
            }

            System.out.println("Enter appointment number: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if(option < 0 || option >= patient.getAppointments().size()) {
                System.out.println("Invalid option");
            }
            else {
                appointmentRepository.deleteAppointment(patient.getAppointments().get(option).getId());
                System.out.println("Appointment deleted");
            }
        }
    }

    private boolean equalDate(Date date1, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String d1 = dateFormat.format(date1);
        String d2 = dateFormat.format(date2);

        return d1.equals(d2);
    }

    private boolean isWeekEnd(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E");
        String dateString = dateFormat.format(date);

        if(dateString.equals("Sat") || dateString.equals("Sun")){
            return true;
        }

        return false;
    }
}