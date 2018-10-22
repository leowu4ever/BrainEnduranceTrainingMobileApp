package com.kent.lw.brainendurancetrainingmobileapp;

import android.Manifest;
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
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Random;

public class TrainingActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    // acc
    private SensorManager sm;
    private Sensor s;

    // map
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //task parameters
    private int randomStimulusInterval = 1;
    private static final int DEFAULT_ZOOM = 18;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;

    // handler
    private Handler handler;
    private int timeRemaining = 1;
    private MediaPlayer mp;
    private SoundPool sp;
    private int beepSound, speedupSound;
    private Random random;
    private long durationMili;
    private long min;
    private long sec;
    private long hour;
    private final double MAG_THRESHOLD = 7.0;
    private final int STEP_COUNT_INTERVAL = 2000;
    private final double STRIDE_LENGTH = 0.5;
    private double x, y, z, xLast, yLast, zLast, mag;
    private int stepsLast, steps, stepsDiff, stepsTemp;
    private final int MAP_UPDATE_INTERVAL = 3000;
    private TextView tvSpeed, tvStep, tvDistance, tvDuration;
    private Runnable stimulusRunnable, durationRunnable, speedRunnable, mapRunnable;
    private LatLng curLocation, lastLocation;

    private boolean mapInited = false;

    private double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        randomStimulusInterval = intent.getExtras().getInt("RANDOM_STIMULUS_INTERVAL");
        //Log.d("rsi", randomStimulusInterval + "");

        getLocationPermission();
        initMap();
        initButton();
        initSoundPool();

        tvSpeed = findViewById(R.id.tv_speed);
        tvStep = findViewById(R.id.tv_speed_step);
        tvDistance = findViewById(R.id.tv_distance);
        tvDuration = findViewById(R.id.tv_duration);
        initAcc();


        random = new Random();
        handler = new Handler();

        curLocation = new LatLng(0, 0);
        lastLocation = new LatLng(0, 0);


        stimulusRunnable = new Runnable() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    int wait = random.nextInt(randomStimulusInterval) + 1;
                    handler.postDelayed(this, 1000 * wait);
                    // can do volume and priority for background noise
                    sp.play(beepSound, 0.1f, 0.1f, 0, 0, 1);
                }
            }
        };
        handler.postDelayed(stimulusRunnable, 0);

        durationRunnable = new Runnable() {
            @Override
            public void run() {
                durationMili = durationMili + 1000;
                hour = (durationMili) / 1000 / 3600;
                min = (durationMili / 1000) / 60;
                sec = (durationMili / 1000) % 60;
                tvDuration.setText("Duration: " + hour + "h " + min + "m " + sec + "s");
                handler.postDelayed(this, 1000);

            }
        };
        handler.postDelayed(durationRunnable, 1000);

        speedRunnable = new Runnable() {
            @Override
            public void run() {

                stepsTemp = steps;
                stepsDiff = stepsTemp - stepsLast;
                stepsLast = stepsTemp;

                double speed = (stepsDiff * STRIDE_LENGTH) / (STEP_COUNT_INTERVAL / 1000);
                tvStep.setText("step speed: " + speed + "m/s");



                handler.postDelayed(this, STEP_COUNT_INTERVAL);
            }
        };
        handler.post(speedRunnable);


        mapRunnable = new Runnable() {
            @Override
            public void run() {
                getDeviceLocation();
                mMap.addPolyline(new PolylineOptions().clickable(true).add(lastLocation, curLocation));
                distance = distance + SphericalUtil.computeDistanceBetween(lastLocation, curLocation);

                lastLocation = curLocation;

                tvDistance.setText("Distance: " + String.format("%.2f", distance) + "m");
                double speed = distance / durationMili * 1000;
                tvSpeed.setText("GPS speed: " + String.format("%.2f", speed) + "m/s");
                if (speed < 3) {
                    sp.play(speedupSound, 1, 1, 0, 0, 1);
                    Toast.makeText(TrainingActivity.this, "Speed up", Toast.LENGTH_SHORT).show();
                }


                handler.postDelayed(this, MAP_UPDATE_INTERVAL);
            }
        };

        handler.postDelayed(mapRunnable, 0);

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
    protected void onDestroy() {
        handler.removeMessages(0);
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

    private void initButton() {
        Button btnFinish = findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

//
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
//
        mag = Math.sqrt(x * x + y * y + z * z);

        if (mag >= MAG_THRESHOLD) {
            steps++;
        }
//
//        tvDistance.setText(steps * STRIDE_LENGTH + "m");

        // update graph here
        //timeStamp++;
//        seriesX.appendData(new DataPoint(timeStamp, event.values[0]), false, 100000);
//        seriesY.appendData(new DataPoint(timeStamp, event.values[1]), false, 100000);
//        seriesZ.appendData(new DataPoint(timeStamp, event.values[2]), false, 100000);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng l1 = new LatLng(51.28075, 1.080165); //CT1 2XS 51.280795, 1.080165

        //mMap.addMarker(new MarkerOptions().position(l1).title("Marker in Medway"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));

        // draw lines
        //googleMap.addPolyline(new PolylineOptions().clickable(true).add(l1, l2));
        //mMap.addMarker(new MarkerOptions().position(l2).title("Marker in Medway"));
        //Double distance = SphericalUtil.computeDistanceBetween(l1, l2);
        //TextView tvDis = findViewById(R.id.tv_dis);
        //tvDis.setText(distance + "m");

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

}