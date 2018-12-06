package com.kent.lw.brainendurancetrainingmobileapp;

public class Task {

    private int intervalFrom;
    private int intervalTo;

    private float volumeFrom;
    private float volumeTo;

    private float noise;

    private int resThreshold;

    private float minSpeed;

    private float nogoPropotion;


    public Task() {
        reset();

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

    public float getNogoPropotion() {
        return nogoPropotion;
    }

    public void setNogoPropotion(float nogoPropotion) {
        this.nogoPropotion = nogoPropotion;
    }

    public void reset() {
        this.intervalFrom = 0;
        this.intervalTo = 0;

        this.volumeFrom = 0;
        this.volumeTo = 0;

        this.noise = 0;

        this.resThreshold = 0;

        this.minSpeed = 0;

        this.nogoPropotion = 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", intervalFrom=" + intervalFrom +
                ", intervalTo=" + intervalTo +
                ", volumeFrom=" + volumeFrom +
                ", volumeTo=" + volumeTo +
                ", noise=" + noise +
                ", resThreshold=" + resThreshold +
                '}';
    }
}



