package com.jmmartinezro.model.dao;

import com.jmmartinezro.model.Channels;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that get the data from the bTalk dataBase
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
     * "Constant" that saves the number of babies
     */
    private static int numBabies;

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
    private static final String binarySQL = "SELECT * FROM badger.PBinary WHERE DBPATID = ? AND CODE = ? AND TSECSPERVALUE=10000 ORDER BY DBPATID,CODE,STARTTIME";

    /**
     * Query to get the number of babies
     */
    private static final String numBabiesSQL = "SELECT COUNT(DISTINCT DBPATID) FROM badger.PBinary";

    /**
     * Query to get the list of the babies
     */
    private static final String listBabiesSQL = "SELECT DISTINCT DBPATID FROM badger.PBinary;";

    /**
     * Open the connection to the DB
     */
    public Dao() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/badger", "badger", "badger");
            binaryPS = connection.prepareStatement(binarySQL);
            trialPS = connection.prepareStatement(trialSQL);
            ResultSet rs = connection.createStatement().executeQuery(numBabiesSQL);
            rs.next();
            numBabies = rs.getInt(1);
        } catch (SQLException ex) {
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
     * Reads sensor values from pbinary table
     *
     * @param babyID numeric ID of baby
     * @param key the name of the channel
     * @return an array with the values per second
     */
    public SortedMap<Date, Float> readSensor(int babyID, String key) {
        SortedMap<Date, Float> result = null;
        boolean modified = false;
        try {
            ResultSet rs = setRequestValues(babyID, key);
            //TODO Change to map ou move to avoid data loss (and add date variable)
            result = new TreeMap<>();
            Calendar cal = Calendar.getInstance();
            while (rs.next()) {
                Date startDate = rs.getTimestamp("STARTTIME");
                cal.setTime(startDate);
                int numValues = rs.getInt("NUMVALUES");

                byte[] bytes = readBlob(rs, numValues);

                addValues(numValues, bytes, result, key, cal);
                modified = true;
            }
            //TODO Samples per minute
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modified ? result : null;
    }

    /**
     * Add the mean of @param numCopy the values in
     *
     * @param bytes to
     * @param result with the sensor with the specified @param key
     */
    private void addValues(int numCopy, byte[] bytes, SortedMap<Date, Float> result, String key, Calendar cal) {
        //Every minute we save the data
        float sample = 0;
        for (int i = 0; i < numCopy; i++) {
            float value = (int) bytes[i];
            value = (value < 0) ? value + 256 : value;
            value = value / Channels.getDivider(key) + Channels.getOffset(key);
            sample += value;
            if ((i + 1) % MINUTE == 0) {
                result.put(cal.getTime(), sample / MINUTE);
                cal.add(Calendar.MINUTE, +1);
                sample = 0;
            }
        }
    }

    /**
     * Set the values to binaryPS to execute the MySQL Script
     *
     * @param startDate
     * @param endDate
     * @param babyID
     * @param key
     * @return
     * @throws SQLException
     */
    private ResultSet setRequestValues(int babyID, String key) throws SQLException {
        binaryPS.setInt(1, babyID);
        binaryPS.setInt(2, Channels.getCode(key));
        ResultSet rs = binaryPS.executeQuery();
        return rs;
    }

    /**
     * Reads and decodes the sql blob
     *
     * @param rs
     * @param startIndex
     * @param numCopy
     * @return
     * @throws SQLException
     */
    private byte[] readBlob(ResultSet rs, int numCopy) throws SQLException {
        byte[] bytes;
        try {
            Blob data = rs.getBlob("THEVALUES");
            bytes = data.getBytes(0, numCopy);
        } catch (SQLException sQLException) {
            InputStream data = rs.getBinaryStream("THEVALUES");
            bytes = new byte[numCopy];
            try {
                data.read(bytes, 0, numCopy);
            } catch (IOException ex) {
                Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bytes;
    }

    /**
     * Returns a list with the Babie's ID
     *
     * @return
     */
    public int[] getBabiesIds() {
        int[] listBabies = new int[numBabies];
        try {
            ResultSet rs = connection.createStatement().executeQuery(listBabiesSQL);
            for (int i = 0; i < listBabies.length; i++) {
                rs.next();
                listBabies[i] = rs.getInt("DBPATID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listBabies;
    }
}
