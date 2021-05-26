package repository;

import config.DatabaseConfiguration;
import model.medical_services.*;
import model.person.Specialization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MedicalServiceRepository {
    public ArrayList<Consultation> getConsultations() {
        String selectSql = "select * from services s " +
                "left join specializations spec " +
                "on(s.specialization_id = spec.id) " +
                "where s.type = 0";

        ArrayList<Consultation> consultations = new ArrayList<>();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetServices = databaseConnection.prepareStatement(selectSql);

            ResultSet resultSet = preparedStmtGetServices.executeQuery();

            while(resultSet.next()) {
                Consultation consultation = new Consultation(resultSet.getInt(1), resultSet.getFloat(4),
                        new Specialization(resultSet.getString(7)));
                consultations.add(consultation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultations;
    }

    public ArrayList<Radiography> getRadiographies() {
        String selectSql = "select * from services s " +
                "left join specializations spec " +
                "on(s.specialization_id = spec.id) " +
                "where s.type = 1";

        ArrayList<Radiography> radiographies = new ArrayList<>();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetServices = databaseConnection.prepareStatement(selectSql);

            ResultSet resultSet = preparedStmtGetServices.executeQuery();

            while(resultSet.next()) {
                RadiographyArea area = RadiographyArea.valueOf(resultSet.getString(3));
                Radiography radiography = new Radiography(resultSet.getInt(1), resultSet.getFloat(4), area);
                radiographies.add(radiography);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return radiographies;
    }

    public ArrayList<Test> getTests() {
        String selectSql = "select * from services s " +
                "left join specializations spec " +
                "on(s.specialization_id = spec.id) " +
                "where s.type = 2";

        ArrayList<Test> tests = new ArrayList<>();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetServices = databaseConnection.prepareStatement(selectSql);

            ResultSet resultSet = preparedStmtGetServices.executeQuery();

            while(resultSet.next()) {
                TestType testType = TestType.valueOf(resultSet.getString(3));
                Test test = new Test(resultSet.getInt(1), resultSet.getFloat(4), testType);
                tests.add(test);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return tests;
    }
}
