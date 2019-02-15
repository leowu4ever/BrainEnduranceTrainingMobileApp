package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

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
                // TODO 37ms, button travel 100, rubber 100ms
                long resTime = resMili - lastStiMili;

                MainActivity.trainingData.setResMiliList(resMili);
                MainActivity.trainingData.incResCount();
                MainActivity.trainingFragment.setTvResCount(MainActivity.trainingData.getResCount() + "");
                MainActivity.trainingData.setResTimeList(resTime);
                MainActivity.trainingFragment.setTvLogRes("Response time is " + resTime + "ms");

                // valid res
                if (resTime <= MainActivity.task.getResThreshold() && resTime > 100 && MainActivity.trainingData.getStiTypeOn(MainActivity.trainingData.getStiCount() + MainActivity.trainingData.getNogoCount() - 1) == 0) {

                    // update hit respon count textview
                    MainActivity.trainingData.incHitResCount();
                    MainActivity.trainingFragment.setTvHitCount(MainActivity.trainingData.getHitResCount() + "");

                    // update avg res time
                    MainActivity.trainingData.addResTime(resTime);
                    // can get res time directly
                    MainActivity.trainingFragment.setTvAvgResTime(MainActivity.trainingData.getTotalResTime() / MainActivity.trainingData.getHitResCount() + "");

                    // update accuracy
                    MainActivity.trainingFragment.setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");

                    if (MainActivity.trainingData.getDif().equals("Adaptive")) {

                        // for adaptive
                        MainActivity.hitStreak++;
                        switch (MainActivity.hitStreak) {
                            case 1 * MainActivity.ADAPTIVE_HIT_STREAK_LIMIT:
                                // upgrade apvt ot gonogo
                                Log.d("apdative", "upgrade to med" + " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);
                                if (MainActivity.trainingData.getTask().equals("A-PVT")) {
                                    MainActivity.task.setupForApvtMedium();
                                } else if (MainActivity.trainingData.getTask().equals("GO/NO-GO")) {
                                    MainActivity.task.setupForGonogoMedium();
                                    MainActivity.createStiTypeList();
                                }
                                MainActivity.lapseStreak = 0;
                                // gonogo should randomise sti tpye list
                                // clear lapseCount

                                MainActivity.soundHelper.stopNoiseSound();
                                MainActivity.soundHelper.playNoiseSound(MainActivity.task.getNoise(), MainActivity.task.getNoise(), 0, -1, 1);

                                break;
                            case 2 * MainActivity.ADAPTIVE_HIT_STREAK_LIMIT:
                                // upgrade apvt ot gonogo

                                Log.d("apdative", "upgrade to hard" + " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);
                                if (MainActivity.trainingData.getTask().equals("A-PVT")) {
                                    MainActivity.task.setupForApvtHard();
                                } else if (MainActivity.trainingData.getTask().equals("GO/NO-GO")) {
                                    MainActivity.task.setupForGonogoHard();
                                    MainActivity.createStiTypeList();
                                }
                                MainActivity.lapseStreak = 0;
                                // gonogo should randomise sti tpye list
                                // clear lapseCount
                                MainActivity.soundHelper.stopNoiseSound();
                                MainActivity.soundHelper.playNoiseSound(MainActivity.task.getNoise(), MainActivity.task.getNoise(), 0, -1, 1);
                                break;
                        }

                    }
                    Log.d("apdative", " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);


                } else { // lapse
                    MainActivity.trainingData.incLapseCount();
                    MainActivity.trainingFragment.setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");

                    if (MainActivity.trainingData.getDif().equals("Adaptive")) {

                        // for adaptive
                        MainActivity.lapseStreak++;
                        if (MainActivity.lapseStreak == MainActivity.APDATIVE_LAPSE_STREAK_LIMIT) {
                            MainActivity.lapseStreak = 0;
                            MainActivity.hitStreak -= MainActivity.ADAPTIVE_HIT_STREAK_LIMIT;

                            if (MainActivity.hitStreak < 0) {
                                MainActivity.hitStreak = 0;
                            }

                            // do downgrade here

                            // for adaptive
                            int lv = MainActivity.hitStreak / MainActivity.ADAPTIVE_HIT_STREAK_LIMIT;
                            Log.d("apdative", "lv: " + lv);

                            switch (lv) {
                                case 0:
                                    // upgrade apvt ot gonogo
                                    Log.d("apdative", "down to easy" + " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);
                                    if (MainActivity.trainingData.getTask().equals("A-PVT")) {
                                        MainActivity.task.setupForApvtEasy();
                                    } else if (MainActivity.trainingData.getTask().equals("GO/NO-GO")) {
                                        MainActivity.task.setupForGonogoEasy();
                                        MainActivity.createStiTypeList();
                                    }
                                    MainActivity.lapseStreak = 0;
                                    // gonogo should randomise sti tpye list
                                    // clear lapseCount
                                    MainActivity.soundHelper.stopNoiseSound();
                                    MainActivity.soundHelper.playNoiseSound(MainActivity.task.getNoise(), MainActivity.task.getNoise(), 0, -1, 1);
                                    break;

                                case 1:
                                    // upgrade apvt ot gonogo
                                    Log.d("apdative", "down to med" + " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);
                                    if (MainActivity.trainingData.getTask().equals("A-PVT")) {
                                        MainActivity.task.setupForApvtMedium();
                                    } else if (MainActivity.trainingData.getTask().equals("GO/NO-GO")) {
                                        MainActivity.task.setupForGonogoMedium();
                                        MainActivity.createStiTypeList();
                                    }
                                    MainActivity.lapseStreak = 0;
                                    // gonogo should randomise sti tpye list
                                    // clear lapseCount
                                    MainActivity.soundHelper.stopNoiseSound();
                                    MainActivity.soundHelper.playNoiseSound(MainActivity.task.getNoise(), MainActivity.task.getNoise(), 0, -1, 1);
                                    break;
                                case 2:
                                    // upgrade apvt ot gonogo
                                    Log.d("apdative", "down to hard" + " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);
                                    if (MainActivity.trainingData.getTask().equals("A-PVT")) {
                                        MainActivity.task.setupForApvtHard();
                                    } else if (MainActivity.trainingData.getTask().equals("GO/NO-GO")) {
                                        MainActivity.task.setupForGonogoHard();
                                        MainActivity.createStiTypeList();
                                    }
                                    MainActivity.lapseStreak = 0;
                                    // gonogo should randomise sti tpye list
                                    // clear lapseCount
                                    MainActivity.soundHelper.stopNoiseSound();
                                    MainActivity.soundHelper.playNoiseSound(MainActivity.task.getNoise(), MainActivity.task.getNoise(), 0, -1, 1);
                                    break;
                            }
                        }
                        Log.d("apdative", " hitstreak: " + MainActivity.hitStreak + " lapseStreak: " + MainActivity.lapseStreak);

                    }
                }

                // avoid res before starting
            } else if (MainActivity.trainingStarted && MainActivity.trainingData.getStiMiliList().size() == 0) {

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
