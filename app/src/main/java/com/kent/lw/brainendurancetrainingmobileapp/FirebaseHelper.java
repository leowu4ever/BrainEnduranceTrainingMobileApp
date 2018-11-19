package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference db;

    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadAllData(TrainingData td, Task t) {

        db.child(td.getName() + "/" + td.getId() + "/" + "1_User Info" + "/" + "Name").setValue(td.getName());
        db.child(td.getName() + "/" + td.getId() + "/" + "1_User Info" + "/" + "Id").setValue(td.getId());
        db.child(td.getName() + "/" + td.getId() + "/" + "1_User Info" + "/" + "Start Time").setValue(td.getStartTime());
        db.child(td.getName() + "/" + td.getId() + "/" + "1_User Info" + "/" + "Time").setValue(td.getTime());

        db.child(td.getName() + "/" + td.getId() + "/" + "2_Training Configuration" + "/" + "Activity").setValue(td.getActivity());
        db.child(td.getName() + "/" + td.getId() + "/" + "2_Training Configuration" + "/" + "Duration").setValue(td.getDuration());
        db.child(td.getName() + "/" + td.getId() + "/" + "2_Training Configuration" + "/" + "Task").setValue(td.getTask());
        db.child(td.getName() + "/" + td.getId() + "/" + "2_Training Configuration" + "/" + "Dif").setValue(td.getDif());
        db.child(td.getName() + "/" + td.getId() + "/" + "2_Training Configuration" + "/" + "Dif").setValue(t.toString());

        db.child(td.getName() + "/" + td.getId() + "/" + "3_Cognitive Performance" + "/" + "Sti Count").setValue(td.getStiCount());
        db.child(td.getName() + "/" + td.getId() + "/" + "3_Cognitive Performance" + "/" + "Res Count").setValue(td.getResCount());
        db.child(td.getName() + "/" + td.getId() + "/" + "3_Cognitive Performance" + "/" + "Hit Res Count").setValue(td.getHitResCount());
        db.child(td.getName() + "/" + td.getId() + "/" + "3_Cognitive Performance" + "/" + "Accuracy").setValue(td.getAccuracy());
        db.child(td.getName() + "/" + td.getId() + "/" + "3_Cognitive Performance" + "/" + "Avg Res Time").setValue(td.getAvgResTime());

        db.child(td.getName() + "/" + td.getId() + "/" + "4_Physical Performance" + "/" + "Distance").setValue(td.getDistance());
        db.child(td.getName() + "/" + td.getId() + "/" + "4_Physical Performance" + "/" + "Avg Speed").setValue(td.getAvgSpeed());
        db.child(td.getName() + "/" + td.getId() + "/" + "4_Physical Performance" + "/" + "Avg Pace").setValue(td.getAvgPace());

        db.child(td.getName() + "/" + td.getId() + "/" + "5_Stimulus Record" + "/" + "Sti Mili List").setValue(td.getStiMiliList());
        db.child(td.getName() + "/" + td.getId() + "/" + "5_Stimulus Record" + "/" + "Res Mili List").setValue(td.getResMiliList());
        db.child(td.getName() + "/" + td.getId() + "/" + "5_Stimulus Record" + "/" + "Res Time List").setValue(td.getResTimeList());

        db.child(td.getName() + "/" + td.getId() + "/" + "6_Motion Sensors" + "/" + "Accelerometer" + "/" + "Acc X List").setValue(td.getAccXList());
        db.child(td.getName() + "/" + td.getId() + "/" + "6_Motion Sensors" + "/" + "Accelerometer" + "/" + "Acc Y List").setValue(td.getAccYList());
        db.child(td.getName() + "/" + td.getId() + "/" + "6_Motion Sensors" + "/" + "Accelerometer" + "/" + "Acc Z List").setValue(td.getAccZList());

        db.child(td.getName() + "/" + td.getId() + "/" + "6_Motion Sensors" + "/" + "Gyroscope" + "/" + "Gyro X List").setValue(td.getGyroXList());
        db.child(td.getName() + "/" + td.getId() + "/" + "6_Motion Sensors" + "/" + "Gyroscope" + "/" + "Gyro Y List").setValue(td.getGyroYList());
        db.child(td.getName() + "/" + td.getId() + "/" + "6_Motion Sensors" + "/" + "Gyroscope" + "/" + "Gyro Z List").setValue(td.getGyroZList());

        db.child(td.getName() + "/" + td.getId() + "/" + "7_Location" + "/" + "Lat").setValue(td.getLocLatList());
        db.child(td.getName() + "/" + td.getId() + "/" + "7_Location" + "/" + "Lng").setValue(td.getLocLngList());

        //db.child("lwu@kentacuk").removeValue();
    }

    public void upload(String content) {

    }

    public void upload(float content) {

    }
}
