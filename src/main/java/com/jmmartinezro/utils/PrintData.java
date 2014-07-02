package com.jmmartinezro.utils;

import com.jmmartinezro.model.Scenario;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creation of the SQL request to write the parameters of the patient
 *
 * @author Juan Manuel MARTINEZ
 */
public final class PrintData {

    /**
     * StringBuilder used to write the sql file
     */
    private static StringBuilder sqlScript = null;

    /**
     * Writes a sql file with the name of the scenario, the Baby's Id and the
     * data related to
     *
     * @param scenario
     */
    public static final void generateSQLFile(Scenario scenario) {
        PrintWriter writer = createFile(scenario);

        String head
                = "DECLARE @patNum int;\n"
                + "\tSET @patNum = (SELECT Pat_Num FROM Patient WHERE Pat_Ipp = 'BT-" + scenario.getBabyId() + "');\n";
        sqlScript = new StringBuilder(head.length());

        // For each sensor
        for (String key : scenario.getSensorsData().keySet()) {
            sqlScript.append(head)
                    .append("\nDECLARE @paramNum int;\n")
                    .append("\tSET @paramNum = (SELECT Tpara_Num FROM Thes_Param_Pat WHERE Tpara_Libelle='")
                    .append(key).append("');\n\n")
                    .append("DECLARE @paramUnit varchar(10);\n")
                    .append("\tSET @paramUnit = (SELECT Tpara_Unite FROM Thes_Param_Pat where Tpara_Libelle='")
                    .append(key).append("');\n")
                    .append("\nINSERT INTO Pat_Parametre(Pat_Num, Tpara_Num, Ppara_Date, Ppara_Unite, Ppara_Valeur, Ppara_Lib_Valeur, Tutil_Num, Tdate_Mod) VALUES\n");

            Calendar cal = Calendar.getInstance();
            // For each value of the sensor
            for (int i = 0; i < scenario.getSensorsData().get(key).length; i++) {
                float f = scenario.getSensorsData().get(key)[i];
                cal.add(Calendar.MINUTE, -1);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss.SSS");
                sqlScript.append("\t(@patNum, @paramNum,'")
                        .append(dateFormat.format(cal.getTime()))
                        .append("', @paramUnit, ").append(f)
                        .append(", @paramUnit, 12101, GETDATE()),\n");
            }
            sqlScript.deleteCharAt(sqlScript.length() - 2);
            sqlScript.replace(sqlScript.length() - 1, sqlScript.length() - 1, ";\nGO\n");
        }
        // Append the delete script if necesary
        sqlScript.append("/**** DELETE RECORDS *** \n\n")
                .append(head)
                .append("DELETE FROM Pat_Parametre WHERE Pat_Num=@patNum;\nGO\n\n")
                .append("****/");

        if (writer != null) {
            writer.write(sqlScript.toString());
            writer.close();
        }
    }

    /**
     * Creates a file with the specified scenario
     *
     * @param scenario
     * @return
     */
    private static PrintWriter createFile(Scenario scenario) {
        Path pathToFile = Paths.get("GeneratedScripts/Scenario_" + scenario.getNumber() + "_Baby_" + scenario.getBabyId() + ".sql");
        PrintWriter writer = null;
        try {
            Files.createDirectories(pathToFile.getParent());
            Files.createFile(pathToFile);
            writer = new PrintWriter(pathToFile.toString(), "UTF-8");
        } catch (FileAlreadyExistsException ex) {
            //Do nothing because file already exist
        } catch (IOException ex) {
            Logger.getLogger(PrintData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return writer;
    }

    /**
     * Prints in console all the sensor's data related to the scenario
     *
     * @param scenario
     */
    public static final void printScenarioData(Scenario scenario) {
        // For each sensor
        for (String key : scenario.getSensorsData().keySet()) {
            System.out.println(key + " = " + Arrays.toString(scenario.getSensorsData().get(key)));
        }
    }

    /**
     * Prints the SQL file in the console, null if it was not generated
     */
    public static final void printSQLFile() {
        System.out.println(sqlScript);
    }

}
