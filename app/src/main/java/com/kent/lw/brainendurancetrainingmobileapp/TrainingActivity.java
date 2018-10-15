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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
    private int sound;
    private Random random;
    private long durationMili;
    private long min;
    private long sec;
    private long hour;

    private TextView tvSpeed, tvDistance, tvDuration;


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
        tvDistance = findViewById(R.id.tv_distance);
        tvDuration = findViewById(R.id.tv_duration);
        initAcc();


        random = new Random();
        handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    int wait = random.nextInt(randomStimulusInterval) + 1;
                    handler.postDelayed(this, 1000 * wait);
                    // can do volume and priority for background noise
                    sp.play(sound, 1, 1, 0, 0, 2);
                    Log.d("wait", wait + "");


                }
            }
        };
        handler.postDelayed(runnable, 0);

        final Runnable durationRunnable = new Runnable() {
            @Override
            public void run() {
                durationMili = durationMili + 1000;
                hour = (durationMili) / 1000 / 3600;
                min = (durationMili / 1000) / 60;
                sec = (durationMili / 1000) % 60;
                tvDuration.setText(hour + "h" + min + "m " + sec + "s");
                handler.postDelayed(this, 1000);

            }
        };
        handler.postDelayed(durationRunnable, 1000);

    }

    public void initAcc() {
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // use accelerometer
        if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
            Log.d("acc", "found ");

        } else {
            Log.d("acc", "not found");
        }
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

        sound = sp.load(this, R.raw.sound, 1);
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
        tvSpeed.setText("(X" + event.values[0] + ")(Y" + event.values[1] + ")(Z" + event.values[2] + ")");

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
        LatLng l1 = new LatLng(51.28075, 1.080165); //CT1 2XS 51.280795, 1.080165
        LatLng l2 = new LatLng(51.298521, 1.07161);         // uok 51.298521, 1.071619

        //mMap.addMarker(new MarkerOptions().position(l1).title("Marker in Medway"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));

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