package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;

public class MotiData {

    public ArrayList<String> date, time, moti;

    public MotiData() {
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        moti = new ArrayList<String>();
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

    public ArrayList<String> getMoti() {
        return moti;
    }

    public void setMoti(String moti) {
        this.moti.add(moti);
    }
}
