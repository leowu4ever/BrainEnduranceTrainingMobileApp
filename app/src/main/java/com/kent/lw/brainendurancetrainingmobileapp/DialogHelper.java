package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.solver.widgets.ConstraintWidgetGroup;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogHelper {


    public Dialog pauseDialog, finishDialog, countdownDialog;
    public Button btnResume, btnOK;
    public TextView tvFinishDuration, tvFinishDistance, tvFinishSpeed, tvFinishPace, tvFinishART, tvFinishAccuracy, tvCountdown;

    public void initDialog(Context context) {
        //dialog
        pauseDialog = new Dialog(context);
        finishDialog = new Dialog(context);
        countdownDialog = new Dialog(context);
        setupDialog(pauseDialog, R.layout.dialog_pause);
        setupDialog(finishDialog, R.layout.dialog_finish);
        setupDialog(countdownDialog, R.layout.dialog_countdown);



        btnResume = pauseDialog.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
                MainActivity.resumeTraining();
            }
        });

        btnOK = finishDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showTaskFragment();
                finishDialog.dismiss();
            }
        });

        tvFinishDuration = finishDialog.findViewById(R.id.tv_finish_duration);
        tvFinishDistance = finishDialog.findViewById(R.id.tv_finish_distance);
        tvFinishPace = finishDialog.findViewById(R.id.tv_finish_pace);
        tvFinishSpeed = finishDialog.findViewById(R.id.tv_finish_speed);
        tvFinishART = finishDialog.findViewById(R.id.tv_finish_ast);
        tvFinishAccuracy = finishDialog.findViewById(R.id.tv_finish_accuracy);
    }

    public void setupDialog(Dialog d, int layout) {
        d.setContentView(layout);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    public void showFinishDialog() {
        finishDialog.show();
    }
    public void showPauseDialog() {
        pauseDialog.show();
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

    public void setupFinishDialog(TrainingData td) {
        tvFinishDuration.setText("Duration: " + td.getTime());
        tvFinishDistance.setText("Distance: " + td.getDistance() + "KM");
        tvFinishSpeed.setText("Speed: " + td.getAvgSpeed() + "KM/H");
        tvFinishPace.setText("Pace: " + td.getAvgPace() + "MIN/KM");
        tvFinishART.setText("Avg RT: " + td.getAvgResTime() + "ms");
        tvFinishAccuracy.setText("Accuracy: " + td.getAccuracy() + "%");
    }
}


