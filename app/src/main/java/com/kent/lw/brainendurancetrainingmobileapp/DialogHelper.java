package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class DialogHelper {

    // training fragment
    public Dialog pauseDialog, finishDialog, countdownDialog, lockDialog;
    public TextView tvFinishDuration, tvFinishDistance, tvFinishSpeed, tvFinishPace, tvFinishART, tvFinishAccuracy, tvCountdown;
    public Button btnResumeOk, btnFinishOK,  btnUnlock;

    // for history
    public Dialog detailDialog;
    public TextView tvHistoryNogo, tvHistoryInterval, tvHistoryVolume, tvHistoryNoise, tvHistoryThreshold, tvHistoryMinspeed;
    public TextView tvHistoryDate, tvHistoryActivity, tvHistoryDuration, tvHistoryTask, tvHistoryDif, tvHistoryTimeTrained, tvHistoryDistance, tvHistorySpeed, tvHistoryPace, tvHistoryART, tvHistoryAccuracy;
    public ImageView imgRoute;
    public Button btnDetailOk;

    // main entry dialog
    public Dialog diaryDialog;
    public Button btnTrainingDiary, btnMoti, btnRpe, btnNasa;

    // entry list dialog
    public Dialog trainingDiaryDialog, motiDialog, rpeDialog, nasaDialog;
    public Button btnTrainingDiaryAdd, btnMotiAdd, btnRpeAdd, btnNasaAdd;

    // diary add dialog
    public Dialog trainingDiaryAddDialog, motiAddDialog, rpeAddDialog, nasaAddDialog;
    public Button btnTrainingDiarySave, btnMotiSave, btnRpeSave, btnNasaSave;

    public DialogHelper(Context context) {
        init(context);
    }

    public void init(Context context) {
        pauseDialog = new Dialog(context);
        finishDialog = new Dialog(context);
        countdownDialog = new Dialog(context);
        lockDialog = new Dialog(context);
        detailDialog = new Dialog(context);
        diaryDialog = new Dialog(context);
        trainingDiaryAddDialog = new Dialog(context);
        motiAddDialog = new Dialog(context);
        rpeAddDialog = new Dialog(context);
        nasaAddDialog = new Dialog(context);
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

        setupDialog(trainingDiaryAddDialog, R.layout.dialog_diary_training_add);
        setupDialog(motiAddDialog, R.layout.dialog_diary_moti_add);
        setupDialog(rpeAddDialog, R.layout.dialog_diary_rpe_add);
        setupDialog(nasaAddDialog, R.layout.dialog_diary_nasa_add);

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
                dismissFinishDialog();
                MainActivity.showTaskFragment();
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

        btnTrainingDiary = diaryDialog.findViewById(R.id.btn_diary_trainingdiary);
        btnTrainingDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showTrainingDiaryDialog();
            }
        });

        btnMoti = diaryDialog.findViewById(R.id.btn_diary_moti);
        btnMoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showMotiDialog();
            }
        });

        btnRpe = diaryDialog.findViewById(R.id.btn_diary_rpe);
        btnRpe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showRpeDialog();
            }
        });

        btnNasa = diaryDialog.findViewById(R.id.btn_diary_nasa);
        btnNasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDiaryDialog();
                showNasaDialog();
            }
        });

        btnTrainingDiaryAdd = trainingDiaryDialog.findViewById(R.id.btn_diary_trainingdiary_add);
        btnTrainingDiaryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrainingDiaryAddDialog();
                dismissTrainingDiaryDialog();
            }
        });

        btnMotiAdd = motiDialog.findViewById(R.id.btn_diary_moti_add);
        btnMotiAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMotiAddDialog();
                dismissMotiDialog();
            }
        });

        btnRpeAdd = rpeDialog.findViewById(R.id.btn_diary_rpe_add);
        btnRpeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRpeAddDialog();
                dismissRpeDialog();
            }
        });

        btnNasaAdd = nasaDialog.findViewById(R.id.btn_diary_nasa_add);
        btnNasaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNasaAddDialog();
                dismissNasaDialog();
            }
        });

        btnTrainingDiarySave = trainingDiaryAddDialog.findViewById(R.id.btn_diary_trainingdiary_save);
        btnTrainingDiarySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissTrainingDiaryAddDialog();
            }
        });

        btnMotiSave = motiAddDialog.findViewById(R.id.btn_diary_moti_save);
        btnMotiSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMotiAddDialog();
            }
        });

        btnRpeSave = rpeAddDialog.findViewById(R.id.btn_diary_rpe_save);
        btnRpeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissRpeAddDialog();
            }
        });

        btnNasaSave = nasaAddDialog.findViewById(R.id.btn_diary_nasa_save);
        btnNasaSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissNasaAddDialog();
            }
        });

        tvFinishDuration = finishDialog.findViewById(R.id.tv_finish_duration);
        tvFinishDistance = finishDialog.findViewById(R.id.tv_finish_distance);
        tvFinishSpeed = finishDialog.findViewById(R.id.tv_finish_speed);
        tvFinishPace = finishDialog.findViewById(R.id.tv_finish_pace);
        tvFinishART = finishDialog.findViewById(R.id.tv_finish_art);
        tvFinishAccuracy = finishDialog.findViewById(R.id.tv_finish_accuracy);

        tvHistoryDate = detailDialog.findViewById(R.id.tv_history_date);
        tvHistoryActivity = detailDialog.findViewById(R.id.tv_history_activity);
        tvHistoryDuration = detailDialog.findViewById(R.id.tv_history_duration);
        tvHistoryTask = detailDialog.findViewById(R.id.tv_history_task);
        tvHistoryDif = detailDialog.findViewById(R.id.tv_history_dif);
        tvHistoryTimeTrained = detailDialog.findViewById(R.id.tv_history_time_trained);
        tvHistoryDistance = detailDialog.findViewById(R.id.tv_history_distance);
        tvHistorySpeed = detailDialog.findViewById(R.id.tv_history_speed);
        tvHistoryPace = detailDialog.findViewById(R.id.tv_history_pace);
        tvHistoryART = detailDialog.findViewById(R.id.tv_history_art);
        tvHistoryAccuracy = detailDialog.findViewById(R.id.tv_history_accuracy);


        tvHistoryNogo = detailDialog.findViewById(R.id.tv_history_nogo);
        tvHistoryInterval = detailDialog.findViewById(R.id.tv_history_interval);
        tvHistoryVolume = detailDialog.findViewById(R.id.tv_history_volume);
        tvHistoryNoise = detailDialog.findViewById(R.id.tv_history_noise);
        tvHistoryThreshold = detailDialog.findViewById(R.id.tv_history_threshold);
        tvHistoryMinspeed = detailDialog.findViewById(R.id.tv_history_minspeed);

        imgRoute = detailDialog.findViewById(R.id.img_history_route);
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

    public void showTrainingDiaryAddDialog() {
        trainingDiaryAddDialog.show();
    }

    public void dismissTrainingDiaryAddDialog() {
        trainingDiaryAddDialog.dismiss();
    }

    public void showMotiAddDialog() {
        motiAddDialog.show();
    }

    public void dismissMotiAddDialog() {
        motiAddDialog.dismiss();
    }

    public void showRpeAddDialog() {
        rpeAddDialog.show();
    }

    public void dismissRpeAddDialog() {
        rpeAddDialog.dismiss();
    }

    public void showNasaAddDialog() {
        nasaAddDialog.show();
    }

    public void dismissNasaAddDialog() {
        nasaAddDialog.dismiss();
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
        tvFinishDuration.setText("Time Trained: " + DateHelper.getTimeFromMs(td.getTimeTrained()));
        tvFinishDistance.setText("Distance: " + td.getDistance() + "KM");
        tvFinishSpeed.setText("Avg speed: " + td.getAvgSpeed() + "KM/H");
        tvFinishPace.setText("Avg pace: " + td.getAvgPace() + "MIN/KM");
        tvFinishART.setText("Avg Response time: " + td.getAvgResTime() + "ms");
        tvFinishAccuracy.setText("Accuracy: " + td.getAccuracy() + "%");
    }

    public void showHistoryDialog(TrainingData td) {
        setupHistoryDialog(td);
        detailDialog.show();
    }



    public void setupHistoryDialog(final TrainingData td) {

        tvHistoryDate.setText(DateHelper.getDateFromMili(td.getStartTime()) + " " + DateHelper.getTimeFromMili(td.getStartTime()));
        tvHistoryActivity.setText("Activity: " + td.getActivity());
        tvHistoryTask.setText("Task: " + td.getTask());
        tvHistoryDif.setText("Level: " + td.getDif());
        tvHistoryDuration.setText("Duration: " + DateHelper.getTimeFromMs(td.getDuration()));

        tvHistoryTimeTrained.setText("Time trained: " + DateHelper.getTimeFromMs(td.getTimeTrained()));
        tvHistoryDistance.setText("Distance: " + td.getDistance() + "km");
        tvHistorySpeed.setText("Avg speed: " + td.getAvgSpeed() + "km/h");
        tvHistoryPace.setText("Avg pace: " + td.getAvgPace() + "min/km");
        tvHistoryART.setText("Avg res time: " + td.getAvgResTime() + "ms");
        tvHistoryAccuracy.setText("Accuracy: " + td.getAccuracy() + "%");

        if (td.getDif().equals("Custom")) {

            tvHistoryNogo.setText("NO-GO: " + td.getTaskConfig().getNogoPropotion() + "%");
            tvHistoryInterval.setText("Interval: " + td.getTaskConfig().getIntervalFrom() + "~" + td.getTaskConfig().getIntervalTo() + "s");
            tvHistoryVolume.setText("Tone vol: " + td.getTaskConfig().getVolumeFrom() * 100 + "~" +
                    "" + td.getTaskConfig().getVolumeTo() * 100 + "%");
            tvHistoryNoise.setText("Noise vol" +
                    ": " + td.getTaskConfig().getNoise() * 100 + "%");
            tvHistoryThreshold.setText("Valid res time: " + td.getTaskConfig().getResThreshold() + "ms");
            tvHistoryMinspeed.setText("Min speed: " + td.getTaskConfig().getMinSpeed() + "km/h");

        } else {
            tvHistoryNogo.setVisibility(View.GONE);
            tvHistoryInterval.setVisibility(View.GONE);
            tvHistoryVolume.setVisibility(View.GONE);
            tvHistoryNoise.setVisibility(View.GONE);
            tvHistoryThreshold.setVisibility(View.GONE);
            tvHistoryMinspeed.setVisibility(View.GONE);
        }

        if (td.getTask().equals("A-PVT")) {
            tvHistoryNogo.setVisibility(View.GONE);
        }

        // update route
        Bitmap bmImg = BitmapFactory.decodeFile(FileHelper.PATH_ROUTE_DATA + DateHelper.getDateTimeFromMili(td.getStartTime()) + ".png");
        imgRoute.setImageBitmap(bmImg);

        // update speed graph
        GraphView speedGraph = detailDialog.findViewById(R.id.graph_speed);
        speedGraph.removeAllSeries();
        ArrayList<Float> speedList = td.getSpeedList();
        DataPoint[] speedDataPoints = new DataPoint[speedList.size()];

        for (int i = 0; i < speedList.size(); i++) {
            speedDataPoints[i] = (new DataPoint(i, speedList.get(i)));
        }

        LineGraphSeries<DataPoint> speedSeries = new LineGraphSeries<>(speedDataPoints);
        speedSeries.setColor(Color.rgb(0, 133, 119));
        speedSeries.setDrawDataPoints(true);
        speedSeries.setDataPointsRadius(5);
        speedSeries.setThickness(2);
        speedGraph.addSeries(speedSeries);
        speedGraph.setTitle("Speed(km/h)");
        speedGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


        // update res speed time
        GraphView resGraph = detailDialog.findViewById(R.id.graph_res);
        resGraph.removeAllSeries();
        ArrayList<Long> resList = td.getResTimeList();
        DataPoint[] resDataPoints = new DataPoint[resList.size()];

        for (int i = 0; i < resList.size(); i++) {
            resDataPoints[i] = (new DataPoint(i, resList.get(i)));
        }

        BarGraphSeries<DataPoint> resSeries = new BarGraphSeries<>(resDataPoints);
        resSeries.setColor(Color.rgb(0, 133, 119));

        resGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        resGraph.addSeries(resSeries);
        resGraph.setTitle("Response time(ms)");
        resGraph.setPadding(50, 50, 50, 50);
    }
}


