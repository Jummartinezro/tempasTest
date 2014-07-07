package com.jmmartinezro.utils;

import com.jmmartinezro.model.Baby;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static final void generateSQLFile(Baby scenario) {
        PrintWriter writer = createFile(scenario);
        generateString(scenario);
        if (writer != null) {
            writer.write(sqlScript.toString());
            writer.close();
        }
    }

    /**
     * Generate the string to be writed in the console or in a file
     *
     * @param baby
     */
    private static void generateString(Baby baby) {
        String head
                = "DECLARE @patNum int;\n"
                + "\tSET @patNum = (SELECT Pat_Num FROM Patient WHERE Pat_Ipp = 'BT-" + baby.getBabyId() + "');\n";
        //TODO add delete script at the beginning of the file
        sqlScript = new StringBuilder(head.length());
        sqlScript.append(head)
                .append("DELETE FROM Pat_Parametre WHERE Pat_Num=@patNum;\nGO\n\n");
        // For each sensor
        for (String key : baby.getSensorsData().keySet()) {
            sqlScript.append(head)
                    .append("\nDECLARE @paramNum int;\n")
                    .append("\tSET @paramNum = (SELECT Tpara_Num FROM Thes_Param_Pat WHERE Tpara_Libelle='")
                    .append(key).append("');\n\n")
                    .append("DECLARE @paramUnit varchar(10);\n")
                    .append("\tSET @paramUnit = (SELECT Tpara_Unite FROM Thes_Param_Pat where Tpara_Libelle='")
                    .append(key).append("');\n")
                    .append("\nINSERT INTO Pat_Parametre(Pat_Num, Tpara_Num, Ppara_Date, Ppara_Unite, Ppara_Valeur, Ppara_Lib_Valeur, Tutil_Num, Tdate_Mod) VALUES\n");

            // For each value of the sensor
            int i = 1;
            for (Date date : baby.getSensorsData().get(key).keySet()) {
                float f = baby.getSensorsData().get(key).get(date);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss.SSS");
                sqlScript.append("\t(@patNum, @paramNum,'")
                        .append(dateFormat.format(date))
                        .append("', @paramUnit, ").append(f)
                        .append(", @paramUnit, 12101, GETDATE()),\n");
                if (i++ % 1000 == 0) {
                    i = 1;
                    sqlScript.deleteCharAt(sqlScript.length() - 2);
                    sqlScript.replace(sqlScript.length() - 1, sqlScript.length() - 1, ";\nGO\n");
                    sqlScript.append(head)
                            .append("\nDECLARE @paramNum int;\n")
                            .append("\tSET @paramNum = (SELECT Tpara_Num FROM Thes_Param_Pat WHERE Tpara_Libelle='")
                            .append(key).append("');\n\n")
                            .append("DECLARE @paramUnit varchar(10);\n")
                            .append("\tSET @paramUnit = (SELECT Tpara_Unite FROM Thes_Param_Pat where Tpara_Libelle='")
                            .append(key).append("');\n");
                    sqlScript.append("\nINSERT INTO Pat_Parametre(Pat_Num, Tpara_Num, Ppara_Date, Ppara_Unite, Ppara_Valeur, Ppara_Lib_Valeur, Tutil_Num, Tdate_Mod) VALUES\n");
                }
            }
            sqlScript.deleteCharAt(sqlScript.length() - 2);
            sqlScript.replace(sqlScript.length() - 1, sqlScript.length() - 1, ";\nGO\n");
        }
        // Append the delete script if necesary
        sqlScript.append("/**** DELETE RECORDS *** \n\n")
                .append(head)
                .append("DELETE FROM Pat_Parametre WHERE Pat_Num=@patNum;\nGO\n\n")
                .append("****/");
    }

    /**
     * Creates a file with the specified scenario
     *
     * @param baby
     * @return
     */
    private static PrintWriter createFile(Baby baby) {
        Path pathToFile = Paths.get("GeneratedScripts/Baby_" + baby.getBabyId() + ".sql");
        System.out.println("Writing file " + pathToFile);
        PrintWriter writer = null;
        try {
            Files.createDirectories(pathToFile.getParent());
            Files.createFile(pathToFile);
            writer = new PrintWriter(pathToFile.toString(), "UTF-8");
        } catch (FileAlreadyExistsException ex) {
            //TODO Delete and create
        } catch (IOException ex) {
            Logger.getLogger(PrintData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return writer;
    }

    /**
     * Prints in console all the sensor's data related to the scenario
     *
     * @param baby
     */
    public static final void printBabyData(Baby baby) {
        System.out.println("\n" + baby + "\n");
        // For each sensor       
        for (String key : baby.getSensorsData().keySet()) {
            System.out.println(key + " = ");
            // For each date
            for (Date date : baby.getSensorsData().get(key).keySet()) {
                System.out.println(date + "\t=\t" + baby.getSensorsData().get(key).get(date));
            }
        }
    }

    /**
     * Prints the SQL file in the console
     *
     * @param baby the baby to print
     */
    public static final void printSQLFile(Baby baby) {
        generateString(baby);
        System.out.println(sqlScript);
    }
}
