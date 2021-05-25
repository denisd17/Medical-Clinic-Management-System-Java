package repository;

import config.DatabaseConfiguration;
import model.medical_services.Appointment;
import model.person.MedicalRecord;
import model.person.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PatientRepository {

    //Insereaza un pacient in baza de date
    //Insereaza si fisa medicala a acestuia, daca este cazul
    public void insertPatient(Patient patient) {
        String insertSqlPatient = "insert into patients values(?,?,?,?,?,?)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtInsertPatient = databaseConnection.prepareStatement(insertSqlPatient);
            preparedStmtInsertPatient.setString(1, patient.getCnp());
            preparedStmtInsertPatient.setString(2, patient.getFirstName());
            preparedStmtInsertPatient.setString(3, patient.getLastName());
            preparedStmtInsertPatient.setString(4, patient.getPhoneNumber());
            preparedStmtInsertPatient.setString(5, patient.getEmail());
            java.sql.Date sqlDate = new java.sql.Date(patient.getBirthDate().getTime());
            preparedStmtInsertPatient.setDate(6, sqlDate);

            preparedStmtInsertPatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(patient.getMedicalRecord() != null) {
            MedicalRecordRepository medicalRepo = new MedicalRecordRepository();
            medicalRepo.insertRecord(patient.getCnp(), patient.getMedicalRecord());
        }
    }

    //Preia un pacient din baza de date
    //In functie de id (cnp)
    public Patient getPatient(String cnp) {
        String getSqlPatient = "select * from patients p " +
                "left join medical_records m " +
                "on (p.cnp = m.patient_cnp) " +
                " where p.cnp = ?";
        String getSqlAlergies = "select name from alergies " +
                "where medical_rec_id = ?";
        String getSqlDiseases = "select name from diseases " +
                "where medical_rec_id = ?";

        AppointmentRepository appointmentRepository = new AppointmentRepository();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetPatient = databaseConnection.prepareStatement(getSqlPatient);
            preparedStmtGetPatient.setString(1, cnp);

            ResultSet resultSet = preparedStmtGetPatient.executeQuery();

            if(resultSet.next()) {
                ArrayList<Appointment> appointments = appointmentRepository.getAppointmentsByCnp(resultSet.getString(1), 0);
                //Fieldurile null + getInt() returneaza 0
                //Verificam daca pacientul are sau nu fisa medicala
                if(resultSet.getInt(7) == 0) {
                    java.util.Date birthDate = new Date(resultSet.getDate(6).getTime());

                    return new Patient(resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getString(5),
                            resultSet.getString(1), null,
                            birthDate, appointments);
                }
                //Cazul in care pacientul are fisa medicala
                else {
                    Set<String> alergies = new HashSet<>();
                    Set<String> chronicDiseases = new HashSet<>();
                    int medicalRecordId = resultSet.getInt(7);

                    //Obtinerea listei de alergii
                    PreparedStatement preparedStmtGetAlergies = databaseConnection.prepareStatement(getSqlAlergies);
                    preparedStmtGetAlergies.setInt(1, medicalRecordId);
                    ResultSet resultSetAlergies = preparedStmtGetAlergies.executeQuery();

                    while(resultSetAlergies.next()) {
                        alergies.add(resultSetAlergies.getString(1));
                    }

                    //Obtinerea listei de boli cronice
                    PreparedStatement preparedStmtGetDiseases = databaseConnection.prepareStatement(getSqlDiseases);
                    preparedStmtGetDiseases.setInt(1, medicalRecordId);
                    ResultSet resultSetDiseases = preparedStmtGetDiseases.executeQuery();

                    while(resultSetDiseases.next()) {
                        chronicDiseases.add(resultSetDiseases.getString(1));
                    }

                    MedicalRecord medicalRecord = new MedicalRecord(resultSet.getBoolean(8), alergies, chronicDiseases);

                    java.util.Date birthDate = new Date(resultSet.getDate(6).getTime());

                    return new Patient(resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getString(5),
                            resultSet.getString(1), medicalRecord,
                            birthDate, appointments);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Patient> getAllPatients() {
        String getSqlPatient = "select * from patients p " +
                "left join medical_records m " +
                "on (p.cnp = m.patient_cnp)";
        String getSqlAlergies = "select name from alergies " +
                "where medical_rec_id = ?";
        String getSqlDiseases = "select name from diseases " +
                "where medical_rec_id = ?";

        ArrayList<Patient> patients = new ArrayList<>();
        AppointmentRepository appointmentRepository = new AppointmentRepository();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetPatient = databaseConnection.prepareStatement(getSqlPatient);

            ResultSet resultSet = preparedStmtGetPatient.executeQuery();

            while(resultSet.next()) {
                ArrayList<Appointment> appointments = appointmentRepository.getAppointmentsByCnp(resultSet.getString(1), 0);
                //Fieldurile null + getInt() returneaza 0
                //Verificam daca pacientul are sau nu fisa medicala
                if(resultSet.getInt(7) == 0) {
                    java.util.Date birthDate = new Date(resultSet.getDate(6).getTime());

                    Patient patient = new Patient(resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getString(5),
                            resultSet.getString(1), null,
                            birthDate, appointments);

                    patients.add(patient);
                }
                //Cazul in care pacientul are fisa medicala
                else {
                    Set<String> alergies = new HashSet<>();
                    Set<String> chronicDiseases = new HashSet<>();
                    int medicalRecordId = resultSet.getInt(7);

                    //Obtinerea listei de alergii
                    PreparedStatement preparedStmtGetAlergies = databaseConnection.prepareStatement(getSqlAlergies);
                    preparedStmtGetAlergies.setInt(1, medicalRecordId);
                    ResultSet resultSetAlergies = preparedStmtGetAlergies.executeQuery();

                    while(resultSetAlergies.next()) {
                        alergies.add(resultSetAlergies.getString(1));
                    }

                    //Obtinerea listei de boli cronice
                    PreparedStatement preparedStmtGetDiseases = databaseConnection.prepareStatement(getSqlDiseases);
                    preparedStmtGetDiseases.setInt(1, medicalRecordId);
                    ResultSet resultSetDiseases = preparedStmtGetDiseases.executeQuery();

                    while(resultSetDiseases.next()) {
                        chronicDiseases.add(resultSetDiseases.getString(1));
                    }

                    MedicalRecord medicalRecord = new MedicalRecord(resultSet.getBoolean(8), alergies, chronicDiseases);

                    java.util.Date birthDate = new Date(resultSet.getDate(6).getTime());

                    Patient patient = new Patient(resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getString(5),
                            resultSet.getString(1), medicalRecord,
                            birthDate, appointments);

                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    //Sterge un pacient din baza de date
    //In functie de id (cnp)
    public void deletePatient(String cnp) {
        String deleteSqlPatient = "delete from patients where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeletePatient = databaseConnection.prepareStatement(deleteSqlPatient);
            preparedStmtDeletePatient.setString(1, cnp);

            preparedStmtDeletePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatientFirstName(String cnp, String firstName) {
        String updateSqlPatient = "update patients " +
                "set first_name = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdatePatient = databaseConnection.prepareStatement(updateSqlPatient);
            preparedStmtUpdatePatient.setString(1, firstName);
            preparedStmtUpdatePatient.setString(2, cnp);

            preparedStmtUpdatePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatientLastName(String cnp, String lastName) {
        String updateSqlPatient = "update patients " +
                "set last_name = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdatePatient = databaseConnection.prepareStatement(updateSqlPatient);
            preparedStmtUpdatePatient.setString(1, lastName);
            preparedStmtUpdatePatient.setString(2, cnp);

            preparedStmtUpdatePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO: Update medical records before
    public void updatePatientCnp(String cnp, String newCnp) {
        String updateSqlPatient = "update patients " +
                "set cnp = ? " +
                "where cnp = ?";
        String updateSqlMedicalRecord = "update medical_records " +
                "set patient_cnp = ? " +
                "where patient_cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {


            PreparedStatement preparedStmtUpdatePatient = databaseConnection.prepareStatement(updateSqlPatient);
            preparedStmtUpdatePatient.setString(1, newCnp);
            preparedStmtUpdatePatient.setString(2, cnp);

            PreparedStatement preparedStmtUpdateMedicalRecord = databaseConnection.prepareStatement(updateSqlMedicalRecord);
            preparedStmtUpdateMedicalRecord.setString(1, newCnp);
            preparedStmtUpdateMedicalRecord.setString(2, cnp);

            preparedStmtUpdateMedicalRecord.executeUpdate();

            preparedStmtUpdatePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatientPhoneNumber(String cnp, String phoneNumber) {
        String updateSqlPatient = "update patients " +
                "set phone_number = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdatePatient = databaseConnection.prepareStatement(updateSqlPatient);
            preparedStmtUpdatePatient.setString(1, phoneNumber);
            preparedStmtUpdatePatient.setString(2, cnp);

            preparedStmtUpdatePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatientEmail(String cnp, String email) {
        String updateSqlPatient = "update patients " +
                "set email = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdatePatient = databaseConnection.prepareStatement(updateSqlPatient);
            preparedStmtUpdatePatient.setString(1, email);
            preparedStmtUpdatePatient.setString(2, cnp);

            preparedStmtUpdatePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatientBirthDate(String cnp, Date birthDate) {
        String updateSqlPatient = "update patients " +
                "set birth_date = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            java.sql.Date sqlDate = new java.sql.Date(birthDate.getTime());
            PreparedStatement preparedStmtUpdatePatient = databaseConnection.prepareStatement(updateSqlPatient);
            preparedStmtUpdatePatient.setDate(1, sqlDate);
            preparedStmtUpdatePatient.setString(2, cnp);

            preparedStmtUpdatePatient.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfPatientExists(String cnp) {
        String checkSqlPatient = "select count(*) from patients where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtCheckPatient = databaseConnection.prepareStatement(checkSqlPatient);
            preparedStmtCheckPatient.setString(1, cnp);


            ResultSet rs = preparedStmtCheckPatient.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if(count == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
