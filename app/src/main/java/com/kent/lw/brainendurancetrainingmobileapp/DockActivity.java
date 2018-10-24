package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

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
    private LocationRequest mLocationRequest;


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

    private LatLng testLast, testCur;
    private boolean testInit;
    private double testDis;


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
        getLocationPermission();
        curLocation = new LatLng(0, 0);
        lastLocation = new LatLng(0, 0);
        initMap();

        // acc
        initAcc();

        // runnable
        handler = new Handler();

        // soundpool
        initSoundPool();

        // flic
        initFlic();

        btnFlic = findViewById(R.id.btn_flic);
        btnFlic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FlicManager.getInstance(DockActivity.this, new FlicManagerInitializedCallback() {
                        @Override
                        public void onInitialized(FlicManager manager) {
                            manager.initiateGrabButton(DockActivity.this);
                        }
                    });
                } catch (FlicAppNotInstalledException err) {
                    Toast.makeText(DockActivity.this, "Flic App is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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

        // reset training data

        resetTrainingData();

        // get parameters // passing as parameters

        switch (taskSelected) {
            case A_PVT:

                trainingDuration = A_PVT_DURATION;

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
                break;

            case W_AVT:
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

                trainingDuration = trainingDuration - 1000;
                hour = (trainingDuration) / 1000 / 3600;
                min = (trainingDuration / 1000) / 60;
                sec = (trainingDuration / 1000) % 60;


                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(durationRunnable, 0);

        // simtimulus
        stimulusRunnable = new Runnable() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    handler.postDelayed(this, stimulusInterval);
                    // can do volume and priority for background noise
                    sp.play(beepSound, 1f, 1f, 0, 0, 1);
                    Log.d("STIMULUS_TIME", Calendar.getInstance().getTime() + "");

                    long currentDateTime = System.currentTimeMillis();
                    Date currentDate = new Date(currentDateTime);
                    DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

                    Log.d("STIMULUS_MILI", System.currentTimeMillis() + "");
                    Log.d("STIMULUS_CONVERTED    ", df.format(currentDate));


                }
            }
        };
        handler.postDelayed(stimulusRunnable, 0);

        // map
        mapRunnable = new Runnable() {
            @Override
            public void run() {
                getDeviceLocation();

                Polyline polyline = mMap.addPolyline(new PolylineOptions().clickable(true).add(lastLocation, curLocation));
                polyline.setEndCap(new RoundCap());
                polyline.setWidth(15);
                polyline.setColor(ContextCompat.getColor(DockActivity.this, R.color.colorPrimary));

                distance = distance + SphericalUtil.computeDistanceBetween(lastLocation, curLocation);
                lastLocation = curLocation;
                Log.d("last location ", lastLocation.toString());
                Log.d("cur location ", curLocation.toString());


                String distanceString = String.format("%.1f", distance) + "m";
                trainingFragment.setTvDistance(distanceString);


                double speed = distance / durationMili * 1000;
                String speedString = String.format("%.1f", speed) + "m/s";
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
        transaction.remove(trainingFragment);
        transaction.add(R.id.container, taskFragment, "TASK_FRAGMENT");
        transaction.commit();
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        updateLocationUI();
        createLocationRequest();
        getDeviceLocation();
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

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            curLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            if (!mapInited) {
                                lastLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mapInited = true;
                            }
                            //moveCam(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else {

                        }
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            Log.d("LOCATION_COUNT", locationResult.getLocations().size() + "");
                            Log.d("LOCATION_", location.toString());

                            testCur = new LatLng(location.getLatitude(), location.getLongitude());

                            if (!testInit) {
                                testLast = new LatLng(location.getLatitude(), location.getLongitude());
                                testInit = true;
                            }

                            Polyline polyline = mMap.addPolyline(new PolylineOptions().clickable(true).add(testLast, testCur));
                            polyline.setEndCap(new RoundCap());
                            polyline.setWidth(30);

                            testDis = testDis + SphericalUtil.computeDistanceBetween(testLast, testCur);
                            testLast = testCur;
                            Log.d("Test dis ", testDis + "");

                        }
                    }
                }, null /* Looper */);

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
        trainingDuration = 0;
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
                    Toast.makeText(DockActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DockActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
