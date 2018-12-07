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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.List;

public class MapHelper {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static int MAX_DISTANCE_UPDATE_THRESHOLD = 100;
    private final int MAP_UPDATE_INTERVAL = 3000;
    private int MIN_DISTANCE_UPDATE_THRESHOLD = 10;

    public LatLng convertToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public float getDistanceInKM(LatLng l1, LatLng l2) {
        float dis = (float) SphericalUtil.computeDistanceBetween(l1, l2);
        return dis / 1000;
    }

    public void removePolylines(List<Polyline> polylineList) {
        for (Polyline polyline : polylineList) {
            polyline.remove();
        }
        polylineList.clear();
    }

    public void drawAPolyline(GoogleMap mMap, List<Polyline> polylineList, LatLng l1, LatLng l2, Context context, float curSpeed) {
        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(l1, l2));
        polyline.setEndCap(new ButtCap());
        polyline.setWidth(10);

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

    public void updateLocationUI(GoogleMap mMap, Context context) {
        try {
            if (MainActivity.locPermissionEnabled) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(false);
                mMap.getUiSettings().setTiltGesturesEnabled(false);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission(context);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
