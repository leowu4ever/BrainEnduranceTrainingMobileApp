package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseDBHelper {

    public static DatabaseReference db;
    public static String rootPath;

    public FirebaseDBHelper() {

    }

    public static void uploadAllData() {
        db = FirebaseDatabase.getInstance().getReference();
        rootPath = MainActivity.trainingData.getName() + "/" + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getStartTime()) + "/";

        db.child(rootPath + "1_User Info" + "/" + "1_Name").setValue(MainActivity.trainingData.getName());
        db.child(rootPath + "1_User Info" + "/" + "2_Start time").setValue(MainActivity.trainingData.getStartTime());
        db.child(rootPath + "1_User Info" + "/" + "3_Time Trained").setValue(MainActivity.trainingData.getTimeTrained());

        db.child(rootPath + "2_Training Configuration" + "/" + "1_Activity").setValue(MainActivity.trainingData.getActivity());
        db.child(rootPath + "2_Training Configuration" + "/" + "2_Duration").setValue(MainActivity.trainingData.getDuration());
        db.child(rootPath + "2_Training Configuration" + "/" + "3_Task").setValue(MainActivity.trainingData.getTask());
        db.child(rootPath + "2_Training Configuration" + "/" + "4_Difficulty level").setValue(MainActivity.trainingData.getDif());
        db.child(rootPath + "2_Training Configuration" + "/" + "5_Difficulty configuration").setValue(MainActivity.trainingData.getTaskConfig());

        db.child(rootPath + "3_Cognitive Performance" + "/" + "1_Stimulus").setValue(MainActivity.trainingData.getStiCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "2_Nogo").setValue(MainActivity.trainingData.getNogoCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "3_Responses").setValue(MainActivity.trainingData.getResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "4_Hits").setValue(MainActivity.trainingData.getHitResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "5_Lapses").setValue(MainActivity.trainingData.getLapseCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "6_Accuracy").setValue(MainActivity.trainingData.getAccuracy());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "7_Average response time").setValue(MainActivity.trainingData.getAvgResTime());

        db.child(rootPath + "4_Physical Performance" + "/" + "1_Distance").setValue(MainActivity.trainingData.getDistance());
        db.child(rootPath + "4_Physical Performance" + "/" + "2_Average speed").setValue(MainActivity.trainingData.getAvgSpeed());
        db.child(rootPath + "4_Physical Performance" + "/" + "3_Average pace").setValue(MainActivity.trainingData.getAvgPace());
        db.child(rootPath + "4_Physical Performance" + "/" + "4_Speed list").setValue(MainActivity.trainingData.getSpeedList());

        db.child(rootPath + "5_Stimulus Record" + "/" + "1_Stimulus mili list").setValue(MainActivity.trainingData.getStiMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "2_Responses mili list").setValue(MainActivity.trainingData.getResMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "3_Responses time list").setValue(MainActivity.trainingData.getResTimeList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "4_Stimulus type list").setValue(MainActivity.trainingData.getStiTypeList());

        db.child(rootPath + "6_Location" + "/" + "1_Latitude list").setValue(MainActivity.trainingData.getLatList());
        db.child(rootPath + "6_Location" + "/" + "2_Longitude list").setValue(MainActivity.trainingData.getLngList());
    }

    public static void updateStorageRef(ArrayList<String> fileList) {
        db = FirebaseDatabase.getInstance().getReference();
        db.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + "storageRef").setValue(fileList);
    }
}
