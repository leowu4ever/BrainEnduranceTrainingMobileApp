package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference db;

    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadAllData(TrainingData td) {
        db.child(td.getName() + "/" + td.getId() + "/" + "task").setValue(td.getTask());
        db.child(td.getName() + "/" + td.getId() + "/" + "dif").setValue(td.getDif());
        db.child(td.getName() + "/" + td.getId() + "/" + "accX").setValue(td.getAccXList());
        db.child(td.getName() + "/" + td.getId() + "/" + "accY").setValue(td.getAccYList());
        db.child(td.getName() + "/" + td.getId() + "/" + "accZ").setValue(td.getAccZList());
        db.child(td.getName() + "/" + td.getId() + "/" + "locLat").setValue(td.getLocLatList());
        db.child(td.getName() + "/" + td.getId() + "/" + "locLng").setValue(td.getLocLngList());
        db.child(td.getName() + "/" + td.getId() + "/" + "stiTime").setValue(td.getStiTimeList());
        db.child(td.getName() + "/" + td.getId() + "/" + "resTime").setValue(td.getResTimeList());


        //db.child("lwu@kentacuk").removeValue();
    }
}
