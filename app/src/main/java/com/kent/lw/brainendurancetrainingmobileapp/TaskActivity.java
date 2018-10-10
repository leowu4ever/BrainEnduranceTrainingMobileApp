package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class TaskActivity extends AppCompatActivity {
    private SeekBar sbRandomStimulusInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        SeekBar sbRandomStimulusInterval = findViewById(R.id.sb_random_stimulus_interval);
        sbRandomStimulusInterval.setMax(5);

        final int randomStimulusInterval = sbRandomStimulusInterval.getProgress();
        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskActivity.this, TrainingActivity.class);
                intent.putExtra("RANDOM_STIMULUS_INTERVAL", randomStimulusInterval);
                startActivity(intent);
                //Log.d("setsb", randomStimulusInterval + "");
            }
        });
    }
}
