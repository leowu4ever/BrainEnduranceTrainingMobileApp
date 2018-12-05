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
            if (MainActivity.trainingStarted && MainActivity.trainingData.getStiMiliList().size() > 0) {
                long lastStiMili = MainActivity.trainingData.getStiMiliList().get(MainActivity.trainingData.getStiMiliList().size() - 1);
                long resTime = resMili - lastStiMili;

                MainActivity.trainingData.setResMiliList(resMili);
                MainActivity.trainingData.incResCount();
                MainActivity.trainingFragment.setTvResCount(MainActivity.trainingData.getResCount() + "");
                MainActivity.trainingData.setResTimeList(resTime);
                MainActivity.trainingFragment.setTvLogRes("Response time  is " + resTime + "ms");

                if (resTime <= MainActivity.task.getResThreshold() && resTime > 100 && MainActivity.trainingData.getStiTypeOn(MainActivity.trainingData.getStiCount() + MainActivity.trainingData.getNogoCount() - 1) == 0) {

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
            } else if (MainActivity.trainingStarted && MainActivity.trainingData.getStiMiliList().size() == 0){
                MainActivity.trainingData.incLapseCount();
                MainActivity.trainingFragment.setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");
            }
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }
}
