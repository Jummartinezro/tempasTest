package com.jmmartinezro.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Channel's code for the sensors
 *
 * @author Juan Manuel MARTINEZ
 */
public final class Channels {

    private static final Channel HR = new Channel(101, 1, 0, "Heart Rate");
    private static final Channel TC = new Channel(102, 10, 20, "Core Temperature");
    private static final Channel TP = new Channel(103, 10, 20, "Peripheral Temperature");
    private static final Channel OX = new Channel(104, 10, 0, "Transcutaneous Oxygen");
    private static final Channel CO = new Channel(105, 10, 0, "Transcutaneous CO2");
    private static final Channel BM = new Channel(108, 1, 0, "Tension");
    private static final Channel SO = new Channel(114, 1, 0, "Oxygen Saturation");//

    /**
     * The TICKSPERVALUE that is used in the dbager database: we expect one data
     * value per XX ticks. Ticks are Java Date ticks, eg 1 ms
     */
    public static final int TICKSPERVALUE = 1000;

    /**
     * Map that stores all the channels
     */
    private static final Map<String, Channel> channels;

    /**
     * Set the map name - Abbreviation
     */
    static {
        Map<String, Channel> aMap = new HashMap<>();
        aMap.put(HR.name, HR);
        aMap.put(TC.name, TC);
        aMap.put(TP.name, TP);
        aMap.put(OX.name, OX);
        aMap.put(CO.name, CO);
        aMap.put(BM.name, BM);
        aMap.put(SO.name, SO);
        channels = Collections.unmodifiableMap(aMap);
    }

    /**
     * @return a set with the identifier of each channel
     */
    public static Set<String> getKeys() {
        return channels.keySet();
    }

    /**
     * Return the code of the channel with the specified key
     *
     * @param key
     * @return the code of the channel
     */
    public static int getCode(String key) {
        return channels.get(key).code;
    }

    /**
     * Return the divider of the channel with the specified key
     *
     * @param key
     * @return the divider of the channel
     */
    public static float getDivider(String key) {
        return channels.get(key).divider;
    }

    /**
     * Return the offset of the channel with the specified key
     *
     * @param key
     * @return the offset of the channel
     */
    public static float getOffset(String key) {
        return channels.get(key).offset;
    }

    private static final class Channel {

        private final int code;
        private final float divider;
        private final float offset;
        private final String name;

        private Channel(int code, float divider, float offset, String name) {
            this.code = code;
            this.divider = divider;
            this.offset = offset;
            this.name = name;
        }
    }
}
