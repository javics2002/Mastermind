package com.example.libengineandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;


import com.example.logiclib.Background;
import com.example.logiclib.Circles;
import com.example.logiclib.GameData;
import com.example.logiclib.LevelData;
import com.example.logiclib.Theme;
import com.example.logiclib.WorldData;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Esta clase representa un motor de juego para Android que implementa la interfaz Runnable y la interfaz Engine.
public class EngineAndroid implements Runnable, Engine {
    // Atributos
    private final SurfaceView _surfaceView; // Superficie de renderización
    private Thread _renderThread; // Hilo de renderizado
    private boolean _running; // Indica si el motor está en ejecución
    private Scene _currentScene; // Escena actual
    private final InputAndroid _input; // Manejador de entrada
    private final GraphicsAndroid _graphics; // Motor de renderizado
    private final AudioAndroid _audio;
    private Activity activity;
    private AndroidRewardedAd _rewardedAd;
    private OnUserEarnedRewardListener _rewardEarnedCallback;
    private final Gson _gson;


    // Constructor
    public EngineAndroid(SurfaceView myView, float aspectRatio, int logicHeight) {
        _surfaceView = myView;
        _input = new InputAndroid();

        //Cracion del anuncio recompensado
        createRewardedAd();
        AssetManager _assetManager = myView.getContext().getAssets(); // Obtiene el administrador de activos del contexto de la vista
        myView.setOnTouchListener((View.OnTouchListener) _input.getTouchHandler()); // Configura el manejador de eventos táctiles
        _graphics = new GraphicsAndroid(_surfaceView, _assetManager, (int) (height * aspectRatio), height);
        _audio = new AudioAndroid(myView.getContext());

        _gson= new Gson();
    }

	@Override
	public void run() {
		// Espera a que el motor esté en ejecución y el ancho de la superficie sea válido
		while (_running && _surfaceView.getWidth() == 0) ;

		long lastFrameTime = System.nanoTime();

		// Bucle principal del motor
		while (_running) {
			long currTime = System.nanoTime();
			long nanoElTime = currTime - lastFrameTime;
			lastFrameTime = currTime;

			// Procesa eventos de entrada
			handleEvents();

			// Actualiza la lógica del juego
			double elapsedTime = (double) nanoElTime / 1.0E9;
			update(elapsedTime);

			// Renderiza el frame cuando los gráficos estén listos
			while (!_graphics.isValid()) ;
			_graphics.lockCanvas();
			_graphics.prepareFrame();
			render();
			_graphics.unlockCanvas();
		}
	}

    // Método para renderizar la escena actual
    protected void render() {
        _currentScene.render(_graphics);
    }

	// Método para pausar la ejecución del motor
	public void pause() {
		if (_running) {
			_running = false;
			while (true) {
				try {
					// Detiene el hilo de renderizado
					_renderThread.join();
					_renderThread = null;
					break;
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	public void resume() {
		// Si no se estaba ejecutando, inicia la ejecución del motor en un nuevo hilo de renderizado
		if (!_running) {
			_running = true;
			_renderThread = new Thread(this);
			_renderThread.start();
		}
	}

	// Maneja eventos de entrada
	protected void handleEvents() {
		if (_input.getTouchEvents().size() > 0) {
			for (Input.TouchEvent event : _input.getTouchEvents()) {
				_currentScene.handleEvents(event);
			}

			_input.clearEvents();
		}
	}

    @Override
    public  void showAd(){
        _rewardedAd.show();
    }
    private void createRewardedAd() {
        _rewardEarnedCallback = new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Log.d("Ad","User has finished Loading Reward");
            }
        };

        _rewardedAd = new AndroidRewardedAd(this, _rewardEarnedCallback);
    }

    @Override
    public void shareScreenshot(int width, int height) {
        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Create thread
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HandlerThread handlerThread = new HandlerThread("PixelCopier");
            handlerThread.start();
            PixelCopy.request(_surfaceView, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                @Override
                public void onPixelCopyFinished(int copyResult) {
                    if (copyResult == PixelCopy.SUCCESS) {
                        // generar Bitmap a partir de otro dadas unas coordenadas y un tamaño
                        Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

                        File file = saveBitmapToFile(activity, finalBitmap);
                        Uri fileUri = FileProvider.getUriForFile(activity, "com.mydomain.fileprovider", file);
                        //activity.grantUriPermission(activity.getPackageName(), fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(Intent.EXTRA_TEXT, "¡He completado el nivel!");
                        intent.setType("image/png");

                        activity.startActivity(Intent.createChooser(intent, "Share"));
                    }
                    handlerThread.quitSafely();
                }
            }, new Handler(handlerThread.getLooper()));
        }
    }

    private File saveBitmapToFile(Context context, Bitmap bitmap) {
        // Crear un archivo en el directorio de almacenamiento externo
        File file = null;

        try {
            File imagesFolder = new File(context.getCacheDir(), "images");
            imagesFolder.mkdirs();
            file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();

        } catch (IOException e) {
            System.out.println("IOException while trying to write file for sharing: " + e.getMessage());
        }

        return file;
    }

    @Override
    public <T> T jsonToObject(String fileName, Class<T> classOfT) {
        AssetManager assetManager = _surfaceView.getContext().getAssets();

        try {
            // Abre un InputStream para el archivo JSON
            InputStream inputStream = assetManager.open(fileName);

            // Convierte el InputStream a una cadena JSON
            String jsonString = convertInputStreamToString(inputStream);

            // Parsea la cadena JSON a un objeto Java usando Gson

            T jsonObject = _gson.fromJson(jsonString, classOfT);

            // Cierra el InputStream cuando hayas terminado
            inputStream.close();

            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String objectToJson(Object object) {
        return _gson.toJson(object);
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public int filesInFolder(String folderPath) {
        try {
            AssetManager assetManager = _surfaceView.getContext().getAssets();
            String[] files = assetManager.list(folderPath);

            // Comprueba si la lista de archivos es nula o vacía
            if (files == null || files.length == 0)
                return 0;

            // Cuenta el número de archivos con la extensión específica
            int fileCount = 0;
            for (String file : files)
                fileCount++;

            return fileCount;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String[] getFileNames(String folderPath) {
        try {
            AssetManager assetManager = _surfaceView.getContext().getAssets();
            String[] files = assetManager.list(folderPath);

            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Input getInput() {
        return _input;
    }
	// Actualiza la lógica del juego
	protected void update(double deltaTime) {
		_currentScene.update(deltaTime);
	}

	@Override
	public Input getInput() {
		return _input;
	}

	@Override
	public void setCurrentScene(Scene currentScene) {
		_currentScene = currentScene;
	}

	@Override
	public Scene getScene() {
		return _currentScene;
	}

    @Override
    public Audio getAudio() {
        return _audio;
    }

    public Activity getActivity(){ return activity;}

    @Override
    public void loadGameData() {
        String fileName = "GameData.json";
        boolean doesSaveExist = false;

        try {
            FileInputStream fileInputStream = activity.openFileInput(fileName);
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

        String[] worldFolderNames = getFileNames("Levels");
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
                        String[] levels = getFileNames("Levels/" + worldFolderNames[i]);

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

                    String[] levels = getFileNames("Levels/" + worldFolderNames[i]);
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

                String[] levels = getFileNames("Levels/" + worldFolderNames[i]);
                newWorld.setLevelNumber(levels.length);

                GameData.Instance().addWorld(newWorld);
            }
        }

        String[] backgroundsFolderNames = getFileNames("Shop/Backgrounds");
        String[] circlesFolderNames = getFileNames("Shop/Circles");
        String[] themesFolderNames = getFileNames("Shop/Themes");
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
                    final Background background = jsonToObject("Shop/Backgrounds/" + backgroundsFolderNames[i], Background.class);
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
                    final Circles circles = jsonToObject("Shop/Circles/" + circlesFolderNames[i], Circles.class);
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
                    final Theme theme = jsonToObject("Shop/Themes/" + themesFolderNames[i], Theme.class);
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
                final Background background = jsonToObject("Shop/Backgrounds/" + backgroundsFolderNames[i], Background.class);
                background.name = backgroundsFolderNames[i];

                GameData.Instance().addBackground(background);
            }

            for (int i = 0; i < circlesFolderNames.length; i++) {
                final Circles circles = jsonToObject("Shop/Circles/" + circlesFolderNames[i], Circles.class);
                circles.name = circlesFolderNames[i];

                GameData.Instance().addCircles(circles);
            }

            for (int i = 0; i < themesFolderNames.length; i++) {
                final Theme theme = jsonToObject("Shop/Themes/" + themesFolderNames[i], Theme.class);
                theme.name = themesFolderNames[i];

                GameData.Instance().addTheme(theme);
            }
        }
    }

    @Override
    public void saveGameData() {
        String fileName = "GameData.json";

        try {
            FileOutputStream fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
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

    private native String hashJson(String fileContent);
	@Override
	public Graphics getGraphics() {
		return _graphics;
	}
}