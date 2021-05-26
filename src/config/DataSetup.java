package config;

import model.person.Patient;
import repository.RepositoryHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSetup {

    public void setUp() {
        String createPatientsTableSql = "create table if not exists patients( " +
                "cnp varchar(30) primary key, " +
                "first_name varchar(30), " +
                "last_name varchar(30), " +
                "phone_number varchar(30), " +
                "email varchar(30), " +
                "birth_date date)";

        String createNursesTableSql = "create table if not exists nurses( " +
                "cnp varchar(30) primary key, " +
                "first_name varchar(30), " +
                "last_name varchar(30), " +
                "phone_number varchar(30), " +
                "email varchar(30))";

        String createDoctorsTableSql = "create table if not exists doctors( " +
                "cnp varchar(30) primary key, " +
                "first_name varchar(30), " +
                "last_name varchar(30), " +
                "phone_number varchar(30), " +
                "email varchar(30), " +
                "specialization_id int, " +
                "foreign key(specialization_id) references specializations(id))";

        String createSpecializationsTableSql = "create table if not exists specializations( " +
                "id int primary key auto_increment, " +
                "name varchar(30))";

        String createScheduleTableSql = "create table if not exists schedule( " +
                "id int primary key auto_increment, " +
                "cnp varchar(30), " +
                "monday_start int, " +
                "monday_end int, " +
                "tuesday_start int, " +
                "tuesday_end int, " +
                "wednesday_start int, " +
                "wednesday_end int, " +
                "thursday_start int, " +
                "thursday_end int, " +
                "friday_start int, " +
                "friday_end int)";

        String createMedicalRecordsTableSql = "create table if not exists medical_records( " +
                "id int primary key auto_increment, " +
                "smoker boolean, " +
                "patient_cnp varchar(30), " +
                "foreign key(patient_cnp) references patients(cnp)" +
                "on delete cascade)";

        String createAlergiesTableSql = "create table if not exists alergies( " +
                "id int primary key auto_increment, " +
                "name varchar(30), " +
                "medical_rec_id int, " +
                "foreign key(medical_rec_id) references medical_records(id)" +
                "on delete cascade)";

        String createDiseasesTableSql = "create table if not exists diseases( " +
                "id int primary key auto_increment, " +
                "name varchar(30), " +
                "medical_rec_id int, " +
                "foreign key(medical_rec_id) references medical_records(id)" +
                "on delete cascade)";

        String createAppointmentsTableSql = "create table if not exists appointments( " +
                "id int primary key auto_increment, " +
                "patient_cnp varchar(30), " +
                "nurse_cnp varchar(30), " +
                "doctor_cnp varchar(30), " +
                "appointment_date date, " +
                "hour int, " +
                "foreign key(patient_cnp) references patients(cnp) on delete cascade, " +
                "foreign key(nurse_cnp) references nurses(cnp) on delete cascade, " +
                "foreign key(doctor_cnp) references doctors(cnp) on delete cascade)";

        String createServicesTableSql = "create table if not exists services( " +
                "id int primary key auto_increment, " +
                "type int, " +
                "name varchar(30), " +
                "price decimal(10,2), " +
                "specialization_id int, " +
                "foreign key(specialization_id) references specializations(id) on delete cascade)";

        String createAppServicesTable = "create table if not exists app_services( " +
                "id int primary key auto_increment, " +
                "app_id int, " +
                "service_id int, " +
                "foreign key(app_id) references appointments(id) on delete cascade, " +
                "foreign key(service_id) references services(id) on delete cascade)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        RepositoryHelper repositoryHelper = RepositoryHelper.getRepositoryHelper();

        try {
            repositoryHelper.executeSql(databaseConnection, createPatientsTableSql);
            repositoryHelper.executeSql(databaseConnection, createSpecializationsTableSql);
            repositoryHelper.executeSql(databaseConnection, createNursesTableSql);
            repositoryHelper.executeSql(databaseConnection, createDoctorsTableSql);
            repositoryHelper.executeSql(databaseConnection, createScheduleTableSql);
            repositoryHelper.executeSql(databaseConnection, createMedicalRecordsTableSql);
            repositoryHelper.executeSql(databaseConnection, createAlergiesTableSql);
            repositoryHelper.executeSql(databaseConnection, createDiseasesTableSql);
            repositoryHelper.executeSql(databaseConnection, createAppointmentsTableSql);
            repositoryHelper.executeSql(databaseConnection, createServicesTableSql);
            repositoryHelper.executeSql(databaseConnection, createAppServicesTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public void addPatient(){
        String insertPersonSql = "INSERT INTO patients(cnp, FirstName, LastName, PhoneNumber, eMail, BirthDate) " +
                "VALUES('980', 'testPrenume', 'testNume', 'testTelefon', 'testEmail', '2008-7-04')";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        RepositoryHelper repositoryHelper = RepositoryHelper.getRepositoryHelper();

        try {
            repositoryHelper.executeUpdateSql(databaseConnection, insertPersonSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayPerson() {
        String selectSql = "SELECT * FROM patients";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        RepositoryHelper repositoryHelper = RepositoryHelper.getRepositoryHelper();

        try {
            ResultSet resultSet = repositoryHelper.executeQuerySql(databaseConnection, selectSql);
            while (resultSet.next()) {
                System.out.println("cnp: " + resultSet.getString(1));
                System.out.println("prenume: " + resultSet.getString(2));
                System.out.println("nume: " + resultSet.getString(3));
                System.out.println("telefon: " + resultSet.getString(4));
                System.out.println("email: " + resultSet.getString(5));
                System.out.println("data: " + resultSet.getDate(6));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
