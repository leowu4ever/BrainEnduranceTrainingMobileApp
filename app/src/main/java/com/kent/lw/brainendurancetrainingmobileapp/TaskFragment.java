package com.kent.lw.brainendurancetrainingmobileapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    private Button btnStart;
    private Spinner spinnerTask, spinnerDif;

    private taskCommunicator taskCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        taskCommunicator = (taskCommunicator) getActivity();

        btnStart = getActivity().findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskCommunicator.startTraining();
            }
        });

        spinnerTask = getActivity().findViewById(R.id.spinner_task);
        spinnerDif = getActivity().findViewById(R.id.spinner_dif);


    }
}