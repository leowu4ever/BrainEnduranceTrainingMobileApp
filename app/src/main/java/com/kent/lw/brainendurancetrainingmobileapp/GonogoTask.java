package com.kent.lw.brainendurancetrainingmobileapp;

public class GonogoTask extends Task {

    // W-AVT
    private final String TASK_Gonogo = "W-AVT";
    private final int W_AVT_DURATION = 60 * 60 * 1000;
    private final int W_AVT_INTERVAL = 2 * 1000;


    public GonogoTask(int duration, int intervalFrom, int intervalTo, float volumeFrom, float volumeTo, float noise, int resThreshold) {
        super(duration, intervalFrom, intervalTo, volumeFrom, volumeTo, noise, resThreshold);
    }
}
