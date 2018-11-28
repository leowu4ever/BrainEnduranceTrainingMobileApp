package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

public class FlicReceiver extends FlicBroadcastReceiver {


    @Override
    protected void onRequestAppCredentials(Context context) {
        FlicConfig.setFlicCredentials();
    }

    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
        Long resMili = System.currentTimeMillis();
        if (isDown) {
            if (MainActivity.trainingStarted) {
                long lastStiMili = MainActivity.trainingData.getStiMiliList().get(MainActivity.trainingData.getStiMiliList().size() - 1);
                long resTime = resMili - lastStiMili;

                MainActivity.trainingData.setResMiliList(resMili);
                MainActivity.trainingData.incResCount();
                MainActivity.trainingData.setResTimeList(resTime);
                MainActivity.trainingFragment.setTvLogRes("Response time  is " + resTime + "ms");


                if (MainActivity.trainingData.task.equals("A-PVT")) {
                    Log.d("tasktest", MainActivity.apvtTask.getStiTypeOn(MainActivity.trainingData.getStiCount()) + "");
                    if (resTime <= MainActivity.apvtTask.getResThreshold() && resTime > 100 && MainActivity.apvtTask.getStiTypeOn(MainActivity.trainingData.getStiCount() - 1) == 0) {

                        // update hit respon count textview
                        MainActivity.trainingData.incHitResCount();
                        MainActivity.trainingFragment.setTvHitCount(MainActivity.trainingData.getHitResCount() + "");

                        // update avg res time
                        MainActivity.trainingData.addResTime(resTime);
                        MainActivity.trainingFragment.setTvAvgResTime(MainActivity.trainingData.getTotalResTime() / MainActivity.trainingData.getHitResCount() + "");

                        // update accuracy
                        MainActivity.trainingFragment.setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");
                    } else {
                        MainActivity.trainingData.incLapseCount();
                        MainActivity.trainingFragment.setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");
                    }
                }

                if (MainActivity.trainingData.task.equals("GO/NO-GO")) {

                    if (resTime <= MainActivity.gonogoTask.getResThreshold() && resTime > 100 && MainActivity.gonogoTask.getStiTypeOn(MainActivity.trainingData.getStiCount() - 1) == 0) {

                        // update hit respon count textview
                        MainActivity.trainingData.incHitResCount();
                        MainActivity.trainingFragment.setTvHitCount(MainActivity.trainingData.getHitResCount() + "");

                        // update avg res time
                        MainActivity.trainingData.addResTime(resTime);
                        MainActivity.trainingFragment.setTvAvgResTime(MainActivity.trainingData.getTotalResTime() / MainActivity.trainingData.getHitResCount() + "");

                        // update accuracy
                        MainActivity.trainingFragment.setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");
                    } else {
                        MainActivity.trainingData.incLapseCount();
                        MainActivity.trainingFragment.setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");
                    }
                }
            }
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }
}
