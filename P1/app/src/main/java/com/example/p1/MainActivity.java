package com.example.p1;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aninterface.Scene;
import com.example.libengineandroid.EngineAndroid;
import com.example.libengineandroid.SensorManagerAndroid;
import com.example.logiclib.GameData;
import com.example.logiclib.GameScene;
import com.example.logiclib.InitialScene;
import com.example.logiclib.LevelData;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity  {
    private EngineAndroid _engineAndroid;
    private AdView mAdView;
    private SensorManagerAndroid sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView sf = new SurfaceView(this);
        _engineAndroid = new EngineAndroid(sf,this);

        GameData.Init();
        _engineAndroid.loadGameData();

        Scene firstScene = new InitialScene(_engineAndroid);

        if (GameData.Instance().getCurrentLevelData() != null) {
            LevelData data = GameData.Instance().getCurrentLevelData();
            Scene scene = new GameScene(_engineAndroid, data.attempts, data.leftAttemptsNumber, data.codeSize, data.codeOpt,
                    data.repeat, firstScene, data.worldID, data.levelID, data.reward, data.resultCombination);
            _engineAndroid.setCurrentScene(scene);
        }
        else{
            _engineAndroid.setCurrentScene(firstScene);
            _engineAndroid.resume();
        }

        MobileAds.initialize(this);

        RelativeLayout layout = new RelativeLayout(this);

        layout.addView(sf);
        initialisedownBanner();
        layout.addView(mAdView);

        setContentView(layout);

        createAdRequest();
        createNotificationsChannel();

        sensorManager = new SensorManagerAndroid(this, _engineAndroid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle !=null && bundle.getBoolean("reward")) {
            GameData.Instance().addMoney(20);
        }

        WorkManager.getInstance(this).cancelAllWork();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _engineAndroid.saveGameData();
        _engineAndroid.pause();
        sensorManager.unregisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _engineAndroid.resume();
        sensorManager.registerListener();

    }

    @Override
    protected void onStop() {
        super.onStop();
        createOneTimeNotification();
        createPeriodicNotification();
        sensorManager.unregisterListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameData.Release();
        sensorManager.unregisterListener();
    }

    protected void createAdRequest()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    protected void initialisedownBanner()
    {
        mAdView = new AdView(this);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setEnabled(true);
        mAdView.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams adParams;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // La pantalla está en horizontal
            adParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, // Ancho automático
                    AdSize.AUTO_HEIGHT);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else {
            // La pantalla está en vertical
            adParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    AdSize.AUTO_HEIGHT);
            adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }

        mAdView.setLayoutParams(adParams);
    }

    private void createOneTimeNotification() {
        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        // Configuracion adicional
                        .setInitialDelay(10, TimeUnit.SECONDS)
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

    static {
        System.loadLibrary("PR2");
    }
}

