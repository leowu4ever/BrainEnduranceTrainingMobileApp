package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.List;

public class OverallData {

    public float overallRT;
    public float overallAccuracy;
    public List<Float> rtList;
    public List<Float> accuracyList;

    public List<Long> miliList;

    public float getOverallRT() {
        return overallRT;
    }

    public void setOverallRT(float overallRT) {
        this.overallRT = overallRT;
    }

    public float getOverallAccuracy() {
        return overallAccuracy;
    }

    public void setOverallAccuracy(float overallAccuracy) {
        this.overallAccuracy = overallAccuracy;
    }

    public List<Float> getRtList() {
        return rtList;
    }

    public void setRtList(List<Float> rtList) {
        this.rtList = rtList;
    }

    public List<Float> getAccuracyList() {
        return accuracyList;
    }

    public void setAccuracyList(List<Float> accuracyList) {
        this.accuracyList = accuracyList;
    }

    public List<Long> getMiliList() {
        return miliList;
    }

    public void setMiliList(List<Long> miliList) {
        this.miliList = miliList;
    }
}
