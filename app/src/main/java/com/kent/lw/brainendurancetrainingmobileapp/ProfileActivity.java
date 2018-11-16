package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
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
    private DialogHelper dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dh = new DialogHelper();
        dh.initDialog(this);

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

            final TextView tvDate = new TextView(this);
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

                    String temp = readJsonFile(Environment.getExternalStorageDirectory() + JsonHelper.STORAGE_PATH + fileTemp.getName()).replace("\\", "");
                    temp = temp.substring(1, temp.length() - 1);
                    String ori = "{\"accXList\":[],\"accYList\":[],\"accZList\":[],\"accuracy\":0.0,\"activity\":\"Walking\",\"avgPace\":41.166115,\"avgResTime\":0,\"avgSpeed\":1.4575094,\"dif\":\"Custom\",\"distance\":0.01093132,\"duration\":\"26 min\",\"gyroXList\":[],\"gyroYList\":[],\"gyroZList\":[],\"hitResCount\":15,\"id\":1542380328779,\"locLatList\":[51.2807545],\"locLngList\":[1.0802244],\"name\":\"lwu@kentacuk\",\"resCount\":54,\"resMiliList\":[1542380333652,1542380344225,1542380366076,1542380366129,1542380366253,1542380366352,1542380366401,1542380366514,1542380366538,1542380366569,1542380366593,1542380366607,1542380366620,1542380366639,1542380366653,1542380366670,1542380366687,1542380366731,1542380366755,1542380366773,1542380366787,1542380366804,1542380366822,1542380366838,1542380366896,1542380366910,1542380366918,1542380366938,1542380366952,1542380366971,1542380366988,1542380367004,1542380367022,1542380378884,1542380379863,1542380379878,1542380379894,1542380379909,1542380379942,1542380379969,1542380379978,1542380379996,1542380380011,1542380380028,1542380380044,1542380380061,1542380380078,1542380380094,1542380380130,1542380380143,1542380380162,1542380380177,1542380380517,1542380380575],\"resTimeList\":[843,406,7232,7285,7409,7508,7557,7670,7694,7725,7749,7763,7776,7795,7809,7826,7843,7887,7911,7929,7943,7960,7978,7994,8052,8066,8074,8094,8108,8127,8144,8160,8178,1019,1998,2,18,33,66,93,102,120,135,152,168,185,202,218,254,267,286,301,641,699],\"startTime\":1.54238032E12,\"stiCount\":9,\"stiMiliList\":[1542380332809,1542380343819,1542380350825,1542380352835,1542380358844,1542380368852,1542380377865,1542380379876,1542380505828],\"task\":\"A-PVT\",\"time\":50000,\"totalResTime\":4136}";
//                    Log.d("gsonstring", temp);
//                    Log.d("gsonstring", ori);

//                    Log.d("length", temp.length() + "  " + ori.length());
//                    if (temp.equals(ori)){
//                        Log.d("gsoncheck", "same");
//                    } else {
//                        Log.d("gson" +
//                                "check", "not same");
//                    }

                    TrainingData td = g.fromJson(temp, TrainingData.class);

                    dh.setupFinishDialog(td);
                    dh.showFinishDialog();
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
        FileInputStream fis = null;
        try {
            fis = new FileInputStream (new File(jsonPath));  // 2nd line
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text = "";

            while ( (text = br.readLine()) != null ) {
                sb.append(text);
            }
            readings = sb.toString();
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {
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
