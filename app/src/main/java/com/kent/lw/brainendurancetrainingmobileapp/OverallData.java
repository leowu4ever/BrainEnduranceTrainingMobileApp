package com.kent.lw.brainendurancetrainingmobileapp;

import java.util.ArrayList;
import java.util.List;

public class OverallData {
    public List<Long> rtList;
    public List<Float> accuracyList;
    public List<Long> miliList;

    public OverallData() {
        this.rtList = new ArrayList<Long>();
        this.accuracyList = new ArrayList<Float>();
        this.miliList = new ArrayList<Long>();
    }

    public long getOverallRT() {
        if (rtList.size() != 0) {
            return getTotalRT() / getValidTrainingCount();
        } else {
            return 0;
        }
    }

    public float getOverallAccuracy() {
        if (accuracyList.size() != 0) {
            float a = getTotalAccuracy() / getValidTrainingCount();
            a = Float.parseFloat(String.format("%.1f", a));
            return a;
        } else {
            return 0;
        }
    }

    public List<Long> getRtList() {
        return rtList;
    }

    public void setRtList(long rt) {
        this.rtList.add(rt);
    }

    public List<Float> getAccuracyList() {
        return accuracyList;
    }

    public void setAccuracyList(float accuracy) {
        this.accuracyList.add(accuracy);
    }

    public List<Long> getMiliList() {
        return miliList;
    }

    public void setMiliList(long mili) {
        this.miliList.add(mili);
    }

    public long getTotalRT() {
        long total = 0;

        for (long rt : rtList) {
            total = total + rt;
        }
        return total;
    }

    public float getTotalAccuracy() {
        float total = 0f;

        for (float accuracy : accuracyList) {
            total = total + accuracy;
        }
        return total;
    }

    public int getValidTrainingCount() {
        int c = 0;
        for (long rt : rtList) {
            if(rt != 0) {
                c++;
            }
        }
        return c;
    }
}
