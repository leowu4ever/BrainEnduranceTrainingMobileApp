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
    private TextView tvStiCount, tvHitCount, tvLapseCount, tvResCount, tvAccuracy, tvAvgResTime;



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
        handler.postDelayed(showVisualStimulusRunnable, waits);
    }

    private void initUIs() {
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
    }


    private void responseToVisualStimulus() {
        handler.removeCallbacks(hideVisualStimulusRunnable);
        handler.removeCallbacks(showVisualStimulusRunnable);
        hideVisualStimulus();
        int waits = visualTask.getRandomInterval();
        Log.d("visualtest", "show after" + waits + "s");
        handler.postDelayed(showVisualStimulusRunnable, waits);
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
        Log.d("visualtest", "showing");
        btnImgBullseye.setVisibility(View.VISIBLE);
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
}