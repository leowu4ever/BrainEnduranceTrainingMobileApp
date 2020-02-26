package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MemoryFragment extends Fragment {


    private Button btnFinish, btnPause;

    private ImageButton btnLock;

    private Handler handler;

    private Runnable playingWordRunnable;

    private DialogHelper dh;

    public static SoundHelper soundHelper;

    private final int INTERVAL_MAX = 5;
    private final int INTERVAL_INCREMENT = 2 * 1000;

    private Random random;

    private ArrayList<String> chosenWordList, tempWordList;

    // uis for performance
    private TextView tvWordAnnoCount, tvWordLeftCount, tvDuration, tvDistance, tvPace, tvCurSpeed, tvAvgSpeed;

    private TrainingCommunicator trainingCommunicator;

    private MemoryCommunicator memoryCommunicator;

    private MainActivity mainact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        handler = new Handler();
        random = new Random();
        chosenWordList = new ArrayList<String>();
        tempWordList = new ArrayList<String>();
        mainact = (MainActivity) getActivity();
        soundHelper = new SoundHelper(mainact);
        chosenWordList = (ArrayList<String>) mainact.getChosenWordList().clone();
        tempWordList = (ArrayList<String>) chosenWordList.clone();
        return inflater.inflate(R.layout.fragment_memory, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIs();
        initRunnables();
        int waits = ((random.nextInt(INTERVAL_MAX) + 1) * INTERVAL_INCREMENT);
        Log.d("WAIT", "show after" + waits + "s");
        handler.postDelayed(playingWordRunnable, waits+4000);
    }

    private void initUIs() {
        trainingCommunicator = (TrainingCommunicator) getActivity();
        memoryCommunicator = (MemoryCommunicator) getActivity();
        dh = new DialogHelper(getActivity());
        btnFinish = getActivity().findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(playingWordRunnable);
                memoryCommunicator.finishMemoryTraining();
            }
        });

        btnPause = getActivity().findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(playingWordRunnable);
                trainingCommunicator.pauseTraining();
            }
        });

        btnLock = getActivity().findViewById(R.id.btn_lock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dh.showLockDialog();

            }
        });

        // performance info uis
        tvDistance = getActivity().findViewById(R.id.tv_distance);
        tvCurSpeed = getActivity().findViewById(R.id.tv_cur_speed);
        tvDuration = getActivity().findViewById(R.id.tv_duration);
        tvPace = getActivity().findViewById(R.id.tv_pace);
        tvAvgSpeed = getActivity().findViewById(R.id.tv_speed);
        tvWordAnnoCount = getActivity().findViewById(R.id.tv_wordAnnounced);
        tvWordLeftCount = getActivity().findViewById(R.id.tv_wordLeft);
        setTvWordAnnoCount("0");
        setTvWordLeftCount("" + chosenWordList.size());
    }

    private void initRunnables() {
        playingWordRunnable = new Runnable() {
            @Override
            public void run() {
                if (tempWordList.size()==0){
                    tempWordList = chosenWordList;
                }
                int ran = random.nextInt(tempWordList.size());
                String s = tempWordList.get(ran);
                soundHelper.playMemoryTask(s, 1, 1, 1, 0, 1);
                MainActivity.trainingData.incStiCount();
                setTvWordLeftCount(getWordListLength() - MainActivity.trainingData.getStiCount() + "");
                setTvWordAnnoCount(MainActivity.trainingData.getStiCount() + "");
                if (MainActivity.trainingData.getStiCount() > getWordListLength()) {
                    setTvWordLeftCount("-");
                } else {
                    setTvWordLeftCount(getWordListLength() - MainActivity.trainingData.getStiCount() + "");
                }

                String wordToAdd = (s.replace("memorytask_", "").substring(0, 1).toUpperCase() + s.substring(12));
                mainact.addAnnoncedWordtoList(wordToAdd);
                tempWordList.remove(ran);
                int waits = ((random.nextInt(INTERVAL_MAX) + 1) * INTERVAL_INCREMENT);
                Log.d("WAIT", "show after" + waits + "s");
                handler.postDelayed(this, waits);
            }
        };
    }

    public void getWordList(ArrayList<String> list) {
        chosenWordList = (ArrayList<String>) list.clone();
    }

    public void resumeWordRunnable(){
        handler.postDelayed(playingWordRunnable, 1000);
    }

    public int getWordListLength() {
        return chosenWordList.size();
    }

    public void setTvDistance(String s) {
        tvDistance.setText(s);
    }

    public void setTvCurSpeed(String s) {
        tvCurSpeed.setText(s);
    }

    public void setTvDuration(String s) {
        tvDuration.setText(s);
    }

    public void setTvPace(String s) {
        tvPace.setText(s);
    }

    public void setTvAvgSpeed(String s) {
        tvAvgSpeed.setText(s);
    }

    public void setTvWordAnnoCount(String s) {
        tvWordAnnoCount.setText(s);
    }

    public void setTvWordLeftCount(String s) {
        tvWordLeftCount.setText(s);
    }

}