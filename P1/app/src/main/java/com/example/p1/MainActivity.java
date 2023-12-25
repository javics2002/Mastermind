package com.example.p1;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aninterface.Scene;
import com.example.aninterface.Sound;
import com.example.libengineandroid.EngineAndroid;
import com.example.logiclib.Background;
import com.example.logiclib.Circles;
import com.example.logiclib.GameData;
import com.example.logiclib.GameScene;
import com.example.logiclib.InitialScene;
//Auncios
import com.example.logiclib.LevelData;
import com.example.logiclib.Theme;
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

import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity  {
    private EngineAndroid _engineAndroid;
    //Anuncios
    private AdView mAdView;

    private  SensorManagerWrapper sensorManagerWrapper;
    //Crear un sonido para cuando se agita
    private Sound _shakeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView sf = new SurfaceView(this);

        _engineAndroid = new EngineAndroid(sf,this);


        // Initialisation of sensor shake sound
        _shakeSound = _engineAndroid.getAudio().loadSound("shake.wav", false);
        _shakeSound.setVolume(.5f);

        // Init Game Data
        GameData.Init();
        loadGameData();

        Scene firstScene = new InitialScene(_engineAndroid);

        if (GameData.Instance().getCurrentLevelData() != null) {
            LevelData data = GameData.Instance().getCurrentLevelData();
            Scene scene = new GameScene(_engineAndroid, data.attempts, data.leftAttemptsNumber, data.codeSize, data.codeOpt,
                    data.repeat, firstScene, data.worldID, data.levelID, data.resultCombination);
            _engineAndroid.setCurrentScene(scene);
        }
        else{
            _engineAndroid.setCurrentScene(firstScene);
            _engineAndroid.resume();
        }

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
        createNotificationsChannel();
        // Inicializar el wrapper del sensor
        sensorManagerWrapper = new SensorManagerWrapper(this, _shakeSound);
        sensorManagerWrapper.registerListener();
    }

    private void loadGameData() {
        String fileName = "GameData.json";
        boolean doesSaveExist = false;

        try {
            FileInputStream fileInputStream = _engineAndroid.getActivity().openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            String currentFileContent = "";

            int worldNumber = objectInputStream.readInt();
            currentFileContent += worldNumber;

            for (int i = 0; i < worldNumber; i++){
                WorldData worldData = (WorldData) objectInputStream.readObject();
                GameData.Instance().addWorld(worldData);
                currentFileContent += worldData.toString();
            }

            int money = objectInputStream.readInt();
            GameData.Instance().addMoney(money);
            currentFileContent += money;

            int backgroundNumber = objectInputStream.readInt();
            currentFileContent += backgroundNumber;

            for(int i = 0; i < backgroundNumber; i++){
                Background background = (Background) objectInputStream.readObject();
                currentFileContent += background.toString();
                GameData.Instance().addBackground(background);
            }

            int currentBackground = objectInputStream.readInt();
            GameData.Instance().setBackground(currentBackground);
            currentFileContent += currentBackground;

            int circlesNumber = objectInputStream.readInt();
            currentFileContent += circlesNumber;

            for(int i = 0; i < circlesNumber; i++){
                Circles circles = (Circles) objectInputStream.readObject();
                currentFileContent += circles.toString();
                GameData.Instance().addCircles(circles);
            }

            int currentCircle = objectInputStream.readInt();
            GameData.Instance().setCircles(currentCircle);
            currentFileContent += currentCircle;

            int themesNumber = objectInputStream.readInt();
            currentFileContent += themesNumber;

            for(int i = 0; i < themesNumber; i++){
                Theme theme = (Theme) objectInputStream.readObject();
                currentFileContent += theme;
                GameData.Instance().addTheme(theme);
            }

            int currentTheme = objectInputStream.readInt();
            GameData.Instance().setTheme(currentTheme);
            currentFileContent += currentTheme;

            // Load LevelData
            LevelData newData = (LevelData) objectInputStream.readObject();
            GameData.Instance().setCurrentLevelData(newData);

            if (newData != null){
                currentFileContent += newData.toString();
            }

            // HASH
            String saveHash = (String) objectInputStream.readObject();

            String salt = "contrasenya";

            String hash = hashJson(currentFileContent);
            String finalHash = hashJson(hash + currentFileContent + salt);

            doesSaveExist = saveHash.equals(finalHash);

            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] worldFolderNames = _engineAndroid.getFileNames("Levels");
        int numberOfWorlds = GameData.Instance().numberOfWorlds();
        boolean[] loadedWorldSaveData = new boolean[numberOfWorlds];

        if (doesSaveExist) {
            for (int i = 0; i < worldFolderNames.length; i++){
                boolean loadedFolder = false;

                for (int j = 0; j < numberOfWorlds; j++){
                    WorldData worldData = GameData.Instance().getWorldDataByIndex(j);
                    String saveWorldName = worldData.getWorldName();

                    if (saveWorldName.equals(worldFolderNames[i])){
                        // World exists in save file
                        loadedFolder = true;
                        loadedWorldSaveData[j] = true;

                        // Check number of levels and overwrite last level unlocked
                        // in case we need to
                        String[] levels = _engineAndroid.getFileNames("Levels/" + worldFolderNames[i]);

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
                    newWorld.setWorldName(worldFolderNames[i]);

                    String[] levels = _engineAndroid.getFileNames("Levels/" + worldFolderNames[i]);
                    newWorld.setLevelNumber(levels.length);

                    GameData.Instance().addWorld(newWorld);
                }
            }

            int erasedFiles = 0;
            for (int i = 0; i < numberOfWorlds; i++){
                if (!loadedWorldSaveData[i]){
                    // File does exist in save data, but does not exist in folder.
                    // We have to delete the info of the world.
                    GameData.Instance().deleteWorldByIndex(i - erasedFiles);
                    erasedFiles++;
                }
            }
        }
        else { // File does not exist, we need to create world data from assets
            for (int i = 0; i < worldFolderNames.length; i++) {
                WorldData newWorld = new WorldData();
                newWorld.setWorldName(worldFolderNames[i]);

                String[] levels = _engineAndroid.getFileNames("Levels/" + worldFolderNames[i]);
                newWorld.setLevelNumber(levels.length);

                GameData.Instance().addWorld(newWorld);
            }
        }

        String[] backgroundsFolderNames = _engineAndroid.getFileNames("Shop/Backgrounds");
        String[] circlesFolderNames = _engineAndroid.getFileNames("Shop/Circles");
        String[] themesFolderNames = _engineAndroid.getFileNames("Shop/Themes");
        int numberOfBackgrounds = GameData.Instance().getBackgrounds().size();
        int numberOfCircles = GameData.Instance().getCircles().size();
        int numberOfThemes = GameData.Instance().getThemes().size();
        boolean[] loadedBackgroundSaveData = new boolean[numberOfBackgrounds];
        boolean[] loadedCirclesSaveData = new boolean[numberOfCircles];
        boolean[] loadedThemesSaveData = new boolean[numberOfThemes];

        if (doesSaveExist) {
            for (int i = 0; i < backgroundsFolderNames.length; i++){
                boolean loadedBackground = false;

                for (int j = 0; j < numberOfBackgrounds; j++){
                    Background background = GameData.Instance().getBackgrounds().get(j);
                    String backgroundName = background.name;

                    if (backgroundName.equals(backgroundsFolderNames[i])){
                        // Background exists in save file
                        loadedBackground = true;
                        loadedBackgroundSaveData[j] = true;

                        break;
                    }
                }

                if (!loadedBackground){
                    // Background does not exist in save file
                    final Background background = _engineAndroid.jsonToObject("Shop/Backgrounds/"
                            + backgroundsFolderNames[i], Background.class);
                    background.name = backgroundsFolderNames[i];

                    GameData.Instance().addBackground(background);
                }
            }

            int erasedFiles = 0;
            for (int i = 0; i < numberOfBackgrounds; i++){
                if (!loadedBackgroundSaveData[i]){
                    // File does exist in save data, but does not exist in folder.
                    // We have to delete the info of the world.
                    GameData.Instance().removeBackgroundByIndex(i - erasedFiles);

                    if(GameData.Instance().getCurrentBackground() > i - erasedFiles)
                        GameData.Instance().setBackground(GameData.Instance().getCurrentBackground() - 1);
                    else if(GameData.Instance().getCurrentBackground() == i - erasedFiles)
                        GameData.Instance().setBackground(-1);

                    erasedFiles++;
                }
            }

            for (int i = 0; i < circlesFolderNames.length; i++){
                boolean loadedCircles = false;

                for (int j = 0; j < numberOfCircles; j++){
                    Circles circles = GameData.Instance().getCircles().get(j);
                    String circlesName = circles.name;

                    if (circlesName.equals(circlesFolderNames[i])){
                        // Circles exist in save file
                        loadedCircles = true;
                        loadedCirclesSaveData[j] = true;

                        break;
                    }
                }

                if (!loadedCircles){
                    // Background does not exist in save file
                    final Circles circles = _engineAndroid.jsonToObject("Shop/Circles/"
                            + circlesFolderNames[i], Circles.class);
                    circles.name = circlesFolderNames[i];

                    GameData.Instance().addCircles(circles);
                }
            }

            erasedFiles = 0;
            for (int i = 0; i < numberOfCircles; i++){
                if (!loadedCirclesSaveData[i]){
                    // File does exist in save data, but does not exist in folder.
                    // We have to delete the info of the world.
                    GameData.Instance().removeCirclesByIndex(i - erasedFiles);

                    if(GameData.Instance().getCurrentCircles() > i - erasedFiles)
                        GameData.Instance().setCircles(GameData.Instance().getCurrentCircles() - 1);
                    else if(GameData.Instance().getCurrentCircles() == i - erasedFiles)
                        GameData.Instance().setCircles(-1);

                    erasedFiles++;
                }
            }

            for (int i = 0; i < themesFolderNames.length; i++){
                boolean loadedTheme = false;

                for (int j = 0; j < numberOfThemes; j++){
                    Theme theme = GameData.Instance().getThemes().get(j);
                    String themeName = theme.name;

                    if (themeName.equals(themesFolderNames[i])){
                        // Theme exists in save file
                        loadedTheme = true;
                        loadedThemesSaveData[j] = true;

                        break;
                    }
                }

                if (!loadedTheme){
                    // Background does not exist in save file
                    final Theme theme = _engineAndroid.jsonToObject("Shop/Themes/"
                            + themesFolderNames[i], Theme.class);
                    theme.name = themesFolderNames[i];

                    GameData.Instance().addTheme(theme);
                }
            }

            erasedFiles = 0;
            for (int i = 0; i < numberOfThemes; i++){
                if (!loadedThemesSaveData[i]){
                    // File does exist in save data, but does not exist in folder.
                    // We have to delete the info of the world.
                    GameData.Instance().removeThemesByIndex(i - erasedFiles);

                    if(GameData.Instance().getCurrentTheme() > i - erasedFiles)
                        GameData.Instance().setTheme(GameData.Instance().getCurrentTheme() - 1);
                    else if(GameData.Instance().getCurrentTheme() == i - erasedFiles)
                        GameData.Instance().setTheme(-1);


                    erasedFiles++;
                }
            }
        }
        else { // File does not exist, we need to create world data from assets
            for (int i = 0; i < backgroundsFolderNames.length; i++) {
                final Background background = _engineAndroid.jsonToObject("Shop/Backgrounds/"
                        + backgroundsFolderNames[i], Background.class);
                background.name = backgroundsFolderNames[i];

                GameData.Instance().addBackground(background);
            }

            for (int i = 0; i < circlesFolderNames.length; i++) {
                final Circles circles = _engineAndroid.jsonToObject("Shop/Circles/"
                        + circlesFolderNames[i], Circles.class);
                circles.name = circlesFolderNames[i];

                GameData.Instance().addCircles(circles);
            }

            for (int i = 0; i < themesFolderNames.length; i++) {
                final Theme theme = _engineAndroid.jsonToObject("Shop/Themes/"
                        + themesFolderNames[i], Theme.class);
                theme.name = themesFolderNames[i];

                GameData.Instance().addTheme(theme);
            }
        }
    }

    private void saveGameData() {
        String fileName = "GameData.json";

        try {
            FileOutputStream fileOutputStream = _engineAndroid.getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            String currentFileContent = "";

            int numberOfWorlds = GameData.Instance().numberOfWorlds();
            objectOutputStream.writeInt(numberOfWorlds);
            currentFileContent += numberOfWorlds;

            for (int i = 0; i < numberOfWorlds;i++){
                WorldData world = GameData.Instance().getWorldDataByIndex(i);
                objectOutputStream.writeObject(world);
                currentFileContent += world.toString();
            }

            int money = GameData.Instance().getMoney();
            objectOutputStream.writeInt(money);
            currentFileContent += money;

            int backgroundsNumber = GameData.Instance().getBackgrounds().size();
            objectOutputStream.writeInt(backgroundsNumber);
            currentFileContent += backgroundsNumber;

            for(int i = 0; i < backgroundsNumber; i++){
                Background background = GameData.Instance().getBackgrounds().get(i);
                objectOutputStream.writeObject(background);
                currentFileContent += background.toString();
            }

            int currentBackground = GameData.Instance().getCurrentBackground();
            objectOutputStream.writeInt(currentBackground);
            currentFileContent += currentBackground;

            int circlesNumber = GameData.Instance().getCircles().size();
            objectOutputStream.writeInt(circlesNumber);
            currentFileContent += circlesNumber;

            for(int i = 0; i < circlesNumber; i++){
                Circles circles = GameData.Instance().getCircles().get(i);
                objectOutputStream.writeObject(circles);
                currentFileContent += circles.toString();
            }

            int currentCircle = GameData.Instance().getCurrentCircles();
            objectOutputStream.writeInt(currentCircle);
            currentFileContent += currentCircle;

            int themesNumber = GameData.Instance().getThemes().size();
            objectOutputStream.writeInt(themesNumber);
            currentFileContent += themesNumber;

            for(int i = 0; i < themesNumber; i++){
                Theme theme = GameData.Instance().getThemes().get(i);
                objectOutputStream.writeObject(theme);
                currentFileContent += theme.toString();
            }

            int currentTheme = GameData.Instance().getCurrentTheme();
            objectOutputStream.writeInt(currentTheme);
            currentFileContent += currentTheme;

            // Save LevelData
            LevelData data = GameData.Instance().getCurrentLevelData();
            objectOutputStream.writeObject(data);

            if (data != null){
                currentFileContent += data.toString();
            }

            // HASH
            String salt = "contrasenya";

            String hash = hashJson(currentFileContent);
            String finalHash = hashJson(hash + currentFileContent + salt);

            objectOutputStream.writeObject(finalHash);

            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        saveGameData();
        _engineAndroid.pause();
        sensorManagerWrapper.unregisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _engineAndroid.resume();
        sensorManagerWrapper.registerListener();

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
        sensorManagerWrapper.unregisterListener();

    }

    protected void onDestroy() {
        super.onDestroy();
        GameData.Release();
        sensorManagerWrapper.unregisterListener();
    }





    private void createOneTimeNotification() {
        WorkRequest workRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        // Additional configuration
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

    private native String hashJson(String fileContent);
}

