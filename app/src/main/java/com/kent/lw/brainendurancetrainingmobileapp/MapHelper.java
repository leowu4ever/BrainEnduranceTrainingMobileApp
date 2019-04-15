package com.kent.lw.brainendurancetrainingmobileapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {


    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static int MAX_DISTANCE_UPDATE_THRESHOLD = 100;
    public static float MAP_ZOOM = 15f;
    private final int MAP_UPDATE_INTERVAL = 3000;
    private int MIN_DISTANCE_UPDATE_THRESHOLD = 10;

    private static final String ID_START_MARKER_SOURCE = "ID_START_MARKER_SOURCE";
    private static final String ID_START_MARKER_LAYER = "ID_START_MARKER_LAYER";
    private static final String IMG_START_MARKER = "IMG_START_MARKER";
    private static final String ID_FINISH_MARKER_SOURCE = "ID_FINISH_MARKER_SOURCE";
    private static final String ID_FINISH_MARKER_LAYER = "ID_FINISH_MARKER_LAYER";
    private static final String IMG_FINISH_MARKER = "IMG_FINISH_MARKER";
    private static final float ICON_SIZE = 0.15f;

    private List<LineLayer> polylineLayerList = new ArrayList<LineLayer>();
    private List<GeoJsonSource> polylineSourceList = new ArrayList<GeoJsonSource>();
    private String srcStart, srcEnd;
    private String lyrStart, lyrEnd;

    public MapHelper(MapboxMap mapboxMap, Context context){
        Style style =  mapboxMap.getStyle();
        style.addImage(IMG_START_MARKER, BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_marker_start));

        style.addImage(IMG_FINISH_MARKER, BitmapFactory.decodeResource(
                context.getResources(), R.drawable.ic_marker_finish));
    }

    public LocationEngineRequest initLocationRequestSettings() {
        return new LocationEngineRequest.Builder(MAP_UPDATE_INTERVAL)
                .setMaxWaitTime(MAP_UPDATE_INTERVAL)
                .setFastestInterval(MAP_UPDATE_INTERVAL)
                .setDisplacement(MIN_DISTANCE_UPDATE_THRESHOLD)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    public static void getLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context.getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context.getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            MainActivity.locPermissionEnabled = true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void updateLocationUI(MapboxMap map, Context context) {
        try {
            if (MainActivity.locPermissionEnabled) {
                //map.getUiSettings().setScrollGesturesEnabled(false);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setZoomGesturesEnabled(false);
                map.getUiSettings().setTiltGesturesEnabled(false);
                map.getUiSettings().setRotateGesturesEnabled(false);
            } else {
                //map.setMyLocationEnabled(false);
                //map.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission(context);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void zoomToLoc(MapboxMap mapboxMap, LatLng loc) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude()-0.0025
                , loc.getLongitude()), MAP_ZOOM);
        mapboxMap.animateCamera(cameraUpdate, 1200);
    }


    public void addStartMarker(MapboxMap mapboxMap, LatLng loc) {
        Style style = mapboxMap.getStyle();
        String unique = String.valueOf(System.currentTimeMillis());

        srcStart = ID_START_MARKER_SOURCE + unique;
        lyrStart = ID_START_MARKER_LAYER + unique;
        GeoJsonSource source = new GeoJsonSource(srcStart, Feature.fromGeometry(Point.fromLngLat(loc.getLongitude(), loc.getLatitude())));
        SymbolLayer layer = new SymbolLayer(lyrStart, srcStart)
                .withProperties(
                        PropertyFactory.iconImage(IMG_START_MARKER),
                        PropertyFactory.iconSize(ICON_SIZE),
                        PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconAllowOverlap(true)
                );

        style.addSource(source);
        style.addLayer(layer);
    }

    public void addEndMarker(MapboxMap mapboxMap, LatLng loc) {
        Style style = mapboxMap.getStyle();
        String unique = String.valueOf(System.currentTimeMillis());

        srcEnd = ID_FINISH_MARKER_SOURCE + unique;
        lyrEnd = ID_FINISH_MARKER_LAYER + unique;
        GeoJsonSource source = new GeoJsonSource(srcEnd, Feature.fromGeometry(Point.fromLngLat(loc.getLongitude(), loc.getLatitude())));
        SymbolLayer layer = new SymbolLayer(lyrEnd, srcEnd)
                .withProperties(
                        PropertyFactory.iconImage(IMG_FINISH_MARKER),
                        PropertyFactory.iconSize(ICON_SIZE),
                        PropertyFactory.iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconAllowOverlap(true)
                );

        style.addSource(source);
        style.addLayer(layer);

    }

    public void drawAPolyline(MapboxMap mapboxMap, LatLng l1, LatLng l2, Context context, float curSpeed) {

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

        ArrayList route = new ArrayList();
        route.add(Point.fromLngLat(l1.getLongitude(), l1.getLatitude()));
        route.add(Point.fromLngLat(l2.getLongitude(), l2.getLatitude()));
        String sourceId = "line-source"+polylineSourceList.size();
        GeoJsonSource source = new GeoJsonSource(sourceId, Feature.fromGeometry(LineString.fromLngLats(route)));
        LineLayer layer = new LineLayer("layerId"+polylineSourceList.size(), sourceId).withProperties(
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineGapWidth(0f),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineColor(ContextCompat.getColor(context, color)));

        Style style = mapboxMap.getStyle();
        style.addSource(source);
        style.addLayer(layer);
        polylineSourceList.add(source);
        polylineLayerList.add(layer);
    }

    public void removePolylines(MapboxMap mapboxMap) {
        Style style = mapboxMap.getStyle();

        for (LineLayer layer : polylineLayerList) {
            style.removeLayer(layer);
        }
        for (GeoJsonSource source : polylineSourceList) {
            style.removeSource(source);
        }
        polylineSourceList.clear();
        polylineLayerList.clear();

        //remove start marker
        style.removeSource(srcStart);
        style.removeLayer(lyrStart);
        //style.removeImage(IMG_START_MARKER);

        //remove end marker
        style.removeSource(srcEnd);
        style.removeLayer(lyrEnd);
        //style.removeImage(IMG_FINISH_MARKER);

    }

    public LatLng convertToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public float getDistanceInKM(LatLng l1, LatLng l2) {
        float dis = (float) l1.distanceTo(l2);

        return dis / 1000;
    }
}
