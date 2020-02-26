package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MemoryTaskFragment extends Fragment {
    private Button btn_submit ;
    private TextView subject, tv_scoring;
    private MemoryCommunicator memoryCommunicator;
    private MainActivity mainact;
    private ArrayList<CheckBox> createdCheckBox = new ArrayList<CheckBox>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainact = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_memory_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIs();

    }

    private void initUIs() {
        createtask();
        memoryCommunicator = (MemoryCommunicator) getActivity();
        btn_submit = getActivity().findViewById(R.id.btn_submit);
        btn_submit.setText("Submit");
        tv_scoring = getActivity().findViewById(R.id.tv_scoring);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scoreCount = 0;
                if (btn_submit.getText().equals("Submit")) {
                    for (CheckBox cb : createdCheckBox) {
                        if (mainact.getAnnoncedWordList().contains(cb.getText().toString()) && cb.isChecked()) {
                            cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            scoreCount++;
                        } else if (mainact.getAnnoncedWordList().contains(cb.getText().toString()) && !(cb.isChecked())) {
                            cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.s8)));
                        } else if (!(mainact.getAnnoncedWordList().contains(cb.getText().toString())) && cb.isChecked()){
                            cb.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.s8)));
                        }
                        cb.setEnabled(false);
                    }
                    String scoringTvText = "You score " + scoreCount + " / " + mainact.getChosenWordList().size();
                    tv_scoring.setText(scoringTvText);
                    btn_submit.setText("Finish");
                } else if (btn_submit.getText().equals("Finish")) {
                    memoryCommunicator.finishMemoryTask();
                }
            }
        });

    }

    private void createtask() {
        LinearLayout parentLayout = getActivity().findViewById(R.id.linear_layout_selection);
        parentLayout.removeAllViews();
        createdCheckBox.clear();
        for (String str : FileHelper.getavailablememorytask()) {
            CheckBox checBox = new CheckBox(getActivity());
            String textLabel = str.replace("memorytask_", "").substring(0, 1).toUpperCase() + str.substring(12);
            checBox.setText(textLabel);
            parentLayout.addView(checBox);
            createdCheckBox.add(checBox);
        }

    }

}

