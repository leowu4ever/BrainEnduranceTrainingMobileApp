package com.kent.lw.brainendurancetrainingmobileapp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firebaseHelper {

    // firebase
    public void uploadToFirebase() {
        // Firebase Test
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("TEdasdSTas/asdsad").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FIREBASE_TEST", "success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FIREBASE_TEST", "fail");
            }
        });
    }
}
