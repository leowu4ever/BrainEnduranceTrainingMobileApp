package com.kent.lw.brainendurancetrainingmobileapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class MainActivity extends AppCompatActivity implements TaskCommunicator, TrainingCommunicator, OnMapReadyCallback, SensorEventListener {

    private ImageButton btnProfile, btnFlic;

    // A-PVT
    private final String TASK_A_PVT = "A-PVT";
    private final int A_PVT_DURATION = 10 * 60 * 1000;

    private final int A_PVT_INTERVAL_EASY = 4 * 1000;
    private final int A_PVT_INTERVAL_MEDIUM = 8 * 1000;
    private final int A_PVT_INTERVAL_HARD = 11 * 1000;

    // W-AVT
    private final String TASK_W_AVT = "W-AVT";

    private final int W_AVT_DURATION = 60 * 60 * 1000;

    private final int W_AVT_INTERVAL = 2 * 1000;

    private final int W_AVT_NUMBER_OF_SHORTER_ESAY = 40;    // NEED TO BE CHECKED
    private final int W_AVT_NUMBER_OF_SHORTER_MEDIUM = 40;  // NEED TO BE CHECKED
    private final int W_AVT_NUMBER_OF_SHORTER_HARD = 40;    // NEED TO BE CHECKED

    // VISUAL
    private final String TASK_VISUAL = "Visual";

    // DIF
    private final String DIF_EASY = "Easy";
    private final String DIF_MEDIUM = "Medium";
    private final String DIF_HARD = "Hard";
    private final String DIF_ADAPTIVE = "Adaptive";
    private final String DIF_CUSTOM = "Custom";

    //
    public static boolean trainingStarted = false;

    // permission
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // UI
    private Button btnResume, btnOK, btnBack;

    // fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private TaskFragment taskFragment;
    private TrainingFragment trainingFragment;

    // ui
    private Dialog pauseDialog, finishDialog, profileDialog;

    // map
    private GoogleMap mMap;
    public static boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng lastLocation;
    private boolean mapInited = false;
    private LocationRequest mLocationRequest;
    private List<Polyline> polylineList;
    private int MIN_DISTANCE_UPDATE_THRESHOLD = 10;

    // acc
    private SensorManager sm;
    private Sensor s;
    private double x, y, z;
    private int MAX_DISTANCE_UPDATE_THRESHOLD = 200;

    // runnable
    private Handler handler;
    private Runnable stimulusRunnable, durationRunnable;
    // duration
    private long time, hour, min, sec;
    private final int MAP_UPDATE_INTERVAL = 3000;

    // stimulus
    private int stimulusInterval, trainingDuration;

    // soundpool
    private SoundPool sp;
    private int beepSound, speedupSound;

    // distance
    private double distance, speed;
    private LatLng tempLocation;

    // data collection
    public static TrainingData trainingData;

    // local storage
    private String STORAGE_PATH = "/Brain Training Data Folder/";

    private FirebaseHelper firebaseHelper;

    // finish dialog
    public static double stiTotalCount, resCorrectCount, resTotalCount;
    public static long resTotalTime;
    public static float art, accuracy;
    public static long APVT_RES_TIME_COUNTED_THRESHOLD = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskFragment = new TaskFragment();
        trainingFragment = new TrainingFragment();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
        initDialog();
        initBtns();

        // -- map --
        getLocationPermission();
        lastLocation = new LatLng(0, 0);
        initMap();
        polylineList = new ArrayList<Polyline>();

        // acc
        initAcc();

        // runnable
        handler = new Handler();

        // soundpool
        initSoundPool();

        // flic
        initFlic();


        // firebase data model
        trainingData = new TrainingData();
        firebaseHelper = new FirebaseHelper();
    }

    private void initBtns() {
        btnProfile = findViewById(R.id.btn_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileDialog.show();
            }
        });

        btnFlic = findViewById(R.id.btn_flic);
        btnFlic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private void saveDataToFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        trainingData.updateName(user.getEmail().replace(".", ""));
        firebaseHelper.uploadAllData(trainingData);
    }

    private void saveDataToLocal() {
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

    private void initDialog() {
        //dialog
        pauseDialog = new Dialog(this);
        finishDialog = new Dialog(this);
        profileDialog = new Dialog(this);

        pauseDialog.setContentView(R.layout.dialog_pause);
        pauseDialog.setCancelable(false);
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

        btnResume = pauseDialog.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
                resumeTraining();
            }
        });

        finishDialog.setContentView(R.layout.dialog_finish);
        finishDialog.setCancelable(false);
        finishDialog.setCanceledOnTouchOutside(false);
        finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finishDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);


        btnOK = finishDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog.dismiss();
                showTaskFragment();
            }
        });

        profileDialog.setContentView(R.layout.dialog_profile);
        btnBack = profileDialog.findViewById(R.id.btn_back);
        profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog.dismiss();
            }
        });

    }

    // fragment
    @Override
    public void startTraining(String taskSelected, String difSelected) {
        // replace task fragment with training fragment
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.remove(taskFragment);
        transaction.add(R.id.container, trainingFragment, "TRAINING_FRAGMENT");
        transaction.commit();

        // start training

        // start  map

        // reset training dat
        trainingData.resetAllData();
        trainingData.updateTask(taskSelected);
        trainingData.updateDif(difSelected);
        trainingData.updateId(System.currentTimeMillis());
        resetTrainingData();

        // get parameters // passing as parameters
        switch (taskSelected) {
            case TASK_A_PVT:

                trainingDuration = A_PVT_DURATION;

                // stimulus
                switch (difSelected) {
                    case DIF_EASY:
                        stimulusInterval = A_PVT_INTERVAL_EASY;
                        break;

                    case DIF_MEDIUM:
                        stimulusInterval = A_PVT_INTERVAL_MEDIUM;
                        break;

                    case DIF_HARD:
                        stimulusInterval = A_PVT_INTERVAL_HARD;
                        break;
                }
                break;

            case TASK_W_AVT:
                stimulusInterval = W_AVT_INTERVAL;
                trainingDuration = W_AVT_DURATION;
                break;
        }

        // duration
        durationRunnable = new Runnable() {
            @Override
            public void run() {
                String durationString = hour + "h " + min + "m " + sec + "s";
                trainingFragment.setTvDuration(durationString);
                if (trainingDuration > 0) {
                    trainingDuration = trainingDuration - 1000;
                    time = time + 1000;
                    hour = (trainingDuration) / 1000 / 3600;
                    min = (trainingDuration / 1000) / 60;
                    sec = (trainingDuration / 1000) % 60;
                    handler.postDelayed(this, 1000);
                } else {
                    finishTraining();
                }

            }
        };
        handler.postDelayed(durationRunnable, 0);

        // simtimulus
        stimulusRunnable = new Runnable() {
            @Override
            public void run() {
                if (trainingDuration > 0) {
                    // can do volume and priority for background noise
                    sp.play(beepSound, 1f, 1f, 0, 0, 1);

                    trainingData.updateStiTimeList(System.currentTimeMillis());
                    stiTotalCount++;
                    handler.postDelayed(this, stimulusInterval);
                }
            }
        };
        handler.postDelayed(stimulusRunnable, 0);
        trainingStarted = true;
    }

    @Override
    public void pauseTraining() {

        // show dialog
        pauseDialog.show();

        // resume handler
        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);
        trainingStarted = false;
    }

    @Override
    public void finishTraining() {

        TextView tvFinishDuration = finishDialog.findViewById(R.id.tv_finish_duration);

        TextView tvFinishDistance = finishDialog.findViewById(R.id.tv_finish_distance);

        TextView tvFinishSpeed = finishDialog.findViewById(R.id.tv_finish_speed);
        TextView tvFinishART = finishDialog.findViewById(R.id.tv_finish_ast);
        TextView tvFinishAccuracy = finishDialog.findViewById(R.id.tv_finish_accuracy);

        // update finish dialog

        hour = (time) / 1000 / 3600;
        min = (time / 1000) / 60;
        sec = (time / 1000) % 60;
        String distanceString = String.format("%.1f", distance);
        String speedString = String.format("%.1f", speed);
        String artString = String.format("%.1f", (resTotalTime/resCorrectCount));
        String accuracyString = String.format("%.1f", (resCorrectCount/stiTotalCount * 100));


        tvFinishDuration.setText("Duration: " + min + "M" + sec + "S");
        tvFinishDistance.setText("Distance: " + distanceString + "KM");
        tvFinishSpeed.setText("Speed: " + speedString + "M/S");

        tvFinishART.setText("Average Response Time: " + artString + "MS");
        tvFinishAccuracy.setText("Accuracy (correct response/number of stimulus):" + accuracyString + "%" + " (" + (resCorrectCount + "/" + stiTotalCount) + ")");


        finishDialog.show();
        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);
        trainingStarted = false;
        removePolylines();

        saveDataToFirebase();
        saveDataToLocal();
        trainingData.printAllData();
    }

    public void resumeTraining() {

        handler.postDelayed(durationRunnable, 1000);
        handler.postDelayed(stimulusRunnable, 0);
        trainingStarted = true;

    }

    public void showTaskFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.remove(trainingFragment);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }


    // map
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    public void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(10, 10, 10, 10);
        updateLocationUI();
        createLocationRequest();
    }

    private void updateLocationUI() {

        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void createLocationRequest() {
        initLocationRequestSettings();

        // init last location
        try {
            if (mLocationPermissionGranted) {
                Task task = mFusedLocationProviderClient.getLastLocation();
                task.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && !mapInited) {
                            lastLocation = convertToLatLng((Location) task.getResult());
                            mapInited = true;
                        }
                    }
                });

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (trainingStarted) {

                            // update map
                            if (locationResult != null) {

                                for (Location location : locationResult.getLocations()) {
                                    tempLocation = convertToLatLng(location);

                                    if (getDistance(lastLocation, tempLocation) < MAX_DISTANCE_UPDATE_THRESHOLD) {

                                        drawAPolyline(lastLocation, tempLocation);

                                        // update distance
                                        distance = distance + getDistance(lastLocation, tempLocation);
                                        String distanceString = String.format("%.1f", distance) + "m";
                                        trainingFragment.setTvDistance(distanceString);

                                        // update speed
                                        speed = (distance / time) * 1000;
                                        String speedString = String.format("%.1f", speed) + "m/s";
                                        trainingFragment.setTvSpeed(speedString);

                                        lastLocation = tempLocation;
                                        trainingData.updateLocLatList(lastLocation.latitude);
                                        trainingData.updateLocLngList(lastLocation.longitude);

                                        // finally do prompt
                                        if (speed < 3) {
                                            sp.play(speedupSound, 1, 1, 0, 0, 1);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }, null /* Looper */);
            }
        } catch (SecurityException e) {

        }
    }

    private void initLocationRequestSettings() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MAP_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(MAP_UPDATE_INTERVAL);
        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_UPDATE_THRESHOLD);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void drawAPolyline(LatLng l1, LatLng l2) {
        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(l1, l2));
        polyline.setEndCap(new ButtCap());
        polyline.setWidth(10);
        polyline.setColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        polylineList.add(polyline);
    }

    private void removePolylines() {
        for (Polyline polyline : polylineList) {
            polyline.remove();
        }
        polylineList.clear();
    }

    private double getDistance(LatLng l1, LatLng l2) {
        return SphericalUtil.computeDistanceBetween(l1, l2);
    }

    private LatLng convertToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    // acc
    public void initAcc() {
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // use accelerometer
        if (sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            s = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("acc", "found");

        } else {
            Log.d("acc", "not found");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        Log.d("ACC", x + " " + y + " " + z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void resetTrainingData() {
        trainingDuration = 0;
        time = 0;
        hour = 0;
        min = 0;
        sec = 0;
        distance = 0;
        speed = 0;
        stiTotalCount = 0;
        resCorrectCount = 0;
        resTotalCount = 0;
        resTotalTime = 0;
        art = 0;
        accuracy = 0;
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder().setMaxStreams(6).setAudioAttributes(audioAttributes).build();
        } else {
            sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        beepSound = sp.load(this, R.raw.beep, 1);
        speedupSound = sp.load(this, R.raw.speedup, 1);
    }


    // flic
    public void initFlic() {
        FlicManager.setAppCredentials("ddbfde99-d965-41df-8b9d-810bb0c26fe7", "f6e6938e-4d36-46e6-8fe1-d38436bdef83", "Brain Endurance Training Mobile App");
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN | FlicBroadcastReceiverFlags.REMOVED);
                    Toast.makeText(MainActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
