package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VisualFragment extends Fragment {

    private Button btnVisualRes;
    private ImageButton btnImgBullseye;
    private Button btnFinish;

    private VisualTask visualTask;

    private Handler handler;

    private Runnable showVisualStimulusRunnable, hideVisualStimulusRunnable;

    Random random;

    // uis for performance
    private TextView tvDuration;
    private TextView tvStiCount, tvHitCount, tvLapseCount, tvResCount, tvAccuracy, tvAvgResTime, tvResTime;

    private VisualCommunicator visualCommunicator;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visual, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        visualTask = new VisualTask();

        handler = new Handler();
        random = new Random();

        initRunnables();
        initUIs();

        hideVisualStimulus();

        int waits = visualTask.getRandomInterval();
        Log.d("visualtest", "show after" + waits + "s");
        handler.postDelayed(showVisualStimulusRunnable, waits + 4000);
    }

    private void initUIs() {

        visualCommunicator = (VisualCommunicator) getActivity();

        btnImgBullseye = getActivity().findViewById(R.id.btn_img_bullseye);
        btnVisualRes = getActivity().findViewById(R.id.btn_visual_res);
        btnFinish = getActivity().findViewById(R.id.btn_visual_finish);

        btnVisualRes.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseToVisualStimulus();
            }
        }));

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish visual training
                visualCommunicator.finishVisualTraining();
                handler.removeCallbacks(hideVisualStimulusRunnable);
                handler.removeCallbacks(showVisualStimulusRunnable);

            }
        });

        // performance info uis
        tvDuration = getActivity().findViewById(R.id.tv_visual_duration);
        tvStiCount = getActivity().findViewById(R.id.tv_visual_sti_count);
        tvHitCount = getActivity().findViewById(R.id.tv_visual_hit_count);
        tvLapseCount = getActivity().findViewById(R.id.tv_visual_lapse_count);
        tvResCount = getActivity().findViewById(R.id.tv_visual_res_count);
        tvAccuracy = getActivity().findViewById(R.id.tv_visual_accuracy);
        tvAvgResTime = getActivity().findViewById(R.id.tv_visual_avg_res_time);
        tvResTime = getActivity().findViewById(R.id.tv_visual_res_time);
    }


    private void responseToVisualStimulus() {


        if(btnImgBullseye.getVisibility() == View.VISIBLE) {

            handler.removeCallbacks(hideVisualStimulusRunnable);
            hideVisualStimulus();

            int waits = visualTask.getRandomInterval();
            Log.d("visualtest", "show after" + waits + "s");
            handler.postDelayed(showVisualStimulusRunnable, waits);

            // update accuracy, response time, avg response time

            // update hits, lapses, responses

            // cal res time
            Long resMili = System.currentTimeMillis();

            if (MainActivity.trainingStarted && MainActivity.trainingData.getStiMiliList().size() > 0) {

                long lastStiMili = MainActivity.trainingData.getStiMiliList().get(MainActivity.trainingData.getStiMiliList().size() - 1);
                long resTime = resMili - lastStiMili;

                MainActivity.trainingData.setResMiliList(resMili);
                setTvResTime(resTime + "");

                MainActivity.trainingData.incResCount();
                setTvResCount(MainActivity.trainingData.getResCount() + "");

                MainActivity.trainingData.setResTimeList(resTime);

                if (resTime <= visualTask.getValidResThreshold() && resTime > visualTask.getFalseStartThreshold()) {

                    MainActivity.trainingData.incHitResCount();
                    setTvHitCount(MainActivity.trainingData.getHitResCount() + "");

                    // update avg res time
                    MainActivity.trainingData.addResTime(resTime);
                    setTvAvgResTime(MainActivity.trainingData.getAvgResTime() + "");

                    // update accuracy
                    setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");

                } else {
                    MainActivity.trainingData.incLapseCount();
                    setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");
                }
            }

            setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");
        } else {
            MainActivity.trainingData.incLapseCount();
            setTvLapseCount(MainActivity.trainingData.getLapseCount() + "");
            MainActivity.trainingData.incResCount();
            setTvResCount(MainActivity.trainingData.getResCount() + "");
        }
    }

    private void initRunnables() {

        showVisualStimulusRunnable = new Runnable() {
            @Override
            public void run() {
                showVisualStimulus();
                Log.d("visualtest", "disappear in 30s");
                handler.postDelayed(hideVisualStimulusRunnable, visualTask.getStimulusDisappearTime());
            }
        };

        hideVisualStimulusRunnable = new Runnable() {
            @Override
            public void run() {
                hideVisualStimulus();
                int waits = visualTask.getRandomInterval();
                Log.d("visualtest", "show after" + waits + "s");
                handler.postDelayed(showVisualStimulusRunnable, waits);
            }
        };
    }

    private void showVisualStimulus() {

        if (MainActivity.trainingStarted) {
            Log.d("visualtest", "showing");
            btnImgBullseye.setVisibility(View.VISIBLE);

            MainActivity.trainingData.incStiCount();

            // update sti mili

            MainActivity.trainingData.setStiMiliList(System.currentTimeMillis());

            // update stimulus count
            setTvStiCount(MainActivity.trainingData.getStiCount() + "");

            setTvAccuracy(MainActivity.trainingData.getAccuracy() + "");
        }
    }

    private void hideVisualStimulus() {
        Log.d("visualtest", "hiding");
        btnImgBullseye.setVisibility(View.INVISIBLE);
    }


    public void setTvDuration(String s) {
        tvDuration.setText(s);
    }

    public void setTvStiCount(String s) {
        tvStiCount.setText(s);
    }

    public void setTvHitCount(String s) {
        tvHitCount.setText(s);
    }

    public void setTvAccuracy(String s) {
        tvAccuracy.setText(s);
    }

    public void setTvAvgResTime(String s) {
        tvAvgResTime.setText(s);
    }

    public void setTvLapseCount(String s) {
        tvLapseCount.setText(s);
    }

    public void setTvResCount(String s) {
        tvResCount.setText(s);
    }

    public void setTvResTime(String s) {tvResTime.setText(s);}
}