package com.kent.lw.brainendurancetrainingmobileapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TrainingData {

    public String name, task, dif;

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

    public void updateName(String name) {
        this.name = name;
    }

    public void updateTask(String task) {

        this.task = task;
    }

    public void updateDif(String dif) {

        this.dif = dif;
    }

    public void updateAccXList(Double accX) {
        accXList.add(accX);
    }

    public void updateAccYList(Double accY) {
        accYList.add(accY);

    }

    public void updateAccZList(Double accZ) {
        accZList.add(accZ);
    }

    public void updateLocLatList(Double lat) {

        locLatList.add(lat);
    }

    public void updateLocLngList(Double lng) {

        locLngList.add(lng);
    }

    public void updateStiTimeList(Long stiTime) {

        stiTimeList.add(stiTime);
    }

    public void updateResTimeList(Long resTime) {

        resTimeList.add(resTime);
    }



    public void resetAllData() {
        name = "";
        task = "";
        dif = "";
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

    public void upload() {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.uploadAllData(name, task, dif, accXList, accYList, accZList, locLatList, locLngList, stiTimeList, resTimeList);
    }
}
