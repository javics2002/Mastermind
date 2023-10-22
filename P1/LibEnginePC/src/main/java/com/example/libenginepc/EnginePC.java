package com.example.libenginepc;

import javax.swing.JFrame;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;

public class EnginePC implements Runnable, Engine {
    private JFrame _frame;
    private Thread _renderThread;
    private boolean _running;        // Boolean to know if the app is still running
    private Scene _currentScene;
    private GraphicsPC _graphics;
    private InputPC _input;

    public EnginePC(JFrame myView, int logicWidth, int logicHeight) {
        _frame = myView;
        _graphics = new GraphicsPC(myView, logicWidth, logicHeight);
        _input = new InputPC();
        _frame.addMouseListener(_input.getHandlerInput());
    }

    protected void handleEvents() {
        if (_input.getTouchEvent().size() > 0){
            _currentScene.handleEvents(_input);
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

    // Main loop
    @Override
    public void run() {
        // Prevent anyone calling run() except for this class
        if (_renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        long lastFrameTime = System.nanoTime();

        while (_currentScene != null) {
            long currentTime =  System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Convert time from nanoseconds to seconds
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            update(elapsedTime);
            handleEvents();

            //render
            _graphics.prepareFrame();
            render();
            _graphics.show();

            //terminar Frame
            _graphics.finishFrame();
        }
    }

    @Override
    public void resume() {
        //Si estabamos sin ejecutar ejecutamos de nuevo y creamos un hilo para la ejecuccion
        if (!_running) {
            _running = true;
            // Call run() in a new Thread
            _renderThread = new Thread(this);
            _renderThread.start();
        }
    }

    // TODO: Preguntar a TONI que hacer con los join(), de momento dejo el try/catch

    @Override
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
    public Graphics get_graphics() {
        return _graphics;
    }

    @Override
    public Scene getScene() {
        return _currentScene;
    }

    @Override
    public Input get_input() {
        return _input;
    }

    @Override
    public void setCurrentScene(Scene currentScene) {
        _currentScene = currentScene;
    }
}