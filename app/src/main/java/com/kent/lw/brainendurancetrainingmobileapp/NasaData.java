package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;

public class NasaData {

    public ArrayList<String> date, time, temporal, mental, physical, frustration, performance, effort;

    public NasaData() {
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        temporal = new ArrayList<String>();
        mental = new ArrayList<String>();
        physical = new ArrayList<String>();
        frustration = new ArrayList<String>();
        performance = new ArrayList<String>();
        effort = new ArrayList<String>();
    }


    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date.add(date);
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time.add(time);
    }

    public ArrayList<String> getTemporal() {
        return temporal;
    }

    public void setTemporal(String temporal) {
        this.temporal.add(temporal);
    }

    public ArrayList<String> getMental() {
        return mental;
    }

    public void setMental(String mental) {
        this.mental.add(mental);
    }

    public ArrayList<String> getPhysical() {
        return physical;
    }

    public void setPhysical(String physical) {
        this.physical.add(physical);
    }

    public ArrayList<String> getFrustration() {
        return frustration;
    }

    public void setFrustration(String frustration) {
        this.frustration.add(frustration);
    }

    public ArrayList<String> getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance.add(performance);
    }

    public ArrayList<String> getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort.add(effort);
    }

    public void reset() {
        date.clear();
        time.clear();
        temporal.clear();
        mental.clear();
        physical.clear();
        frustration.clear();
        performance.clear();
        effort.clear();
    }
}

