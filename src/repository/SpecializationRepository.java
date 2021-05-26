package repository;

import config.DatabaseConfiguration;
import model.person.Specialization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SpecializationRepository {
    public ArrayList<Specialization> getAllSpecializations() {
        ArrayList<Specialization> specializations = new ArrayList<>();
        String getSqlSpecializations = "select * from specializations";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetSpecializations = databaseConnection.prepareStatement(getSqlSpecializations);
            ResultSet resultSet = preparedStmtGetSpecializations.executeQuery();

            while(resultSet.next()) {
                Specialization specialization = new Specialization(resultSet.getInt(1), resultSet.getString(2));
                specializations.add(specialization);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return specializations;
    }
}
