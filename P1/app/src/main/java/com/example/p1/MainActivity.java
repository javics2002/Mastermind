package com.example.p1;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aninterface.IFile;
import com.example.aninterface.Scene;
import com.example.aninterface.Sound;
import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.Button;
import com.example.logiclib.GameData;
import com.example.logiclib.InitialScene;
//Auncios
import com.example.logiclib.Level;
import com.example.logiclib.LevelData;
import com.example.logiclib.WorldData;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import android.util.Log;
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private EngineAndroid _engineAndroid;
    //Anuncios
    private AdView mAdView;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senProximity;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;

    //Crear un sonido para cuando se agita
    private Sound _shakeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView sf = new SurfaceView(this);

        _engineAndroid = new EngineAndroid(sf,this);

        Scene firstScene = new InitialScene(_engineAndroid);
        _engineAndroid.setCurrentScene(firstScene);
        _engineAndroid.resume();


        // Initialisation of sensor shake sound
        _shakeSound = _engineAndroid.getAudio().loadSound("shake.wav", false);
        _shakeSound.setVolume(.5f);

        // Init Game Data
        GameData.Init(_engineAndroid);
        loadGameData();

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
    }

    private void loadGameData() {
        String fileName = "GameData.json";
        boolean doesSaveExist = false;

        try {
            FileInputStream fileInputStream = _engineAndroid.getActivity().openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            doesSaveExist = true;

            int worldNumber = objectInputStream.readInt();

            for (int i = 0; i < worldNumber; i++){
                WorldData worldData = (WorldData) objectInputStream.readObject();
                GameData.Instance().addWorld(worldData);
            }

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String[] folderWorldNames = _engineAndroid.getFileNames("Levels");
        int numberOfWorlds = GameData.Instance().numberOfWorlds();
        boolean[] loadedSaveDataWorld = new boolean[numberOfWorlds];

        if (doesSaveExist) {
            for (int i = 0; i < folderWorldNames.length; i++){
                boolean loadedFolder = false;

                for (int j = 0; j < numberOfWorlds; j++){
                    WorldData worldData = GameData.Instance().getWorldDataByIndex(j);
                    String saveWorldName = worldData.getWorldName();

                    if (saveWorldName.equals(folderWorldNames[i])){
                        // World exists in save file
                        loadedFolder = true;
                        loadedSaveDataWorld[j] = true;

                        // Check number of levels and overwrite last level unlocked
                        // in case we need to
                        String[] levels = _engineAndroid.getFileNames("Levels/" + folderWorldNames[i]);

                        worldData.setLevelNumber(levels.length);
                        if (worldData.getLastLevelUnlocked() > levels.length){
                            worldData.setLastLevelUnlocked(levels.length);
                        }

                        break;
                    }
                }

                if (!loadedFolder){
                    // World does not exist in save file
                    WorldData newWorld = new WorldData();
                    newWorld.setWorldName(folderWorldNames[i]);

                    String[] levels = _engineAndroid.getFileNames("Levels/" + folderWorldNames[i]);
                    newWorld.setLevelNumber(levels.length);

                    GameData.Instance().addWorld(newWorld);
                }
            }

            for (int i = 0; i < numberOfWorlds; i++){
                if (!loadedSaveDataWorld[i]){
                    // File does exist in save data, but does not exist in folder.
                    // We have to delete the info of the world.
                    GameData.Instance().deleteWorldByIndex(i);
                }
            }
        }
        else { // File does not exist, we need to create world data from assets
            for (int i = 0; i < folderWorldNames.length; i++) {
                WorldData newWorld = new WorldData();
                newWorld.setWorldName(folderWorldNames[i]);

                String[] levels = _engineAndroid.getFileNames("Levels/" + folderWorldNames[i]);
                newWorld.setLevelNumber(levels.length);

                GameData.Instance().addWorld(newWorld);
            }
        }
    }

    private void saveGameData() {
        String fileName = "GameData.json";

        try {
            FileOutputStream fileOutputStream = _engineAndroid.getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            int numberOfWorlds = GameData.Instance().numberOfWorlds();
            objectOutputStream.writeInt(numberOfWorlds);

            for (int i = 0; i < numberOfWorlds;i++){
                WorldData world = GameData.Instance().getWorldDataByIndex(i);
                objectOutputStream.writeObject(world);
            }

            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSensor() {
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (senSensorManager == null)Log.d("","El dispositivo no registra sensores");
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (senAccelerometer == null)Log.d("","El dispositvo no tiene acelerometro");
        senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (senProximity == null)Log.d("","El dispositvo no tiene sensor de Proximidad");
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);


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
        saveGameData();
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
                    _shakeSound.play();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        } else if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
            // Obtenemos la distancia en centímetros
            float distance = event.values[0];
            if (distance < 5) {
                _shakeSound.play();
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
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("game_channel" , "GameChannel", NotificationManager.IMPORTANCE_DEFAULT);
        }

        NotificationManager notificationManager = getSystemService(NotificationManager. class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel) ;
        }
    }
}

