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

    private Dialog taskDialog, difDialog,difCustomDialog, activityDialog, durationDialog, helpAPVTDialog, helpGONOGODialog;
    private Button btnStart, btnActivity, btnDuration, btnTask, btnDif, btnAPVT, btnWAVT, btnVisual, btnEasy, btnMedium, btnHard, btnAdaptive, btnCustom, btnSave, btnWalking, btnMarching, btnRunning, btnDurationSave;
    private String taskSelected, difSelected;

    private RangeBar rbTaskDuration, rbInterval, rbVolume, rbNoise, rbThreshold;
    private TextView tvInterval, tvVolume, tvNoise, tvThreshold;
    private TaskCommunicator taskCommunicator;

    private Button btnHelpAPVT, btnHelpGONOGO, btnHelpAPVTOK, btnHelpGONOGOOK;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragmentBtns();
        initDialogs();
        initAPVTRbs();
        initHelpBtns();
    }

    private void initFragmentBtns() {
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

        btnActivity = getActivity().findViewById(R.id.btn_activity);
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDialog.show();
            }
        });

        btnDuration = getActivity().findViewById(R.id.btn_duration);
        btnDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationDialog.show();
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
    }

    private void initDialogs() {
        taskDialog = new Dialog(getActivity());
        difDialog = new Dialog(getActivity());
        difCustomDialog = new Dialog(getActivity());
        activityDialog = new Dialog(getActivity());
        durationDialog = new Dialog(getActivity());

        helpAPVTDialog = new Dialog(getActivity());
        helpGONOGODialog = new Dialog(getActivity());

        setupDialog(taskDialog, R.layout.dialog_task);
        setupDialog(difDialog, R.layout.dialog_dif);
        setupDialog(difCustomDialog, R.layout.dialog_dif_custom_apvt);
        setupDialog(activityDialog, R.layout.dialog_activity);
        setupDialog(durationDialog, R.layout.dialog_duration);

        setupDialog(helpAPVTDialog, R.layout.dialog_help_apvt);
        setupDialog(helpGONOGODialog, R.layout.dialog_help_gonogo);

        initActivityBtns();
        initTaskBtns();
        initDifBtns();
        initDurationBtns();
    }

    private void setupDialog(Dialog d, int layout) {
        d.setContentView(layout);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    private void initDurationBtns() {
        btnSave = difCustomDialog.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomDialog.dismiss();
                btnDif.setText(btnCustom.getText());
            }
        });

        btnDurationSave = durationDialog.findViewById(R.id.btn_duration_save);
        btnDurationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationDialog.dismiss();
                btnDuration.setText(rbTaskDuration.getRightPinValue() + " min");
            }
        });
    }

    private void initDifBtns() {
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
                difDialog.dismiss();
                difCustomDialog.show();
            }
        });
    }

    private void initTaskBtns() {
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

        btnVisual = taskDialog.findViewById(R.id.btn_visual);
        btnVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnVisual.getText());
                taskDialog.dismiss();
            }
        });
    }

    private void initActivityBtns() {
        btnWalking = activityDialog.findViewById(R.id.btn_walking);
        btnWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivity.setText(btnWalking.getText());
                activityDialog.dismiss();
            }
        });

        btnRunning = activityDialog.findViewById(R.id.btn_running);
        btnRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivity.setText(btnRunning.getText());
                activityDialog.dismiss();
            }
        });

        btnMarching = activityDialog.findViewById(R.id.btn_marching);
        btnMarching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivity.setText(btnMarching.getText());
                activityDialog.dismiss();
            }
        });
    }

    private void initAPVTRbs() {

        rbTaskDuration = durationDialog.findViewById(R.id.rb_task_duration);
        rbTaskDuration.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

            }
        });

        tvInterval = difCustomDialog.findViewById(R.id.tv_interval);
        rbInterval = difCustomDialog.findViewById(R.id.rb_interval);
        rbInterval.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvInterval.setText("Stimulus Interval: from " + (Integer.parseInt(leftPinValue) * 1000) + "ms" + " to " + (Integer.parseInt(rightPinValue) * 1000) + "ms");
                MainActivity.apvtStiIntervalMin = Integer.parseInt(leftPinValue);
                MainActivity.apvtStiIntervalMax = Integer.parseInt(rightPinValue);
            }
        });



        tvVolume = difCustomDialog.findViewById(R.id.tv_volume);
        rbVolume = difCustomDialog.findViewById(R.id.rb_volume);
        rbVolume.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvVolume.setText("Tone Volume: from " + (Float.parseFloat(leftPinValue) * 100) + "%" + " to " + (Float.parseFloat(rightPinValue) * 100) + "%");
                MainActivity.apvtToneVolumeMin = Float.parseFloat(leftPinValue);
                MainActivity.apvtToneVolumeMax = Float.parseFloat(rightPinValue);
            }
        });

        tvNoise = difCustomDialog.findViewById(R.id.tv_noise);
        rbNoise = difCustomDialog.findViewById(R.id.rb_noise);
        rbNoise.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvNoise.setText("Background Noise: " + (Integer.parseInt(rightPinValue) * 100) + "%");
                MainActivity.apvtBgNoise = Integer.parseInt(rightPinValue);


            }
        });

        tvThreshold = difCustomDialog.findViewById(R.id.tv_threshold);
        rbThreshold = difCustomDialog.findViewById(R.id.rb_threshold);
        rbThreshold.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                tvThreshold.setText("Vaild Response : from " + (Integer.parseInt(leftPinValue) * 100) + "ms" + " to " + (Integer.parseInt(rightPinValue) * 100) + "ms");
                MainActivity.apvtValidResThresholdMin = Integer.parseInt(leftPinValue);
                MainActivity.apvtValidResThresholdMax = Integer.parseInt(rightPinValue);
            }
        });
    }

    private void initHelpBtns() {
        btnHelpAPVT = taskDialog.findViewById(R.id.btn_help_apvt);
        btnHelpAPVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpAPVTDialog.show();
            }
        });

        btnHelpAPVTOK = helpAPVTDialog.findViewById(R.id.btn_help_apvt_ok);
        btnHelpAPVTOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpAPVTDialog.dismiss();
            }
        });

        btnHelpGONOGO = taskDialog.findViewById(R.id.btn_help_gonogo);
        btnHelpGONOGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGONOGODialog.show();
            }
        });
        btnHelpGONOGOOK = helpGONOGODialog.findViewById(R.id.btn_help_gonogo_ok);
        btnHelpGONOGOOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGONOGODialog.dismiss();
            }
        });
    }
}