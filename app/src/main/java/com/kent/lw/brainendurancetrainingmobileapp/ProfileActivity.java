package com.kent.lw.brainendurancetrainingmobileapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    private Button btnHistoryOk;
    private File fileTemp;
    private DialogHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dh = new DialogHelper();
        dh.initDialog(this);

        btnHistoryOk = findViewById(R.id.btn_back);
        btnHistoryOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initHistory();
        // init overall
        // read in overall performance json
        // convert to class object
        initOverall();
    }

    private void initOverall() {

        // check if there is an overall file
        // if no then 0% for both
        // otherwise display the relative result


    }

    private void initHistory() {
        File f = new File(Environment.getExternalStorageDirectory() + FileHelper.PATH_TRAINING_DATA);
        File[] files = f.listFiles();
        // reads every file
        for (File file : files) {
            fileTemp = file;
            createUIs(file);
        }
    }

    private void createUIs(File file) {

        // history layout container
        LinearLayout parentLayout = findViewById(R.id.linear_layout_history);
        LinearLayout containerLayout = new LinearLayout(this);
        parentLayout.addView(containerLayout);

        // date tv  1111-11-11@11:11:11
        final TextView tvDate = new TextView(this);
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
        Button btnDetail = new Button(this);
        buttonLayout.addView(btnDetail);

        // button container
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 90);
        params.setMargins(2, 2, 2, 2);
        params.gravity = Gravity.CENTER;
        btnDetail.setLayoutParams(params);
        btnDetail.setTextColor(Color.parseColor("#ffffff"));

        // button to launch a history detail dialog
        btnDetail.setBackgroundResource(R.drawable.button_rounded_corner);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingData td = FileHelper.readTrainingDataFromLocal(fileTemp.getName());
                dh.showHistoryDialog(td);
            }
        });
    }
}