package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    // btn default text
    private final String btnDefaultText = "Please select";
    private TaskCommunicator taskCommunicator;
    // fragment
    private Button btnStart;
    // task dialog
    private Dialog taskDialog;
    private Button btnTask, btnAPVT, btnGonono, btnLang, btnVisual, btnMemory, btnHelpApvt, btnHelpGonogo, btnHelpMemory, btnTaskBack;
    // dif dialog
    private Dialog difDialog;
    private Button btnDif, btnEasy, btnMedium, btnHard, btnAdaptive, btnCustom, btnDifBack;
    // dif prompt dialog
    private Dialog promptDialog;
    private Button btnDifPromptOk;
    private TextView tvPrompt;
    // activity dialog
    private Dialog activityDialog;
    private Button btnWalking, btnMarching, btnRunning, btnActivity, btnActivityBack;
    // duration dialog
    private Dialog durationDialog;
    private Button btnDuration, btnDurationSave, btnDurationBack;
    private RangeBar rbTaskDuration;
    // help dialog
    private Dialog helpApvtDialog, helpGonogoDialog, helpMemoryDialog;
    private Button btnHelpApvtOK, btnHelpGonogoOK, btnHelpMemoryOK;
    // apvt custom dialog
    private Dialog difCustomAPVTDialog;
    private Button btnCustomSaveApvt, btnCustomBackApvt;
    private RangeBar rbIntervalApvt, rbVolumeApvt, rbNoiseApvt, rbThresholdApvt, rbMinspeedApvt;
    private Spinner spNoiseTypeApvt;
    //gonogo dialog
    private Dialog difCustomGonogoDialog;
    private Button btnCustomSaveGonogo, btnCustomBackGonogo;
    private RangeBar rbNogo, rbIntervalGonogo, rbVolumeGonogo, rbNoiseGonogo, rbThresholdGonogo, rbMinspeedGonogo;
    private Spinner spNoiseTypeGonogo;
    //memory switch dialog
    private Dialog memorySwitchDialog;
    private Button btnMemoryTestConfirm;
    private RadioGroup rgMemoryTest;
    private int chosenWordSet;
    private ArrayList wordList1, wordList2, memoryChosenWordList;
    //visual stimulus dialog
    private Dialog visualStimulusDialog;
    private ImageView ivStimulus;
    private RadioGroup rgVisualStimulus;
    private Button btnVisualStimulusConfirm;
    private String visualChosenStimulus;

    Random rand = new Random();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        wordList1 = new ArrayList<String>();
        wordList2 = new ArrayList<String>();
        //memoryChosenWordList = new ArrayList<String>();
        super.onActivityCreated(savedInstanceState);
        initFragBtns();
        initDialogs();
        initAPVTRbs();
        initGonogoRbs();
        initHelpBtns();
    }

    private void initFragBtns() {
        taskCommunicator = (TaskCommunicator) getActivity();

        btnTask = getActivity().findViewById(R.id.btn_task);
        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDialog.show();
            }
        });

        btnDif = getActivity().findViewById(R.id.btn_dif);
        btnDif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnTask.getText().equals(btnDefaultText)) {
                    difDialog.show();
                } else {
                    tvPrompt.setText("Please select a cognitive task.");
                    promptDialog.show();
                }
            }
        });

        btnActivity = getActivity().findViewById(R.id.btn_activity);
        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDialog.show();
            }
        });

        btnDuration = getActivity().findViewById(R.id.btn_duration);
        btnDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationDialog.show();
            }
        });
        btnStart = getActivity().findViewById(R.id.btn_start);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnTask.getText().equals(btnDefaultText) && !btnDuration.getText().equals(btnDefaultText) && !btnActivity.getText().equals(btnDefaultText) && !btnDif.getText().equals(btnDefaultText)) {

                    MainActivity.trainingData.setActivity(btnActivity.getText() + "");
                    MainActivity.trainingData.setTask(btnTask.getText() + "");
                    MainActivity.trainingData.setDif(btnDif.getText() + "");
                    MainActivity.trainingData.setTaskConfig(MainActivity.task);

                    if (btnTask.getText().equals("A-PVT")) {
                        MainActivity.trainingData.getTaskConfig().setNogoProportion(0);
                    }


                    taskCommunicator.startTraining();


                } else {
                    promptDialog.show();
                    tvPrompt.setText("Please complete all sections.");
                }
            }
        });
    }

    private void initDialogs() {
        taskDialog = new Dialog(getActivity());
        difDialog = new Dialog(getActivity());
        difCustomAPVTDialog = new Dialog(getActivity());
        difCustomGonogoDialog = new Dialog(getActivity());
        activityDialog = new Dialog(getActivity());
        durationDialog = new Dialog(getActivity());
        helpApvtDialog = new Dialog(getActivity());
        helpGonogoDialog = new Dialog(getActivity());
        helpMemoryDialog = new Dialog(getActivity());
        promptDialog = new Dialog(getActivity());
        memorySwitchDialog = new Dialog(getActivity());
        visualStimulusDialog = new Dialog(getActivity());

        setupDialog(taskDialog, R.layout.dialog_task);
        setupDialog(difDialog, R.layout.dialog_dif);
        setupDialog(difCustomAPVTDialog, R.layout.dialog_dif_custom_apvt);
        setupDialog(difCustomGonogoDialog, R.layout.dialog_dif_custom_gonogo);
        setupDialog(activityDialog, R.layout.dialog_activity);
        setupDialog(durationDialog, R.layout.dialog_duration);
        setupDialog(promptDialog, R.layout.dialog_prompt);

        setupDialog(helpApvtDialog, R.layout.dialog_help_apvt);
        setupDialog(helpGonogoDialog, R.layout.dialog_help_gonogo);
        setupDialog(helpMemoryDialog, R.layout.dialog_help_memory);

        setupDialog(memorySwitchDialog, R.layout.dialog_memory_switch);
        setupDialog(visualStimulusDialog, R.layout.dialog_visual_stimulus_switch);

        initActivityBtns();
        initTaskBtns();
        initDifBtns();
        initDurationBtns();
        initCustomSaveBtns();

        tvPrompt = promptDialog.findViewById(R.id.tv_prompt);
        btnDifPromptOk = promptDialog.findViewById(R.id.btn_dif_prompt_ok);
        btnDifPromptOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        });
    }

    private void setupDialog(Dialog d, int layout) {
        d.setContentView(layout);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    private void initDurationBtns() {

        rbTaskDuration = durationDialog.findViewById(R.id.rb_task_duration);

        btnDurationSave = durationDialog.findViewById(R.id.btn_duration_save);
        btnDurationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update duration
                MainActivity.trainingData.setDuration(Integer.parseInt(rbTaskDuration.getRightPinValue()) * 60 * 1000);
                durationDialog.dismiss();
                btnDuration.setText(rbTaskDuration.getRightPinValue() + " min");
            }
        });

        btnDurationBack = durationDialog.findViewById(R.id.btn_duration_back);
        btnDurationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                durationDialog.dismiss();
            }
        });
    }

    private void initCustomSaveBtns() {
        btnCustomSaveApvt = difCustomAPVTDialog.findViewById(R.id.btn_apvt_save);
        btnCustomSaveApvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // no need to set nogo proportion
                MainActivity.task.setNogoProportion(0);
                int intervalFrom = Integer.parseInt(rbIntervalApvt.getLeftPinValue());
                int intervalTo = Integer.parseInt(rbIntervalApvt.getRightPinValue());
                float volumeFrom = (Float.parseFloat(rbVolumeApvt.getLeftPinValue()) / 100);
                float volumeTo = (Float.parseFloat(rbVolumeApvt.getRightPinValue()) / 100);
                float noise = (Float.parseFloat(rbNoiseApvt.getRightPinValue()) / 100);
                int noiseType = spNoiseTypeApvt.getSelectedItemPosition();
                int resThreshold = Integer.parseInt(rbThresholdApvt.getRightPinValue());
                float minSpeed = Float.parseFloat(rbMinspeedApvt.getRightPinValue());

                MainActivity.task.setupForCustom(0, intervalFrom, intervalTo, volumeFrom, volumeTo, noise, noiseType, resThreshold, minSpeed);

                difCustomAPVTDialog.dismiss();
                btnDif.setText(btnCustom.getText());
            }
        });

        btnCustomBackApvt = difCustomAPVTDialog.findViewById(R.id.btn_apvt_back);
        btnCustomBackApvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomAPVTDialog.dismiss();
            }
        });

        btnCustomSaveGonogo = difCustomGonogoDialog.findViewById(R.id.btn_gonogo_save);
        btnCustomSaveGonogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // no need to set nogo proportion
                int nogoProportion = Integer.parseInt(rbNogo.getRightPinValue());
                int intervalFrom = Integer.parseInt(rbIntervalGonogo.getLeftPinValue());
                int intervalTo = Integer.parseInt(rbIntervalGonogo.getRightPinValue());
                float volumeFrom = (Float.parseFloat(rbVolumeGonogo.getLeftPinValue()) / 100);
                float volumeTo = (Float.parseFloat(rbVolumeGonogo.getRightPinValue()) / 100);
                float noise = (Float.parseFloat(rbNoiseGonogo.getRightPinValue()) / 100);
                int noiseType = spNoiseTypeGonogo.getSelectedItemPosition();
                int resThreshold = Integer.parseInt(rbThresholdGonogo.getRightPinValue());
                float minSpeed = Float.parseFloat(rbMinspeedGonogo.getRightPinValue());

                MainActivity.task.setupForCustom(nogoProportion, intervalFrom, intervalTo, volumeFrom, volumeTo, noise, noiseType, resThreshold, minSpeed);

                difCustomGonogoDialog.dismiss();
                btnDif.setText(btnCustom.getText());
            }
        });

        btnCustomBackGonogo = difCustomGonogoDialog.findViewById(R.id.btn_gonogo_back);
        btnCustomBackGonogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difCustomGonogoDialog.dismiss();
            }
        });
    }

    private void initDifBtns() {
        btnEasy = difDialog.findViewById(R.id.btn_easy);
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnEasy.getText());
                difDialog.dismiss();

                if (btnTask.getText().equals("A-PVT")) {
                    MainActivity.task.setupForApvtEasy();
                } else if (btnTask.getText().equals("GO/NO-GO") || btnTask.getText().equals("Language")) {
                    MainActivity.task.setupForGonogoEasy();
                }
            }
        });

        btnMedium = difDialog.findViewById(R.id.btn_medium);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnMedium.getText());
                difDialog.dismiss();

                if (btnTask.getText().equals("A-PVT")) {
                    MainActivity.task.setupForApvtMedium();
                } else if (btnTask.getText().equals("GO/NO-GO") || btnTask.getText().equals("Language")) {
                    MainActivity.task.setupForGonogoMedium();
                }
            }
        });

        btnHard = difDialog.findViewById(R.id.btn_hard);
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnHard.getText());
                difDialog.dismiss();

                if (btnTask.getText().equals("A-PVT")) {
                    MainActivity.task.setupForApvtHard();
                } else if (btnTask.getText().equals("GO/NO-GO") || btnTask.getText().equals("Language")) {
                    MainActivity.task.setupForGonogoHard();
                }
            }
        });

        btnAdaptive = difDialog.findViewById(R.id.btn_adaptive);
        btnAdaptive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDif.setText(btnAdaptive.getText());
                difDialog.dismiss();

                if (btnTask.getText().equals("A-PVT")) {
                    MainActivity.task.setupForApvtEasy();
                } else if (btnTask.getText().equals("GO/NO-GO") || btnTask.getText().equals("Language")) {
                    MainActivity.task.setupForGonogoEasy();
                }
            }
        });

        btnCustom = difDialog.findViewById(R.id.btn_custom);
        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnTask.getText().equals(btnAPVT.getText())) {
                    difDialog.dismiss();
                    difCustomAPVTDialog.show();
                }

                if (btnTask.getText().equals(btnGonono.getText()) || btnTask.getText().equals("Language")) {
                    difDialog.dismiss();
                    difCustomGonogoDialog.show();
                }
            }
        });

        btnDifBack = difDialog.findViewById(R.id.btn_dif_back);
        btnDifBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difDialog.dismiss();
            }
        });
    }

    private void initTaskBtns() {
        btnAPVT = taskDialog.findViewById(R.id.btn_apvt);
        btnAPVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnAPVT.getText());
                taskDialog.dismiss();
            }
        });

        btnGonono = taskDialog.findViewById(R.id.btn_gonogo);
        btnGonono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnGonono.getText());
                taskDialog.dismiss();
            }
        });

        btnLang = taskDialog.findViewById(R.id.btn_language);
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTask.setText(btnLang.getText());
                taskDialog.dismiss();
            }
        });

        btnVisual = taskDialog.findViewById(R.id.btn_visual);
        btnVisual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.trainingData.setActivity("-");
                MainActivity.trainingData.setTask("-");
                MainActivity.trainingData.setDif("-");
                MainActivity.trainingData.setTaskConfig(MainActivity.task);

                btnTask.setText(btnVisual.getText());
                taskDialog.dismiss();

                MainActivity.trainingData.setTask("Visual");

                // hard code visual task configuration
                rbTaskDuration = durationDialog.findViewById(R.id.rb_task_duration);

                MainActivity.trainingData.setDuration(Integer.parseInt(rbTaskDuration.getRightPinValue()) * 60 * 1000);


                visualStimulusDialog.show();
            }
        });


        ivStimulus = visualStimulusDialog.findViewById(R.id.vs_Iv);
        ivStimulus.setImageResource(R.drawable.sti_bullseye);


        rgVisualStimulus = visualStimulusDialog.findViewById(R.id.rg_vs);
        rgVisualStimulus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rgVisualStimulus.findViewById(checkedId);
                switch (rgVisualStimulus.indexOfChild(radioButton)) {
                    case 0:
                        ivStimulus.setImageResource(R.drawable.sti_bullseye);
                        visualChosenStimulus = "sti_bullseye";
                        break;
                    case 1:
                        ivStimulus.setImageResource(R.drawable.sti_dummy);
                        visualChosenStimulus = "sti_dummy";
                        break;
                }
            }
        });


        btnVisualStimulusConfirm = visualStimulusDialog.findViewById(R.id.btn_vsConfirm);
        btnVisualStimulusConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (visualChosenStimulus == null) {
                    Toast.makeText(getActivity(), "Please chose a target to proceed", Toast.LENGTH_LONG).show();
                } else {
                    visualStimulusDialog.dismiss();
                    MainActivity.trainingData.setTask("Visual");
                    // start countdown and duration
                    taskCommunicator.startVisualTraining();
                }
            }
        });


        //init button listener
        btnMemory = taskDialog.findViewById(R.id.btn_memory);
        btnMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rand = new Random();
                MainActivity.trainingData.setActivity("-");
                MainActivity.trainingData.setTask("-");
                MainActivity.trainingData.setDif("-");
                MainActivity.trainingData.setTaskConfig(MainActivity.task);

                chosenWordSet = 1;

                ArrayList<String> wordList = FileHelper.getavailablememorytask();//get workable word list from file helper
                if (wordList.size()>=4){
                    int randomIndex = rand.nextInt((wordList.size() - 3) + 1) + 2;//get random number of chosen words
                    wordList1 = generateWordSet(wordList, (randomIndex));//generate wordlists
                    wordList2 = generateWordSet(wordList, (randomIndex));

                    btnTask.setText(btnMemory.getText());
                    taskDialog.dismiss();
                    MainActivity.trainingData.setDuration(Integer.parseInt(rbTaskDuration.getRightPinValue()) * 60 * 1000);

                    setAvailableWordRow(wordList1);//set default wordlist to list 1
                    memorySwitchDialog.show();
                }else{Toast.makeText(getActivity(), "No memory task available at this moment", Toast.LENGTH_LONG).show();}
            }
        });

        rgMemoryTest = memorySwitchDialog.findViewById(R.id.wordlistradiogroup);
        rgMemoryTest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {//if radiogroup clicked and changed
                View radioButton = rgMemoryTest.findViewById(checkedId);//get clicked radiobutton
                switch (rgMemoryTest.indexOfChild(radioButton)) {//get index of clicked radiobutton
                    case 0:
                        chosenWordSet = 1;
                        setAvailableWordRow(wordList1);
                        memoryChosenWordList = (ArrayList<String>) wordList1.clone();
                        break;
                    case 1:
                        chosenWordSet = 2;
                        setAvailableWordRow(wordList2);
                        memoryChosenWordList = (ArrayList<String>) wordList2.clone();
                        break;
                }
            }
        });

        btnMemoryTestConfirm = memorySwitchDialog.findViewById(R.id.btn_wordListConfirm);
        btnMemoryTestConfirm.setOnClickListener(new View.OnClickListener() {//verify if a word list were chosen
            @Override
            public void onClick(View v) {
                memoryChosenWordList = (ArrayList<String>) wordList1.clone();
                if (chosenWordSet == 0) {
                    Toast.makeText(getActivity(), "Please chose a word list to proceed", Toast.LENGTH_LONG).show();
                } else if (chosenWordSet == 1 || chosenWordSet == 2) {
                    memorySwitchDialog.dismiss();
                    MainActivity.trainingData.setTask("Memory");
                    taskCommunicator.startMemoryTraining();
                }
            }
        });


        btnTaskBack = taskDialog.findViewById(R.id.btn_task_back);
        btnTaskBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskDialog.dismiss();
            }
        });
    }

    private void setAvailableWordRow(ArrayList<String> words) {
        LinearLayout parentLayout = memorySwitchDialog.findViewById(R.id.wordlistItem);
        parentLayout.removeAllViews();
        for (String s : words) {
            TextView tvItem = new TextView(memorySwitchDialog.getContext());
            tvItem.setText(s.replace("memorytask_", "").substring(0, 1).toUpperCase() + s.substring(12));
            parentLayout.addView(tvItem);
            ;
        }
    }

    private void initActivityBtns() {
        btnWalking = activityDialog.findViewById(R.id.btn_walking);
        btnWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivity.setText(btnWalking.getText());
                activityDialog.dismiss();
            }
        });

        btnRunning = activityDialog.findViewById(R.id.btn_running);
        btnRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivity.setText(btnRunning.getText());
                activityDialog.dismiss();
            }
        });

        btnMarching = activityDialog.findViewById(R.id.btn_marching);
        btnMarching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActivity.setText(btnMarching.getText());
                activityDialog.dismiss();
            }
        });

        btnActivityBack = activityDialog.findViewById(R.id.btn_activity_back);
        btnActivityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDialog.dismiss();
            }
        });
    }

    private void initAPVTRbs() {

        rbIntervalApvt = difCustomAPVTDialog.findViewById(R.id.rb_interval_apvt);
        rbVolumeApvt = difCustomAPVTDialog.findViewById(R.id.rb_volume_apvt);
        rbNoiseApvt = difCustomAPVTDialog.findViewById(R.id.rb_noise_apvt);
        spNoiseTypeApvt = difCustomAPVTDialog.findViewById(R.id.sp_noise_type_apvt);
        rbThresholdApvt = difCustomAPVTDialog.findViewById(R.id.rb_threshold_apvt);
        rbMinspeedApvt = difCustomAPVTDialog.findViewById(R.id.rb_minspeed_apvt);
    }

    private void initGonogoRbs() {

        rbNogo = difCustomGonogoDialog.findViewById(R.id.rb_nogo_gonogo);
        rbIntervalGonogo = difCustomGonogoDialog.findViewById(R.id.rb_interval_gonogo);
        rbVolumeGonogo = difCustomGonogoDialog.findViewById(R.id.rb_volume_gonogo);
        rbNoiseGonogo = difCustomGonogoDialog.findViewById(R.id.rb_noise_gonogo);
        spNoiseTypeGonogo = difCustomGonogoDialog.findViewById(R.id.sp_noise_type_gonogo);
        rbThresholdGonogo = difCustomGonogoDialog.findViewById(R.id.rb_threshold_gonogo);
        rbMinspeedGonogo = difCustomGonogoDialog.findViewById(R.id.rb_minspeed_gonogo);
    }

    private void initHelpBtns() {
        btnHelpApvt = taskDialog.findViewById(R.id.btn_help_apvt);
        btnHelpApvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpApvtDialog.show();
            }
        });
        btnHelpApvtOK = helpApvtDialog.findViewById(R.id.btn_help_apvt_ok);
        btnHelpApvtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpApvtDialog.dismiss();
            }
        });
        btnHelpGonogo = taskDialog.findViewById(R.id.btn_help_gonogo);
        btnHelpGonogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGonogoDialog.show();
            }
        });
        btnHelpGonogoOK = helpGonogoDialog.findViewById(R.id.btn_help_gonogo_ok);
        btnHelpGonogoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGonogoDialog.dismiss();
            }
        });
        btnHelpMemory = taskDialog.findViewById(R.id.btn_help_memory);
        btnHelpMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpMemoryDialog.show();
            }
        });
        btnHelpMemoryOK = helpMemoryDialog.findViewById(R.id.btn_help_memorytask_ok);
        btnHelpMemoryOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpMemoryDialog.dismiss();
            }
        });

    }

    public ArrayList<String> generateWordSet(ArrayList<String> list, int noofItems) {
        ArrayList<String> tempList = (ArrayList<String>) list.clone();//templist for word reductions avoid duplicates
        ArrayList<String> newList = new ArrayList<String>();//list for returning generate words
        for (int i = 0; i < noofItems; i++) {

            int randomIndex = rand.nextInt(tempList.size());//pick random index from size of list
            newList.add(tempList.get(randomIndex));//add new word to returning list by index
            tempList.remove(randomIndex);//remove chosen word
        }
        return newList;
    }

    public ArrayList<String> getChosenWordSet() {
        return memoryChosenWordList;
    }
}