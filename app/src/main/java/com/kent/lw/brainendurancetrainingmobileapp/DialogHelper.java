package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogHelper {

    public Dialog pauseDialog, finishDialog, countdownDialog, lockDialog, detailDialog;
    public Button btnResumeOk, btnFinishOK, btnDetailOk, btnUnlock;
    public TextView tvFinishDuration, tvFinishDistance, tvFinishSpeed, tvFinishPace, tvFinishART, tvFinishAccuracy, tvCountdown;
    public TextView tvHistoryDate, tvHistoryTime, tvHistoryActivity, tvHistoryTask, tvHistoryDif, tvHistoryDuration, tvHistoryDistance, tvHistorySpeed, tvHistoryPace, tvHistoryART, tvHistoryAccuracy;

    public Dialog diaryDialog, trainingDiaryDialog, motiDialog, rpeDialog, nasaDialog;
    public Button btnTrainingDiary, btnMoti, btnRpe, btnNasa, btnTrainingDiarySave, btnMotiSave, btnRpeSave, btnNasaSave;


    public DialogHelper(Context context) {
        init(context);

    }
    public void init(Context context) {
        //dialog
        pauseDialog = new Dialog(context);
        finishDialog = new Dialog(context);
        countdownDialog = new Dialog(context);
        lockDialog = new Dialog(context);
        detailDialog = new Dialog(context);

        diaryDialog = new Dialog(context);
        trainingDiaryDialog = new Dialog(context);
        motiDialog = new Dialog(context);
        rpeDialog = new Dialog(context);
        nasaDialog = new Dialog(context);

        setupDialog(pauseDialog, R.layout.dialog_pause);
        setupDialog(finishDialog, R.layout.dialog_finish);
        setupDialog(countdownDialog, R.layout.dialog_countdown);
        setupDialog(lockDialog, R.layout.dialog_lock);
        setupDialog(detailDialog, R.layout.dialog_detail);

        setupDialog(diaryDialog, R.layout.dialog_diary);
        setupDialog(trainingDiaryDialog, R.layout.dialog_diary_training);
        setupDialog(motiDialog, R.layout.dialog_diary_moti);
        setupDialog(rpeDialog, R.layout.dialog_diary_rpe);
        setupDialog(nasaDialog, R.layout.dialog_diary_nasa);

        btnResumeOk = pauseDialog.findViewById(R.id.btn_resume);
        btnResumeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPauseDialog();
                MainActivity.resumeTraining();
            }
        });

        btnFinishOK = finishDialog.findViewById(R.id.btn_ok);
        btnFinishOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showTaskFragment();
                dismissFinishDialog();
            }
        });


        btnDetailOk = detailDialog.findViewById(R.id.btn_detail_ok);
        btnDetailOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.dismiss();
            }
        });

        btnUnlock = lockDialog.findViewById(R.id.btn_unlock);
        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissLockDialog();
            }
        });

        btnTrainingDiary = diaryDialog.findViewById(R.id.btn_trainingdiary);
        btnTrainingDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showTrainingDiaryDialog();
            }
        });

        btnMoti = diaryDialog.findViewById(R.id.btn_moti);
        btnMoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showMotiDialog();
            }
        });

        btnRpe = diaryDialog.findViewById(R.id.btn_rpe);
        btnRpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showRpeDialog();
            }
        });

        btnNasa = diaryDialog.findViewById(R.id.btn_nasa);
        btnNasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showNasaDialog();
            }
        });

        btnTrainingDiarySave = trainingDiaryDialog.findViewById(R.id.btn_diary_trainingdiary_save);
        btnTrainingDiarySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTrainingDiaryDialog();
            }
        });

        btnMotiSave = motiDialog.findViewById(R.id.btn_diary_moti_save);
        btnMotiSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMotiDialog();
            }
        });

        btnRpeSave = rpeDialog.findViewById(R.id.btn_diary_rpe_save);
        btnRpeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissRpeDialog();
            }
        });

        btnNasaSave = nasaDialog.findViewById(R.id.btn_diary_nasa_save);
        btnNasaSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissNasaDialog();
            }
        });

        tvFinishDuration = finishDialog.findViewById(R.id.tv_finish_duration);
        tvFinishDistance = finishDialog.findViewById(R.id.tv_finish_distance);
        tvFinishSpeed = finishDialog.findViewById(R.id.tv_finish_speed);
        tvFinishPace = finishDialog.findViewById(R.id.tv_finish_pace);
        tvFinishART = finishDialog.findViewById(R.id.tv_finish_art);
        tvFinishAccuracy = finishDialog.findViewById(R.id.tv_finish_accuracy);

        tvHistoryDate = detailDialog.findViewById(R.id.tv_history_date);
        tvHistoryTime = detailDialog.findViewById(R.id.tv_history_time);
        tvHistoryActivity = detailDialog.findViewById(R.id.tv_history_activity);
        tvHistoryTask = detailDialog.findViewById(R.id.tv_history_task);
        tvHistoryDif = detailDialog.findViewById(R.id.tv_history_dif);
        tvHistoryDuration = detailDialog.findViewById(R.id.tv_history_duration);
        tvHistoryDistance = detailDialog.findViewById(R.id.tv_history_distance);
        tvHistorySpeed = detailDialog.findViewById(R.id.tv_history_speed);
        tvHistoryPace = detailDialog.findViewById(R.id.tv_history_pace);
        tvHistoryART = detailDialog.findViewById(R.id.tv_history_art);
        tvHistoryAccuracy = detailDialog.findViewById(R.id.tv_history_accuracy);


    }

    public void setupDialog(Dialog d, int layout) {
        d.setContentView(layout);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    public void showFinishDialog(TrainingData td) {
        setupFinishDialog(td);
        finishDialog.show();
    }

    public void dismissFinishDialog() {
        finishDialog.dismiss();
    }

    public void showPauseDialog() {
        pauseDialog.show();
    }

    public void dismissPauseDialog() {
        pauseDialog.dismiss();
    }

    public void showCountdownDialog() {
        countdownDialog.show();
    }

    public void dismissCountdownDialog() {
        countdownDialog.dismiss();
    }

    public void setCountdownText(String s) {
        tvCountdown = countdownDialog.findViewById(R.id.tv_countdown);
        tvCountdown.setText(s);
    }

    public void showLockDialog() {
        lockDialog.show();
    }

    public void dismissLockDialog() {
        lockDialog.dismiss();
    }

    public void showDiaryDialog() {
        diaryDialog.show();
    }

    public void dismissDiaryDialog() {
        diaryDialog.dismiss();
    }

    public void showTrainingDiaryDialog() {
        trainingDiaryDialog.show();
    }

    public void dismissTrainingDiaryDialog() {
        trainingDiaryDialog.dismiss();
    }

    public void showMotiDialog() {
        motiDialog.show();
    }

    public void dismissMotiDialog() {
        motiDialog.dismiss();
    }

    public void showRpeDialog() {
        rpeDialog.show();
    }

    public void dismissRpeDialog() {
        rpeDialog.dismiss();
    }

    public void showNasaDialog() {
        nasaDialog.show();
    }

    public void dismissNasaDialog() {
        nasaDialog.dismiss();
    }

    public boolean isLockDialogShowing() {
        return lockDialog.isShowing();
    }

    public void setupFinishDialog(TrainingData td) {
        tvFinishDuration.setText("Duration: " + DateHelper.getTimeFromMs(td.getTimeTrained()));
        tvFinishDistance.setText("Distance: " + td.getDistance() + "KM");
        tvFinishSpeed.setText("Avg speed: " + td.getAvgSpeed() + "KM/H");
        tvFinishPace.setText("Avg pace: " + td.getAvgPace() + "MIN/KM");
        tvFinishART.setText("Avg RT: " + td.getAvgResTime() + "ms");
        tvFinishAccuracy.setText("Accuracy: " + td.getAccuracy() + "%");
    }

    public void showHistoryDialog(TrainingData td) {
        setupHistoryDialog(td);
        detailDialog.show();
    }

    public void setupHistoryDialog(TrainingData td) {

        tvHistoryDate.setText("Date: " + DateHelper.getDateFromMili(td.getStartTime()));
        tvHistoryTime.setText("Time: " + DateHelper.getTimeFromMili(td.getStartTime()));
        tvHistoryActivity.setText("Activity: " + td.getActivity());
        tvHistoryTask.setText("Cognitive task: " + td.getTask());
        tvHistoryDif.setText("Difficulty level: " + td.getDif());
        tvHistoryDuration.setText("Duration: " + DateHelper.getTimeFromMs(td.getTimeTrained()));
        tvHistoryDistance.setText("Distance: " + td.getDistance() + "KM");
        tvHistorySpeed.setText("Avg speed: " + td.getAvgSpeed() + "KM/H");
        tvHistoryPace.setText("Avg pace: " + td.getAvgPace() + "MIN/KM");
        tvHistoryART.setText("Avg RT: " + td.getAvgResTime() + "ms");
        tvHistoryAccuracy.setText("Accuracy: " + td.getAccuracy() + "%");
    }
}


