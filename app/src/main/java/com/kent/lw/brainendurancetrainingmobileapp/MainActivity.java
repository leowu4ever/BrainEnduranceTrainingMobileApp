package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

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

public class MainActivity extends AppCompatActivity implements TaskCommunicator, TrainingCommunicator, VisualCommunicator, View.OnClickListener, OnMapReadyCallback {

    public static final int ADAPTIVE_HIT_STREAK_LIMIT = 5;
    public static final int APDATIVE_LAPSE_STREAK_LIMIT = 2;
    public static boolean trainingStarted = false;
    // fragments
    public static FragmentManager fragmentManager;
    public static FragmentTransaction transaction;
    public static SupportMapFragment mapFragment;

    public static TaskFragment taskFragment;
    public static TrainingFragment trainingFragment;
    public static VisualFragment visualFragment;
    // permission
    public static boolean locPermissionEnabled;
    // runnable
    public static Handler handler;
    public static Runnable countdownRunnbale, stimulusRunnable, durationRunnable, visualDurationRunnable;
    // data collection
    public static TrainingData trainingData;
    public static OverallData overallData;
    public static TrainingDiaryData trainingDiaryData;
    public static MotiData motiData;
    public static RpeData rpeData;
    public static NasaData nasaData;
    public static FeedbackData feedbackData;
    public static com.kent.lw.brainendurancetrainingmobileapp.Task task;
    // helper class
    public static SoundHelper soundHelper;
    // for adaptive
    //TO-DO should reset everytime
    public static int hitStreak;
    public static int lapseStreak;
    private final int COUNTDONW_WAIT = 6000;
    public DialogHelper dialogHelper;
    public MapHelper mapHelper;
    private ImageButton btnProfile, btnFlic, btnDiary, btnMap, btnFeedback;
    // map
    //private GoogleMap map;
    //private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng lastLoc;
    //private LocationRequest mLocationRequest;
    private int countdown = 4000;
    private float distance, speed, pace;

    //OSM
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationEngine mLocationEngine;
    private LocationEngineRequest mLocationEngineRequest;
    String a = "a";
    String b = "b";

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
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
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

    public static void showVisualFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.remove(taskFragment);
        //mapFragment.getView().setVisibility(View.GONE);
        transaction.add(R.id.container, visualFragment, "VISUAL_FREGAMENT");
        transaction.commit();
    }

    public static void hideVisualFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.remove(visualFragment);
        //mapFragment.getView().setVisibility(View.VISIBLE);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }

    public static void createStiTypeList() {
        // create sti list with 0
        ArrayList<Integer> indexList = new ArrayList<Integer>();

        int totalStiCount = trainingData.getDuration() / 1000 / task.getIntervalFrom() + 1;

        if (trainingData.getDif().equals("Adaptive")) {
            totalStiCount = trainingData.getDuration() / 1000 / 2;
        }

        for (int i = 0; i < totalStiCount; i++) {
            trainingData.setStiTypeList(0);
            indexList.add(i);
        }

        // modify the list for nogo.
        Collections.shuffle(indexList);
        float nogoCount = totalStiCount * task.getNogoProportion() / 100;
        for (int i = 0; i < nogoCount; i++) {
            trainingData.setStiTypeOn(indexList.get(i), 1);
        }
        trainingData.setStiTypeOn(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        keepDisplayOn();
        hideStatusbar();

        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.open_street_maps_key));
        setContentView(R.layout.activity_main);

        lastLoc = new LatLng(0, 0);
        //Initialize Map
        MapHelper.getLocationPermission(this);
        initMap(savedInstanceState);


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
        feedbackData = FileHelper.readSurveyDataFromLocal();

        task = new com.kent.lw.brainendurancetrainingmobileapp.Task();


        SensorHelper sensorHelper = new SensorHelper(this);
    }

    private void keepDisplayOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void hideStatusbar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initMap(Bundle savedInstanceState) {
        //OSM
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.setVisibility(View.INVISIBLE);

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
        btnFeedback = findViewById(R.id.btn_feedback);
        btnFeedback.setOnClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setPadding(10, 10, 10, 10);
        mapboxMap.setMinZoomPreference(11d);
        mapboxMap.setMaxZoomPreference(mapHelper.MAP_ZOOM);

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                MapboxMap mapboxMap = MainActivity.this.mapboxMap;

                mapHelper = new MapHelper(mapboxMap, getApplicationContext());
                mLocationEngine = LocationEngineProvider.getBestLocationEngine(getApplicationContext());
                mLocationEngineRequest = mapHelper.initLocationRequestSettings();
                mapHelper.updateLocationUI(mapboxMap, MainActivity.this);
                createLocationRequest();
                zoomToCurLoc();

                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(MainActivity.this, style).build());
                locationComponent.setLocationComponentEnabled(true);

                //locationComponent.setCameraMode(CameraMode.TRACKING);
                //locationComponent.setRenderMode(RenderMode.COMPASS);
            }
        });
    }

    public void zoomToCurLoc() {
        try {
            if (locPermissionEnabled) {
                mLocationEngine.requestLocationUpdates(mLocationEngineRequest, new LocationEngineCallback<LocationEngineResult>() {
                    @Override
                    public void onSuccess(LocationEngineResult locationResult) {
                        if (locationResult != null) {
                            for (Location location : locationResult.getLocations()) {
                                mapHelper.zoomToLoc(mapboxMap, new LatLng(location.getLatitude(), location.getLongitude()));
                                mLocationEngine.removeLocationUpdates(this);
                                mapView.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) { }
                }, null);
            }
        }
        catch (SecurityException e) { }
    }

    private void createLocationRequest() {
        try {
            if (locPermissionEnabled) {
                mLocationEngine.requestLocationUpdates(mLocationEngineRequest, new LocationEngineCallback<LocationEngineResult>() {
                    @Override
                    public void onSuccess(LocationEngineResult locationResult) {
                        if (trainingStarted && !trainingData.getTask().equals("Visual")) {
                            if (locationResult != null) {
                                for(Location location : locationResult.getLocations()){
                                    LatLng curLoc = mapHelper.convertToLatLng(location);
                                    mapHelper.zoomToLoc(mapboxMap, new LatLng(trainingData.getMidLat(), trainingData.getMidLng()));

                                    long newLocTime = System.currentTimeMillis();
                                    long lastLocTime = trainingData.getLastLocUpdateMili();
                                    long locTimeDif = newLocTime - lastLocTime;
                                    trainingData.setLocUpdateMiliList(newLocTime);

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
                                        mapHelper.drawAPolyline(mapboxMap, lastLoc, curLoc, MainActivity.this, curSpeed);

                                        // update location
                                        lastLoc = curLoc;
                                        trainingData.setLatList(lastLoc.getLatitude());
                                        trainingData.setLngList(lastLoc.getLongitude());

                                        // finally do prompt
                                        if (curSpeed < MainActivity.task.getMinSpeed()) {
                                            soundHelper.playSpeedupSound(1, 1, 0, 0, 1);
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Location location = locationResult.getLastLocation();
                            mapHelper.zoomToLoc(mapboxMap, new LatLng(location.getLatitude(), location.getLongitude()));
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) { }
                }, null);

            }
            else {
                mapHelper.getLocationPermission(this);
            }
        }
        catch (SecurityException e) { }

    }

    @Override
    public void startTraining() {
        mLocationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult locationResult) {
                //Location locationResult = (Location) task.getResult();
                if (locationResult != null) {
                    lastLoc = mapHelper.convertToLatLng(locationResult.getLastLocation());
                    mapHelper.zoomToLoc(mapboxMap, lastLoc);
                    mapHelper.addStartMarker(mapboxMap, lastLoc);
                    trainingData.setLocUpdateMiliList(System.currentTimeMillis());
                    trainingData.setLatList(lastLoc.getLatitude());
                    trainingData.setLngList(lastLoc.getLongitude());

                    // replace task fragment with training fragment
                    showTrainingFragment();
                    btnProfile.setVisibility(View.GONE);
                    btnFlic.setVisibility(View.GONE);
                    btnDiary.setVisibility(View.GONE);
                    btnMap.setVisibility(View.GONE);
                    btnFeedback.setVisibility(View.GONE);

                    resetTempData();
                    trainingData.setStartTime(System.currentTimeMillis());
                    handler.postDelayed(countdownRunnbale, 0);

                    // start after count down
                    handler.postDelayed(durationRunnable, 4000);
                    createStiTypeList();
                    handler.postDelayed(stimulusRunnable, COUNTDONW_WAIT);

                }
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this,"Unable to start your task because the location service is unavailable now. Please connect to the internet with your Sim card or WIFI.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void startVisualTraining() {
        //task fragment disappear

        //map fragment disappear
        mapView.setVisibility(View.GONE);
        showVisualFragment();

        //hide button
        btnProfile.setVisibility(View.GONE);
        btnFlic.setVisibility(View.GONE);
        btnDiary.setVisibility(View.GONE);
        btnMap.setVisibility(View.GONE);
        btnFeedback.setVisibility(View.GONE);

        //show visual fragment

        // we dont need location services here

        // reset temp data
        resetTempData();

        //set the start time
        trainingData.setStartTime(System.currentTimeMillis());

        //start countdown

        //start duration

        handler.postDelayed(countdownRunnbale, 0);

        handler.postDelayed(visualDurationRunnable, 4000);

        //start visual task runnable

    }

    public void pauseTraining() {
        trainingStarted = false;
        soundHelper.stopNoiseSound();
        dialogHelper.showPauseDialog();
        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);
    }

    public void finishTraining() {

        mapHelper.addEndMarker(mapboxMap, new LatLng(trainingData.getLastLat(), trainingData.getLastLng()));

        hideTrainingFragment();

        trainingStarted = false;
        soundHelper.stopNoiseSound();
        soundHelper.playFinishSound(1, 1, 0, 0, 1);
        dialogHelper.dismissLockDialog();

        dialogHelper.showFinishDialog(trainingData);

        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);

        new Handler().postDelayed(new Runnable()
        {
            MapboxMap.SnapshotReadyCallback callback = new MapboxMap.SnapshotReadyCallback() {
                Bitmap bitmap;

                @Override
                public void onSnapshotReady(Bitmap snapshot) {
                    bitmap = snapshot;
                    try {
                        File file = new File(FileHelper.PATH_ROUTE_DATA + DateHelper.getDateTimeFromMili(MainActivity.trainingData.getStartTime()) + ".png");
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                        mapHelper.removePolylines(mapboxMap);

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
                        btnFeedback.setVisibility(View.VISIBLE);

                        trainingData.reset();
                        task.reset();

                        zoomToCurLoc();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            @Override
            public void run()
            {
                mapboxMap.snapshot(callback);
            }
        }, 2500);


    }

    public void finishVisualTraining() {

        // for the finish button in visual fragment
        // hide visual fragment
        mapView.setVisibility(View.VISIBLE);
        hideVisualFragment();
        trainingStarted = false;

        // play finish sound
        soundHelper.playFinishSound(1, 1, 0, 0, 1);

        // show finsih dialog
        dialogHelper.showFinishDialog(trainingData);
        // remove runnable of visual duration

        handler.removeCallbacks(visualDurationRunnable);


        trainingData.setName(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ""));
        FileHelper.saveTrainingDataToLocal();

        // update overall data
        overallData.setRtList(trainingData.getAvgResTime());
        overallData.setAccuracyList(trainingData.getAccuracy());
        FileHelper.saveOverallDataToLocal();

        // show circle button
        btnProfile.setVisibility(View.VISIBLE);
        btnFlic.setVisibility(View.VISIBLE);
        btnDiary.setVisibility(View.VISIBLE);
        btnMap.setVisibility(View.VISIBLE);
        btnFeedback.setVisibility(View.VISIBLE);
        // reset training data
        // reset task
        trainingData.reset();
        task.reset();

    }

    public void resetTempData() {
        distance = 0;
        speed = 0;
        countdown = 4000;

        hitStreak = 0;
        lapseStreak = 0;
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
                // play noise when starts
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

        visualDurationRunnable = new Runnable() {
            @Override
            public void run() {

                // start count down
                // check if it finishes
                int timeLeftInMili = trainingData.getTimeLeftInMili();
                if (timeLeftInMili > 0) {
                    int min = (timeLeftInMili / 1000) / 60;
                    int sec = (timeLeftInMili / 1000) % 60;
                    String durationString = min + "M " + sec + "S";
                    visualFragment.setTvDuration(durationString);
                    trainingData.setTimeTrained(trainingData.getTimeTrained() + 1000);

                    handler.postDelayed(this, 1000);
                } else {
                    finishVisualTraining();
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
        visualFragment =  new VisualFragment();
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

            case (R.id.btn_feedback):
                dialogHelper.showFeedbackDialog();
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

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch( event.getKeyCode() ) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                // do something
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                // do something
                break;

            default:
                super.dispatchKeyEvent(event);
                break;
        }

        return true;
    }

}
