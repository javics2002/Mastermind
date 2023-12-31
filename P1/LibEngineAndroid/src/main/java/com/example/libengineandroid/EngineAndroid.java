package com.example.libengineandroid;

import android.content.res.AssetManager;
import android.view.SurfaceView;
import android.view.View;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

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

    // Constructor
    public EngineAndroid(SurfaceView myView,float aspectRatio , int logicHeight) {


        _surfaceView = myView;
        _input = new InputAndroid();
        // Administrador de activos del juego
        AssetManager _assetManager = myView.getContext().getAssets(); // Obtiene el administrador de activos del contexto de la vista
        myView.setOnTouchListener((View.OnTouchListener) _input.getTouchHandler()); // Configura el manejador de eventos táctiles
        _graphics = new GraphicsAndroid(_surfaceView, _assetManager, (int) (logicHeight * aspectRatio), logicHeight);
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
}