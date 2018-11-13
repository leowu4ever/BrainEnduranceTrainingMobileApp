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
    public float accuracy;

    public List<Long> stiTimeList, resTimeList;

    // physical
    public float distance, avgSpeed, avgPace, startTime, time;

    public List<Double> accXList, accYList, accZList;

    public List<Double> gyroXList, gyroYList, gyroZList;

    public List<Double> locLatList, locLngList;

    public TrainingData() {
        accXList = new ArrayList<Double>();
        accYList = new ArrayList<Double>();
        accZList = new ArrayList<Double>();

        gyroXList = new ArrayList<Double>();
        gyroYList = new ArrayList<Double>();
        gyroZList = new ArrayList<Double>();

        locLatList = new ArrayList<Double>();
        locLngList = new ArrayList<Double>();

        stiTimeList = new ArrayList<Long>();
        resTimeList = new ArrayList<Long>();
    }

    public String getActivity() {
        return activity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDif(String dif) {
        this.dif = dif;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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

        float a = (float) hitResCount/ (float) stiCount * 100;
        return a;
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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(float avgPace) {
        this.avgPace = avgPace;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setAccXList(Double accX) {
        accXList.add(accX);
    }

    public void setAccYList(Double accY) {
        accYList.add(accY);
    }

    public void setAccZList(Double accZ) {
        accZList.add(accZ);
    }


    public void setGyroXList(Double gyroX) {
        gyroXList.add(gyroX);
    }

    public void setGyroYList(Double gyroY) {
        gyroYList.add(gyroY);
    }

    public void setGyroZList(Double gyroZ) {
        gyroZList.add(gyroZ);
    }

    public void setLocLatList(Double lat) {
        locLatList.add(lat);
    }

    public void setLocLngList(Double lng) {
        locLngList.add(lng);
    }

    public void setStiTimeList(Long stiTime) {
        stiTimeList.add(stiTime);
    }

    public void setResTimeList(Long resTime) {
        resTimeList.add(resTime);
    }

    public String getName() {
        return this.name;
    }

    public String getTask() {
        return this.task;
    }

    public String getDif() {
        return this.dif;
    }

    public Long getId() {
        return this.id;
    }

    public List<Double> getAccXList() {
        return accXList;
    }

    public List<Double> getAccYList() {
        return accYList;
    }

    public List<Double> getAccZList() {
        return accZList;
    }

    public List<Double> getGyroXList() {
        return gyroXList;
    }

    public List<Double> getGyroYList() {
        return gyroYList;
    }

    public List<Double> getGyroZList() {
        return gyroZList;
    }

    public List<Double> getLocLatList() {
        return locLatList;
    }

    public List<Double> getLocLngList() {
        return locLngList;
    }

    public List<Long> getResTimeList() {
        return resTimeList;
    }

    public List<Long> getStiTimeList() {
        return stiTimeList;
    }

    public void incStiCount() {
        stiCount++;
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
        startTime = 0;
        time = 0;

        accXList.clear();
        accYList.clear();
        accZList.clear();

        locLatList.clear();
        locLngList.clear();

        stiTimeList.clear();
        resTimeList.clear();
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
        for (Long sti : stiTimeList) {
            temp = temp + sti.toString() + "|";
        }
        Log.d("TRAINING_DATA", "[Sti] " + temp);

        temp = "";
        for (Long res : resTimeList) {
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
                ", stiTimeList=" + stiTimeList +
                ", resTimeList=" + resTimeList +
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
