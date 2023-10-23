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

  private SurfaceView myView ; //Lo complementario al JFrame
    private AssetManager mgr;

    private Thread renderThread; // Hilo de render
    private boolean running;     // boolean to know if its running

    private Scene currentScene; // Escena actual
    private InputAndroid input;        // Input
    private GraphicsAndroid graphics;  // Graficos

    public EngineAndroid(SurfaceView myView, int logicWidth, int logicHeight){
        this.myView = myView;


         this.input = new InputAndroid();

         //El propio sufaceview puede obtener el contexto y devolver asi los assets
         this.mgr = myView.getContext().getAssets();
         //EN LUGAR DE RECIBIR EL THIS.INPUT DEBERIA DE LLAMAR AL TOUCHHANDLER QUE LO DEVUELVA EL INPUIT PARA ASOCIARSELO
         this.myView.setOnTouchListener((View.OnTouchListener) this.input);
         this.graphics = new GraphicsAndroid(this.myView, this.mgr, logicWidth, logicHeight);


    }

    @Override
    public void run() {


        while(this.running && this.myView.getWidth() == 0);
        long lastFrameTime = System.nanoTime();

        //Calculo de tiempo y bucle principal
        while(running) {
            long currTime = System.nanoTime();
            long nanoElTime = currTime - lastFrameTime;
            lastFrameTime = currTime;

            //inputs
            this.handleEvents();

            //update
            double elapsedTime = (double) nanoElTime / 1.0E9;
            this.update(elapsedTime);
            // Pintamos el frame cuando los graficos puedan
            //Siempre pasamos por el proceso de
            //Bloquear el hilo
            //Preparar lo que vamos a
            while (!this.graphics.isValid());
            this.graphics.lockCanvas();
            this.graphics.prepareFrame();
            this.render();
            this.graphics.unlockCanvas();


        }
    }
    protected void render() {
        //Clear del fondo (con el color tambien igual en el PC
        this.get_graphics().clear(0xe7d6bd);
        this.currentScene.render(this.graphics);
    }

    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    //A diferencia de en PC no llamamos a start simplemente hacemos un join
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                }
            }
        }
    }


    @Override
    public void resume() {
        //Si estabamos sin ejecutar ejecutamos de nuevo y creamos un hilo para la ejecuccion( Igual que en PC)
      if (!this.running) {
          this.running = true;
          this.renderThread = new Thread(this);
          this.renderThread.start();
      }
  }
    protected void handleEvents() {
        this.currentScene.handleEvents(this.input);

    }

    //Limpieza de los eventos del Input
    protected void clearInputs() {
        this.input.clearEvents();
    }
    protected void update(double deltaTime)
    {
        this.currentScene.update(deltaTime);

    }

    @Override
    public Input get_input () {
        return this.input;
    }

    @Override
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    @Override
    public Scene getScene() {
        return this.currentScene;

    }
    @Override
    public Graphics get_graphics() {
        return this.graphics;
    }




}
