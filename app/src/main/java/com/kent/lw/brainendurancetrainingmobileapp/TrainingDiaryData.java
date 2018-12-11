package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;

public class TrainingDiaryData {

    public ArrayList<String> date, time, duration, type, load;

    public TrainingDiaryData() {
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        duration = new ArrayList<String>();
        type = new ArrayList<String>();
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

    public ArrayList<String> getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.add(duration);
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type.add(type);
    }

    public ArrayList<String> getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load.add(load);
    }
}
