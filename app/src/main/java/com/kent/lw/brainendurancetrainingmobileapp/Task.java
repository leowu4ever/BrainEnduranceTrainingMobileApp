package com.kent.lw.brainendurancetrainingmobileapp;

public class Task {

    private int duration;

    private int intervalFrom;
    private int intervalTo;

    private int volumeFrom;
    private int volumeTo;

    private int noise;

    private int resThreshold;

    public Task(int duration, int intervalFrom, int intervalTo, int volumeFrom, int volumeTo, int noise, int resThreshold) {
        this.duration = duration;
        this.intervalFrom = intervalFrom;
        this.intervalTo = intervalTo;
        this.volumeFrom = volumeFrom;
        this.volumeTo = volumeTo;
        this.noise = noise;
        this.resThreshold = resThreshold;
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

    public int getVolumeFrom() {
        return volumeFrom;
    }

    public void setVolumeFrom(int volumeFrom) {
        this.volumeFrom = volumeFrom;
    }

    public int getVolumeTo() {
        return volumeTo;
    }

    public void setVolumeTo(int volumeTo) {
        this.volumeTo = volumeTo;
    }

    public int getNoise() {
        return noise;
    }

    public void setNoise(int noise) {
        this.noise = noise;
    }

    public int getResThreshold() {
        return resThreshold;
    }

    public void setResThreshold(int resThreshold) {
        this.resThreshold = resThreshold;
    }
}



