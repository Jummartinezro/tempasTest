/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.tempastest;

import com.jmmartinezro.model.Channels;
import com.jmmartinezro.model.Scenario;
import com.jmmartinezro.model.dao.Dao;
import com.jmmartinezro.utils.GenerateSQL;

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
        Scenario scenario = dao.readScenario(scenarioNumber);
        for (String key : Channels.channels.keySet()) {
            //TODO Look over the sensors to decode the blob
            float[] data = dao.readSensor(scenario.getBabyId(),
                    scenario.getStartDate(), scenario.getEndDate(), key);
            if (data != null) {
                scenario.setSensorData(key, data);
            }
        }
        GenerateSQL.generateSQLFile(scenario);
    }
}
