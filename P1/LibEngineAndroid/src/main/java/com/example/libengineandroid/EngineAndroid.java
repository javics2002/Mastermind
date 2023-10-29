package com.example.libengineandroid;
import android.view.SurfaceView;
import android.view.View;

import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;
//Libreria para la gestion de recursos , es una prueba
import android.content.res.AssetManager;

//Como el motor de pc tiene los mismos atributos mismos metodos
//A parte este motor tambien hereda de runnable y de la interfaz del Engine
public class EngineAndroid implements Runnable,Engine {
    private SurfaceView _surfaceView; //Lo complementario al JFrame
    private AssetManager _assetManager;

    private Thread _renderThread; // Hilo de render
    private boolean _running;     // boolean to know if its running

    private Scene _currentScene; // Escena actual
    private InputAndroid _input;        // Input
    private GraphicsAndroid _graphics;  // Graficos

    public EngineAndroid(SurfaceView myView, int logicWidth, int logicHeight){
        _surfaceView = myView;
        _input = new InputAndroid();
        //El propio sufaceview puede obtener el contexto y devolver asi los assets
        _assetManager = myView.getContext().getAssets();
        //EN LUGAR DE RECIBIR EL THIS.INPUT DEBERIA DE LLAMAR AL TOUCHHANDLER QUE LO DEVUELVA EL INPUIT PARA ASOCIARSELO
        myView.setOnTouchListener((View.OnTouchListener) _input.getTouchHandler());
        _graphics = new GraphicsAndroid(_surfaceView, _assetManager, logicWidth, logicHeight);
    }

    @Override
    public void run() {
        while(_running && _surfaceView.getWidth() == 0);

        long lastFrameTime = System.nanoTime();

        //Calculo de tiempo y bucle principal
        while(_running) {
            long currTime = System.nanoTime();
            long nanoElTime = currTime - lastFrameTime;
            lastFrameTime = currTime;

            //inputs
            handleEvents();

            //update
            double elapsedTime = (double) nanoElTime / 1.0E9;
            update(elapsedTime);
            // Pintamos el frame cuando los graficos puedan
            //Siempre pasamos por el proceso de
            //Bloquear el hilo
            //Preparar lo que vamos a
            while (!_graphics.isValid());
            _graphics.lockCanvas();
            _graphics.prepareFrame();
            render();
            _graphics.unlockCanvas();
        }
    }

    protected void render() {
        //Clear del fondo (con el color tambien igual en el PC
        getGraphics().clear(0xe7d6bd);
        _currentScene.render(_graphics);
    }

    public void pause() {
        if (_running) {
            _running = false;
            while (true) {
                try {
                    //A diferencia de en PC no llamamos a start simplemente hacemos un join
                    _renderThread.join();
                    _renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    @Override
    public void resume() {
        //Si estabamos sin ejecutar ejecutamos de nuevo y creamos un hilo para la ejecuccion( Igual que en PC)
        if (!_running) {
            _running = true;
            _renderThread = new Thread(this);
            _renderThread.start();
        }
    }

    protected void handleEvents() {
        _currentScene.handleEvents(_input);
        _input.clearEvents();
    }

    //Limpieza de los eventos del Input
    protected void clearInputs() {
        _input.clearEvents();
    }

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
}
