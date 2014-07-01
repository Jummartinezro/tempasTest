/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmartinezro.model;

import java.util.Date;

/**
 *
 * @author Juan Manuel MARTINEZ
 */
public class Scenario {

    private final int number;
    private final int babyId;
    private final Date startDate;
    private final Date endDate;

    //TODO private String action if necessary
    public Scenario(int number,int source, Date startDate, Date endDate) {
        this.number = number;
        this.babyId = source;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getNumber() {
        return number;
    }
    
    public int getBabyID() {
        return babyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Scenario{" + "number=" + number + ", source=" + babyId + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }

}
