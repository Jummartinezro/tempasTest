package com.jmmartinezro.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Model that stores the data of the baby 
 * @author Juan Manuel MARTINEZ
 */
public class Baby {

    /**
     * The id of the baby
     */
    private int babyId;

    /**
     * A map containing the data of each read.
     */
    private final Map<String, SortedMap<Date, Float>> sensorsData;

    /**
     * Creates a baby with the specified id
     * @param babyId
     */
    public Baby(int babyId) {
        this.babyId = babyId;
        this.sensorsData = new HashMap<>();
    }

    /**
     * @return the babyId
     */
    public int getBabyId() {
        return babyId;
    }

    /**
     * @param babyId the babyId to set
     */
    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    /**
     * @return the sensorsData
     */
    public Map<String, SortedMap<Date, Float>> getSensorsData() {
        return sensorsData;
    }

    /**
     * Add a set of data to the specified sensor.
     *
     * @param key
     * @param sensorData
     */
    public void setSensorData(String key, SortedMap<Date, Float> sensorData) {
        getSensorsData().put(key, sensorData);
    }

    @Override
    public String toString() {
        return "Baby{" + "babyId=" + babyId + '}';
    }   
}
