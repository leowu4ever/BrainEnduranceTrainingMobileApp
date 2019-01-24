package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class MainActivity extends AppCompatActivity implements TaskCommunicator, TrainingCommunicator, OnMapReadyCallback, View.OnClickListener {

    public static boolean trainingStarted = false;

    // fragments
    public static FragmentManager fragmentManager;
    public static FragmentTransaction transaction;
    public static TaskFragment taskFragment;
    public static TrainingFragment trainingFragment;

    // permission
    public static boolean locPermissionEnabled;

    // runnable
    public static Handler handler;
    public static Runnable countdownRunnbale, stimulusRunnable, durationRunnable;

    // data collection
    public static TrainingData trainingData;
    public static OverallData overallData;
    public static TrainingDiaryData trainingDiaryData;
    public static MotiData motiData;
    public static RpeData rpeData;
    public static NasaData nasaData;

    public static com.kent.lw.brainendurancetrainingmobileapp.Task task;

    // helper class
    public static SoundHelper soundHelper;
    private final int COUNTDONW_WAIT = 6000;
    public DialogHelper dialogHelper;
    public MapHelper mapHelper;
    private ImageButton btnProfile, btnFlic, btnDiary, btnMap;
    // map
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng lastLoc;
    private LocationRequest mLocationRequest;
    private int countdown = 4000;
    private float distance, speed, pace;

    // for adaptive
    //TO-DO should reset everytime
    public static int hitStreak;
    public static final int ADAPTIVE_HIT_STREAK_LIMIT  = 5;
    public static int lapseStreak;
    public static final int APDATIVE_LAPSE_STREAK_LIMIT = 2;

    public static void resumeTraining() {
        trainingStarted = true;
        soundHelper.playNoiseSound(task.getNoise(), task.getNoise(), 0, -1, 1);
        handler.postDelayed(durationRunnable, 1000);
        handler.postDelayed(stimulusRunnable, 0);
    }

    public static void showTaskFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }

    public static void hideTrainingFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.remove(trainingFragment);
        transaction.commit();
    }

    public static void showTrainingFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.remove(taskFragment);
        transaction.add(R.id.container, trainingFragment, "TRAINING_FRAGMENT");
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        keepDisplayOn();
        hideStatusbar();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastLoc = new LatLng(0, 0);
        initMap();

        initBtns();
        initFragments();

        dialogHelper = new DialogHelper(this);
        soundHelper = new SoundHelper(this);

        // runnable
        handler = new Handler();
        initRunnables();

        // firebase data model
        trainingData = new TrainingData();
        overallData = FileHelper.readOverallDataFromLocal();

        trainingDiaryData = FileHelper.readTrainingdiaryDataFromLocal();
        motiData = FileHelper.readMotiDataFromLocal();
        rpeData = FileHelper.readRpeDataFromLocal();
        nasaData = FileHelper.readNasaDataFromLocal();


        task = new com.kent.lw.brainendurancetrainingmobileapp.Task();

        mapHelper = new MapHelper();
        mLocationRequest = new LocationRequest();
        mapHelper.getLocationPermission(this);

        SensorHelper sensorHelper = new SensorHelper(this);
    }

    private void keepDisplayOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void hideStatusbar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initBtns() {
        btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(this);
        btnFlic = findViewById(R.id.btn_flic);
        btnFlic.setOnClickListener(this);
        btnDiary = findViewById(R.id.btn_diary);
        btnDiary.setOnClickListener(this);
        btnMap = findViewById(R.id.btn_map);
        btnMap.setOnClickListener(this);
    }

    // fragment
    @Override
    public void startTraining() {

        // replace task fragment with training fragment
        showTrainingFragment();
        btnProfile.setVisibility(View.GONE);
        btnFlic.setVisibility(View.GONE);
        btnDiary.setVisibility(View.GONE);
        btnMap.setVisibility(View.GONE);


        Task task = mFusedLocationProviderClient.getLastLocation();
        task.addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    lastLoc = mapHelper.convertToLatLng((Location) task.getResult());
                    mapHelper.zoomToLoc(map, lastLoc);
                    mapHelper.addStartMarker(map, lastLoc);
                    trainingData.setLocUpdateTimeList(System.currentTimeMillis());
                    trainingData.setLatList(lastLoc.latitude);
                    trainingData.setLngList(lastLoc.longitude);
                }
            }
        });


        resetTempData();
        trainingData.setStartTime(System.currentTimeMillis());
        handler.postDelayed(countdownRunnbale, 0);

        // start after count down
        handler.postDelayed(durationRunnable, 4000);
        createStiTypeList();
        handler.postDelayed(stimulusRunnable, COUNTDONW_WAIT);

    }

    public static void createStiTypeList() {
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        int totalStiCount = trainingData.getDuration() / 1000 / task.getIntervalFrom() + 1;
        for (int i = 0; i < totalStiCount; i++) {
            trainingData.setStiTypeList(0);
            indexList.add(i);
        }
        Collections.shuffle(indexList);
        float nogoCount = totalStiCount * task.getNogoProportion() / 100;
        for (int i = 0; i < nogoCount; i++) {
            trainingData.setStiTypeOn(indexList.get(i), 1);
        }
        trainingData.setStiTypeOn(0, 0);
    }

    public void pauseTraining() {
        trainingStarted = false;
        soundHelper.stopNoiseSound();
        dialogHelper.showPauseDialog();
        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);
    }

    public void finishTraining() {

        hideTrainingFragment();

        trainingStarted = false;
        soundHelper.stopNoiseSound();
        soundHelper.playFinishSound(1, 1, 0, 0, 1);
        dialogHelper.dismissLockDialog();

        dialogHelper.showFinishDialog(trainingData);

        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);

        mapHelper.addEndMarker(map, new LatLng(trainingData.getLastLat(), trainingData.getLastLng()));

        SnapshotReadyCallback callback = new SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                try {
                    File file = new File(FileHelper.PATH_ROUTE_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getStartTime()) + ".png");
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                    mapHelper.removePolylines();
                    trainingData.setName(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ""));

                    // upload to db
                    //FirebaseDBHelper.uploadTdToDb(trainingData);
                    FileHelper.saveTrainingDataToLocal();

                    // overall
                    overallData.setRtList(trainingData.getAvgResTime());
                    overallData.setAccuracyList(trainingData.getAccuracy());
                    FileHelper.saveOverallDataToLocal();

                    btnProfile.setVisibility(View.VISIBLE);
                    btnFlic.setVisibility(View.VISIBLE);
                    btnDiary.setVisibility(View.VISIBLE);
                    btnMap.setVisibility(View.VISIBLE);

                    trainingData.reset();
                    task.reset();

                    zoomToCurLoc();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        map.snapshot(callback);
    }

    public void resetTempData() {
        distance = 0;
        speed = 0;
        countdown = 4000;

        hitStreak = 0;
        lapseStreak = 0;
    }

    public void initMap() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setPadding(10, 10, 10, 10);
        mapHelper.updateLocationUI(map, this);
        createLocationRequest();
        zoomToCurLoc();
    }


    public void zoomToCurLoc() {
        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location location = (Location) task.getResult();
                        if(location != null) {
                            mapHelper.zoomToLoc(map, new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    } else {
                    }
                }
            });

        } catch (SecurityException e) {
        }
    }

    private void createLocationRequest() {
        mapHelper.initLocationRequestSettings(mLocationRequest);

        // init last location
        try {
            if (locPermissionEnabled) {

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (trainingStarted) {

                            if (locationResult != null) {

                                for (Location location : locationResult.getLocations()) {
                                    LatLng curLoc = mapHelper.convertToLatLng(location);
                                    mapHelper.zoomToLoc(map, new LatLng(trainingData.getMidLat(), trainingData.getMidLng()));

                                    long newLocTime = System.currentTimeMillis();
                                    long lastLocTime = trainingData.getLastLocUpdateTime();
                                    long locTimeDif = newLocTime - lastLocTime;
                                    trainingData.setLocUpdateTimeList(newLocTime);

                                    float newDis = mapHelper.getDistanceInKM(lastLoc, curLoc);

                                    if (newDis < mapHelper.MAX_DISTANCE_UPDATE_THRESHOLD) {

                                        // update distance
                                        distance = distance + newDis;
                                        String distanceString = String.format("%.3f", distance);
                                        trainingFragment.setTvDistance(distanceString);
                                        trainingData.setDistance(distance);

                                        // update speed
                                        speed = (distance / trainingData.getTimeTrained()) * 1000 * 60 * 60;
                                        String speedString = String.format("%.1f", speed);
                                        trainingFragment.setTvSpeed(speedString);
                                        trainingData.setAvgSpeed(speed);

                                        // update pace
                                        pace = 1 / ((distance / trainingData.getTimeTrained()) * 1000 * 60);
                                        String paceString = String.format("%.1f", pace);
                                        trainingFragment.setTvPace(paceString);
                                        trainingData.setAvgPace(pace);

                                        // update cur speed
                                        float curSpeed = (newDis / locTimeDif) * 1000 * 60 * 60;
                                        String curSpeedString = String.format("%.1f", curSpeed);
                                        trainingFragment.setTvCurSpeed(curSpeedString);
                                        trainingData.setSpeedList(curSpeed);

                                        // draw route based on speed
                                        mapHelper.drawAPolyline(map, lastLoc, curLoc, MainActivity.this, curSpeed);

                                        // update location
                                        lastLoc = curLoc;
                                        trainingData.setLatList(lastLoc.latitude);
                                        trainingData.setLngList(lastLoc.longitude);

                                        // finally do prompt
                                        if (curSpeed < MainActivity.task.getMinSpeed()) {
                                            soundHelper.playSpeedupSound(1, 1, 0, 0, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, null);
            }
        } catch (SecurityException e) {
        }
    }

    private void initRunnables() {
        countdownRunnbale = new Runnable() {
            @Override
            public void run() {
                if (countdown > 1000 && countdown <= 4000) {
                    if (countdown == 4000) {
                        soundHelper.playStartSound(1, 1, 0, 0, 1);
                        dialogHelper.showCountdownDialog();
                    }
                    countdown = countdown - 1000;
                    dialogHelper.setCountdownText(countdown / 1000 + "");
                    handler.postDelayed(countdownRunnbale, 1000);
                } else {
                    dialogHelper.dismissCountdownDialog();
                    trainingStarted = true;
                    handler.removeCallbacks(countdownRunnbale);
                }
            }
        };

        durationRunnable = new Runnable() {
            @Override
            public void run() {
                if (trainingData.getTimeTrained() == 0 && task.getNoise() != 0) {
                    soundHelper.playNoiseSound(task.getNoise(), task.getNoise(), 0, -1, 1);
                }

                int timeLeftInMili = trainingData.getTimeLeftInMili();
                if (timeLeftInMili > 0) {
                    int min = (timeLeftInMili / 1000) / 60;
                    int sec = (timeLeftInMili / 1000) % 60;
                    String durationString = min + "M " + sec + "S";
                    trainingFragment.setTvDuration(durationString);
                    trainingData.setTimeTrained(trainingData.getTimeTrained() + 1000);

                    handler.postDelayed(this, 1000);
                } else {
                    finishTraining();
                }
            }
        };

        stimulusRunnable = new Runnable() {
            @Override
            public void run() {
                int timeLeftInMili = trainingData.getTimeLeftInMili();
                if (timeLeftInMili > 0) {
                    Random rd = new Random();
                    float randomVolume = rd.nextFloat() * (task.getVolumeTo() - task.getVolumeFrom()) + task.getVolumeFrom();
                    // get current sti type from stiTypeList
                    if (trainingData.getStiTypeOn(trainingData.getStiCount() + trainingData.getNogoCount()) == 0) {
                        soundHelper.playBeepSound(randomVolume, randomVolume, 0, 0, 1);
                        trainingData.incStiCount();

                    } else {
                        soundHelper.playNogoSound(randomVolume, randomVolume, 0, 0, 1);
                        trainingData.incNogoCount();
                    }
                    trainingData.setStiMiliList(System.currentTimeMillis());

                    // update sti count on tv and td
                    trainingFragment.setTvStiCount(trainingData.getStiCount() + "");

                    // update accuracy
                    trainingFragment.setTvAccuracy(trainingData.getAccuracy() + "");

                    int randomInterval = rd.nextInt(task.getIntervalTo() - task.getIntervalFrom() + 1) + task.getIntervalFrom();
                    trainingFragment.setTvSti("Next stimulus in " + randomInterval + "s");
                    handler.postDelayed(this, randomInterval * 1000);
                }
            }
        };

    }

    private void initFragments() {
        taskFragment = new TaskFragment();
        trainingFragment = new TrainingFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.btn_profile):
                Intent i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
                break;

            case (R.id.btn_flic):
                try {
                    FlicManager.getInstance(MainActivity.this, new FlicManagerInitializedCallback() {
                        @Override
                        public void onInitialized(FlicManager manager) {
                            manager.initiateGrabButton(MainActivity.this);
                        }
                    });
                } catch (FlicAppNotInstalledException err) {
                    Toast.makeText(MainActivity.this, "Flic App is not installed", Toast.LENGTH_SHORT).show();
                }
                break;

            case (R.id.btn_diary):
                dialogHelper.showDiaryDialog();
                break;

            case (R.id.btn_map):
                zoomToCurLoc();
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN | FlicBroadcastReceiverFlags.REMOVED);
                    Toast.makeText(MainActivity.this, "Flic button connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Flic button not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
