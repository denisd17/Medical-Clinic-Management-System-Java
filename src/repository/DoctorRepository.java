package repository;

import config.DatabaseConfiguration;
import model.medical_services.Appointment;
import model.person.Doctor;
import model.person.Nurse;
import model.person.Specialization;
import model.person.WorkDay;

import java.sql.*;
import java.util.ArrayList;


public class DoctorRepository {
    public void insertDoctor(Doctor doctor) {
        String insertSqlDoctor = "insert into doctors values(?, ?, ?, ?, ?, ?)";
        String insertSqlConsultation = "insert into services(type, name, price, specialization_id) " +
                "values(?, ?, ?, ?)";
        String insertSqlSpecialization = "insert into specializations(name) values(?)";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            int specializationId = checkIfSpecializationExists(doctor.getSpecializare());

            if(specializationId == -1) {
                PreparedStatement preparedStmtInsertSpecialization = databaseConnection.prepareStatement(insertSqlSpecialization, Statement.RETURN_GENERATED_KEYS);

                preparedStmtInsertSpecialization.setString(1, doctor.getSpecializare().getSpecializationName());
                preparedStmtInsertSpecialization.executeUpdate();

                ResultSet generatedKeys = preparedStmtInsertSpecialization.getGeneratedKeys();
                generatedKeys.next();
                specializationId = generatedKeys.getInt(1);

                PreparedStatement preparedStmtInsertConsultation = databaseConnection.prepareStatement(insertSqlConsultation);
                preparedStmtInsertConsultation.setInt(1,0);
                preparedStmtInsertConsultation.setString(2,doctor.getSpecializare().getSpecializationName());
                preparedStmtInsertConsultation.setFloat(3, (float)99.99);
                preparedStmtInsertConsultation.setInt(4, specializationId);
                preparedStmtInsertConsultation.executeUpdate();
            }

            PreparedStatement preparedStmtInsertDoctor = databaseConnection.prepareStatement(insertSqlDoctor);
            preparedStmtInsertDoctor.setString(1, doctor.getCnp());
            preparedStmtInsertDoctor.setString(2, doctor.getFirstName());
            preparedStmtInsertDoctor.setString(3, doctor.getLastName());
            preparedStmtInsertDoctor.setString(4, doctor.getPhoneNumber());
            preparedStmtInsertDoctor.setString(5, doctor.getEmail());
            preparedStmtInsertDoctor.setInt(6, specializationId);
            preparedStmtInsertDoctor.executeUpdate();

            //Inserarea programului de lucru
            ScheduleRepository scheduleRepository = new ScheduleRepository();
            scheduleRepository.insertSchedule(doctor.getCnp(), doctor.getSchedule());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Doctor getDoctor(String cnp) {
        String getSqlDoctor = "select * from doctors " +
                " where cnp = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();

        try {
            PreparedStatement preparedStmtGetDoctor = databaseConnection.prepareStatement(getSqlDoctor);
            preparedStmtGetDoctor.setString(1, cnp);

            ResultSet resultSet = preparedStmtGetDoctor.executeQuery();

            if(resultSet.next()) {
                ArrayList<Appointment> appointments = appointmentRepository.getAppointmentsByCnp(resultSet.getString(1), 2);
                WorkDay[] schedule = scheduleRepository.getSchedule(cnp);
                Specialization specialization = getSpecialization(resultSet.getInt(6));

                return new Doctor(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                resultSet.getString(5), resultSet.getString(1), schedule, appointments, specialization);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Doctor> getAllDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();

        String getSqlDoctor = "select * from doctors";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();

        try {
            PreparedStatement preparedStmtGetDoctor = databaseConnection.prepareStatement(getSqlDoctor);

            ResultSet resultSet = preparedStmtGetDoctor.executeQuery();

            if(resultSet.next()) {
                ArrayList<Appointment> appointments = appointmentRepository.getAppointmentsByCnp(resultSet.getString(1), 2);
                WorkDay[] schedule = scheduleRepository.getSchedule(resultSet.getString(1));
                Specialization specialization = getSpecialization(resultSet.getInt(6));

                Doctor doctor = new Doctor(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getString(1), schedule, appointments, specialization);

                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public void deleteDoctor(String cnp) {
        String getSpecId = "select specialization_id from doctors where cnp = ?";
        String deleteSqlDoctor = "delete from doctors where cnp = ?";
        String countDoctors = "select count(*) from doctors where specialization_id = ?";
        String deleteSpecialization = "delete from specializations where id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        ScheduleRepository scheduleRepository = new ScheduleRepository();

        try {
            PreparedStatement preparedStmtGetSpecId = databaseConnection.prepareStatement(getSpecId);
            preparedStmtGetSpecId.setString(1, cnp);

            ResultSet rs = preparedStmtGetSpecId.executeQuery();
            rs.next();
            int specId = rs.getInt(1);

            PreparedStatement preparedStmtDeleteDoctor = databaseConnection.prepareStatement(deleteSqlDoctor);
            preparedStmtDeleteDoctor.setString(1, cnp);

            preparedStmtDeleteDoctor.executeUpdate();
            scheduleRepository.deleteSchedule(cnp);

            PreparedStatement preparedStmtGetSpecCount = databaseConnection.prepareStatement(countDoctors);
            preparedStmtGetSpecCount.setInt(1, specId);
            ResultSet rs2 = preparedStmtGetSpecCount.executeQuery();
            rs2.next();

            if(rs2.getInt(1) == 0) {
                PreparedStatement preparedStmtDeleteSpec = databaseConnection.prepareStatement(deleteSpecialization);
                preparedStmtDeleteSpec.setInt(1, specId);

                preparedStmtDeleteSpec.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctorFirstName(String cnp, String firstName) {
        String updateSqlDoctor = "update doctors " +
                "set first_name = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateDoctor = databaseConnection.prepareStatement(updateSqlDoctor);
            preparedStmtUpdateDoctor.setString(1, firstName);
            preparedStmtUpdateDoctor.setString(2, cnp);

            preparedStmtUpdateDoctor.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctorLastName(String cnp, String lastName) {
        String updateSqlDoctor = "update doctors " +
                "set last_name = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateDoctor = databaseConnection.prepareStatement(updateSqlDoctor);
            preparedStmtUpdateDoctor.setString(1, lastName);
            preparedStmtUpdateDoctor.setString(2, cnp);

            preparedStmtUpdateDoctor.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctorPhoneNumber(String cnp, String phoneNumber) {
        String updateSqlDoctor = "update doctors " +
                "set phone_number = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateDoctor = databaseConnection.prepareStatement(updateSqlDoctor);
            preparedStmtUpdateDoctor.setString(1, phoneNumber);
            preparedStmtUpdateDoctor.setString(2, cnp);

            preparedStmtUpdateDoctor.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctorEmail(String cnp, String email) {
        String updateSqlDoctor = "update doctors " +
                "set email = ? " +
                "where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateDoctor = databaseConnection.prepareStatement(updateSqlDoctor);
            preparedStmtUpdateDoctor.setString(1, email);
            preparedStmtUpdateDoctor.setString(2, cnp);

            preparedStmtUpdateDoctor.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctorSchedule(String cnp, String day, int startHour, int endHour) {
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        scheduleRepository.updateSchedule(cnp, day, startHour, endHour);
    }

    public boolean checkIfDoctorExists(String cnp) {
        String checkSqlDoctor = "select count(*) from doctors where cnp = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtCheckDoctor = databaseConnection.prepareStatement(checkSqlDoctor);
            preparedStmtCheckDoctor.setString(1, cnp);


            ResultSet rs = preparedStmtCheckDoctor.executeQuery();
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

    //Returneaza -1 daca specializarea nu exista
    //Returneaza id-ul acesteia daca exista
    public int checkIfSpecializationExists(Specialization specialization) {
        String checkSqlSpecialization = "select id from specializations where name = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtCheckSpecialization = databaseConnection.prepareStatement(checkSqlSpecialization);
            preparedStmtCheckSpecialization.setString(1, specialization.getSpecializationName());
            ResultSet rs = preparedStmtCheckSpecialization.executeQuery();

            //Specializarea exista deja
            if(rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    //Insereaza specializarea (in cazul in care nu exista) si returneaza id-ul alocat acesteia
    public int insertSpecialization(Specialization specialization) {
        String insertSqlSpecialization = "insert into specializations(name) values(?)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
        int result = checkIfSpecializationExists(specialization);

        if(result != -1) {
            return result;
        }
        else {
            try {
                PreparedStatement preparedStmtInsertSpecialization = databaseConnection.prepareStatement(insertSqlSpecialization, Statement.RETURN_GENERATED_KEYS);
                preparedStmtInsertSpecialization.setString(1, specialization.getSpecializationName());
                preparedStmtInsertSpecialization.executeUpdate();
                ResultSet rs2 = preparedStmtInsertSpecialization.getGeneratedKeys();
                rs2.next();

                return rs2.getInt(1);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public Specialization getSpecialization(int id) {
        String getSqlSpecialization = "select name from specializations where id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetSpecialization = databaseConnection.prepareStatement(getSqlSpecialization);
            preparedStmtGetSpecialization.setInt(1, id);
            ResultSet resultSet = preparedStmtGetSpecialization.executeQuery();

            if(resultSet.next()) {
                return new Specialization(resultSet.getString(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteSpecialization(Specialization specialization) {
        String deleteSqlSpecialization = "delete from specializations where name = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeleteSpecialization = databaseConnection.prepareStatement(deleteSqlSpecialization);
            preparedStmtDeleteSpecialization.setString(1, specialization.getSpecializationName());
            preparedStmtDeleteSpecialization.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}