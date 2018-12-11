package com.kent.lw.brainendurancetrainingmobileapp;

public class Task {

    private int intervalFrom;
    private int intervalTo;

    private float volumeFrom;
    private float volumeTo;

    private float noise;
    private int resThreshold;
    private float minSpeed;
    private int nogoProportion;

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
        return Float.parseFloat(String.format("%.1f", volumeTo));
    }

    public void setVolumeTo(float volumeTo) {
        this.volumeTo = volumeTo;
    }

    public float getNoise() {
        return Float.parseFloat(String.format("%.1f", noise));
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

    public int getNogoProportion() {
        return nogoProportion;
    }

    public void setNogoProportion(int nogoProportion) {
        this.nogoProportion = nogoProportion;
    }

    public void reset() {
        this.intervalFrom = 0;
        this.intervalTo = 0;

        this.volumeFrom = 0;
        this.volumeTo = 0;

        this.noise = 0;

        this.resThreshold = 0;

        this.minSpeed = 0;

        this.nogoProportion = 0;
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


    public void setupForCustom(int nogoProp, int interFrom, int interTo, float volFrom, float volTo, float noi, int thresh, float minspd) {
        setNogoProportion(nogoProp);
        setIntervalFrom(interFrom);
        setIntervalTo(interTo);
        setVolumeFrom(volFrom);
        setVolumeTo(volTo);
        setNoise(noi);
        setResThreshold(thresh);
        setMinSpeed(minspd);
    }

    public void setupForApvtEasy() {

        // apvt easy
        final int APVT_EASY_INTERVAL_FROM = 7;
        final int APVT_EASY_INTERVAL_TO = 7;

        final float APVT_EASY_VOLUME_FROM = 1;
        final float APVT_EASY_VOLUME_TO = 1;

        final float APVT_EASY_NOISE = 0;
        final int APVT_EASY_RES_THRESHOLD = 1500;
        final float APVT_EASY_MIN_SPEED = 5;
        final int APVT_EASY_NOGO_PROPORTION = 0;

        setNogoProportion(APVT_EASY_NOGO_PROPORTION);
        setIntervalFrom(APVT_EASY_INTERVAL_FROM);
        setIntervalTo(APVT_EASY_INTERVAL_TO);
        setVolumeFrom(APVT_EASY_VOLUME_FROM);
        setVolumeTo(APVT_EASY_VOLUME_TO);
        setNoise(APVT_EASY_NOISE);
        setResThreshold(APVT_EASY_RES_THRESHOLD);
        setMinSpeed(APVT_EASY_MIN_SPEED);
    }

    public void setupForApvtMedium() {
        // apvt medium
        final int APVT_MEDIUM_INTERVAL_FROM = 5;
        final int APVT_MEDIUM_INTERVAL_TO = 5;

        final float APVT_MEDIUM_VOLUME_FROM = 0.7f;
        final float APVT_MEDIUM_VOLUME_TO = 0.7f;

        final float APVT_MEDIUM_NOISE = 0.5f;
        final int APVT_MEDIUM_RES_THRESHOLD = 1500;
        final float APVT_MEDIUM_MIN_SPEED = 5;
        final int APVT_MEDIUM_NOGO_PROPORTION = 0;

        setNogoProportion(APVT_MEDIUM_NOGO_PROPORTION);
        setIntervalFrom(APVT_MEDIUM_INTERVAL_FROM);
        setIntervalTo(APVT_MEDIUM_INTERVAL_TO);
        setVolumeFrom(APVT_MEDIUM_VOLUME_FROM);
        setVolumeTo(APVT_MEDIUM_VOLUME_TO);
        setNoise(APVT_MEDIUM_NOISE);
        setResThreshold(APVT_MEDIUM_RES_THRESHOLD);
        setMinSpeed(APVT_MEDIUM_MIN_SPEED);
    }

    public void setupForApvtHard() {

        // apvt hard
        final int APVT_HARD_INTERVAL_FROM = 3;
        final int APVT_HARD_INTERVAL_TO = 3;

        final float APVT_HARD_VOLUME_FROM = 0.5f;
        final float APVT_HARD_VOLUME_TO = 0.5f;

        final float APVT_HARD_NOISE = 1;
        final int APVT_HARD_RES_THRESHOLD = 1500;
        final float APVT_HARD_MIN_SPEED = 5;
        final int APVT_HARD_NOGO_PROPORTION = 0;

        setNogoProportion(APVT_HARD_NOGO_PROPORTION);
        setIntervalFrom(APVT_HARD_INTERVAL_FROM);
        setIntervalTo(APVT_HARD_INTERVAL_TO);
        setVolumeFrom(APVT_HARD_VOLUME_FROM);
        setVolumeTo(APVT_HARD_VOLUME_TO);
        setNoise(APVT_HARD_NOISE);
        setResThreshold(APVT_HARD_RES_THRESHOLD);
        setMinSpeed(APVT_HARD_MIN_SPEED);
    }

    public void setupForGonogoEasy() {

        // gonogo easy
        final int GONOGO_EASY_INTERVAL_FROM = 7;
        final int GONOGO_EASY_INTERVAL_TO = 7;

        final float GONOGO_EASY_VOLUME_FROM = 1;
        final float GONOGO_EASY_VOLUME_TO = 1;

        final float GONOGO_EASY_NOISE = 0;
        final int GONOGO_EASY_RES_THRESHOLD = 1500;
        final float GONOGO_EASY_MIN_SPEED = 5;
        final int GONOGO_EASY_NOGO_PROPORTION = 10;

        setNogoProportion(GONOGO_EASY_NOGO_PROPORTION);
        setIntervalFrom(GONOGO_EASY_INTERVAL_FROM);
        setIntervalTo(GONOGO_EASY_INTERVAL_TO);
        setVolumeFrom(GONOGO_EASY_VOLUME_FROM);
        setVolumeTo(GONOGO_EASY_VOLUME_TO);
        setNoise(GONOGO_EASY_NOISE);
        setResThreshold(GONOGO_EASY_RES_THRESHOLD);
        setMinSpeed(GONOGO_EASY_MIN_SPEED);
    }

    public void setupForGonogoMedium() {

        // gonogo medium
        final int GONOGO_MEDIUM_INTERVAL_FROM = 5;
        final int GONOGO_MEDIUM_INTERVAL_TO = 5;

        final float GONOGO_MEDIUM_VOLUME_FROM = 0.7f;
        final float GONOGO_MEDIUM_VOLUME_TO = 0.7f;

        final float GONOGO_MEDIUM_NOISE = 0.5f;
        final int GONOGO_MEDIUM_RES_THRESHOLD = 1500;
        final float GONOGO_MEDIUM_MIN_SPEED = 5;
        final int GONOGO_MEDIUM_NOGO_PROPORTION = 20;

        setNogoProportion(GONOGO_MEDIUM_NOGO_PROPORTION);
        setIntervalFrom(GONOGO_MEDIUM_INTERVAL_FROM);
        setIntervalTo(GONOGO_MEDIUM_INTERVAL_TO);
        setVolumeFrom(GONOGO_MEDIUM_VOLUME_FROM);
        setVolumeTo(GONOGO_MEDIUM_VOLUME_TO);
        setNoise(GONOGO_MEDIUM_NOISE);
        setResThreshold(GONOGO_MEDIUM_RES_THRESHOLD);
        setMinSpeed(GONOGO_MEDIUM_MIN_SPEED);
    }

    public void setupForGonogoHard() {

        // gonogo hard
        final int GONOGO_HARD_INTERVAL_FROM = 3;
        final int GONOGO_HARD_INTERVAL_TO = 3;

        final float GONOGO_HARD_VOLUME_FROM = 0.5f;
        final float GONOGO_HARD_VOLUME_TO = 0.5f;

        final float GONOGO_HARD_NOISE = 1;
        final int GONOGO_HARD_RES_THRESHOLD = 1500;
        final float GONOGO_HARD_MIN_SPEED = 5;
        final int GONOGO_HARD_NOGO_PROPORTION = 30;
        setNogoProportion(GONOGO_HARD_NOGO_PROPORTION);
        setIntervalFrom(GONOGO_HARD_INTERVAL_FROM);
        setIntervalTo(GONOGO_HARD_INTERVAL_TO);
        setVolumeFrom(GONOGO_HARD_VOLUME_FROM);
        setVolumeTo(GONOGO_HARD_VOLUME_TO);
        setNoise(GONOGO_HARD_NOISE);
        setResThreshold(GONOGO_HARD_RES_THRESHOLD);
        setMinSpeed(GONOGO_HARD_MIN_SPEED);
    }
}



