package repository;

import config.DatabaseConfiguration;
import model.medical_services.Appointment;
import model.person.MedicalRecord;
import model.person.Nurse;
import model.person.Patient;
import model.person.WorkDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NurseRepository {

    public void insertNurse(Nurse nurse) {
        String insertSqlNurse = "insert into nurses values(?, ?, ?, ?, ?)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtInsertNurse = databaseConnection.prepareStatement(insertSqlNurse);
            preparedStmtInsertNurse.setString(1, nurse.getCnp());
            preparedStmtInsertNurse.setString(2, nurse.getFirstName());
            preparedStmtInsertNurse.setString(3, nurse.getLastName());
            preparedStmtInsertNurse.setString(4, nurse.getPhoneNumber());
            preparedStmtInsertNurse.setString(5, nurse.getEmail());

            preparedStmtInsertNurse.executeUpdate();

            //Inserarea programului de lucru
            ScheduleRepository scheduleRepository = new ScheduleRepository();
            scheduleRepository.insertSchedule(nurse.getCnp(), nurse.getSchedule());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Nurse getNurse(String cnp) {
        String getSqlNurse = "select * from nurses " +
                " where cnp = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        try {
            PreparedStatement preparedStmtGetNurse = databaseConnection.prepareStatement(getSqlNurse);
            preparedStmtGetNurse.setString(1, cnp);

            ResultSet resultSet = preparedStmtGetNurse.executeQuery();

            if(resultSet.next()) {
                ArrayList<Appointment> appointments = appointmentRepository.getAppointmentsByCnp(resultSet.getString(1), 1);
                WorkDay[] schedule = scheduleRepository.getSchedule(cnp);
                return new Nurse(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getString(1), schedule, appointments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Nurse> getAllNurses() {
        String getSqlNurse = "select * from nurses";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        ArrayList<Nurse> nurses = new ArrayList<>();
        try {
            PreparedStatement preparedStmtGetNurse = databaseConnection.prepareStatement(getSqlNurse);

            ResultSet resultSet = preparedStmtGetNurse.executeQuery();

            while(resultSet.next()) {
                ArrayList<Appointment> appointments = appointmentRepository.getAppointmentsByCnp(resultSet.getString(1), 1);
                WorkDay[] schedule = scheduleRepository.getSchedule(resultSet.getString(1));
                Nurse nurse = new Nurse(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getString(1), schedule, appointments);

                nurses.add(nurse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurses;
    }

    public void deleteNurse(String cnp) {
        String deleteSqlNurse = "delete from nurses where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        ScheduleRepository scheduleRepository = new ScheduleRepository();

        try {
            PreparedStatement preparedStmtDeleteNurse = databaseConnection.prepareStatement(deleteSqlNurse);
            preparedStmtDeleteNurse.setString(1, cnp);

            preparedStmtDeleteNurse.executeUpdate();
            scheduleRepository.deleteSchedule(cnp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNurseFirstName(String cnp, String firstName) {
        String updateSqlNurse = "update nurses " +
                "set first_name = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateNurse = databaseConnection.prepareStatement(updateSqlNurse);
            preparedStmtUpdateNurse.setString(1, firstName);
            preparedStmtUpdateNurse.setString(2, cnp);

            preparedStmtUpdateNurse.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNurseLastName(String cnp, String lastName) {
        String updateSqlNurse = "update nurses " +
                "set last_name = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateNurse = databaseConnection.prepareStatement(updateSqlNurse);
            preparedStmtUpdateNurse.setString(1, lastName);
            preparedStmtUpdateNurse.setString(2, cnp);

            preparedStmtUpdateNurse.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNursePhoneNumber(String cnp, String phoneNumber) {
        String updateSqlNurse = "update nurses " +
                "set phone_number = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateNurse = databaseConnection.prepareStatement(updateSqlNurse);
            preparedStmtUpdateNurse.setString(1, phoneNumber);
            preparedStmtUpdateNurse.setString(2, cnp);

            preparedStmtUpdateNurse.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNurseEmail(String cnp, String email) {
        String updateSqlNurse = "update nurses " +
                "set email = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateNurse = databaseConnection.prepareStatement(updateSqlNurse);
            preparedStmtUpdateNurse.setString(1, email);
            preparedStmtUpdateNurse.setString(2, cnp);

            preparedStmtUpdateNurse.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNurseSchedule(String cnp, String day, int startHour, int endHour) {
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        scheduleRepository.updateSchedule(cnp, day, startHour, endHour);
    }

    public boolean checkIfNurseExists(String cnp) {
        String checkSqlNurse = "select count(*) from nurses where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtCheckNurse = databaseConnection.prepareStatement(checkSqlNurse);
            preparedStmtCheckNurse.setString(1, cnp);


            ResultSet rs = preparedStmtCheckNurse.executeQuery();
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
