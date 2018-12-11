package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class FirebaseStorageHelper {

    public static int uploadCount;
    public static int deleteCount;

    public static int totalUploadCount;
    public static int totalDeleteCount;

    public static ArrayList<String> fileList = new ArrayList<>();


    public static void uploadAllFileToFirestorage(Context context) {
        uploadCount = 0;
        totalUploadCount = 0;
        setTotalFileCount();
        FirebaseDBHelper.deleteStorageRef();
        uploadAFolderToFirestorage(context, FileHelper.PATH_TRAINING_DATA, "Training Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_MOTION_DATA, "Motion Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_OVERALL_DATA, "Overall Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_ROUTE_DATA, "Route Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_TRAININGDIARY_DATA, "Training Diary Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_MOTI_DATA, "Feedback Data/Motivation Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_RPE_DATA, "Feedback Data/Rpe Data/");
        uploadAFolderToFirestorage(context, FileHelper.PATH_NASA_DATA, "Feedback Data/Nasa Data/");

    }

    public static void setTotalFileCount() {
        File f = new File(FileHelper.PATH_TRAINING_DATA);
        File[] files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_MOTION_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_OVERALL_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_ROUTE_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_TRAININGDIARY_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_MOTI_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_RPE_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

        f = new File(FileHelper.PATH_NASA_DATA);
        files = f.listFiles();
        totalUploadCount = totalUploadCount + files.length;

    }

    public static void uploadAFolderToFirestorage(final Context context, String folderPath, String firestorageFolderName) {

        fileList.clear();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        File f = new File(folderPath);
        File[] files = f.listFiles();

        // reads every file
        for (File file : files) {
            Uri fileLocPath = Uri.fromFile(new File(folderPath + file.getName()));
            final String firebaseStoragePath = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + firestorageFolderName + fileLocPath.getLastPathSegment();
            StorageReference storageRef = storage.getReference(firebaseStoragePath);

            final UploadTask uploadTask = storageRef.putFile(fileLocPath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadCount++;
                    Toast.makeText(context, "Uploading... (" + uploadCount + "/" + totalUploadCount + ")" , Toast.LENGTH_SHORT).show();
                    if (uploadCount == totalUploadCount) {
                        Toast.makeText(context, "All uploading completed.", Toast.LENGTH_SHORT).show();
                    }

                    fileList.add(firebaseStoragePath);
                    FirebaseDBHelper.updateStorageRef(fileList);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Uploading failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void deleteAFolderToFirestorage(final Context context) {

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        deleteCount = 0;

        // get storage ref
        db.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/" + "storageRef").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalDeleteCount = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    StorageReference desertRef = storage.getReference().child(data.getValue().toString());
                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            deleteCount++;
                            Toast.makeText(context, "Deleting... (" + deleteCount + "/" + totalDeleteCount + ")" , Toast.LENGTH_SHORT).show();
                            if (deleteCount == totalDeleteCount) {
                                Toast.makeText(context, "All deleting completed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(context, "Deleting failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                FirebaseDBHelper.deleteTdFromDb();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // remove loc data
        FileHelper.deleteDir(new File(FileHelper.PATH_USER_FOLDER));

        // recreate an overall to refresh
        MainActivity.overallData.reset();
        FileHelper.initDir();
    }
}