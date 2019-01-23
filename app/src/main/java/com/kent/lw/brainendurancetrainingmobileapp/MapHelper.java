package com.kent.lw.brainendurancetrainingmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static int MAX_DISTANCE_UPDATE_THRESHOLD = 100;
    public static int MAP_ZOOM = 14;
    private final int MAP_UPDATE_INTERVAL = 3000;
    private int MIN_DISTANCE_UPDATE_THRESHOLD = 10;
    private List<Polyline> polylineList = new ArrayList<Polyline>();
    private Marker startMarker, endMarker;

    public LatLng convertToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public float getDistanceInKM(LatLng l1, LatLng l2) {
        float dis = (float) SphericalUtil.computeDistanceBetween(l1, l2);
        return dis / 1000;
    }

    public void removePolylines() {
        for (Polyline polyline : polylineList) {
            polyline.remove();
        }
        polylineList.clear();
        startMarker.remove();
        endMarker.remove();
    }

    public void drawAPolyline(GoogleMap mMap, LatLng l1, LatLng l2, Context context, float curSpeed) {
        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(l1, l2));
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(13);

        // alert color here

        int color = 0;
        if (curSpeed > 0 && curSpeed <= 2) {
            color = R.color.s1;
        } else if (curSpeed > 2 && curSpeed <= 4) {
            color = R.color.s2;
        } else if (curSpeed > 4 && curSpeed <= 6) {
            color = R.color.s3;
        } else if (curSpeed > 6 && curSpeed <= 8) {
            color = R.color.s4;
        } else if (curSpeed > 8 && curSpeed <= 10) {
            color = R.color.s5;
        } else if (curSpeed > 10 && curSpeed <= 12) {
            color = R.color.s6;
        } else if (curSpeed > 12 && curSpeed <= 14) {
            color = R.color.s7;
        } else if (curSpeed > 14 && curSpeed <= 16) {
            color = R.color.s8;
        } else {
            color = R.color.s9;
        }

        polyline.setColor(ContextCompat.getColor(context, color));
        polylineList.add(polyline);
    }

    public void initLocationRequestSettings(LocationRequest mLocationRequest) {
        mLocationRequest.setInterval(MAP_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(MAP_UPDATE_INTERVAL);
        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_UPDATE_THRESHOLD);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void getLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context.getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            MainActivity.locPermissionEnabled = true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void updateLocationUI(GoogleMap map, Context context) {
        try {
            if (MainActivity.locPermissionEnabled) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setZoomGesturesEnabled(false);
                map.getUiSettings().setTiltGesturesEnabled(false);
                map.getUiSettings().setRotateGesturesEnabled(false);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission(context);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void zoomToLoc(GoogleMap map, LatLng loc) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.latitude - 0.009
                , loc.longitude), MAP_ZOOM);
        map.animateCamera(cameraUpdate, 100, null);
    }

    public void addStartMarker(GoogleMap map, LatLng loc) {
        startMarker = map.addMarker(new MarkerOptions().position(loc));
        startMarker.setTitle("START");
    }

    public void addEndMarker(GoogleMap map, LatLng loc) {
        endMarker = map.addMarker(new MarkerOptions().position(loc));
        endMarker.setTitle("END");
    }

}
