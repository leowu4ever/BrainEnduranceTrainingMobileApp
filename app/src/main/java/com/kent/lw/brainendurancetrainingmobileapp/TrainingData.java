package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;
import java.util.List;

public class TrainingData {

    // user info
    public Long id;
    public String name;

    // for activity
    public String activity, duration, task, dif;

    public Task taskConfig;

    // cognitive task
    public int stiCount, resCount, hitResCount, lapseCount;
    public float accuracy;
    public long totalResTime, avgResTime;

    public List<Long> stiMiliList, resMiliList, resTimeList;
    public List<Integer> stiTypeList;

    // physical
    public float distance, avgSpeed, avgPace;
    public String startTime;
    public long time;

    public List<Double> latList, lngList;

    public TrainingData() {
        latList = new ArrayList<Double>();
        lngList = new ArrayList<Double>();

        stiMiliList = new ArrayList<Long>();
        resMiliList = new ArrayList<Long>();
        resTimeList = new ArrayList<Long>();
        stiTypeList = new ArrayList<Integer>();

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTask() {
        return this.task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDif() {
        return this.dif;
    }

    public void setDif(String dif) {
        this.dif = dif;
    }

    public Task getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(Task taskConfig) {
        this.taskConfig = taskConfig;
    }


    public int getStiCount() {
        return stiCount;
    }

    public void setStiCount(int stiCount) {
        this.stiCount = stiCount;
    }

    public int getResCount() {
        return resCount;
    }

    public void setResCount(int resCount) {
        this.resCount = resCount;
    }

    public int getHitResCount() {
        return hitResCount;
    }

    public void setHitResCount(int hitResCount) {
        this.hitResCount = hitResCount;
    }

    public int getLapseCount() {
        return lapseCount;
    }

    public void setLapseCount(int lapseCount) {
        this.lapseCount = lapseCount;
    }

    public float getAccuracy() {
        float a = (float) hitResCount / (float) stiCount * 100;
        a = Float.parseFloat(String.format("%.1f", a));
        if (!Float.isNaN(a) && !Float.isInfinite(a)) {
            return a;
        } else {
            return 0;
        }
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public long getTotalResTime() {
        return totalResTime;
    }

    public void setTotalResTime(int totalResTime) {
        this.totalResTime = totalResTime;
    }

    public long getAvgResTime() {
        if (hitResCount == 0) {
            return 0;
        } else {
            return totalResTime / hitResCount;
        }
    }

    public void setAvgResTime(long avgResTime) {
        this.avgResTime = avgResTime;
    }

    public List<Long> getResMiliList() {
        return resMiliList;
    }

    public void setResMiliList(Long resMili) {
        resMiliList.add(resMili);
    }

    public List<Long> getStiMiliList() {
        return stiMiliList;
    }

    public void setStiMiliList(Long stiMili) {
        stiMiliList.add(stiMili);
    }

    public List<Integer> getStiTypeList() {
        return stiTypeList;
    }

    public void setStiTypeList(int stiType) {
        this.stiTypeList.add(stiType);
    }

    public int getStiTypeOn(int index) {
        return stiTypeList.get(index);
    }

    public void setStiTypeOn(int index, int stiType) {
        this.stiTypeList.set(index, stiType);
    }

    public List<Long> getResTimeList() {
        return resTimeList;
    }

    public void setResTimeList(Long resTime) {
        resTimeList.add(resTime);
    }

    public float getDistance() {
        return Float.parseFloat(String.format("%.3f", distance));
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAvgSpeed() {
        if (!Float.isNaN(avgSpeed) && !Float.isInfinite(avgSpeed)) {
            return Float.parseFloat(String.format("%.1f", avgSpeed));
        } else {
            return 0;
        }
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getAvgPace() {
        if (!Float.isNaN(avgPace) && !Float.isInfinite(avgPace)) {
            return Float.parseFloat(String.format("%.1f", avgPace));
        } else {
            return 0;
        }
    }

    public void setAvgPace(float avgPace) {
        this.avgPace = avgPace;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = DateHelper.getDateTimeFromMili(startTime);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Double> getLatList() {
        return latList;
    }

    public void setLatList(Double lat) {
        latList.add(lat);
    }

    public List<Double> getLngList() {
        return lngList;
    }

    public void setLngList(Double lng) {
        lngList.add(lng);
    }

    public void incStiCount() {
        stiCount++;
    }

    public void incResCount() {
        resCount++;
    }

    public void incHitResCount() {
        hitResCount++;
    }

    public void incLapseCount() {
        lapseCount++;
    }

    public void addResTime(long resTime) {
        totalResTime = totalResTime + resTime;
    }

    public void resetData() {

        id = 0l;
        name = "";

        task = "";
        dif = "";
        activity = "";
        duration = "";

        stiCount = 0;
        resCount = 0;
        hitResCount = 0;
        lapseCount = 0;

        accuracy = 0;

        totalResTime = 0;
        avgResTime = 0;

        latList.clear();
        lngList.clear();

        stiMiliList.clear();
        stiTypeList.clear();
        resMiliList.clear();
        resTimeList.clear();

        distance = 0;
        avgSpeed = 0;
        avgPace = 0;

        startTime = "";
        time = 0;
    }
}
