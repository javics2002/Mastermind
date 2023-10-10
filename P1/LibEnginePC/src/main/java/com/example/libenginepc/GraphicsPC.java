package com.example.libenginepc;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.example.aninterface.Graphics;

public class GraphicsPC implements Graphics{

    private JFrame myView;
    private BufferStrategy bufferStrategy;

   //Deberiamos de tener una fuente asociada pero de momento nos quedamos asi
    private Graphics2D graphics2D;

    // Tam logico
    private int logicWidth;
    private int logicHeight;

    // Medidas de bordes
    private int borderWidth;
    private int borderHeight;
    private int borderTop;
    private int window;

    // Factores de escala
    private float factorScale;
    private float factorX;
    private float factorY;


    //Para construir nuestros graficos primero probamos a crear el buffer en varios intentos
    //Esto lo hacemos por que me huele a  que si no pasan problemas de sincronizacion como carreras y eso
    //Creamos dos buffers para evitar el parapadeo (Uno dibuja el otro enseña)
    GraphicsPC(JFrame myView, int logicWidth, int logicHeight) {
        this.myView = myView;
        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while (intentos-- > 0) {
            try {
                this.myView.createBufferStrategy(2);
                break;
            } catch (Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        ;
        //Creamos el buffer y los graficos
        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

        //Ahora vamos a darle valor tanto al factor de escala como  a los bordes(Bandas al rededor del juego)
        /* Explicacion de por que pelotas pongo un inset para obtener los bordes
        * Por ejemplo, supongamos que myView es un JFrame y tiene una barra de título de 30 píxeles de altura.
        * Si el contenedor myView tiene un inset superior de 30 píxeles debido a la barra de título, this.myView.getInsets().top devolverá 30.
        * Esto significa que hay un margen superior de 30 píxeles en el contenedor myView que no puede ser utilizado para colocar componentes.
        * */

        this.logicWidth = logicWidth;
        this.logicHeight = logicHeight;
        //  setResolution(this.logicWidth,this.logicHeight);

        this.factorX = (float)getWidth() / (float)this.logicWidth;
        this.factorY = (float)getHeight() / (float)this.logicHeight;
        this.factorScale = Math.min(this.factorX, this.factorY);

        // Establecer bordes
        if(((float)getWidth()/(float)getHeight())<((float)2/(float)3))
        {
            this.window = (int)(this.logicWidth * this.factorX);
            int a = (int) ((getHeight() - this.window) / 2);
            this.borderHeight = a; //Bordes arriba y abajo
        }
        else {
            this.window = (int)(this.logicWidth*this.factorY);
            int a = (int) ((getWidth() - this.window) / 2);
            this.borderWidth = a; //Bordes Laterales
        }
        this.borderTop = this.myView.getInsets().top;            // TOMANDO EL INSET SUPERIOR

    }

 //Ahora voy a intentar overridear los metodos que se definian en la interfaz
 //@Override
 //public void setColor(Color  color) { this.graphics2D.setColor(null); } // CAMBIA COLOR

    @Override
    public void clear(int color) {                                       //LIMPIA PANTALLA CON COLOR
        this.graphics2D.setColor(null);
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        this.graphics2D.setPaintMode();
    }



}
