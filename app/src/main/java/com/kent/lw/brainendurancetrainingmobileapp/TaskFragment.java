package com.kent.lw.brainendurancetrainingmobileapp;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    private Dialog taskDialog, difDialog,difCustomDialog;
    private Button btnStart, btnTask, btnDif, btnAPVT, btnWAVT, btnEasy, btnMedium, btnHard, btnAdaptive, btnCustom, btnSave;
    private String taskSelected, difSelected;

    private RangeBar rbTaskDuration, rbInterval, rbStiDuration, rbVolume, rbNoise, rbThreshold;
    private TextView tvTaskDuration, tvInterval, tvStiDuration, tvVolume, tvNoise, tvThreshold;

    private TaskCommunicator taskCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        taskCommunicator = (TaskCommunicator) getActivity();
        btnTask = getActivity().findViewById(R.id.btn_task);
        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDialog.show();
            }
        });

        btnDif = getActivity().findViewById(R.id.btn_dif);
        btnDif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difDialog.show();
            }
        });

        btnStart = getActivity().findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSelected = btnTask.getText().toString();
                difSelected =  btnDif.getText().toString();
                taskCommunicator.startTraining(taskSelected, difSelected);

            }
        });

        initDialogs();
        initRbs();
    }

    private void initDialogs() {
        taskDialog = new Dialog(getActivity());
        taskDialog.setContentView(R.layout.dialog_task);
        taskDialog.setCancelable(false);
        taskDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        taskDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        difDialog = new Dialog(getActivity());
        difDialog.setContentView(R.layout.dialog_dif);
        difDialog.setCancelable(false);
        difDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        difDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        difCustomDialog = new Dialog(getActivity());
        difCustomDialog.setContentView(R.layout.dialog_dif_custom);
        difCustomDialog.setCancelable(false);
        difCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        difCustomDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        btnAPVT = taskDialog.findViewById(R.id.btn_apvt);
        btnAPVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnAPVT.getText());
                taskDialog.dismiss();
            }
        });

        btnWAVT = taskDialog.findViewById(R.id.btn_wavt);
        btnWAVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnWAVT.getText());
                taskDialog.dismiss();
            }
        });

        btnEasy = difDialog.findViewById(R.id.btn_easy);
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnEasy.getText());
                difDialog.dismiss();
            }
        });

        btnMedium = difDialog.findViewById(R.id.btn_medium);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnMedium.getText());
                difDialog.dismiss();
            }
        });

        btnHard = difDialog.findViewById(R.id.btn_hard);
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnHard.getText());
                difDialog.dismiss();

            }
        });

        btnAdaptive = difDialog.findViewById(R.id.btn_adaptive);
        btnAdaptive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnAdaptive.getText());
                difDialog.dismiss();

            }
        });

        btnCustom = difDialog.findViewById(R.id.btn_custom);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnCustom.getText());
                difDialog.dismiss();
                difCustomDialog.show();
            }
        });

        btnSave = difCustomDialog.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomDialog.dismiss();
            }
        });

    }

    private void initRbs() {
        tvTaskDuration = difCustomDialog.findViewById(R.id.tv_task_duration);
        rbTaskDuration = difCustomDialog.findViewById(R.id.rb_task_duration);
        rbTaskDuration.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvTaskDuration.setText("Task Duration: " + rightPinValue + "min");
            }
        });

        tvInterval = difCustomDialog.findViewById(R.id.tv_interval);
        rbInterval = difCustomDialog.findViewById(R.id.rb_interval);
        rbInterval.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvInterval.setText("Stimulus Interval: fron " + (Integer.parseInt(leftPinValue) * 100) + "ms" + " to " + (Integer.parseInt(rightPinValue) * 100) + "ms");
            }
        });

        tvStiDuration = difCustomDialog.findViewById(R.id.tv_sti_duration);
        rbStiDuration = difCustomDialog.findViewById(R.id.rb_sti_duration);
        rbStiDuration.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvStiDuration.setText("Stimulus Duration: fron " + (Integer.parseInt(leftPinValue) * 100) + "ms" + " to " + (Integer.parseInt(rightPinValue) * 100) + "ms");
            }
        });

        tvVolume = difCustomDialog.findViewById(R.id.tv_volume);
        rbVolume = difCustomDialog.findViewById(R.id.rb_volume);
        rbVolume.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvVolume.setText("Tone Volume: fron " + (Integer.parseInt(leftPinValue) * 100) + "%" + " to " + (Integer.parseInt(rightPinValue) * 100) + "%");
            }
        });

        tvNoise = difCustomDialog.findViewById(R.id.tv_noise);
        rbNoise = difCustomDialog.findViewById(R.id.rb_noise);
        rbNoise.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvNoise.setText("Background Noise: " + (Integer.parseInt(rightPinValue) * 100) + "%");
            }
        });

        tvThreshold = difCustomDialog.findViewById(R.id.tv_threshold);
        rbThreshold = difCustomDialog.findViewById(R.id.rb_threshold);
        rbThreshold.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvThreshold.setText("Vaild Response : from " + (Integer.parseInt(leftPinValue) * 100) + "ms" + " to " + (Integer.parseInt(rightPinValue) * 100) + "ms");
            }
        });
    }
}