package com.kent.lw.brainendurancetrainingmobileapp;

import android.Manifest;
import android.content.Context;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;


public class MainActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback {

    private SensorManager sm;
    private Sensor s;
    private TextView tv;

    private GoogleMap mMap;
    private GraphView graphX;
    private GraphView graphY;
    private GraphView graphZ;

    private LineGraphSeries<DataPoint> seriesX;
    private LineGraphSeries<DataPoint> seriesY;
    private LineGraphSeries<DataPoint> seriesZ;

    private int timeStamp = 0;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;

    private MediaPlayer mp;
    private SoundPool sp;
    private int sound;
    private Handler handler;
    private int timeRemaining = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        initAcc();
        initFlic();
        initBtns();
        initGraph();
        initMap();
        initSoundPool();

        handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    sp.play(sound, 1, 1, 0, 0, 2);
                    handler.postDelayed(this, 1000);
                    timeRemaining = timeRemaining - 1000;
                }
            }
        };
        handler.postDelayed(runnable, 1000);

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

    public void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void initFlic() {
        FlicManager.setAppCredentials("ddbfde99-d965-41df-8b9d-810bb0c26fe7", "f6e6938e-4d36-46e6-8fe1-d38436bdef83", "Brain Endurance Training Mobile App");
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
        tv = findViewById(R.id.tv_acc);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tv.setText("(X: " + event.values[0] + ")      (Y: " + event.values[1] + ")      (Z: " + event.values[2] + ")");

        // update graph here
        timeStamp++;
//        seriesX.appendData(new DataPoint(timeStamp, event.values[0]), false, 100000);
//        seriesY.appendData(new DataPoint(timeStamp, event.values[1]), false, 100000);
//        seriesZ.appendData(new DataPoint(timeStamp, event.values[2]), false, 100000);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng l1 = new LatLng(51.28075, 1.080165); //CT1 2XS 51.280795, 1.080165
        LatLng l2 = new LatLng(51.298521, 1.07161);         // uok 51.298521, 1.071619

        mMap.addMarker(new MarkerOptions().position(l1).title("Marker in Medway"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(l1));

        // draw lines
        googleMap.addPolyline(new PolylineOptions().clickable(true).add(l1, l2));

        mMap.addMarker(new MarkerOptions().position(l2).title("Marker in Medway"));
        Double distance = SphericalUtil.computeDistanceBetween(l1, l2);
        TextView tvDis = findViewById(R.id.tv_dis);
        tvDis.setText(distance + "m");

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

    public void initGraph() {

        graphX = findViewById(R.id.graph_x);
        graphY = findViewById(R.id.graph_y);
        graphZ = findViewById(R.id.graph_z);

        seriesX = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(timeStamp, 0),
        });

        seriesY = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(timeStamp, 0),
        });

        seriesZ = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(timeStamp, 0),
        });

        graphX.addSeries(seriesX);
        graphY.addSeries(seriesY);
        graphZ.addSeries(seriesZ);

    }

    public void initBtns() {
        Button btnFlic = findViewById(R.id.btn_flic);
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


        Button btnSound = findViewById(R.id.btn_sound);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mediaplayer
                //play();

                sp.play(sound, 1, 1, 0, 0, 2);
            }
        });
    }

    public void play() {
        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.sound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMP();
                }
            });
        }
        mp.start();
    }

    public void stopMP() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

}
