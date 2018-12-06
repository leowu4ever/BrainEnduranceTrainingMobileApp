package com.kent.lw.brainendurancetrainingmobileapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHelper implements SensorEventListener {

    private final int accSensor = Sensor.TYPE_LINEAR_ACCELERATION;
    private final int gryoSensor = Sensor.TYPE_GYROSCOPE;
    // acc
    private SensorManager sm;
    private Sensor accelerometer, gyroscope;

    public SensorHelper(Context context) {
        initAcc(context);
    }

    // acc
    public void initAcc(Context context) {
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sm.getDefaultSensor(accSensor) != null && sm.getDefaultSensor(gryoSensor) != null) {
            accelerometer = sm.getDefaultSensor(accSensor);
            gyroscope = sm.getDefaultSensor(gryoSensor);

            sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            sm.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (MainActivity.trainingStarted) {
            long timestamp = System.currentTimeMillis();
            if (event.sensor.getType() == accSensor) {
                FileHelper.saveStreamMotionDataToLocal(timestamp + "__" + event.values[0] + " " + event.values[1] + " " + event.values[2] + "\n", "acc");
            }

            if (event.sensor.getType() == gryoSensor) {
                FileHelper.saveStreamMotionDataToLocal(timestamp + "__" + event.values[0] + " " + event.values[1] + " " + event.values[2] + "\n", "gyro");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
