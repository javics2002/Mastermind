package com.example.p1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.aninterface.Sound;

public class SensorManagerWrapper implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senProximity;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;
    private final int cmDistance=5;

    private Sound _shakeSound;

    public SensorManagerWrapper(Context context, Sound shakeSound) {
        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);

        _shakeSound = shakeSound;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    _shakeSound.play();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
        if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            if (distance < cmDistance) {
                _shakeSound.play();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void registerListener() {
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener() {
        senSensorManager.unregisterListener(this);
    }
}