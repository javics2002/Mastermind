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
import android.provider.MediaStore;
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
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private AndroidRewardedAd rewardedAd;
    private OnUserEarnedRewardListener rewardEarnedCallback;


    // Constructor
    public EngineAndroid(SurfaceView myView,Activity actividad) {
        float aspectRatio = 2f / 3f;
        int height = 720;
        this.activity = actividad;
        _surfaceView = myView;
        _input = new InputAndroid();

        //Cracion del anuncio recompensado
        createRewardedAd();
        AssetManager _assetManager = myView.getContext().getAssets(); // Obtiene el administrador de activos del contexto de la vista
        myView.setOnTouchListener((View.OnTouchListener) _input.getTouchHandler()); // Configura el manejador de eventos táctiles
        _graphics = new GraphicsAndroid(_surfaceView, _assetManager, (int) (height * aspectRatio), height);
        _audio = new AudioAndroid(myView.getContext());
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
        // Limpia el fondo con un color específico
        getGraphics().clear(0xe7d6bd);
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
                }
                catch (InterruptedException ignored) {
                }
            }
        }
    }

    @Override
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
        _currentScene.handleEvents(_input);
        _input.clearEvents();
    }

    // Actualiza la lógica del juego
    protected void update(double deltaTime) {
        _currentScene.update(deltaTime);
    }

    @Override
    public  void showAd(){
        rewardedAd.show();
    }
    private void createRewardedAd() {
        rewardEarnedCallback = new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Log.d("TAG", "TE HAS GANADO UN ANUNCIO");
            }
        };

        rewardedAd = new AndroidRewardedAd(this, rewardEarnedCallback);
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
                        String bitmapPath = MediaStore.Images.Media.insertImage(activity.getContentResolver(), finalBitmap, "palette", "share palette");
                        Uri test = Uri.parse(bitmapPath);

                        //Uri fileUri = FileProvider.getUriForFile(activity, "com.mydomain.fileprovider", file);

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                        intent.putExtra(Intent.EXTRA_STREAM, test);
                        intent.putExtra(Intent.EXTRA_TEXT, "¡Mira esta imagen!");
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
    public Graphics getGraphics() {
        return _graphics;
    }

    @Override
    public Audio getAudio() {
        return _audio;
    }
    public Activity getActivity(){ return activity;}

    @Override
    public int filesInFolder(String folderPath) {
        return 0;
    }

    @Override
    public String objectToJson(Object object){
        return "";
    }

    @Override
    public <T> T jsonToObject(String fileName, Class<T> classOfT) {
        return null;
    }
}