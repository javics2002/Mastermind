package com.example.p1;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aninterface.Scene;
import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.Button;
import com.example.logiclib.InitialScene;
//Auncios
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private EngineAndroid _engineAndroid;
    //Anuncios
    private AdView mAdView;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;

    //Crear un sonido para cuando se agita

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView sf = new SurfaceView(this);


        _engineAndroid = new EngineAndroid(sf,this);

        Scene firstScene = new InitialScene(_engineAndroid);
        _engineAndroid.setCurrentScene(firstScene);
        _engineAndroid.resume();


        //Anuncios Inicializado
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        //Creacion de un layout relativo para introducir el anuncio
        RelativeLayout layout = new RelativeLayout(this);

        // Se añade el surfaceView a este layout
        layout.addView(sf);
        initialisedownBanner();
        layout.addView(mAdView);


        //Esto es para hacer que se vea
        setContentView(layout);

        createAdRequest();
        createSensor();
        createNotificationsChannel();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null && bundle.getBoolean("reward")) {
            //GameManager.getInstance().addCoins(10);

        }

    }

    private void createSensor() {
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

       // shakeSound =
    }
    @Override
    protected void onStart() {
        super.onStart();

        WorkManager.getInstance(this).cancelAllWork();
    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        _engineAndroid.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        _engineAndroid.resume();


    }
    protected void createAdRequest()
    {
        //Request de anuncios
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    };
    protected void initialisedownBanner()
    {
        mAdView = new AdView(this);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setEnabled(true);
        mAdView.setVisibility(View.VISIBLE);

        //Hacemos que se situe en la parte de abajo
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                AdSize.AUTO_HEIGHT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mAdView.setLayoutParams(adParams);
    }
    @Override
    protected void onStop() {
        super.onStop();
        createOneTimeNotification();
        createPeriodicNotification();


    }

    protected void onDestroy() {
        super.onDestroy();


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
                   // shakeSound.play();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void createOneTimeNotification() {
        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        // Additional configuration
                        .setInitialDelay(30, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }

    private void createPeriodicNotification() {
        PeriodicWorkRequest workRequestPeriodic =
                new PeriodicWorkRequest.Builder(NotificationWorker.class,
                        15, TimeUnit.MINUTES,
                        5, TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance(this).enqueue(workRequestPeriodic);
    }

    private void createNotificationsChannel() {
        NotificationChannel channel = new NotificationChannel("game_channel" , "GameChannel", NotificationManager.IMPORTANCE_DEFAULT) ;

        NotificationManager notificationManager = getSystemService(NotificationManager. class);
        notificationManager.createNotificationChannel(channel) ;
    }
}

