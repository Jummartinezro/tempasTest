/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.utils;

import com.jmmartinezro.model.Scenario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Creation of the SQL request to write the parameters of the patient
 *
 * @author Juan Manuel MARTINEZ
 */
public final class GenerateSQL {

    public static final void generateSQLFile(Scenario scenario) {
        //StringBuilder sqlScript=new StringBuilder(null);
        for (String key : scenario.getSensorsData().keySet()) {
            System.out.println(key + " = " + Arrays.toString(scenario.getSensorsData().get(key)));
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < scenario.getSensorsData().get(key).length; i++) {
                float f = scenario.getSensorsData().get(key)[i];
                cal.add(Calendar.MINUTE, -1);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                System.out.println(dateFormat.format(cal.getTime())+ "\t" + f);
                //System.out.println(oneMinuteBack + "\t" + f);
            }
        }
    }
}
