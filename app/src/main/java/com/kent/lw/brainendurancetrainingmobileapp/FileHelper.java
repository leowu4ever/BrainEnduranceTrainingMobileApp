package com.kent.lw.brainendurancetrainingmobileapp;

import android.os.Environment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
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
    public static String PATH_ROOT_FOLDER = Environment.getExternalStorageDirectory() + "/Brain Training Data Folder/";
    public static String PATH_USER_FOLDER = Environment.getExternalStorageDirectory() + "/Brain Training Data Folder/" + FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "") + "/";
    public static String PATH_TRAINING_DATA = PATH_USER_FOLDER + "Training Data/";
    public static String PATH_MOTION_DATA = PATH_USER_FOLDER + "Motion Data/";

    public static String PATH_OVERALL_DATA = PATH_USER_FOLDER + "Overall Data/";
    public static String FILENAME_OVERALL_DATA = "Overall.json";

    public static String PATH_ROUTE_DATA = PATH_USER_FOLDER + "Route Data/";

    public static String PATH_TRAININGDIARY_DATA = PATH_USER_FOLDER + "Training Diary Data/";
    public static String FILENAME_TRAININGDIARY_DATA = "Trainingdiary.json";

    public static String PATH_FEEDBACK_FOLDER = PATH_USER_FOLDER + "Feedback Data/";
    public static String PATH_MOTI_DATA = PATH_FEEDBACK_FOLDER + "Motivation Data/";
    public static String FILENAME_MOTI_DATA = "Motivation.json";

    public static String PATH_RPE_DATA = PATH_FEEDBACK_FOLDER + "Rpe Data/";
    public static String FILENAME_RPE_DATA = "Rpe.json";

    public static String PATH_NASA_DATA = PATH_FEEDBACK_FOLDER + "Nasa Data/";
    public static String FILENAME_NASA_DATA = "Nasa.json";

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

    public static void saveTrainingdiaryDataToLocal() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(PATH_TRAININGDIARY_DATA + FILENAME_TRAININGDIARY_DATA)) {
            gson.toJson(MainActivity.trainingDiaryData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TrainingDiaryData readTrainingdiaryDataFromLocal() {
        Gson g = new Gson();
        String path = PATH_TRAININGDIARY_DATA + FILENAME_TRAININGDIARY_DATA;
        String readings = readJsonFile(path);
        TrainingDiaryData trainingDiaryData = g.fromJson(readings, TrainingDiaryData.class);
        if (trainingDiaryData == null) {
            trainingDiaryData = new TrainingDiaryData();
        }
        return trainingDiaryData;
    }

    public static void saveMotiDataToLocal() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(PATH_MOTI_DATA + FILENAME_MOTI_DATA)) {
            gson.toJson(MainActivity.motiData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MotiData readMotiDataFromLocal() {
        Gson g = new Gson();
        String path = PATH_MOTI_DATA + FILENAME_MOTI_DATA;
        String readings = readJsonFile(path);
        MotiData motiData = g.fromJson(readings, MotiData.class);
        if (motiData == null) {
            motiData = new MotiData();
        }
        return motiData;
    }

    public static void saveRpeDataToLocal() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(PATH_RPE_DATA + FILENAME_RPE_DATA)) {
            gson.toJson(MainActivity.rpeData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RpeData readRpeDataFromLocal() {
        Gson g = new Gson();
        String path = PATH_RPE_DATA + FILENAME_RPE_DATA;
        String readings = readJsonFile(path);
        RpeData rpeData = g.fromJson(readings, RpeData.class);
        if (rpeData == null) {
            rpeData = new RpeData();
        }
        return rpeData;
    }

    public static void saveNasaDataToLocal() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(PATH_NASA_DATA + FILENAME_NASA_DATA)) {
            gson.toJson(MainActivity.nasaData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NasaData readNasaDataFromLocal() {
        Gson g = new Gson();
        String path = PATH_NASA_DATA + FILENAME_NASA_DATA;
        String readings = readJsonFile(path);
        NasaData nasaData = g.fromJson(readings, NasaData.class);
        if (nasaData == null) {
            nasaData = new NasaData();
        }
        return nasaData;
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
        File rootfolder = new File(PATH_ROOT_FOLDER);
        if (!rootfolder.exists()) {
            rootfolder.mkdir();
        }

        File userFolder = new File(PATH_USER_FOLDER);
        if (!userFolder.exists()) {
            userFolder.mkdir();
        }

        File trainingDataFolder = new File(PATH_TRAINING_DATA);
        if (!trainingDataFolder.exists()) {
            trainingDataFolder.mkdir();
        }

        File motionDataFolder = new File(PATH_MOTION_DATA);
        if (!motionDataFolder.exists()) {
            motionDataFolder.mkdir();
        }

        File overallDataFolder = new File(PATH_OVERALL_DATA);
        if (!overallDataFolder.exists()) {
            overallDataFolder.mkdir();
        }

        File routelDataFolder = new File(PATH_ROUTE_DATA);
        if (!routelDataFolder.exists()) {
            routelDataFolder.mkdir();
        }

        File trainingdiaryDataFolder = new File(PATH_TRAININGDIARY_DATA);
        if (!trainingdiaryDataFolder.exists()) {
            trainingdiaryDataFolder.mkdir();
        }


        File feedbackFolder = new File(PATH_FEEDBACK_FOLDER);
        if (!feedbackFolder.exists()) {
            feedbackFolder.mkdir();
        }

        File motiDataFolder = new File(PATH_MOTI_DATA);
        if (!motiDataFolder.exists()) {
            motiDataFolder.mkdir();
        }

        File rpeDataFolder = new File(PATH_RPE_DATA);
        if (!rpeDataFolder.exists()) {
            rpeDataFolder.mkdir();
        }

        File nasaDataFolder = new File(PATH_NASA_DATA);
        if (!nasaDataFolder.exists()) {
            nasaDataFolder.mkdir();
        }

        File overallDataFile = new File(PATH_OVERALL_DATA + FILENAME_OVERALL_DATA);
        if (!overallDataFile.exists()) {
            saveOverallDataToLocal();
        }

        File trainingdiaryDataFile = new File(PATH_TRAININGDIARY_DATA + FILENAME_TRAININGDIARY_DATA);
        if (!trainingdiaryDataFile.exists()) {
            saveTrainingdiaryDataToLocal();
        }

        File motiDataFile = new File(PATH_MOTI_DATA + FILENAME_MOTI_DATA);
        if (!motiDataFile.exists()) {
            saveMotiDataToLocal();
        }

        File rpeDataFile = new File(PATH_RPE_DATA + FILENAME_RPE_DATA);
        if (!rpeDataFile.exists()) {
            saveRpeDataToLocal();
        }

        File nasaDataFile = new File(PATH_NASA_DATA + FILENAME_NASA_DATA);
        if (!nasaDataFile.exists()) {
            saveNasaDataToLocal();
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
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
