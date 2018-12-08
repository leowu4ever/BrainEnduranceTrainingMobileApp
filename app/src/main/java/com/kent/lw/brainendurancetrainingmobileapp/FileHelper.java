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
    public static String PATH_ROOT = Environment.getExternalStorageDirectory() + "/Brain Training Data Folder/";
    public static String PATH_TRAINING_DATA = PATH_ROOT + "Training Data/";
    public static String PATH_MOTION_DATA = PATH_ROOT + "Motion Data/";
    public static String PATH_OVERALL_DATA = PATH_ROOT + "Overall Data/";
    public static String PATH_ROUTE_DATA = PATH_ROOT + "Route Data/";

    public static String FILENAME_OVERALL_DATA = "Overall.json";


    public FileHelper() {
    }

    public static void saveOverallDataToLocal() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(PATH_OVERALL_DATA + FILENAME_OVERALL_DATA)) {
            gson.toJson(MainActivity.overallData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OverallData readOverallDataFromLocal() {
        Gson g = new Gson();
        String path = PATH_OVERALL_DATA + FILENAME_OVERALL_DATA;
        String readings = readJsonFile(path);
        OverallData overallData = g.fromJson(readings, OverallData.class);
        if (overallData == null) {
            overallData = new OverallData();
        }
        return overallData;
    }

    public static TrainingData readTrainingDataFromLocal(String filename) {
        Gson g = new Gson();
        String path = PATH_TRAINING_DATA + filename;
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

    public static void initDir() {

        File rootPath = new File(PATH_ROOT);
        if (!rootPath.exists()) {
            rootPath.mkdir();
        }

        File trainingDataPath = new File(PATH_TRAINING_DATA);
        if (!trainingDataPath.exists()) {
            trainingDataPath.mkdir();
        }

        File motionDataPath = new File(PATH_MOTION_DATA);
        if (!motionDataPath.exists()) {
            motionDataPath.mkdir();
        }

        File overallDataPath = new File(PATH_OVERALL_DATA);
        if (!overallDataPath.exists()) {
            overallDataPath.mkdir();
        }

        File routelDataPath = new File(PATH_ROUTE_DATA);
        if (!routelDataPath.exists()) {
            routelDataPath.mkdir();
        }


        File file = new File(PATH_OVERALL_DATA + FILENAME_OVERALL_DATA);
        if (!file.exists()) {
            saveOverallDataToLocal();
        }
    }

    public static void saveTrainingDataToLocal() {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(PATH_TRAINING_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getStartTime()) + ".json")) {
            gson.toJson(MainActivity.trainingData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveStreamMotionDataToLocal(String steamData, String DataType) {
        File file = new File(PATH_MOTION_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getStartTime()) + "_" + DataType + ".txt");

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


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
