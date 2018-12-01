package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private int duration;

    private int intervalFrom;
    private int intervalTo;

    private float volumeFrom;
    private float volumeTo;

    private float noise;

    private int resThreshold;

    private float minSpeed;

    private List<Integer> stiTypeList;

    public Task() {

        this.duration = 0;
        this.intervalFrom = 0;
        this.intervalTo = 0;
        this.volumeFrom = 0;
        this.volumeTo = 0;
        this.noise = 0;
        this.resThreshold = 0;
        minSpeed = 0;
        stiTypeList = new ArrayList<Integer>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIntervalFrom() {
        return intervalFrom;
    }

    public void setIntervalFrom(int intervalFrom) {
        this.intervalFrom = intervalFrom;
    }

    public int getIntervalTo() {
        return intervalTo;
    }

    public void setIntervalTo(int intervalTo) {
        this.intervalTo = intervalTo;
    }

    public float getVolumeFrom() {
        return volumeFrom;
    }

    public void setVolumeFrom(float volumeFrom) {
        this.volumeFrom = volumeFrom;
    }

    public float getVolumeTo() {
        return volumeTo;
    }

    public void setVolumeTo(float volumeTo) {
        this.volumeTo = volumeTo;
    }

    public float getNoise() {
        return noise;
    }

    public void setNoise(float noise) {
        this.noise = noise;
    }

    public int getResThreshold() {
        return resThreshold;
    }

    public void setResThreshold(int resThreshold) {
        this.resThreshold = resThreshold;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
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

    @Override
    public String toString() {
        return "Task{" +
                "duration=" + duration +
                ", intervalFrom=" + intervalFrom +
                ", intervalTo=" + intervalTo +
                ", volumeFrom=" + volumeFrom +
                ", volumeTo=" + volumeTo +
                ", noise=" + noise +
                ", resThreshold=" + resThreshold +
                '}';
    }
}



