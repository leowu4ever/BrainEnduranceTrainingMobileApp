package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper {

    public static String PATH_ROOT = "/Brain Training Data Folder/";
    public static String PATH_TRAINING_DATA = "/Brain Training Data Folder/Training Data/";
    public static String PATH_MOTION_DATA = "/Brain Training Data Folder/Motion Data/";
    public static String PATH_OVERALL_DATA = "/Brain Training Data Folder/Overall Data/";
    public static String FILENAME_OVERALL_DATA = "Overall.json";

    public static void saveOverallDataToLocal() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory() + PATH_OVERALL_DATA + FILENAME_OVERALL_DATA)) {
            gson.toJson(MainActivity.overallData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OverallData readOverallDataFromLocal() {
        Gson g = new Gson();
        String path = Environment.getExternalStorageDirectory() + PATH_OVERALL_DATA + FILENAME_OVERALL_DATA;
        String readings = readJsonFile(path);
        return g.fromJson(readings, OverallData.class);
    }

    public static TrainingData readTrainingDataFromLocal(String filename) {
        Gson g = new Gson();
        String path = Environment.getExternalStorageDirectory() + PATH_TRAINING_DATA + filename;
        String readings = readJsonFile(path);
        return g.fromJson(readings, TrainingData.class);
    }

    private static String readJsonFile(String filePath) {
        String readings = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath));  // 2nd line
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String readinText = "";

            while ((readinText = br.readLine()) != null) {
                sb.append(readinText);
            }
            readings = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readings;
    }

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

        File overallDataPath = new File(Environment.getExternalStorageDirectory() + PATH_OVERALL_DATA);
        if (!overallDataPath.exists()) {
            overallDataPath.mkdir();
        }

        File file = new File(Environment.getExternalStorageDirectory() + PATH_OVERALL_DATA + FILENAME_OVERALL_DATA);
        if (!file.exists()) {
            saveOverallDataToLocal();
        }
    }

    public void saveTrainingDataToLocal() {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory() + PATH_TRAINING_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getId()) + ".json")) {
            gson.toJson(MainActivity.trainingData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveStreamMotionDataToLocal(String steamData, String DataType) {
        File file = new File(Environment.getExternalStorageDirectory() + PATH_MOTION_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getId()) + "_" + DataType + ".txt");

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
