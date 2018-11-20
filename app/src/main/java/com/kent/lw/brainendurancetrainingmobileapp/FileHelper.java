package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileHelper {

    public static String PATH_ROOT = "/Brain Training Data Folder/";
    public static String PATH_TRAINING_DATA = "/Brain Training Data Folder/Training Data/";
    public static String PATH_MOTION_DATA = "/Brain Training Data Folder/Motion Data/";


    public void initDir() {

        File rootPath = new File(Environment.getExternalStorageDirectory() + FileHelper.PATH_ROOT);
        if (!rootPath.exists()) {
            rootPath.mkdir();
        }

        File trainingDataPath = new File(Environment.getExternalStorageDirectory() + PATH_TRAINING_DATA);
        if (!trainingDataPath.exists()) {
            trainingDataPath.mkdir();
        }

        File motionDataPath = new File(Environment.getExternalStorageDirectory() + PATH_MOTION_DATA);
        if (!motionDataPath.exists()) {
            motionDataPath.mkdir();
        }
    }

    public void saveJsonToLocal() {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory() + PATH_TRAINING_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getId()) + ".json")) {
            gson.toJson(gson.toJson(MainActivity.trainingData), writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveTxtToLocal(String steamData, String DataType) {
        File file = new File(Environment.getExternalStorageDirectory() + FileHelper.PATH_MOTION_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getId()) + "_" + DataType + "_.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file.getAbsoluteFile().toString(), true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.append(steamData);
                osw.close();

                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
