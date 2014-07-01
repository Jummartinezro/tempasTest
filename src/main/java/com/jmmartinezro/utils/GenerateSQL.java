/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jmmartinezro.utils;

import com.jmmartinezro.model.Scenario;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Creation of the SQL request to  write the parameters of the patient
 * @author Juan Manuel MARTINEZ
 */
public final class GenerateSQL {
    public static final void generateSQLFile(Scenario scenario){
        Calendar cal = Calendar.getInstance();
        for (String key : scenario.getSensorsData().keySet()) {
            System.out.println(key + " = " + Arrays.toString(scenario.getSensorsData().get(key)));
//            System.out.println("# Elems (" + key + ") = " + scenario.getSensorsData().get(key).length);    
//            cal.add(Calendar.SECOND, -1);
//            Date oneHourBack = cal.getTime();
//            System.out.println("Hour = " + oneHourBack);
        }
    }
}
