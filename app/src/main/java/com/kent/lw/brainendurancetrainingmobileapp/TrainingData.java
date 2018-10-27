package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;
import java.util.List;

public class TrainingData {

    private String name, task, dif;

    private List<Double> accXList;
    private List<Double> accYList;
    private List<Double> accZList;

    private List<Double> locLatList;
    private List<Double> locLngList;

    private List<Long> stiTimeList;
    private List<Long> resTimeList;



    public TrainingData() {
        accXList = new ArrayList<Double>();
        accYList = new ArrayList<Double>();
        accZList = new ArrayList<Double>();

        locLatList = new ArrayList<Double>();
        locLngList = new ArrayList<Double>();

        stiTimeList = new ArrayList<Long>();
        resTimeList = new ArrayList<Long>();
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

    public void updateName(String name) {
        this.name = name;
    }

    public void updateTask(String task) {
        this.task = task;
    }

    public void updateDif(String dif) {
        this.dif = dif;
    }

    public void resetAllData() {
        this.name = "";
        this.task = "";
        this.dif = "";
        accXList.clear();
        accYList.clear();
        accZList.clear();

        locLatList.clear();
        locLngList.clear();

        stiTimeList.clear();
        resTimeList.clear();
    }

    public void upload() {
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.uploadAllData(name, task, dif, accXList, accYList, accZList, locLatList, locLngList, stiTimeList, resTimeList);

    }
}
