/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.model.dao;

import com.jmmartinezro.model.Channels;
import com.jmmartinezro.model.Scenario;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Manuel MARTINEZ
 */
public class Dao {

    /**
     * The connection to the DB
     */
    private Connection connection = null;

    /**
     * Constant to find the number of seconds in a minute (60 :P )
     */
    private static final int MINUTE = (int) TimeUnit.MINUTES.toSeconds(1);

    /**
     * The trial ps for querying trials table.
     */
    private PreparedStatement trialPS;

    /**
     * The trial sql for querying trials table.
     */
    //TODO get also the action (if necessary)
    private static final String trialSQL = "SELECT SourceName, StartDateTime, EndDateTime FROM bt45mysql.bt45graphvstext WHERE id = ?";

    /**
     * The binary ps for querying pbinary table.
     */
    private PreparedStatement binaryPS;

    /**
     * The binary sql for querying pbinary table.
     */
    private static final String binarySQL = "SELECT * FROM badger.PBinary WHERE DBPATID = ? AND TSECSPERVALUE=10000 AND CODE = ? AND STARTTIME < ? AND ENDTIME > ?";

    /**
     * Open the connection to the DB
     */
    public Dao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/badger", "badger", "badger");
            binaryPS = connection.prepareStatement(binarySQL);
            trialPS = connection.prepareStatement(trialSQL);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Closes the DB connection.
     */
    public void close() {
        try {
            binaryPS.close();
            trialPS.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads the scenario with his number
     *
     * @param scenarioNumber with his number
     * @return an Scenario (if exists, null o.c)
     */
    public Scenario readScenario(int scenarioNumber) {
        ResultSet resultSet;
        Scenario scenario = null;
        try {
            trialPS.setInt(1, scenarioNumber);
            resultSet = trialPS.executeQuery();
            if (resultSet.next()) {
                int sourceName = resultSet.getInt("SourceName");
                Date startDate = resultSet.getTimestamp("StartDateTime");
                Date endDate = resultSet.getTimestamp("EndDateTime");
                scenario = new Scenario(scenarioNumber, sourceName, startDate, endDate);
                System.out.println(scenario + "\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scenario;
    }

    /**
     * Reads sensor values from pbinary table
     *
     * @param babyID numeric ID of baby
     * @param startDate the start time of the period to read
     * @param endDate the end time of the period to read
     * @param key the name of the channel
     * @return an array with the values per second
     */
    public float[] readSensor(int babyID, Date startDate, Date endDate, String key) {
        int numElements = (int) ((endDate.getTime() - startDate.getTime())
                / Channels.TICKSPERVALUE);
        float[] result = new float[numElements / MINUTE - 1];
        boolean modified = false;
        try {

            Timestamp startD = new Timestamp(startDate.getTime());
            Timestamp endD = new Timestamp(endDate.getTime());

            binaryPS.setInt(1, babyID);
            binaryPS.setInt(2, Channels.getCode(key));
            binaryPS.setTimestamp(3, endD);
            binaryPS.setTimestamp(4, startD);

            ResultSet rs = binaryPS.executeQuery();
            while (rs.next()) {
                Date recordStartTime = rs.getTimestamp("STARTTIME");
                Blob data = rs.getBlob("THEVALUES");

                int startRecord = (int) ((startDate.getTime() - recordStartTime
                        .getTime()) / Channels.TICKSPERVALUE);
                int endRecord = (int) ((endDate.getTime() - recordStartTime
                        .getTime()) / Channels.TICKSPERVALUE);
                int numCopy = endRecord - startRecord;
                byte[] bytes = data.getBytes(startRecord, numCopy);

                //Every minute we save the data
                int sample = 0;
                for (int i = 0; i < numCopy && sample + MINUTE < numElements; i++) {
                    int value = (int) bytes[sample += MINUTE];
                    value = (value < 0) ? value + 256 : value;
                    result[i] = value / Channels.getDivider(key) + Channels.getOffset(key);
                }
                modified = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modified ? result : null;
    }

}
