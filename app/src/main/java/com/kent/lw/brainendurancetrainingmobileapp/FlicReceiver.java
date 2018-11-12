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

        if (isDown) {
            if (MainActivity.trainingStarted) {
//                Log.d("STIMULUS_MILI_BUTTON", System.currentTimeMillis() + "");
//                Log.d("STIMULUS_MILI_BUTTON_NA", System.nanoTime() + "");
                Long resMili = System.currentTimeMillis();
                MainActivity.trainingData.setResTimeList(resMili);
                long lastSti = MainActivity.trainingData.getStiTimeList().get(MainActivity.trainingData.getStiTimeList().size() - 1);
                long resTime = resMili - lastSti;

                if(resTime <= MainActivity.apvtTask.getResThreshold()) {

                    MainActivity.resCorrectCount++;
                    MainActivity.resTotalTime = MainActivity.resTotalTime + resTime;

                    // update hit respon count textview
                    MainActivity.trainingData.incHitResCount();
                    MainActivity.trainingFragment.setTvHitCount(MainActivity.trainingData.getHitResCount() + "");


                    // update accuracy
                    MainActivity.trainingFragment.setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");

                }

                Toast.makeText(context, "resTime is " + resTime + "correct count is " + MainActivity.resCorrectCount, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }
}
