package com.example.libenginepc;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import javax.swing.JFrame;

public class EnginePC implements Runnable, Engine {
    private Thread _renderThread;
    private volatile boolean _running;        // Boolean to know if the app is still running
    private Scene _currentScene;
    private final GraphicsPC _graphics;
    private final InputPC _input;
    private AudioPC _audio;


    public EnginePC(JFrame myView,float aspectRatio , int logicHeight) {
        int _logicWidth = (int) (logicHeight * aspectRatio);
        _graphics = new GraphicsPC(myView, _logicWidth, logicHeight);
        _input = new InputPC();
        _audio = new AudioPC();
        myView.addMouseListener(_input.getHandlerInput());
    }

    protected void handleEvents() {
        if (_input.getMouseEvents().size() > 0) {

            for (Input.TouchEvent event : _input.getMouseEvents()){
                _currentScene.handleEvents(event);
            }

            _input.clearEvents();
        }
    }

    protected void update(double deltaTime) {
        _currentScene.update(deltaTime);
    }

    protected void render() {
        _graphics.clear(0xe7d6bd);
        _currentScene.render(_graphics);
    }


    @Override
    public void run() {
        if (_renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        long lastFrameTime = System.nanoTime();

        while (_currentScene != null) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Convertimos el tiempo de nanosegundos a segundos
            double deltaTime = (double) nanoElapsedTime / 1.0E9;

            handleEvents();
            update(deltaTime);

            _graphics.prepareFrame();
            render();
            _graphics.show();
            _graphics.finishFrame();
        }
    }

    /*
     Inicia un proceso en un hilo separado si la aplicación no está en curso.
    */

    public void resume() {
        if (!_running) {
            _running = true;
            _renderThread = new Thread(this);
            _renderThread.start();
        }
    }

    /*
      Pausa el hilo de la aplicación. Es obligatorio hacer el join() dentro de un catch.
    */

    public void pause() {
        if (_running) {
            _running = false;
            try {
                _renderThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            _renderThread = null;
        }
    }

    @Override
    public Graphics getGraphics() {
        return _graphics;
    }

    @Override
    public Scene getScene() {
        return _currentScene;
    }

    @Override
    public Input getInput() {
        return _input;
    }

    public Audio getAudio() {
        return _audio;
    }

    @Override
    public void setCurrentScene(Scene currentScene) {
        _currentScene = currentScene;
    }
}