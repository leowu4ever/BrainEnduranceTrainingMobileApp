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
                MainActivity.trainingData.setResMiliList(resMili);
                long lastStiMili = MainActivity.trainingData.getStiMiliList().get(MainActivity.trainingData.getStiMiliList().size() - 1);
                long resTime = resMili - lastStiMili;

                MainActivity.trainingFragment.setTvLogRes("Response time  is " + resTime + "ms");
                MainActivity.trainingData.incResCount();
                MainActivity.trainingData.setResTimeList(resTime);

                // within valid res threshold
                if(resTime <= MainActivity.apvtTask.getResThreshold() && resTime > 100) {

                    MainActivity.resTotalTime = MainActivity.resTotalTime + resTime;

                    // update hit respon count textview
                    MainActivity.trainingData.incHitResCount();
                    MainActivity.trainingFragment.setTvHitCount(MainActivity.trainingData.getHitResCount() + "");

                    // update avg res time
                    MainActivity.trainingData.addResTime(resTime);
                    MainActivity.trainingFragment.setTvAvgResTime(MainActivity.trainingData.getTotalResTime() / MainActivity.trainingData.getHitResCount() + "");

                    // update accuracy
                    MainActivity.trainingFragment.setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");
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
