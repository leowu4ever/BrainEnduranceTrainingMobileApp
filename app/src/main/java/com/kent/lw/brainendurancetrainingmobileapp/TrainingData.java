package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;
import java.util.List;

public class TrainingData {

    private List<Double> accXList;
    private List<Double> accYList;
    private List<Double> accZList;

    private List<Double> locLatList;
    private List<Double> locLngList;

    private List<Long> stiTimeList;
    private List<Long> resTimeList;

    private String name, task, dif;


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

    }

    public void updateAccYList(Double accX) {

    }

    public void updateLocLatList(Double lat) {

    }

    public void updateLocLngList(Double lng) {

    }

    public void updateStiTimeList(Long stiTime) {

    }

    public void updateResTimeList(Long resTime) {

    }

    public void updateName(String name) {

    }

    public void updateTask(String name) {

    }

    public void updateDif(String dif) {

    }

}
