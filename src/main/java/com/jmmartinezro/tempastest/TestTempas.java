/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.tempastest;

import com.jmmartinezro.model.Channels;
import com.jmmartinezro.model.Scenario;
import com.jmmartinezro.model.dao.Dao;
import com.jmmartinezro.utils.PrintData;
import java.util.ArrayList;

/**
 *
 * @author Juan Manuel MARTINEZ
 */
public class TestTempas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dao dao = new Dao();
        //TODO replace for the scenario number
        int scenarioNumber = 1;
        if (args.length > 0) {
            scenarioNumber = new Integer(args[0]);
        }
        // TODO Get the list of all the babies
        int[] babiesIds = dao.getBabiesIds();

        for (scenarioNumber = 1; scenarioNumber <= 26; scenarioNumber++) {
            Scenario scenario = dao.readScenario(scenarioNumber);
            //TODO For each baby, set the scenario (What to do if the baby doesn't have data in the scenario dates ??)
            for (int i = 0; i < babiesIds.length; i++) {
                scenario.setBabyId(babiesIds[i]);
                for (String key : Channels.getKeys()) {
                    ArrayList<Float> data = dao.readSensor(scenario.getBabyId(),
                            scenario.getStartDate(), scenario.getEndDate(), key);
                    if (data != null) {
                        scenario.setSensorData(key, data);
                    }
                }
                //PrintData.printScenarioData(scenario);
                PrintData.generateSQLFile(scenario);
                //PrintData.printSQLFile(scenario);
            }
        }
    }
}
