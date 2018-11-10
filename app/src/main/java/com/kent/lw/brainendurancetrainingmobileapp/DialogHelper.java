package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogHelper {


    public Dialog pauseDialog, finishDialog;
    public Button btnResume, btnOK;
    public TextView tvFinishDuration, tvFinishDistance, tvFinishSpeed, tvFinishART, tvFinishAccuracy;

    public void initDialog(Context context) {
        //dialog
        pauseDialog = new Dialog(context);
        finishDialog = new Dialog(context);


        pauseDialog.setContentView(R.layout.dialog_pause);
        pauseDialog.setCancelable(false);
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        btnResume = pauseDialog.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
                MainActivity.resumeTraining();
            }
        });

        finishDialog.setContentView(R.layout.dialog_finish);
        finishDialog.setCancelable(false);
        finishDialog.setCanceledOnTouchOutside(false);
        finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finishDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);


        btnOK = finishDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog.dismiss();
                MainActivity.showTaskFragment();
            }
        });

        tvFinishDuration = finishDialog.findViewById(R.id.tv_finish_duration);
        tvFinishDistance = finishDialog.findViewById(R.id.tv_finish_distance);
        tvFinishSpeed = finishDialog.findViewById(R.id.tv_finish_speed);
        tvFinishART = finishDialog.findViewById(R.id.tv_finish_ast);
        tvFinishAccuracy = finishDialog.findViewById(R.id.tv_finish_accuracy);

    }

    public void setTvFinishDuration(String s) {
        tvFinishDuration.setText(s);
    }

    public void setTvFinishDistance(String s) {
        tvFinishDistance.setText(s);
    }
    public void setTvFinishSpeed(String s) {
        tvFinishSpeed.setText(s);
    }
    public void setTvFinishART(String s) {
        tvFinishART.setText(s);
    }
    public void setTvFinishAccuracy(String s) {
        tvFinishAccuracy.setText(s);
    }

    public void showFinishDialog() {
        finishDialog.show();
    }

    public void showPauseDialog() {
        pauseDialog.show();
    }

}


