package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class FirebaseDBHelper {

    public static DatabaseReference db;
    public static String rootPath;

    public FirebaseDBHelper() {

    }

    public static void uploadTdToDb(TrainingData trainingData) {
        db = FirebaseDatabase.getInstance().getReference();
        rootPath = trainingData.getName() + "/" + DateHelper.getDateTimeFromMili(trainingData.getStartTime()) + "/";

        db.child(rootPath + "1_User Info" + "/" + "1_Name").setValue(trainingData.getName());
        db.child(rootPath + "1_User Info" + "/" + "2_Start time").setValue(trainingData.getStartTime());
        db.child(rootPath + "1_User Info" + "/" + "3_Time Trained").setValue(trainingData.getTimeTrained());

        db.child(rootPath + "2_Training Configuration" + "/" + "1_Activity").setValue(trainingData.getActivity());
        db.child(rootPath + "2_Training Configuration" + "/" + "2_Duration").setValue(trainingData.getDuration());
        db.child(rootPath + "2_Training Configuration" + "/" + "3_Task").setValue(trainingData.getTask());
        db.child(rootPath + "2_Training Configuration" + "/" + "4_Difficulty level").setValue(trainingData.getDif());
        db.child(rootPath + "2_Training Configuration" + "/" + "5_Difficulty configuration").setValue(trainingData.getTaskConfig());

        db.child(rootPath + "3_Cognitive Performance" + "/" + "1_Stimulus").setValue(trainingData.getStiCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "2_Nogo").setValue(trainingData.getNogoCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "3_Responses").setValue(trainingData.getResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "4_Hits").setValue(trainingData.getHitResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "5_Lapses").setValue(trainingData.getLapseCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "6_Accuracy").setValue(trainingData.getAccuracy());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "7_Average response time").setValue(trainingData.getAvgResTime());

        db.child(rootPath + "4_Physical Performance" + "/" + "1_Distance").setValue(trainingData.getDistance());
        db.child(rootPath + "4_Physical Performance" + "/" + "2_Average speed").setValue(trainingData.getAvgSpeed());
        db.child(rootPath + "4_Physical Performance" + "/" + "3_Average pace").setValue(trainingData.getAvgPace());
        db.child(rootPath + "4_Physical Performance" + "/" + "4_Speed list").setValue(trainingData.getSpeedList());

        db.child(rootPath + "5_Stimulus Record" + "/" + "1_Stimulus mili list").setValue(trainingData.getStiMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "2_Responses mili list").setValue(trainingData.getResMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "3_Responses time list").setValue(trainingData.getResTimeList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "4_Stimulus type list").setValue(trainingData.getStiTypeList());

        db.child(rootPath + "6_Location" + "/" + "1_Latitude list").setValue(trainingData.getLatList());
        db.child(rootPath + "6_Location" + "/" + "2_Longitude list").setValue(trainingData.getLngList());
    }

    public static void updateStorageRef(ArrayList<String> fileList) {
        db = FirebaseDatabase.getInstance().getReference();
        db.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + "storageRef").setValue(fileList);
    }


    public static void uploadTdFromLocToDb() {
        File f = new File(FileHelper.PATH_TRAINING_DATA);
        File[] files = f.listFiles();
        // reads every file
        for (int i = 0; i < files.length; i++) {
            TrainingData td = FileHelper.readTrainingDataFromLocal(files[i].getName());
            uploadTdToDb(td);
        }
    }

    public static void deleteTdFromDb() {
        db = FirebaseDatabase.getInstance().getReference();
        db.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/").removeValue();
    }

    public static void deleteStorageRef() {
        db = FirebaseDatabase.getInstance().getReference();
        db.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + "storageRef").removeValue();

    }
}
