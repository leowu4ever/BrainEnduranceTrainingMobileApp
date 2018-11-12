package com.kent.lw.brainendurancetrainingmobileapp;

public class Task {

    private int duration;
    private int interval;
    private int volume;
    private int noise;

    public Task(int duration, int interval, int volume, int noise, int resThreshold) {
        this.duration = duration;
        this.interval = interval;
        this.volume = volume;
        this.noise = noise;
        this.resThreshold = resThreshold;
    }

    private int resThreshold;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
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



