package com.kent.lw.brainendurancetrainingmobileapp;

public class ApvtTask extends Task {

    // A-PVT
    private final String TASK_A_PVT = "A-PVT";
    private final int A_PVT_DURATION = 10 * 60 * 1000;
    private final int A_PVT_INTERVAL_EASY = 4 * 1000;
    private final int A_PVT_INTERVAL_MEDIUM = 8 * 1000;
    private final int A_PVT_INTERVAL_HARD = 11 * 1000;


    public ApvtTask(int duration, int intervalFrom, int intervalTo, float volumeFrom, float volumeTo, float noise, int resThreshold) {
        super(duration, intervalFrom, intervalTo, volumeFrom, volumeTo, noise, resThreshold);
    }
}