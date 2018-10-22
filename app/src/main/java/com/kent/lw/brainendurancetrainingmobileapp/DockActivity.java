package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class DockActivity extends AppCompatActivity implements taskCommunicator, trainingCommunicator {

    private FrameLayout container;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private TaskFragment taskFragment;
    private TrainingFragment trainingFragment;


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

    }

    @Override
    public void finishTraining() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.replace(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }


}
