package repository;

import config.DatabaseConfiguration;
import model.person.MedicalRecord;

import java.sql.*;
import java.util.HashSet;

public class MedicalRecordRepository {

    //Insereaza o fisa medicala ptr un pacient dat
    public void insertRecord(String cnp, MedicalRecord medicalRecord) {
        String insertSqlMedical = "insert into medical_records(smoker, patient_cnp)" +
                "values(?, ?)";
        String insertSqlAlergy = "insert into alergies(name, medical_rec_id)" +
                "values(?, ?)";

        String insertSqlDisease = "insert into diseases(name, medical_rec_id)" +
                "values(?, ?)";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtInsertRecord = databaseConnection.prepareStatement(insertSqlMedical, Statement.RETURN_GENERATED_KEYS);
            preparedStmtInsertRecord.setBoolean(1, medicalRecord.getSmoker());
            preparedStmtInsertRecord.setString(2, cnp);

            preparedStmtInsertRecord.executeUpdate();

            ResultSet generatedKeys = preparedStmtInsertRecord.getGeneratedKeys();
            generatedKeys.next();
            int recordKey = generatedKeys.getInt(1);

            if(!medicalRecord.getAlergies().isEmpty()) {
                for(String alergy : medicalRecord.getAlergies()) {
                    PreparedStatement preparedStmtInsertAlergy = databaseConnection.prepareStatement(insertSqlAlergy);
                    preparedStmtInsertAlergy.setString(1, alergy);
                    preparedStmtInsertAlergy.setInt(2, recordKey);
                    preparedStmtInsertAlergy.executeUpdate();
                }
            }

            if(!medicalRecord.getChronicDiseases().isEmpty()) {
                for(String disease : medicalRecord.getChronicDiseases()) {
                    PreparedStatement preparedStmtInsertDisease = databaseConnection.prepareStatement(insertSqlDisease);
                    preparedStmtInsertDisease.setString(1, disease);
                    preparedStmtInsertDisease.setInt(2, recordKey);
                    preparedStmtInsertDisease.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Preia fisa medicala in functie de cnp-ul pacientului
    public MedicalRecord getRecord(String cnp) {
        String getSqlMedicalRecord = "select * from medical_records " +
                "where patient_cnp = ?";
        String getSqlAlergies = "select name from alergies where medical_rec_id = ?";
        String getSqlDiseases = "select name from diseases where medical_rec_id = ?";

        HashSet<String> alergies = new HashSet<>();
        HashSet<String> diseases = new HashSet<>();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetMedicalRecord = databaseConnection.prepareStatement(getSqlMedicalRecord);
            preparedStmtGetMedicalRecord.setString(1, cnp);
            ResultSet resultSetMedicalRecord = preparedStmtGetMedicalRecord.executeQuery();

            if(resultSetMedicalRecord.next()) {
                int medicalRecordId = resultSetMedicalRecord.getInt(1);
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
                    diseases.add(resultSetDiseases.getString(1));
                }

                return new MedicalRecord(resultSetMedicalRecord.getInt(1), resultSetMedicalRecord.getBoolean(2), alergies, diseases);
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Sterge fisa medicala a pacientului selectat
    public void deleteRecord(String cnp) {
        String deleteSqlMedicalRecord = "delete from medical_records where patient_cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeleteRecord = databaseConnection.prepareStatement(deleteSqlMedicalRecord);
            preparedStmtDeleteRecord.setString(1, cnp);

            preparedStmtDeleteRecord.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testSetNull(String cnp) {
        String sql = "update medical_records set patient_cnp = null where patient_cnp = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateSmokerStatus = databaseConnection.prepareStatement(sql);
            preparedStmtUpdateSmokerStatus.setString(1, cnp);

            preparedStmtUpdateSmokerStatus.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSmokerStatus(String cnp, Boolean smokerStatus) {
        String updateSqlSmokerStatus = "update medical_records " +
                "set smoker = ? " +
                "where patient_cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateSmokerStatus = databaseConnection.prepareStatement(updateSqlSmokerStatus);
            preparedStmtUpdateSmokerStatus.setBoolean(1, smokerStatus);
            preparedStmtUpdateSmokerStatus.setString(2, cnp);

            preparedStmtUpdateSmokerStatus.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDisease(int id, String disease) {
        String insertSqlDisease = "insert into diseases(name, medical_rec_id) " +
                "values(?, ?) ";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtInsertDisease = databaseConnection.prepareStatement(insertSqlDisease);
            preparedStmtInsertDisease.setString(1, disease);
            preparedStmtInsertDisease.setInt(2, id);

            preparedStmtInsertDisease.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDisease(int id, String disease) {
        String deleteSqlDisease = "delete from diseases where name = ? and medical_rec_id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeleteDisease = databaseConnection.prepareStatement(deleteSqlDisease);
            preparedStmtDeleteDisease.setString(1, disease);
            preparedStmtDeleteDisease.setInt(2, id);

            preparedStmtDeleteDisease.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAlergy(int id, String alergy) {
        String insertSqlAlergy = "insert into alergies(name, medical_rec_id) " +
                "values(?, ?) ";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtInsertAlergy = databaseConnection.prepareStatement(insertSqlAlergy);
            preparedStmtInsertAlergy.setString(1, alergy);
            preparedStmtInsertAlergy.setInt(2, id);

            preparedStmtInsertAlergy.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAlergy(int id, String alergy) {
        String deleteSqlAlergy = "delete from alergies where name = ? and medical_rec_id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeleteAlergy = databaseConnection.prepareStatement(deleteSqlAlergy);
            preparedStmtDeleteAlergy.setString(1, alergy);
            preparedStmtDeleteAlergy.setInt(2, id);

            preparedStmtDeleteAlergy.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
