package com.example.p1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.aninterface.Engine;
import com.example.aninterface.Sound;

public class SensorManagerWrapper implements SensorEventListener {
    private SensorManager _senSensorManager;
    private Sensor _senAccelerometer;
    private Sensor _senProximity;
    private long _lastUpdate = 0;
    private float _lastX, _lastY, _lastZ;
    private static final int SHAKE_THRESHOLD = 1000;
    private final int _cmDistance = 5;
    private Sound _shakeSound;

    public SensorManagerWrapper(Context context, Engine engine) {
        _senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        _senAccelerometer = _senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _senProximity = _senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        _senSensorManager.registerListener(this, _senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        _senSensorManager.registerListener(this, _senProximity, SensorManager.SENSOR_DELAY_NORMAL);

        // Initialisation of sensor shake sound
        _shakeSound = engine.getAudio().loadSound("shake.wav", false);
        _shakeSound.setVolume(.5f);
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

                float speed = Math.abs(x + y + z - _lastX - _lastY - _lastZ) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    _shakeSound.play();
                }

                _lastX = x;
                _lastY = y;
                _lastZ = z;
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