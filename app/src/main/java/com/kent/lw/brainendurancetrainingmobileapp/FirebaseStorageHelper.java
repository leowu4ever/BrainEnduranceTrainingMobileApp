package com.kent.lw.brainendurancetrainingmobileapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseStorageHelper {

    public static void uploadAllFileToFirestorage() {
        uploadAFolderToFirestorage(FileHelper.PATH_TRAINING_DATA, "Training Data/");
        uploadAFolderToFirestorage(FileHelper.PATH_MOTION_DATA, "Motion Data/");
        uploadAFolderToFirestorage(FileHelper.PATH_OVERALL_DATA, "Overall Data/");
    }

    public static void uploadAFolderToFirestorage (String folderPath, String firestorageFolderName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        File f = new File(folderPath);
        File[] files = f.listFiles();

        // reads every file
        for (File file : files) {
            Uri uri = Uri.fromFile(new File(folderPath + file.getName()));
            StorageReference riversRef = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + firestorageFolderName + uri.getLastPathSegment());
            final UploadTask uploadTask = riversRef.putFile(uri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }
    }
}