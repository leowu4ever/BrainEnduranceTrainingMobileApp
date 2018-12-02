package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDBHelper {

    private DatabaseReference db;

    public FirebaseDBHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadAllData(TrainingData td, Task t) {

        String rootPath = td.getName() + "/" + DateHelper.getDateTimeFromMili(td.getId()) + "/";

        db.child(rootPath + "1_User Info" + "/" + "Name").setValue(td.getName());
        db.child(rootPath + "1_User Info" + "/" + "Id").setValue(td.getId());
        db.child(rootPath + "1_User Info" + "/" + "Start Time").setValue(td.getStartTime());
        db.child(rootPath + "1_User Info" + "/" + "Time").setValue(td.getTime());

        db.child(rootPath + "2_Training Configuration" + "/" + "Activity").setValue(td.getActivity());
        db.child(rootPath + "2_Training Configuration" + "/" + "Duration").setValue(td.getDuration());
        db.child(rootPath + "2_Training Configuration" + "/" + "Task").setValue(td.getTask());
        db.child(rootPath + "2_Training Configuration" + "/" + "Dif").setValue(td.getDif());
        db.child(rootPath + "2_Training Configuration" + "/" + "Dif Config").setValue(td.getTaskConfig());

        db.child(rootPath + "3_Cognitive Performance" + "/" + "Sti Count").setValue(td.getStiCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Res Count").setValue(td.getResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Hit Res Count").setValue(td.getHitResCount());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Accuracy").setValue(td.getAccuracy());
        db.child(rootPath + "3_Cognitive Performance" + "/" + "Avg Res Time").setValue(td.getAvgResTime());

        db.child(rootPath + "4_Physical Performance" + "/" + "Distance").setValue(td.getDistance());
        db.child(rootPath + "4_Physical Performance" + "/" + "Avg Speed").setValue(td.getAvgSpeed());
        db.child(rootPath + "4_Physical Performance" + "/" + "Avg Pace").setValue(td.getAvgPace());

        db.child(rootPath + "5_Stimulus Record" + "/" + "Sti Mili List").setValue(td.getStiMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "Res Mili List").setValue(td.getResMiliList());
        db.child(rootPath + "5_Stimulus Record" + "/" + "Res Time List").setValue(td.getResTimeList());

        db.child(rootPath + "6_Location" + "/" + "Lat").setValue(td.getLatList());
        db.child(rootPath + "6_Location" + "/" + "Lng").setValue(td.getLngList());

    }
}
