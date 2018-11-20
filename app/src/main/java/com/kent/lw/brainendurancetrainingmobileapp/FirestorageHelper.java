package com.kent.lw.brainendurancetrainingmobileapp;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirestorageHelper {

    // great now upload files

    public static void uploadFiles() {

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app

        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + FileHelper.PATH_MOTION_DATA + "1542662918079_acc_.txt"));
        StorageReference riversRef = storageRef.child("Motion Data/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

                Log.d("storage","fail");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("storage","succ");

            }
        });
    }
}
