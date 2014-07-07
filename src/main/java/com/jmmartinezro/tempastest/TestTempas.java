package com.jmmartinezro.tempastest;

import com.jmmartinezro.model.Baby;
import com.jmmartinezro.model.Channels;
import com.jmmartinezro.model.dao.Dao;
import java.util.Date;
import java.util.SortedMap;

/**
 * Class to save in an sql file the data of the bTalk database.
 *
 * @author Juan Manuel MARTINEZ
 */
public class TestTempas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dao dao = new Dao();
        int[] babiesIds = dao.getBabiesIds();
        int total = 0;
        for (int i = 0; i < babiesIds.length; i++) {
            Baby baby = new Baby(babiesIds[i]);
            int j = 0;
            for (String key : Channels.getKeys()) {
                SortedMap<Date, Float> data = dao.readSensor(baby.getBabyId(), key);
                if (data != null) {
                    baby.setSensorData(key, data);
                    j += data.size();
                }
                //System.out.println("Baby "+ baby.getBabyId() +"# " + key + " = " + data.size());
            }
            System.out.println("Baby " + baby.getBabyId() + " = " + j);
            total += j;
            //PrintData.printBabyData(baby);
            //PrintData.generateSQLFile(baby);
            //PrintData.printSQLFile(baby);
        }
        // Counting the values entered
        System.out.println("Total = " + total);
    }
}
