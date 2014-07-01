/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Channel's code for the sensors
 *
 * @author Juan Manuel MARTINEZ
 */
public final class Channels {

    public static final int HR = 101;
    public static final int TC = 102;
    public static final int TP = 103;
    public static final int OX = 104;
    public static final int CO = 105;
    public static final int BM = 108;
    public static final int SO = 114;

    /**
     * The TICKSPERVALUE that is used in the dbager database: we expect one data
     * value per XX ticks. Ticks are Java Date ticks, eg 1 ms
     */
    public static final int TICKSPERVALUE = 1000;

    /**
     * Map that stores all the channels
     */
    public static final Map<String, Integer> channels;

    static {
        Map<String, Integer> aMap = new HashMap<>();
        aMap.put("HR", HR);
        aMap.put("TC", TC);
        aMap.put("TP", TP);
        aMap.put("OX", OX);
        aMap.put("CO", CO);
        aMap.put("BM", BM);
        aMap.put("SO", SO);
        channels = Collections.unmodifiableMap(aMap);
    }

    public static final int getCode(String nameChannel) {
        return channels.get(nameChannel);
    }
}
