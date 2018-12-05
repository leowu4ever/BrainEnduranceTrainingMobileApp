package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseStorageHelper {

    public static int successCount;
    public static int totalFileCount;

    public static void uploadAllFileToFirestorage(Context context) {
        setTotalFileCount();
        uploadAFolderToFirestorage(context, FileHelper.PATH_TRAINING_DATA, "Training Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_MOTION_DATA, "Motion Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_OVERALL_DATA, "Overall Data/");
    }

    public static void setTotalFileCount() {
        File f = new File(FileHelper.PATH_TRAINING_DATA);
        File[] files = f.listFiles();
        totalFileCount = totalFileCount + files.length;

        f = new File(FileHelper.PATH_MOTION_DATA);
        files = f.listFiles();
        totalFileCount = totalFileCount + files.length;

        f = new File(FileHelper.PATH_OVERALL_DATA);
        files = f.listFiles();
        totalFileCount = totalFileCount + files.length;

    }

    public static void uploadAFolderToFirestorage(final Context context, String folderPath, String firestorageFolderName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        File f = new File(folderPath);
        File[] files = f.listFiles();

        // reads every file
        for (File file : files) {
            Uri filePath = Uri.fromFile(new File(folderPath + file.getName()));
            StorageReference storagePath = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + firestorageFolderName + filePath.getLastPathSegment());
            final UploadTask uploadTask = storagePath.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    successCount++;
                    Toast.makeText(context, successCount + "/" +  totalFileCount + " Successful " , Toast.LENGTH_SHORT).show();
                    if(successCount == totalFileCount) {
                        Toast.makeText(context,  "Completed " , Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}