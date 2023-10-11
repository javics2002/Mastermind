package com.example.libenginepc;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
public class EnginePC implements Runnable, Engine {
    private JFrame myView;           //VENTANA
    private Thread renderThread;    //HILO DE RENDER
    private boolean running;        //BOOLEANO PARA SABER SI LA APLIACION ESTA CORRIENDO

    private State currentScene;    //ESCENA ACTUAL
    private GraphicsPC graphics;    //GRAPHICS QUE DIBUJA


    public EnginePC(JFrame myView, int logicWidth, int logicHeight) {
        this.myView = myView;
        //this.input = new InputPC();
        this.graphics = new GraphicsPC(this.myView, logicWidth, logicHeight);
    }

    protected void update(double deltaTime) { this.currentScene.update(deltaTime); } //UPDATE DE LA ESCENA
    // Bucle principal
    @Override
    public void run() {
        if (this.renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        //Para el control del tiempo vamos a usar el tiempo
        //El problema que he tenido es que se da en nano segundos y lo he tenido que convertir a segundos
        //Primero hacemos una comprobacion para obtener el tiempo del frame cuando empezamos
        while (this.running && this.myView.getWidth() == 0) ;
        long lastFrameTime = System.nanoTime();
        while (this.currentScene != null) { //this.running -> Calculamos (asi se hacia para los timers )el tiempo pasado en nanosegundos
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;



            //UPDATE CON EL TIEMPO A SEGUNDOS
            // Esto es el numero de nanosegundos en un segundo -> 1.0E9 Lo he buscado en internet
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            this.update(elapsedTime);


            //render
            this.graphics.prepareFrame();
            this.render();
            this.graphics.show();
            //terminar Frame , liberar el recurso
            this.graphics.finishFrame();



        }

    }

    protected void render() {
        // "Borramos" el fondo.
        this.graphics.clear(0xe7d6bd);

        // Pintamos la escena
        this.currentScene.render(this.graphics);

        //SI ESTE COMENTARIO NO SE HA BORRADO SIGNIFICA QUE NO HE CONSEGUIDO DIBUJAR  LOS FACKIN  BORDES PANZA

    }
    //Ahora viene lo mas bonito que me ha tocado programar en pleno 2k23
    //Esto es el maravilloso codigo de sincronizacion con los hilos para parar la puñetera aplicacion
    //Te juro javi y rayo que esto parece facil pero no ha sido asi

    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    //Hasta que me he dado cuenta del join....
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca, aunque ya no se nada...
                }
            }
        }
    }

    //Esto si que ha sido mas chill , definicion de metodos getters
    @Override
    public Graphics getGraphics() {
        return this.graphics;
    }
    @Override
    public State getState() {
        return this.currentScene;
    }
    @Override
    public void setCurrentScene(State currentScene) {  //CAMBIO ESCENA
        this.currentScene = currentScene;
    }
}