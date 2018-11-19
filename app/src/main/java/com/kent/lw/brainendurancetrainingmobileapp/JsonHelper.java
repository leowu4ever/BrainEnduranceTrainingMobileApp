package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHelper {

    public static String PATH_TRAINING_DATA = "/Brain Training Data Folder/Training Data/";
    public static String PATH_MOTION_DATA = "/Brain Training Data Folder/Motion Data/";

    public void saveDataToLocal(TrainingData trainingData) {
        Gson gson = new Gson();
        File filePath = new File(Environment.getExternalStorageDirectory() + PATH_TRAINING_DATA);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        try (FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory() + PATH_TRAINING_DATA + trainingData.getId() + ".json")) {
            gson.toJson(gson.toJson(trainingData), writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
