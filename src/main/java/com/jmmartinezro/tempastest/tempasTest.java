/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.tempastest;

import com.jmmartinezro.model.Channels;
import com.jmmartinezro.model.Scenario;
import com.jmmartinezro.model.dao.Dao;
import java.util.Iterator;

/**
 *
 * @author Juan Manuel MARTINEZ
 */
public class tempasTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dao dao = new Dao();
        //TODO replace for the scenario number
        int scenarioNumber = 1;
        Scenario scenario = dao.readScenario(scenarioNumber);
        for (String key : Channels.channels.keySet()) {
            // System.out.println("Sensor Name = " + key);
            // System.out.println("Sensor Code = " + Channels.getCode(key));
            //TODO Look over the sensors to decode the blob
            dao.readSensor(scenario.getBabyID(), scenario.getStartDate(),
                    scenario.getEndDate(), key);
        }
    }
}
