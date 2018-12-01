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
    private Button btnCustomSaveApvt;
    private RangeBar rbIntervalApvt, rbVolumeApvt, rbNoiseApvt, rbThresholdApvt, rbMinspeedApvt;
    //gonogo dialog
    private Dialog difCustomGonogoDialog;
    private Button btnCustomSaveGonogo;
    private RangeBar rbNogo, rbIntervalGonogo, rbVolumeGonogo, rbNoiseGonogo, rbThresholdGonogo, rbMinspeedGonogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragBtns();
        initDialogs();
        initAPVTRbs();
        initGonogoRbs();
        initHelpBtns();
    }

    private void initFragBtns() {
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
                    MainActivity.trainingData.setTaskConfig(MainActivity.apvtTask);


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
        initCustomSaveBtns();

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

        rbTaskDuration = durationDialog.findViewById(R.id.rb_task_duration);

        btnDurationSave = durationDialog.findViewById(R.id.btn_duration_save);
        btnDurationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update duration
                MainActivity.apvtTask.setDuration(Integer.parseInt(rbTaskDuration.getRightPinValue()) * 60 * 1000);
                MainActivity.gonogoTask.setDuration(Integer.parseInt(rbTaskDuration.getRightPinValue()) * 60 * 1000);
                durationDialog.dismiss();
                btnDuration.setText(rbTaskDuration.getRightPinValue() + " min");
            }
        });
    }

    private void initCustomSaveBtns() {
        btnCustomSaveApvt = difCustomAPVTDialog.findViewById(R.id.btn_apvt_save);
        btnCustomSaveApvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.apvtTask.setIntervalFrom(Integer.parseInt(rbIntervalApvt.getLeftPinValue()));
                MainActivity.apvtTask.setIntervalTo(Integer.parseInt(rbIntervalApvt.getRightPinValue()));
                MainActivity.apvtTask.setVolumeFrom(Float.parseFloat(rbVolumeApvt.getLeftPinValue()) / 100);
                MainActivity.apvtTask.setVolumeTo(Float.parseFloat(rbVolumeApvt.getRightPinValue()) / 100);
                MainActivity.apvtTask.setNoise(Float.parseFloat(rbNoiseApvt.getRightPinValue()) / 100);
                MainActivity.apvtTask.setResThreshold(Integer.parseInt(rbThresholdApvt.getRightPinValue()));
                MainActivity.apvtTask.setMinSpeed(Float.parseFloat(rbMinspeedApvt.getRightPinValue()));

                difCustomAPVTDialog.dismiss();
                btnDif.setText(btnCustom.getText());
                // show task configuration
            }
        });

        btnCustomSaveGonogo = difCustomGonogoDialog.findViewById(R.id.btn_gonogo_save);
        btnCustomSaveGonogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomGonogoDialog.dismiss();
                btnDuration.setText(btnCustom.getText());
            }
        });

        btnCustomSaveGonogo = difCustomGonogoDialog.findViewById(R.id.btn_gonogo_save);
        btnCustomSaveGonogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set parameters
                MainActivity.gonogoTask.setNogoPropotion(Integer.parseInt(rbNogo.getRightPinValue()));
                MainActivity.gonogoTask.setIntervalFrom(Integer.parseInt(rbIntervalGonogo.getLeftPinValue()));
                MainActivity.gonogoTask.setIntervalTo(Integer.parseInt(rbIntervalGonogo.getRightPinValue()));
                MainActivity.gonogoTask.setVolumeFrom(Float.parseFloat(rbVolumeGonogo.getLeftPinValue()) / 100);
                MainActivity.gonogoTask.setVolumeTo(Float.parseFloat(rbVolumeGonogo.getRightPinValue()) / 100);
                MainActivity.gonogoTask.setNoise(Float.parseFloat(rbNoiseGonogo.getRightPinValue()) / 100);
                MainActivity.gonogoTask.setResThreshold(Integer.parseInt(rbThresholdGonogo.getRightPinValue()));
                MainActivity.gonogoTask.setMinSpeed(Float.parseFloat(rbMinspeedGonogo.getRightPinValue()));

                difCustomGonogoDialog.dismiss();
                btnDif.setText(btnCustom.getText());
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

        rbIntervalApvt = difCustomAPVTDialog.findViewById(R.id.rb_interval_apvt);
        rbVolumeApvt = difCustomAPVTDialog.findViewById(R.id.rb_volume_apvt);
        rbNoiseApvt = difCustomAPVTDialog.findViewById(R.id.rb_noise_apvt);
        rbThresholdApvt = difCustomAPVTDialog.findViewById(R.id.rb_threshold_apvt);
        rbMinspeedApvt = difCustomAPVTDialog.findViewById(R.id.rb_minspeed_apvt);
    }

    private void initGonogoRbs() {

        rbNogo = difCustomGonogoDialog.findViewById(R.id.rb_nogo_gonogo);
        rbIntervalGonogo = difCustomGonogoDialog.findViewById(R.id.rb_interval_gonogo);
        rbVolumeGonogo = difCustomGonogoDialog.findViewById(R.id.rb_volume_gonogo);
        rbNoiseGonogo = difCustomGonogoDialog.findViewById(R.id.rb_noise_gonogo);
        rbThresholdGonogo = difCustomGonogoDialog.findViewById(R.id.rb_threshold_gonogo);
        rbMinspeedGonogo = difCustomGonogoDialog.findViewById(R.id.rb_minspeed_gonogo);
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