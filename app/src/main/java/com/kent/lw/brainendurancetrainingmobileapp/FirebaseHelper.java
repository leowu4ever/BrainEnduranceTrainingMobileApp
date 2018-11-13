package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference db;

    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadAllData(TrainingData td) {
        db.child(td.getName() + "/" + td.getId() + "/" + "Name").setValue(td.getName());
        db.child(td.getName() + "/" + td.getId() + "/" + "Id").setValue(td.getId());

        db.child(td.getName() + "/" + td.getId() + "/" + "Activity").setValue(td.getActivity());
        db.child(td.getName() + "/" + td.getId() + "/" + "Duration").setValue(td.getDuration());


        db.child(td.getName() + "/" + td.getId() + "/" + "Task").setValue(td.getTask());
        db.child(td.getName() + "/" + td.getId() + "/" + "Dif").setValue(td.getDif());

        db.child(td.getName() + "/" + td.getId() + "/" + "Sti Count").setValue(td.getStiCount());
        db.child(td.getName() + "/" + td.getId() + "/" + "Res Count").setValue(td.getResCount());
        db.child(td.getName() + "/" + td.getId() + "/" + "Hit Res Count").setValue(td.getHitResCount());

        db.child(td.getName() + "/" + td.getId() + "/" + "Sti Time List").setValue(td.getStiTimeList());
        db.child(td.getName() + "/" + td.getId() + "/" + "Res Time List").setValue(td.getResTimeList());

        db.child(td.getName() + "/" + td.getId() + "/" + "Distance").setValue(td.getDistance());
        db.child(td.getName() + "/" + td.getId() + "/" + "Avg Speed").setValue(td.getAvgSpeed());
        db.child(td.getName() + "/" + td.getId() + "/" + "Avg Pace").setValue(td.getAvgPace());
        db.child(td.getName() + "/" + td.getId() + "/" + "Start Time").setValue(td.getStartTime());
        db.child(td.getName() + "/" + td.getId() + "/" + "Time").setValue(td.getTime());

        db.child(td.getName() + "/" + td.getId() + "/" + "Acc X List").setValue(td.getAccXList());
        db.child(td.getName() + "/" + td.getId() + "/" + "Acc Y List").setValue(td.getAccYList());
        db.child(td.getName() + "/" + td.getId() + "/" + "Acc Z List").setValue(td.getAccZList());

        db.child(td.getName() + "/" + td.getId() + "/" + "Gyro X List").setValue(td.getGyroXList());
        db.child(td.getName() + "/" + td.getId() + "/" + "Gyro Y List").setValue(td.getGyroYList());
        db.child(td.getName() + "/" + td.getId() + "/" + "Gyro Z List").setValue(td.getGyroZList());

        db.child(td.getName() + "/" + td.getId() + "/" + "Lat").setValue(td.getLocLatList());
        db.child(td.getName() + "/" + td.getId() + "/" + "Lng").setValue(td.getLocLngList());


        //db.child("lwu@kentacuk").removeValue();
    }
}
