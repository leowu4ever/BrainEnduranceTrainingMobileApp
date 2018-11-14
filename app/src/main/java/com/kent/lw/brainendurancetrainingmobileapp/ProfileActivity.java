package com.kent.lw.brainendurancetrainingmobileapp;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    private Button btnBack;
    private File fileTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        File f = new File(Environment.getExternalStorageDirectory() + JsonHelper.STORAGE_PATH);
        File[] files = f.listFiles();


        // reads every file
        for (File file : files) {

            Log.d("filesss", file.getName() + "");
//
            fileTemp = file;

            LinearLayout parentLayout = findViewById(R.id.linear_layout_history);
            LinearLayout containerLayout = new LinearLayout(this);
            parentLayout.addView(containerLayout);

            TextView tvDate = new TextView(this);
            tvDate.setText(getDateFromMili(Long.parseLong(file.getName().replace(".json", ""))));
            tvDate.setGravity(Gravity.CENTER);
            tvDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            containerLayout.addView(tvDate);

            TextView tvTime = new TextView(this);
            tvTime.setText(getTimeFromMili(Long.parseLong(file.getName().replace(".json", ""))));
            tvTime.setGravity(Gravity.CENTER);
            tvTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            containerLayout.addView(tvTime);


            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            buttonLayout.setGravity(Gravity.CENTER);
            containerLayout.addView(buttonLayout);
            Button btnDetail = new Button(this);
            buttonLayout.addView(btnDetail);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(90, 90);
            params.setMargins(2, 2, 2, 2);
            params.gravity = Gravity.CENTER;
            btnDetail.setLayoutParams(params);
            btnDetail.setTextColor(Color.parseColor("#ffffff"));

            btnDetail.setBackgroundResource(R.drawable.button_rounded_corner);
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson g = new Gson();

                    String temp = readJsonFile(Environment.getExternalStorageDirectory() + JsonHelper.STORAGE_PATH + fileTemp.getName());
                    Log.d("gsongson-temp", temp);

                    String s = temp.substring(1, temp.length() - 1);

                    Log.d("gsongson-s", s);



                    TrainingData t = g.fromJson(s, TrainingData.class);
                    Log.d("gsongson-temp", temp);

                    Log.d("gsongson", t.toString());
                }
            });
        }
    }

    public static String getTimeFromMili(long miili)
    {
        Date date = new Date();
        date.setTime(miili);
        String formattedDate = new SimpleDateFormat("HH:mm:ss").format(date);
        return formattedDate;
    }

    public static String getDateFromMili(long mili)
    {
        Date date = new Date();
        date.setTime(mili);
        String formattedDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
        return formattedDate;
    }

    private String readJsonFile(String jsonPath) {

        String readings = "";
        FileInputStream inputStream = null;
        try {
             inputStream = new FileInputStream (new File(jsonPath));  // 2nd line

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                readings = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {

        }
        return readings;
    }
}
