package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Environment;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHelper {

    public static String STORAGE_PATH = "/Brain Training Data Folder/";

    public void saveDataToLocal(TrainingData trainingData) {
        Gson gson = new Gson();
        File filePath = new File(Environment.getExternalStorageDirectory() + STORAGE_PATH);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        try (FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory() + STORAGE_PATH + trainingData.getId() + ".json")) {
            gson.toJson(gson.toJson(trainingData), writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
