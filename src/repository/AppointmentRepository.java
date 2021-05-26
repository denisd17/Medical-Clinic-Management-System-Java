package repository;

import config.DatabaseConfiguration;
import model.medical_services.*;
import model.person.Doctor;
import model.person.Specialization;
import model.person.WorkDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class AppointmentRepository {
    public void insertAppointment(Appointment appointment) {
        String insertSqlAppointment = "insert into appointments(patient_cnp, nurse_cnp, doctor_cnp, appointment_date, hour) " +
                "values(?, ?, ?, ?, ?)";
        String insertSqlAppService = "insert into app_services(app_id, service_id) " +
                "values(?, ?)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            //Inserare in tabelul appointments
            PreparedStatement preparedStmtInsertAppointment = databaseConnection.prepareStatement(insertSqlAppointment, Statement.RETURN_GENERATED_KEYS);

            preparedStmtInsertAppointment.setString(1, appointment.getPatientNumericCode());
            preparedStmtInsertAppointment.setString(2, appointment.getNurseNumericCode());
            preparedStmtInsertAppointment.setString(3, appointment.getDoctorNumericCode());
            java.sql.Date sqlDate = new java.sql.Date(appointment.getDate().getTime());
            preparedStmtInsertAppointment.setDate(4, sqlDate);
            preparedStmtInsertAppointment.setInt(5, appointment.getHour());

            preparedStmtInsertAppointment.executeUpdate();

            ResultSet generatedKeys = preparedStmtInsertAppointment.getGeneratedKeys();
            generatedKeys.next();
            int appId = generatedKeys.getInt(1);

            //Inserare in app_services
            for(MedicalService m : appointment.getMedicalServices()){
                PreparedStatement preparedStmtInsertAppService = databaseConnection.prepareStatement(insertSqlAppService);
                preparedStmtInsertAppService.setInt(1, appId);
                preparedStmtInsertAppService.setInt(2, m.getId());

                preparedStmtInsertAppService.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Appointment getAppointmentById(int id) {
        String getSqlAppointment = "select * from appointments where id = ?";
        String getSqlAppointmentServices = "select s.type, s.price, s.name, spec.name from app_services aps " +
                "join services s " +
                "on(aps.service_id = s.id) " +
                "left join specializations spec " +
                "on(s.specialization_id = spec.id) " +
                "where aps.app_id = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtGetAppointment = databaseConnection.prepareStatement(getSqlAppointment);
            preparedStmtGetAppointment.setInt(1, id);

            ResultSet resultSet = preparedStmtGetAppointment.executeQuery();

            resultSet.next();
            java.util.Date appDate = new java.util.Date(resultSet.getDate(5).getTime());
            Appointment newAppointment = new Appointment(resultSet.getInt(1), appDate, resultSet.getInt(6),
                    resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));

            PreparedStatement preparedStmtGetAppointmentServices = databaseConnection.prepareStatement(getSqlAppointmentServices);
            preparedStmtGetAppointmentServices.setInt(1, id);
            ResultSet resultSet2 = preparedStmtGetAppointmentServices.executeQuery();

            while(resultSet2.next()) {
                int serviceType = resultSet2.getInt(1);
                float price = resultSet2.getFloat(2);
                String serviceName = resultSet2.getString(3);
                String specName = resultSet2.getString(4);

                //Consultation
                if(serviceType == 0){
                    Specialization specialization = new Specialization(specName);
                    Consultation newConsultation = new Consultation(price, specialization);
                    newAppointment.addMedicalService(newConsultation);
                }
                //Radiography
                else if(serviceType == 1){
                    RadiographyArea area = RadiographyArea.valueOf(serviceName);
                    Radiography newRadiography = new Radiography(price, area);
                    newAppointment.addMedicalService(newRadiography);
                }
                //Test
                else {
                    TestType testType = TestType.valueOf(serviceName);
                    Test newTest = new Test(price, testType);
                    newAppointment.addMedicalService(newTest);
                }
            }

            return newAppointment;
        }
        catch(SQLException e){
                e.printStackTrace();
        }

        return null;
    }

    //0 - Patient
    //1 - Nurse
    //2 - Doctor
    public ArrayList<Appointment> getAppointmentsByCnp(String cnp, int personType) {
        String getSqlPatientAppointments = "select * from appointments " +
                "where patient_cnp = ?";
        String getSqlDoctorAppointments = "select * from appointments " +
                "where doctor_cnp = ?";
        String getSqlNurseAppointments = "select * from appointments " +
                "where nurse_cnp = ?";

        String getSqlAppointmentServices = "select s.type, s.price, s.name, spec.name from app_services aps " +
                "join services s " +
                "on(aps.service_id = s.id) " +
                "left join specializations spec " +
                "on(s.specialization_id = spec.id) " +
                "where aps.app_id = ?";

        ArrayList<Appointment> appointments = new ArrayList<>();

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            //Pacient
            if(personType == 0) {
                PreparedStatement preparedStmtGetPatientAppointments = databaseConnection.prepareStatement(getSqlPatientAppointments);
                preparedStmtGetPatientAppointments.setString(1, cnp);

                ResultSet resultSet = preparedStmtGetPatientAppointments.executeQuery();

                while(resultSet.next()) {
                    java.util.Date appDate = new java.util.Date(resultSet.getDate(5).getTime());
                    Appointment newAppointment = new Appointment(resultSet.getInt(1), appDate, resultSet.getInt(6),
                            resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));

                    PreparedStatement preparedStmtGetAppointmentServices = databaseConnection.prepareStatement(getSqlAppointmentServices);
                    preparedStmtGetAppointmentServices.setInt(1, newAppointment.getId());

                    ResultSet resultSet2 = preparedStmtGetAppointmentServices.executeQuery();

                    while(resultSet2.next()) {
                        int serviceType = resultSet2.getInt(1);
                        float price = resultSet2.getFloat(2);
                        String serviceName = resultSet2.getString(3);
                        String specName = resultSet2.getString(4);

                        //Consultation
                        if(serviceType == 0){
                            Specialization specialization = new Specialization(specName);
                            Consultation newConsultation = new Consultation(price, specialization);
                            newAppointment.addMedicalService(newConsultation);
                        }
                        //Radiography
                        else if(serviceType == 1){
                            RadiographyArea area = RadiographyArea.valueOf(serviceName);
                            Radiography newRadiography = new Radiography(price, area);
                            newAppointment.addMedicalService(newRadiography);
                        }
                        //Test
                        else {
                            TestType testType = TestType.valueOf(serviceName);
                            Test newTest = new Test(price, testType);
                            newAppointment.addMedicalService(newTest);
                        }
                    }
                    appointments.add(newAppointment);
                }
            }
            //Asistenta
            else if(personType == 1){
                PreparedStatement preparedStmtGetNurseAppointments = databaseConnection.prepareStatement(getSqlNurseAppointments);
                preparedStmtGetNurseAppointments.setString(1, cnp);

                ResultSet resultSet = preparedStmtGetNurseAppointments.executeQuery();

                while(resultSet.next()) {
                    java.util.Date appDate = new java.sql.Date(resultSet.getDate(2).getTime());
                    Appointment newAppointment = new Appointment(resultSet.getInt(1), appDate, resultSet.getInt(3),
                            resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));

                    PreparedStatement preparedStmtGetAppointmentServices = databaseConnection.prepareStatement(getSqlAppointmentServices);
                    preparedStmtGetAppointmentServices.setInt(1, newAppointment.getId());

                    ResultSet resultSet2 = preparedStmtGetAppointmentServices.executeQuery();

                    while(resultSet2.next()) {
                        int serviceType = resultSet2.getInt(1);
                        float price = resultSet2.getFloat(2);
                        String serviceName = resultSet2.getString(3);
                        String specName = resultSet2.getString(4);

                        //Consultation
                        if(serviceType == 0){
                            Specialization specialization = new Specialization(specName);
                            Consultation newConsultation = new Consultation(price, specialization);
                            newAppointment.addMedicalService(newConsultation);
                        }
                        //Radiography
                        else if(serviceType == 1){
                            RadiographyArea area = RadiographyArea.valueOf(serviceName);
                            Radiography newRadiography = new Radiography(price, area);
                            newAppointment.addMedicalService(newRadiography);
                        }
                        //Test
                        else {
                            TestType testType = TestType.valueOf(serviceName);
                            Test newTest = new Test(price, testType);
                            newAppointment.addMedicalService(newTest);
                        }
                    }
                    appointments.add(newAppointment);
                }
            }
            //Doctor
            else {
                PreparedStatement preparedStmtGetDoctorAppointments = databaseConnection.prepareStatement(getSqlDoctorAppointments);
                preparedStmtGetDoctorAppointments.setString(1, cnp);

                ResultSet resultSet = preparedStmtGetDoctorAppointments.executeQuery();

                while(resultSet.next()) {
                    java.util.Date appDate = new java.sql.Date(resultSet.getDate(2).getTime());
                    Appointment newAppointment = new Appointment(resultSet.getInt(1), appDate, resultSet.getInt(3),
                            resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));

                    PreparedStatement preparedStmtGetAppointmentServices = databaseConnection.prepareStatement(getSqlAppointmentServices);
                    preparedStmtGetAppointmentServices.setInt(1, newAppointment.getId());

                    ResultSet resultSet2 = preparedStmtGetAppointmentServices.executeQuery();

                    while(resultSet2.next()) {
                        int serviceType = resultSet2.getInt(1);
                        float price = resultSet2.getFloat(2);
                        String serviceName = resultSet2.getString(3);
                        String specName = resultSet2.getString(4);

                        //Consultation
                        if(serviceType == 0){
                            Specialization specialization = new Specialization(specName);
                            Consultation newConsultation = new Consultation(price, specialization);
                            newAppointment.addMedicalService(newConsultation);
                        }
                        //Radiography
                        else if(serviceType == 1){
                            RadiographyArea area = RadiographyArea.valueOf(serviceName);
                            Radiography newRadiography = new Radiography(price, area);
                            newAppointment.addMedicalService(newRadiography);
                        }
                        //Test
                        else {
                            TestType testType = TestType.valueOf(serviceName);
                            Test newTest = new Test(price, testType);
                            newAppointment.addMedicalService(newTest);
                        }
                    }
                    appointments.add(newAppointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    public void deleteAppointment(int id) {
        String deleteSqlAppointment = "delete from appointments " +
                "where id = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtDeleteAppointment = databaseConnection.prepareStatement(deleteSqlAppointment);
            preparedStmtDeleteAppointment.setInt(1, id);

            preparedStmtDeleteAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMedicalService(int appointmentId, MedicalService medicalService) {
        String insertSqlMedicalService = "insert into app_services(app_id, service_id) values(?, ?)";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateAppointment = databaseConnection.prepareStatement(insertSqlMedicalService);
            preparedStmtUpdateAppointment.setInt(1, appointmentId);
            preparedStmtUpdateAppointment.setInt(2, medicalService.getId());

            preparedStmtUpdateAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteMedicalService(int appointmentId, MedicalService medicalService) {
        String deleteSqlMedicalService = "delete from app_services " +
                "where app_id = ? and service_id = ?";

        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateAppointment = databaseConnection.prepareStatement(deleteSqlMedicalService);
            preparedStmtUpdateAppointment.setInt(1, appointmentId);
            preparedStmtUpdateAppointment.setInt(2, medicalService.getId());

            preparedStmtUpdateAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeDate(int appointmentId, Date date) {
        String updateSqlAppointment = "update appointments " +
                "set appointment_date = ? " +
                "where id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStmtUpdateAppointment = databaseConnection.prepareStatement(updateSqlAppointment);
            preparedStmtUpdateAppointment.setDate(1, sqlDate);
            preparedStmtUpdateAppointment.setInt(2, appointmentId);

            preparedStmtUpdateAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeHour(int appointmentId, int hour) {
        String updateSqlAppointment = "update appointments " +
                "set hour = ? " +
                "where id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateAppointment = databaseConnection.prepareStatement(updateSqlAppointment);
            preparedStmtUpdateAppointment.setInt(1, hour);
            preparedStmtUpdateAppointment.setInt(2, appointmentId);

            preparedStmtUpdateAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeNurse(int appointmentId, String cnp) {
        String updateSqlAppointment = "update appointments " +
                "set nurse_cnp = ? " +
                "where id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateAppointment = databaseConnection.prepareStatement(updateSqlAppointment);
            preparedStmtUpdateAppointment.setString(1, cnp);
            preparedStmtUpdateAppointment.setInt(2, appointmentId);

            preparedStmtUpdateAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeDoctor(int appointmentId, String cnp) {
        String updateSqlAppointment = "update appointments " +
                "set doctor_cnp = ? " +
                "where id = ?";
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();

        try {
            PreparedStatement preparedStmtUpdateAppointment = databaseConnection.prepareStatement(updateSqlAppointment);
            preparedStmtUpdateAppointment.setString(1, cnp);
            preparedStmtUpdateAppointment.setInt(2, appointmentId);

            preparedStmtUpdateAppointment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
