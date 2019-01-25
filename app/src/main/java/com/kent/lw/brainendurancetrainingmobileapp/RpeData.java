package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;

public class RpeData {

    public ArrayList<String> date, time, load;

    public RpeData() {
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        load = new ArrayList<String>();
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

    public ArrayList<String> getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load.add(load);
    }

    public void reset() {
        date.clear();
        time.clear();
        load.clear();
    }
}
