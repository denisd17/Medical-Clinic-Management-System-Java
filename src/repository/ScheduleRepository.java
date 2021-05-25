package repository;

import config.DatabaseConfiguration;
import model.person.Doctor;
import model.person.WorkDay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleRepository {
    public void insertSchedule(String cnp, WorkDay[] workDays) {
        String insertSqlSchedule = "insert into schedule(cnp, monday_start, monday_end, tuesday_start, tuesday_end, " +
                "wednesday_start, wednesday_end, thursday_start, thursday_end, friday_start, friday_end) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtInsertSchedule = databaseConnection.prepareStatement(insertSqlSchedule);
            preparedStmtInsertSchedule.setString(1, cnp);
            preparedStmtInsertSchedule.setInt(2, workDays[0].getStartHour());
            preparedStmtInsertSchedule.setInt(3, workDays[0].getEndHour());
            preparedStmtInsertSchedule.setInt(4, workDays[1].getStartHour());
            preparedStmtInsertSchedule.setInt(5, workDays[1].getEndHour());
            preparedStmtInsertSchedule.setInt(6, workDays[2].getStartHour());
            preparedStmtInsertSchedule.setInt(7, workDays[2].getEndHour());
            preparedStmtInsertSchedule.setInt(8, workDays[3].getStartHour());
            preparedStmtInsertSchedule.setInt(9, workDays[3].getEndHour());
            preparedStmtInsertSchedule.setInt(10, workDays[4].getStartHour());
            preparedStmtInsertSchedule.setInt(11, workDays[4].getEndHour());
            preparedStmtInsertSchedule.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WorkDay[] getSchedule(String cnp) {
        String getSqlSchedule = "select * from schedule where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetSchedule = databaseConnection.prepareStatement(getSqlSchedule);
            preparedStmtGetSchedule.setString(1, cnp);

            ResultSet rs = preparedStmtGetSchedule.executeQuery();

            if(rs.next()) {
                WorkDay[] workDays = new WorkDay[5];

                for(int i = 0; i < 5; i++) {
                    int j = 2 * i + 3;
                    workDays[i] = new WorkDay();
                    workDays[i].setStartHour(rs.getInt(j));
                    workDays[i].setEndHour(rs.getInt(j+1));
                }

                return workDays;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteSchedule(String cnp) {
        String deleteSqlSchedule = "delete from schedule where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeleteSchedule = databaseConnection.prepareStatement(deleteSqlSchedule);
            preparedStmtDeleteSchedule.setString(1, cnp);

            preparedStmtDeleteSchedule.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSchedule(String cnp, String day, int startHour, int endHour) {
        String dayStart = day + "_start";
        String dayEnd = day + "_end";
        String updateSqlSchedule = "update schedule " +
                "set " + dayStart + " = ?, " +
                dayEnd + " = ? " +
                "where cnp = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateSchedule = databaseConnection.prepareStatement(updateSqlSchedule);
            preparedStmtUpdateSchedule.setInt(1, startHour);
            preparedStmtUpdateSchedule.setInt(2, endHour);
            preparedStmtUpdateSchedule.setString(3, cnp);

            preparedStmtUpdateSchedule.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}