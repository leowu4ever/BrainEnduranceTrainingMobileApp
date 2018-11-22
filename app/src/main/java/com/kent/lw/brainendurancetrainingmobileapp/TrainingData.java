package com.kent.lw.brainendurancetrainingmobileapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TrainingData {

    // user info
    public Long id;
    public String name;

    // for activity
    public String activity;

    public String duration;

    // task
    public String task, dif;

    // cognitive task
    public int stiCount, resCount, hitResCount;
    public long totalResTime;

    public long avgResTime;
    public float accuracy;

    public List<Long> stiMiliList, resMiliList, resTimeList;

    // physical
    public float distance, avgSpeed, avgPace;
    public String startTime;
    public long time;

    public List<Double> accXList, accYList, accZList;

    public List<Double> gyroXList, gyroYList, gyroZList;

    public List<Double> locLatList, locLngList;

    public Task taskConfig;

    public TrainingData() {
        accXList = new ArrayList<Double>();
        accYList = new ArrayList<Double>();
        accZList = new ArrayList<Double>();

        gyroXList = new ArrayList<Double>();
        gyroYList = new ArrayList<Double>();
        gyroZList = new ArrayList<Double>();

        locLatList = new ArrayList<Double>();
        locLngList = new ArrayList<Double>();

        stiMiliList = new ArrayList<Long>();
        resMiliList = new ArrayList<Long>();
        resTimeList = new ArrayList<Long>();
    }

    public Task getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(Task taskConfig) {
        this.taskConfig = taskConfig;
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

    public float getAccuracy() {
        float a = (float) hitResCount / (float) stiCount * 100;
        a = Float.parseFloat(String.format("%.3f", a));
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Double> getAccXList() {
        return accXList;
    }

    public void setAccXList(Double accX) {
        accXList.add(accX);
    }

    public List<Double> getAccYList() {
        return accYList;
    }

    public void setAccYList(Double accY) {
        accYList.add(accY);
    }

    public List<Double> getAccZList() {
        return accZList;
    }

    public void setAccZList(Double accZ) {
        accZList.add(accZ);
    }

    public List<Double> getGyroXList() {
        return gyroXList;
    }

    public void setGyroXList(Double gyroX) {
        gyroXList.add(gyroX);
    }

    public List<Double> getGyroYList() {
        return gyroYList;
    }

    public void setGyroYList(Double gyroY) {
        gyroYList.add(gyroY);
    }

    public List<Double> getGyroZList() {
        return gyroZList;
    }

    public void setGyroZList(Double gyroZ) {
        gyroZList.add(gyroZ);
    }

    public List<Double> getLocLatList() {
        return locLatList;
    }

    public void setLocLatList(Double lat) {
        locLatList.add(lat);
    }

    public List<Double> getLocLngList() {
        return locLngList;
    }

    public void setLocLngList(Double lng) {
        locLngList.add(lng);
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

    public List<Long> getResTimeList() {
        return resTimeList;
    }

    public void setResTimeList(Long resTime) {
        resTimeList.add(resTime);
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

    public void addResTime(long resTime) {
        totalResTime = totalResTime + resTime;
    }

    public void resetAllData() {
        name = "";
        task = "";
        dif = "";
        id = 0l;

        stiCount = 0;
        resCount = 0;
        hitResCount = 0;
        accuracy = 0;
        totalResTime = 0;

        distance = 0;
        avgSpeed = 0;
        avgPace = 0;
        startTime = "";
        time = 0;

        accXList.clear();
        accYList.clear();
        accZList.clear();

        locLatList.clear();
        locLngList.clear();

        stiMiliList.clear();
        resMiliList.clear();
    }

    public void printAllData() {
        Log.d("TRAINING_DATA", "[Name]: " + name);
        Log.d("TRAINING_DATA", "[Task]: " + task);
        Log.d("TRAINING_DATA", "[Dif]: " + dif);
        Log.d("TRAINING_DATA", "[Id]: " + id.toString());

        String temp = "";
        for (Double accX : accXList) {
            temp = temp + accX.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[AccX] " + temp);

        temp = "";
        for (Double accY : accYList) {
            temp = temp + accY.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[AccY] " + temp);

        temp = "";
        for (Double accZ : accZList) {
            temp = temp + accZ.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[AccZ] " + temp);


        temp = "";
        for (Double lat : locLatList) {
            temp = temp + lat.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[Lat] " + temp);

        temp = "";
        for (Double lng : locLngList) {
            temp = temp + lng.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[Lng] " + temp);

        temp = "";
        for (Long sti : stiMiliList) {
            temp = temp + sti.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[Sti] " + temp);

        temp = "";
        for (Long res : resMiliList) {
            temp = temp + res.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[Res] " + temp);

    }

    @Override
    public String toString() {
        return "TrainingData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", activity='" + activity + '\'' +
                ", duration='" + duration + '\'' +
                ", task='" + task + '\'' +
                ", dif='" + dif + '\'' +
                ", stiCount=" + stiCount +
                ", resCount=" + resCount +
                ", hitResCount=" + hitResCount +
                ", totalResTime=" + totalResTime +
                ", accuracy=" + accuracy +
                ", stiMiliList=" + stiMiliList +
                ", resMiliList=" + resMiliList +
                ", distance=" + distance +
                ", avgSpeed=" + avgSpeed +
                ", avgPace=" + avgPace +
                ", startTime=" + startTime +
                ", time=" + time +
                ", accXList=" + accXList +
                ", accYList=" + accYList +
                ", accZList=" + accZList +
                ", gyroXList=" + gyroXList +
                ", gyroYList=" + gyroYList +
                ", gyroZList=" + gyroZList +
                ", locLatList=" + locLatList +
                ", locLngList=" + locLngList +
                '}';
    }
}
