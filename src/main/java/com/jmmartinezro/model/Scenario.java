/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juan Manuel MARTINEZ
 */
public class Scenario {

    private final int number;
    private final int babyId;
    private final Date startDate;
    private final Date endDate;
    /**
     * A map containing the data of each read.
     */
    private final Map<String, float[]> sensorsData;

    /**
     * Set a new scenario to test
     *
     * @param number
     * @param source
     * @param startDate
     * @param endDate
     */
    //TODO private String action if necessary
    public Scenario(int number, int source, Date startDate, Date endDate) {
        this.number = number;
        this.babyId = source;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sensorsData = new HashMap<>();
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the babyId
     */
    public int getBabyId() {
        return babyId;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return the sensorsData
     */
    public Map<String, float[]> getSensorsData() {
        return sensorsData;
    }

    /**
     * Add a set of data to the specified sensor.
     *
     * @param key
     * @param sensorData
     */
    public void setSensorData(String key, float[] sensorData) {
        getSensorsData().put(key, sensorData);
    }

    @Override
    public String toString() {
        return "Scenario{" + "number=" + getNumber() + ", source=" + getBabyId() + ", startDate=" + getStartDate() + ", endDate=" + getEndDate() + '}';
    }

}
