package com.example.libengineandroid;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.aninterface.Sound;

public class SensorManagerAndroid implements SensorEventListener {
    private SensorManager _senSensorManager;
    private Sensor _senAccelerometer;
    private Sensor _senProximity;
    private long _lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;
    private final int _cmDistance =5;

    private Sound _shakeSound;

    public SensorManagerAndroid(Context context, Sound shakeSound) {
        _senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        _senAccelerometer = _senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _senProximity = _senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        _senSensorManager.registerListener(this, _senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        _senSensorManager.registerListener(this, _senProximity, SensorManager.SENSOR_DELAY_NORMAL);

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

            if ((curTime - _lastUpdate) > 100) {
                long diffTime = (curTime - _lastUpdate);
                _lastUpdate = curTime;

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
            if (distance < _cmDistance) {
                _shakeSound.play();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void registerListener() {
        boolean isAccelerometerListenerRegistered = _senSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).contains(_senAccelerometer);
        boolean isProximityListenerRegistered = _senSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).contains(_senProximity);
        if(!isAccelerometerListenerRegistered &&!isProximityListenerRegistered) {
            _senSensorManager.registerListener(this, _senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            _senSensorManager.registerListener(this, _senProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListener() {
        _senSensorManager.unregisterListener(this);
    }
}
