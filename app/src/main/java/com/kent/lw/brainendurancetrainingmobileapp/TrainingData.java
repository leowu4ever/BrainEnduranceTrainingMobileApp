package com.kent.lw.brainendurancetrainingmobileapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TrainingData {

    public String name, task, dif;

    public Long id;

    public List<Double> accXList;
    public List<Double> accYList;
    public List<Double> accZList;

    public List<Double> locLatList;
    public List<Double> locLngList;

    public List<Long> stiTimeList;
    public List<Long> resTimeList;

    public TrainingData() {
        accXList = new ArrayList<Double>();
        accYList = new ArrayList<Double>();
        accZList = new ArrayList<Double>();

        locLatList = new ArrayList<Double>();
        locLngList = new ArrayList<Double>();

        stiTimeList = new ArrayList<Long>();
        resTimeList = new ArrayList<Long>();
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
    public void setAccXList(Double accX) {
        accXList.add(accX);
    }

    public void setAccYList(Double accY) {
        accYList.add(accY);
    }

    public void setAccZList(Double accZ) {
        accZList.add(accZ);
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

    public void resetAllData() {
        name = "";
        task = "";
        dif = "";
        id = 0l;
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
}
