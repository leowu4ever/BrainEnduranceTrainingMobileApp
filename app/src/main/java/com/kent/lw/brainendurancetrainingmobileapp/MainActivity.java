package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;


public class MainActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback {

    private final Handler mHandler = new Handler();
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

    // map --------------

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private CameraPosition mCameraPosition;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;


    // map --------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAcc();
        initFlic();
        initBtns();
        initGraph();
        initMap();

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
        seriesX.appendData(new DataPoint(timeStamp, event.values[0]), false, 100000);
        seriesY.appendData(new DataPoint(timeStamp, event.values[1]), false, 100000);
        seriesZ.appendData(new DataPoint(timeStamp, event.values[2]), false, 100000);

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
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions().clickable(true)
                .add(l1, l2));

        mMap.addMarker(new MarkerOptions().position(l2).title("Marker in Medway"));
        Double distance = SphericalUtil.computeDistanceBetween(l1, l2);
        TextView tvDis = findViewById(R.id.tv_dis);
        tvDis.setText(distance + "m");
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

        Button btnTask = findViewById(R.id.btn_task);
        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });

        Button btnSign = findViewById(R.id.btn_signup);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        Button btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }


}
