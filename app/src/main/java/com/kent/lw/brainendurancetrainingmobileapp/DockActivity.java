package com.kent.lw.brainendurancetrainingmobileapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

public class DockActivity extends AppCompatActivity implements TaskCommunicator, TrainingCommunicator, OnMapReadyCallback, SensorEventListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private TaskFragment taskFragment;
    private TrainingFragment trainingFragment;
    private static final int DEFAULT_ZOOM = 15;
    private Button btnResume, btnOK, btnBack;
    private ImageButton btnProfile, btnFlic;
    private final double MAG_THRESHOLD = 7.0;
    // --- need to be refactored
    // Task TAG
    private final String A_PVT = "A-PVT";
    private final String W_AVT = "W-AVT";
    private final String EASY = "Easy";
    private final String MEDIUM = "Medium";
    private final String HARD = "Hard";
    private final int A_PVT_DURATION = 10 * 60 * 1000;
    private final int W_AVT_DURATION = 60 * 60 * 1000;
    private final int A_PVT_INTERVAL_EASY = 4 * 1000;
    private final int A_PVT_INTERVAL_MEDIUM = 8 * 1000;
    private final int A_PVT_INTERVAL_HARD = 11 * 1000;
    private final int W_AVT_INTERVAL = 2 * 1000;
    private final int W_AVT_NUMBER_OF_SHORTER_ESAY = 40;    // NEED TO BE CHECKED
    private final int W_AVT_NUMBER_OF_SHORTER_MEDIUM = 40;  // NEED TO BE CHECKED
    private final int W_AVT_NUMBER_OF_SHORTER_HARD = 40;    // NEED TO BE CHECKED
    // fragments
    private FrameLayout container;
    // ui
    private Dialog pauseDialog, finishDialog, profileDialog;
    // map
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng curLocation, lastLocation;
    private boolean mapInited = false;
    // acc
    private SensorManager sm;
    private Sensor s;
    private double x, y, z, xLast, yLast, zLast, mag;
    private int stepsLast, steps, stepsDiff, stepsTemp;
    // runnable
    private Handler handler;
    private Runnable stimulusRunnable, durationRunnable, speedRunnable, mapRunnable;

    // duration
    private long durationMili, hour, min, sec;
    private final int MAP_UPDATE_INTERVAL = 3000;
    // stimulus
    private int timeRemaining = 1;
    private int stimulusInterval;
    private int trainingDuration;
    // soundpool
    private SoundPool sp;
    private int beepSound, speedupSound;
    // distance
    private double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock);

        container = findViewById(R.id.container);

        taskFragment = new TaskFragment();
        trainingFragment = new TrainingFragment();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();

        //dialog
        pauseDialog = new Dialog(this);
        finishDialog = new Dialog(this);
        profileDialog = new Dialog(this);

        btnProfile = findViewById(R.id.btn_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog.setContentView(R.layout.dialog_profile);
                btnBack = profileDialog.findViewById(R.id.btn_back);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileDialog.dismiss();
                    }
                });
                profileDialog.show();
            }
        });


        // -- map --
        curLocation = new LatLng(0, 0);
        lastLocation = new LatLng(0, 0);
        getLocationPermission();
        initMap();

        // acc
        initAcc();

        // runnable
        handler = new Handler();

        initSoundPool();

    }

    @Override
    public void startTraining(String taskSelected, String difSelected) {
        // replace task fragment with training fragment
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.replace(R.id.container, trainingFragment, "TRAINING_FRAGMENT");
        transaction.commit();

        // start training


        // start  map

        // reset training data

        resetTrainingData();

        // get parameters // passing as parameters

        switch (taskSelected) {
            case A_PVT:

                trainingDuration = A_PVT_DURATION;

                // duration
                durationRunnable = new Runnable() {
                    @Override
                    public void run() {
                        durationMili = durationMili + 1000;
                        hour = (durationMili) / 1000 / 3600;
                        min = (durationMili / 1000) / 60;
                        sec = (durationMili / 1000) % 60;
                        String durationString = hour + "h " + min + "m " + sec + "s";
                        trainingFragment.setTvDuration(durationString);


                        handler.postDelayed(this, 1000);
                    }
                };
                handler.postDelayed(durationRunnable, 1000);

                // stimulus
                switch (difSelected) {
                    case EASY:
                        stimulusInterval = A_PVT_INTERVAL_EASY;
                        break;

                    case MEDIUM:
                        stimulusInterval = A_PVT_INTERVAL_MEDIUM;
                        break;

                    case HARD:
                        stimulusInterval = A_PVT_INTERVAL_HARD;
                        break;
                }

                stimulusRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (timeRemaining > 0) {
                            handler.postDelayed(this, stimulusInterval);
                            // can do volume and priority for background noise
                            sp.play(beepSound, 1f, 1f, 0, 0, 1);
                        }
                    }
                };
                handler.postDelayed(stimulusRunnable, 0);
                break;


            case W_AVT:
                stimulusInterval = W_AVT_INTERVAL;
                trainingDuration = W_AVT_DURATION;
        }

        mapRunnable = new Runnable() {
            @Override
            public void run() {
                getDeviceLocation();
                mMap.addPolyline(new PolylineOptions().clickable(true).add(lastLocation, curLocation));

                distance = distance + SphericalUtil.computeDistanceBetween(lastLocation, curLocation);
                lastLocation = curLocation;
                Log.d("last location ", lastLocation.toString());
                Log.d("cur location ", curLocation.toString());


                String distanceString = String.format("%.2f", distance) + "m";
                trainingFragment.setTvDistance(distanceString);


                double speed = distance / durationMili * 1000;
                String speedString = String.format("%.2f", speed) + "m/s";
                trainingFragment.setTvSpeed(speedString);

                if (speed < 3) {
                    sp.play(speedupSound, 1, 1, 0, 0, 1);
                }
                handler.postDelayed(this, MAP_UPDATE_INTERVAL);
            }
        };

        handler.postDelayed(mapRunnable, 0);
    }

    @Override
    public void pauseTraining() {

        // show dialog
        pauseDialog.setContentView(R.layout.dialog_pause);
        pauseDialog.setCancelable(false);
        pauseDialog.setCanceledOnTouchOutside(false);

        btnResume = pauseDialog.findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
                resumeTraining();
            }
        });
        pauseDialog.show();

        // resume handler
        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);
        handler.removeCallbacks(mapRunnable);

    }

    @Override
    public void finishTraining() {


        finishDialog.setContentView(R.layout.dialog_finish);
        finishDialog.setCancelable(false);
        finishDialog.setCanceledOnTouchOutside(false);

        btnOK = finishDialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog.dismiss();
                showTaskFragment();
            }
        });

        finishDialog.show();
        handler.removeCallbacks(durationRunnable);
        handler.removeCallbacks(stimulusRunnable);
        handler.removeCallbacks(mapRunnable);


    }

    public void resumeTraining() {

        handler.postDelayed(durationRunnable, 1000);
        handler.postDelayed(stimulusRunnable, 0);
        handler.postDelayed(mapRunnable, 0);

    }

    public void showTaskFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.replace(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }

    private void getLocationPermission() {

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
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
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();

                            curLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            if (!mapInited) {
                                lastLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mapInited = true;
                            }

                            moveCam(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {

                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void moveCam(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

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
        mag = Math.sqrt(x * x + y * y + z * z);

        if (mag >= MAG_THRESHOLD) {
            steps++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void resetTrainingData() {
        durationMili = 0;
        hour = 0;
        min = 0;
        sec = 0;
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

}
