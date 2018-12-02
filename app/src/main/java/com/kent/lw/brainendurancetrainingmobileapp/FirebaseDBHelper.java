package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDBHelper {

    public static  DatabaseReference db;

    public FirebaseDBHelper() {

    }

    public static void uploadAllData() {
        db = FirebaseDatabase.getInstance().getReference();
        String rootPath = MainActivity.trainingData.getName() + "/" + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getId()) + "/";

        db.child(rootPath + "1_User Info" + "/" + "Name").setValue(MainActivity.trainingData.getName());
        db.child(rootPath + "1_User Info" + "/" + "Id").setValue(MainActivity.trainingData.getId());
        db.child(rootPath + "1_User Info" + "/" + "Start Time").setValue(MainActivity.trainingData.getStartTime());
        db.child(rootPath + "1_User Info" + "/" + "Time").setValue(MainActivity.trainingData.getTime());

        db.child(rootPath + "2_Training Configuration" + "/" + "Activity").setValue(MainActivity.trainingData.getActivity());
        db.child(rootPath + "2_Training Configuration" + "/" + "Duration").setValue(MainActivity.trainingData.getDuration());
        db.child(rootPath + "2_Training Configuration" + "/" + "Task").setValue(MainActivity.trainingData.getTask());
        db.child(rootPath + "2_Training Configuration" + "/" + "Dif").setValue(MainActivity.trainingData.getDif());
        db.child(rootPath + "2_Training Configuration" + "/" + "Dif Config").setValue(MainActivity.trainingData.getTaskConfig());

        db.child(rootPath + "3_Cognitive Performance" + "/" + "Sti Count").setValue(MainActivity.trainingData.getStiCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Res Count").setValue(MainActivity.trainingData.getResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Hit Res Count").setValue(MainActivity.trainingData.getHitResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Accuracy").setValue(MainActivity.trainingData.getAccuracy());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Avg Res Time").setValue(MainActivity.trainingData.getAvgResTime());

        db.child(rootPath + "4_Physical Performance" + "/" + "Distance").setValue(MainActivity.trainingData.getDistance());
        db.child(rootPath + "4_Physical Performance" + "/" + "Avg Speed").setValue(MainActivity.trainingData.getAvgSpeed());
        db.child(rootPath + "4_Physical Performance" + "/" + "Avg Pace").setValue(MainActivity.trainingData.getAvgPace());

        db.child(rootPath + "5_Stimulus Record" + "/" + "Sti Mili List").setValue(MainActivity.trainingData.getStiMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "Res Mili List").setValue(MainActivity.trainingData.getResMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "Res Time List").setValue(MainActivity.trainingData.getResTimeList());

        db.child(rootPath + "6_Location" + "/" + "Lat").setValue(MainActivity.trainingData.getLatList());
        db.child(rootPath + "6_Location" + "/" + "Lng").setValue(MainActivity.trainingData.getLngList());
    }
}
