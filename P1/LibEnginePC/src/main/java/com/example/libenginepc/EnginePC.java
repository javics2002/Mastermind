package com.example.libenginepc;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
import java.awt.event.*;
public class EnginePC implements Runnable, Engine {
    private JFrame myView;           // Window
    private Thread renderThread;
    private boolean running;        // Boolean to know if the app is still running
    private State currentScene;
    private GraphicsPC graphics;
    private InputPC input;

    public EnginePC(JFrame myView, int logicWidth, int logicHeight,int windowSX, int windowSY) {
        this.myView = myView;
        this.graphics = new GraphicsPC(this.myView, logicWidth, logicHeight,windowSX,  windowSY);
        this.input = new InputPC();
        myView.addMouseListener(this.input.getHandlerInput());
    }

    protected void update(double deltaTime) {
        this.currentScene.update(deltaTime);
    }

    protected void handleEvents() {
        if (this.input.getTouchEvent().size() > 0){
            this.currentScene.handleEvents(input.getTouchEvent().get(0));
            this.input.clearEvents();
        }
    }

    // Main loop
    @Override
    public void run() {
        // Prevent anyone calling run() except for this class
        if (this.renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");

        long lastFrameTime = System.nanoTime();

        while (this.currentScene != null) {
            long currentTime =  System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Convert time from nanoseconds to seconds
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            this.update(elapsedTime);
            this.handleEvents();

                //render
            this.graphics.prepareFrame();
            this.render();
            this.graphics.show();
            //terminar Frame
            this.graphics.finishFrame();


        }

    }

    protected void render() {
        this.graphics.clear(0xe7d6bd);
        this.currentScene.render(this.graphics);
    }

    public void resume() {
        if (!this.running) {
            this.running = true;
            // Call run() in a new Thread
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    // TODO: Preguntar a TONI que hacer con los join(), de momento dejo el try/catch
    public void pause() {
        if (this.running) {
            this.running = false;

            try {
                this.renderThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            this.renderThread = null;
        }
    }

    @Override
    public Graphics getGraphics() {
        return this.graphics;
    }
    @Override
    public State getScene() {
        return this.currentScene;
    }

    @Override
    public Input getInput() {
        return this.input;
    }

    @Override
    public void setCurrentScene(State currentScene) {
        this.currentScene = currentScene;
    }
}