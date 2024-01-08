package com.example.libengineandroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Sound;

public class SensorManagerAndroid implements SensorEventListener {
	// Atributos de la clase
	private SensorManager _senSensorManager;
	private Sensor _senAccelerometer;
	private Sensor _senProximity;
	private long _lastUpdate = 0;
	private float _lastX, _lastY, _lastZ;
	private static final int SHAKE_THRESHOLD = 1000;
	private final int _cmDistance = 5;
	private Sound _shakeSound;

	Audio _audio;

	// Constructor
	public SensorManagerAndroid(Context context, Engine engine) {
		// Inicialización del SensorManager y sensores
		_senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		_senAccelerometer = _senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		_senProximity = _senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		// Registro de esta clase como SensorEventListener para ambos sensores
		_senSensorManager.registerListener(this, _senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		_senSensorManager.registerListener(this, _senProximity, SensorManager.SENSOR_DELAY_NORMAL);

		// Inicialización del sonido asociado al "shake"
		_audio = engine.getAudio();
		_shakeSound = _audio.loadSound("shake.wav", false);
		_shakeSound.setVolume(.5f);
	}

	// Método que se llama cuando hay un cambio en los sensores
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor mySensor = event.sensor;

		// Manejo del acelerómetro
		if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			long curTime = System.currentTimeMillis();

			if ((curTime - _lastUpdate) > 100) {
				long diffTime = (curTime - _lastUpdate);
				_lastUpdate = curTime;

				// Cálculo de la velocidad de cambio y reproducción del sonido si supera el umbral
				float speed = Math.abs(x + y + z - _lastX - _lastY - _lastZ) / diffTime * 10000;
				if (speed > SHAKE_THRESHOLD) {
					_audio.playSound(_shakeSound);
				}

				_lastX = x;
				_lastY = y;
				_lastZ = z;
			}
		}

		// Manejo del sensor de proximidad
		if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
			float distance = event.values[0];

			// Reproducción del sonido si la distancia es menor que el umbral definido
			if (distance < _cmDistance) {
				_audio.playSound(_shakeSound);
			}
		}
	}

	// Método llamado cuando cambia la precisión de un sensor
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// No se realiza ninguna acción específica en respuesta a cambios en la precisión del sensor
	}

	// Método para registrar esta clase como oyente de eventos para los sensores
	public void registerListener() {
		boolean isAccelerometerListenerRegistered = _senSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).contains(_senAccelerometer);
		boolean isProximityListenerRegistered = _senSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).contains(_senProximity);

		// Registro solo si no están registrados ya
		if (!isAccelerometerListenerRegistered && !isProximityListenerRegistered) {
			_senSensorManager.registerListener(this, _senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			_senSensorManager.registerListener(this, _senProximity, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	// Método para anular el registro de esta clase como oyente de eventos para los sensores
	public void unregisterListener() {
		_senSensorManager.unregisterListener(this);
	}
}



