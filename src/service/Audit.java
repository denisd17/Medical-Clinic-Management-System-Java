package service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Audit {
    private static Audit single_instance = null;

    private Audit() {

    }

    public static Audit getInstance() {
        if(single_instance == null)
            single_instance = new Audit();

        return single_instance;
    }

    public static void writeToAudit(int action, int table) {
        try{
            FileWriter csvWriter = new FileWriter("src/resources/Audit/audit.csv", true);

            String actionDescription = null;
            String tableName = null;
            Date actionTime = new Date();
            String actionTimeString = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(actionTime);

            switch(action) {
                case 1:
                    actionDescription = "NEW";
                    break;
                case 2:
                    actionDescription = "UPDATE";
                    break;
                case 3:
                    actionDescription = "DELETE";
                    break;
            }

            switch(table) {
                case 1:
                    tableName = "PATIENT";
                    break;
                case 2:
                    tableName = "EMPLOYEE";
                    break;
                case 3:
                    tableName = "MEDICAL SERVICE";
                    break;
                case 4:
                    tableName = "APPOINTMENT";
                    break;
                case 5:
                    tableName = "MEDICAL RECORD";
                    break;
            }


            csvWriter.append(actionDescription);
            csvWriter.append(" ");
            csvWriter.append(tableName);
            csvWriter.append(",");
            csvWriter.append(actionTimeString);
            csvWriter.append("\n");

            csvWriter.flush();
            csvWriter.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
