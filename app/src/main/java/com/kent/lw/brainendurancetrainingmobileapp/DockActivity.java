package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class DockActivity extends AppCompatActivity implements taskCommunicator, trainingCommunicator {

    private FrameLayout container;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private TaskFragment taskFragment;
    private TrainingFragment trainingFragment;

    private Dialog pauseDialog, finishDialog, profileDialog;

    private Button btnResume, btnOK, btnBack;

    private ImageButton btnProfile, btnFlic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock);

        container = findViewById(R.id.container);

        taskFragment = new TaskFragment();
        trainingFragment = new TrainingFragment();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();

        //dialog
        pauseDialog = new Dialog(this);
        finishDialog = new Dialog(this);
        profileDialog = new Dialog(this);

        btnProfile = findViewById(R.id.btn_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog.setContentView(R.layout.dialog_profile);
                btnBack = profileDialog.findViewById(R.id.btn_back);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileDialog.dismiss();
                    }
                });
                profileDialog.show();
            }
        });




    }

    @Override
    public void startTraining() {
        // replace task fragment with training fragment
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.replace(R.id.container, trainingFragment, "TRAINING_FRAGMENT");
        transaction.commit();
        // start training

        // start  map

        // get parameters


    }

    @Override
    public void pauseTraining() {

        // show dialog
        pauseDialog.setContentView(R.layout.dialog_pause);

        btnResume = pauseDialog.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
                resumeTraining();
            }
        });
        pauseDialog.show();

        // resume handler

    }

    @Override
    public void finishTraining() {


        finishDialog.setContentView(R.layout.dialog_finish);

        btnOK = finishDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog.dismiss();
                showTaskFragment();
            }
        });

        finishDialog.show();

    }

    public void resumeTraining() {
    }

    public void showTaskFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.replace(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }
}
