package com.kent.lw.brainendurancetrainingmobileapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseHelper {

    private DatabaseReference db;

    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadAllData(String name, String task, String dif, List<Double> accXList, List<Double> accYList, List<Double> accZList,
                              List<Double> locLatList, List<Double> locLngList, List<Long> stiTimeList, List<Long> resTimeList) {
        db.child(name).setValue(name);
        db.child(name + "/" + task).setValue(task);
        db.child(name + "/" + dif).setValue(dif);
        db.child(name + "/" + "accX").setValue(accXList);
        db.child(name + "/" + "accY").setValue(accYList);
        db.child(name + "/" + "accZ").setValue(accZList);
        db.child(name + "/" + "locLat").setValue(locLatList);
        db.child(name + "/" + "locLng").setValue(locLngList);
        db.child(name + "/" + "stiTime").setValue(stiTimeList);
        db.child(name + "/" + "resTime").setValue(resTimeList);
    }
}
