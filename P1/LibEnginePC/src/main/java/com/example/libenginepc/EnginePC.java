package com.example.libenginepc;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
public class EnginePC implements Runnable, Engine {
    private JFrame myView;           // Window
    private Thread renderThread;
    private boolean running;        // Boolean to know if the app is still running
    private State currentScene;
    private GraphicsPC graphics;

    public EnginePC(JFrame myView, int logicWidth, int logicHeight,int windowSX, int windowSY) {
        this.myView = myView;
        //this.input = new InputPC();
        this.graphics = new GraphicsPC(this.myView, logicWidth, logicHeight,windowSX,  windowSY);
    }

    protected void update(double deltaTime) {
        this.currentScene.update(deltaTime);
    }

    // Main loop
    @Override
    public void run() {
        // Prevent anyone calling run() except for this class
        if (this.renderThread != Thread.currentThread())
            throw new RuntimeException("run() should not be called directly");


        // TODO: Testear si todo funciona bien, si lo hace, borrar este comentario aleatorio

        //El problema que he tenido es que se da en nano segundos y lo he tenido que convertir a segundos
        //Primero hacemos una comprobacion para obtener el tiempo del frame cuando empezamos
        // while (this.running && this.myView.getWidth() == 0);

        long lastFrameTime = System.nanoTime();

        while (this.currentScene != null) {
            long currentTime =  System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Convert time from nanoseconds to seconds
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            this.update(elapsedTime);

            // Render
            this.graphics.prepareFrame();
            this.render();
            this.graphics.show();
            this.graphics.finishFrame();
        }

    }

    protected void render() {
        this.graphics.clear(0xe7d6bd);
        this.currentScene.render(this.graphics);

        // TODO: Bordes?
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
    public void setCurrentScene(State currentScene) {
        this.currentScene = currentScene;
    }
}