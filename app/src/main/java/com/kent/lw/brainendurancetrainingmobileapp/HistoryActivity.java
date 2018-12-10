package com.kent.lw.brainendurancetrainingmobileapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class HistoryActivity extends AppCompatActivity {

    private Button btnHistoryOk, btnHistoryUpload;
    private TextView tvOverallAccuracy, tvOverallRT;
    private DialogHelper dh;
    private File[] files;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        hideStatusbar();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dh = new DialogHelper(this);

        initUI();
        initHistory();
        initOverall();
    }

    private void initUI() {
        btnHistoryOk = findViewById(R.id.btn_back);
        btnHistoryOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnHistoryUpload = findViewById(R.id.btn_upload);
        btnHistoryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorageHelper.uploadAllFileToFirestorage(HistoryActivity.this);
                // upload to db too

            }
        });

//        btnHistoryDelete = findViewById(R.id.btn_delete);
//        btnHistoryDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseStorageHelper.deleteAFolderToFirestorage(HistoryActivity.this);
//                onBackPressed();
//
//            }
//        });

        tvOverallAccuracy = findViewById(R.id.tv_overall_accuracy);
        tvOverallRT = findViewById(R.id.tv_overall_rt);
    }

    private void hideStatusbar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initOverall() {
        OverallData overallData = FileHelper.readOverallDataFromLocal();
        tvOverallAccuracy.setText(overallData.getOverallAccuracy() + "%");
        tvOverallRT.setText(overallData.getOverallRT() + "ms");
    }

    private void initHistory() {
        File f = new File(FileHelper.PATH_TRAINING_DATA);
        files = f.listFiles();
        // reads every file

        for (int i = 0; i < files.length; i++) {
            createHistoryRow(i, files[i]);
        }
    }

    private void createHistoryRow(int id, File file) {

        // history layout container
        LinearLayout parentLayout = findViewById(R.id.linear_layout_history);
        LinearLayout containerLayout = new LinearLayout(this);
        parentLayout.addView(containerLayout);

        // date tv  1111-11-11@11:11:11
        TextView tvDate = new TextView(this);
        tvDate.setText(file.getName().subSequence(0, 10));
        tvDate.setGravity(Gravity.CENTER);
        tvDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        containerLayout.addView(tvDate);

        // time tv
        TextView tvTime = new TextView(this);
        tvTime.setText(file.getName().subSequence(11, 19));
        tvTime.setGravity(Gravity.CENTER);
        tvTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        containerLayout.addView(tvTime);

        // horizontal layout containing everything
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        buttonLayout.setGravity(Gravity.CENTER);
        containerLayout.addView(buttonLayout);
        final Button btnDetail = new Button(this);
        buttonLayout.addView(btnDetail);

        // button container
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 90);
        params.setMargins(2, 2, 2, 2);
        params.gravity = Gravity.CENTER;
        btnDetail.setLayoutParams(params);
        btnDetail.setText(">");
        btnDetail.setPadding(2, 2, 2, 2);

        btnDetail.setTextColor(Color.parseColor("#ffffff"));

        // button to launch a history detail dialog
        btnDetail.setBackgroundResource(R.drawable.button_rounded_corner);
        btnDetail.setId(id);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingData td = FileHelper.readTrainingDataFromLocal(files[btnDetail.getId()].getName());
                dh.showHistoryDialog(td);
            }
        });
    }
}