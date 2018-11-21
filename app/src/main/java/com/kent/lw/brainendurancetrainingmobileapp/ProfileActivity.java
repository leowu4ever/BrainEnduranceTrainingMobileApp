package com.kent.lw.brainendurancetrainingmobileapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

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
                Gson g = new Gson();

                String temp = readJsonFile(Environment.getExternalStorageDirectory() + FileHelper.PATH_TRAINING_DATA + fileTemp.getName()).replace("\\", "");
                temp = temp.substring(1, temp.length() - 1);
                TrainingData td = g.fromJson(temp, TrainingData.class);
                dh.showHistoryDialog(td);
            }
        });
    }

    private String readJsonFile(String path) {

        String readings = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));  // 2nd line
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String readinText = "";

            while ((readinText = br.readLine()) != null) {
                sb.append(readinText);
            }
            readings = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readings;
    }
}