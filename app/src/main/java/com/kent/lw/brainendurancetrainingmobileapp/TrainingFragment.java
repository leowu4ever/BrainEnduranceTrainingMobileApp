package com.kent.lw.brainendurancetrainingmobileapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingFragment extends Fragment {

    private Button btnPause;
    private Button btnFinish;

    private trainingCommunicator trainingCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trainingCommunicator = (trainingCommunicator) getActivity();

        btnPause = getActivity().findViewById(R.id.btn_pause);

        btnFinish = getActivity().findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainingCommunicator.finishTraining();
            }
        });
    }
}
