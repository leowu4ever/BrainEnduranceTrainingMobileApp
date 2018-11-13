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

    private int MIN_DISTANCE_UPDATE_THRESHOLD = 10;
    private final int MAP_UPDATE_INTERVAL = 3000;

    public LatLng convertToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public double getDistance(LatLng l1, LatLng l2) {
        return SphericalUtil.computeDistanceBetween(l1, l2);
    }

    public void removePolylines(List<Polyline> polylineList) {
        for (Polyline polyline : polylineList) {
            polyline.remove();
        }
        polylineList.clear();
    }

    public void drawAPolyline(GoogleMap mMap, List<Polyline> polylineList, LatLng l1, LatLng l2, Context context) {
        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(l1, l2));
        polyline.setEndCap(new ButtCap());
        polyline.setWidth(10);
        polyline.setColor(ContextCompat.getColor(context, R.color.colorAccent));
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
            MainActivity.mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void updateLocationUI(GoogleMap mMap, Context context) {

        try {
            if (MainActivity.mLocationPermissionGranted) {
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
