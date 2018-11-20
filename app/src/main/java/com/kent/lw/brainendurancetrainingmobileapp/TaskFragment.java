package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    // btn default text
    private final String btnDefaultText = "Please select";
    private TaskCommunicator taskCommunicator;
    private String taskSelected, difSelected;
    // fragment
    private Button btnStart;
    // task dialog
    private Dialog taskDialog;
    private Button btnTask, btnAPVT, btnGonono, btnVisual, btnHelpApvt, btnHelpGonogo;
    // dif dialog
    private Dialog difDialog;
    private Button btnDif, btnEasy, btnMedium, btnHard, btnAdaptive, btnCustom;
    // dif prompt dialog
    private Dialog promptDialog;
    private Button btnDifPromptOk;
    private TextView tvPrompt;
    // activity dialog
    private Dialog activityDialog;
    private Button btnWalking, btnMarching, btnRunning, btnActivity;
    // duration dialog
    private Dialog durationDialog;
    private Button btnDuration, btnDurationSave;
    private RangeBar rbTaskDuration;
    // help dialog
    private Dialog helpApvtDialog, helpGonogoDialog;
    private Button btnHelpApvtOK, btnHelpGonogoOK;
    // apvt custom dialog
    private Dialog difCustomAPVTDialog;
    private Button btnCustomApvtSave;
    private RangeBar rbIntervalApvt, rbVolumeApvt, rbNoiseApvt, rbThresholdApvt;
    private TextView tvIntervalApvt, tvVolumeApvt, tvNoiseApvt, tvThresholdApvt;
    //gonogo dialog
    private Dialog difCustomGonogoDialog;
    private Button btnCustomGonogoSave;
    private RangeBar rbIntervalGonogo, rbVolumeGonogo, rbNoiseGonogo, rbThresholdGonogo;
    private TextView tvIntervalGonogo, tvVolumeGonogo, tvNoiseGonogo, tvThresholdGonogo;

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
                if (!btnTask.getText().equals(btnDefaultText)) {
                    difDialog.show();
                } else {
                    tvPrompt.setText("Please select a cognitive task.");
                    promptDialog.show();
                }
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

                if (!btnTask.getText().equals(btnDefaultText) && !btnDuration.getText().equals(btnDefaultText) && !btnActivity.getText().equals(btnDefaultText) && !btnDif.getText().equals(btnDefaultText)) {
                    taskSelected = btnTask.getText().toString();
                    difSelected = btnDif.getText().toString();

                    MainActivity.trainingData.setActivity(btnActivity.getText() + "");
                    MainActivity.trainingData.setDuration(btnDuration.getText() + "");
                    MainActivity.trainingData.setTask(btnTask.getText() + "");
                    MainActivity.trainingData.setDif(btnDif.getText() + "");

                    taskCommunicator.startTraining(taskSelected, difSelected);
                } else {
                    promptDialog.show();
                    tvPrompt.setText("Please complete all sections.");
                }
            }
        });
    }

    private void initDialogs() {
        taskDialog = new Dialog(getActivity());
        difDialog = new Dialog(getActivity());
        difCustomAPVTDialog = new Dialog(getActivity());
        difCustomGonogoDialog = new Dialog(getActivity());
        activityDialog = new Dialog(getActivity());
        durationDialog = new Dialog(getActivity());
        helpApvtDialog = new Dialog(getActivity());
        helpGonogoDialog = new Dialog(getActivity());
        promptDialog = new Dialog(getActivity());

        setupDialog(taskDialog, R.layout.dialog_task);
        setupDialog(difDialog, R.layout.dialog_dif);
        setupDialog(difCustomAPVTDialog, R.layout.dialog_dif_custom_apvt);
        setupDialog(difCustomGonogoDialog, R.layout.dialog_dif_custom_gonogo);
        setupDialog(activityDialog, R.layout.dialog_activity);
        setupDialog(durationDialog, R.layout.dialog_duration);
        setupDialog(promptDialog, R.layout.dialog_prompt);

        setupDialog(helpApvtDialog, R.layout.dialog_help_apvt);
        setupDialog(helpGonogoDialog, R.layout.dialog_help_gonogo);

        initActivityBtns();
        initTaskBtns();
        initDifBtns();
        initDurationBtns();
        initCustomBtns();

        tvPrompt = promptDialog.findViewById(R.id.tv_prompt);
        btnDifPromptOk = promptDialog.findViewById(R.id.btn_dif_prompt_ok);
        btnDifPromptOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        });
    }

    private void setupDialog(Dialog d, int layout) {
        d.setContentView(layout);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    private void initDurationBtns() {

        btnDurationSave = durationDialog.findViewById(R.id.btn_duration_save);
        btnDurationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationDialog.dismiss();
                btnDuration.setText(rbTaskDuration.getRightPinValue() + " min");
            }
        });


        rbTaskDuration = durationDialog.findViewById(R.id.rb_task_duration);
        rbTaskDuration.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                MainActivity.apvtTask.setDuration(Integer.parseInt(rightPinValue) * 60 * 1000);
            }
        });
    }

    private void initCustomBtns() {
        btnCustomApvtSave = difCustomAPVTDialog.findViewById(R.id.btn_apvt_save);
        btnCustomApvtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomAPVTDialog.dismiss();
                btnDif.setText(btnCustom.getText());

                // show task configuration
                Log.d("apvttask", MainActivity.apvtTask.toString());

            }
        });

        btnCustomGonogoSave = difCustomGonogoDialog.findViewById(R.id.btn_gonogo_save);
        btnCustomGonogoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomGonogoDialog.dismiss();
                btnDuration.setText(btnCustom.getText());
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
                if (btnTask.getText().equals(btnAPVT.getText())) {
                    difDialog.dismiss();
                    difCustomAPVTDialog.show();
                }

                if (btnTask.getText().equals(btnGonono.getText())) {
                    difDialog.dismiss();
                    difCustomGonogoDialog.show();
                }
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

        btnGonono = taskDialog.findViewById(R.id.btn_gonogo);
        btnGonono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnGonono.getText());
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

        tvIntervalApvt = difCustomAPVTDialog.findViewById(R.id.tv_interval_apvt);
        rbIntervalApvt = difCustomAPVTDialog.findViewById(R.id.rb_interval_apvt);
        rbIntervalApvt.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                //tvIntervalApvt.setText("Interstimulus interval: " + leftPinValue + " ~ " + rightPinValue + "s");

                MainActivity.apvtTask.setIntervalFrom(Integer.parseInt(leftPinValue));
                MainActivity.apvtTask.setIntervalTo(Integer.parseInt(rightPinValue));
            }
        });

        tvVolumeApvt = difCustomAPVTDialog.findViewById(R.id.tv_volume_apvt);
        rbVolumeApvt = difCustomAPVTDialog.findViewById(R.id.rb_volume_apvt);
        rbVolumeApvt.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                //tvVolumeApvt.setText("Tone volume: " + (Float.parseFloat(leftPinValue) * 100) + " ~ " + (Float.parseFloat(rightPinValue) * 100) + "%");

                MainActivity.apvtTask.setVolumeFrom(Float.parseFloat(leftPinValue)/100);
                MainActivity.apvtTask.setVolumeTo(Float.parseFloat(rightPinValue)/100);
            }
        });

        tvNoiseApvt = difCustomAPVTDialog.findViewById(R.id.tv_noise_apvt);
        rbNoiseApvt = difCustomAPVTDialog.findViewById(R.id.rb_noise_apvt);
        rbNoiseApvt.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                //tvNoiseApvt.setText("Background noise: " + (Float.parseFloat(rightPinValue) * 100) + "%");
                MainActivity.apvtTask.setNoise(Float.parseFloat(rightPinValue)/100);
            }
        });

        tvThresholdApvt = difCustomAPVTDialog.findViewById(R.id.tv_threshold_apvt);
        rbThresholdApvt = difCustomAPVTDialog.findViewById(R.id.rb_threshold_apvt);
        rbThresholdApvt.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                //tvThresholdApvt.setText("Vaild response time: " + (Integer.parseInt(rightPinValue) * 100) + "ms");
                MainActivity.apvtTask.setResThreshold(Integer.parseInt(rightPinValue));
            }
        });
    }

    private void initHelpBtns() {
        btnHelpApvt = taskDialog.findViewById(R.id.btn_help_apvt);
        btnHelpApvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpApvtDialog.show();
            }
        });
        btnHelpApvtOK = helpApvtDialog.findViewById(R.id.btn_help_apvt_ok);
        btnHelpApvtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpApvtDialog.dismiss();
            }
        });
        btnHelpGonogo = taskDialog.findViewById(R.id.btn_help_gonogo);
        btnHelpGonogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGonogoDialog.show();
            }
        });
        btnHelpGonogoOK = helpGonogoDialog.findViewById(R.id.btn_help_gonogo_ok);
        btnHelpGonogoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGonogoDialog.dismiss();
            }
        });
    }
}