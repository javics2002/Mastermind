package com.example.libenginepc;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javax.swing.JFrame;

import com.example.aninterface.IntImage;
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


        this.logicWidth = logicWidth;
        this.logicHeight = logicHeight;
         setResolution(this.logicWidth,this.logicHeight);

        this.factorX = (float)getWidth() / (float)this.logicWidth;
        this.factorY = (float)getHeight() / (float)this.logicHeight;
        this.factorScale = Math.min(this.factorX, this.factorY);

        // Establecer bordes

        //Ahora vamos a darle valor tanto al factor de escala como  a los bordes(Bandas al rededor del juego)
        /* Explicacion de por que pelotas pongo un inset para obtener los bordes
         * Por ejemplo, supongamos que myView es un JFrame y tiene una barra de título de 30 píxeles de altura.
         * Si el contenedor myView tiene un inset superior de 30 píxeles debido a la barra de título, this.myView.getInsets().top devolverá 30.
         * Esto significa que hay un margen superior de 30 píxeles en el contenedor myView que no puede ser utilizado para colocar componentes.
         * */
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




    // METODOS COLOR
    @Override
    public void setColor(int color) { this.graphics2D.setColor(new Color(color)); } // CAMBIA COLOR
    @Override
    public void clear(int color) {                                       //LIMPIA PANTALLA CON COLOR
        this.graphics2D.setColor(new Color(color));
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        this.graphics2D.setPaintMode();
    }

    //METODOS PARA GESTION DE FRAME
    public void show() { this.bufferStrategy.show(); }           // MUESTRA EL BUFFER STRATEGY

    public void prepareFrame() {                                // ACTUALIZA LA NUEVA RESOLUCION EN CADA FRAME
        setResolution(getWidth(),getHeight());
        this.graphics2D = (Graphics2D)this.bufferStrategy.getDrawGraphics();
    }

    public void finishFrame() {
        this.graphics2D.dispose();
    }    // LIBERA EL GRAPHICS

    public boolean cambioBuffer(){                              // CAMBIA EL BUFFER
        if(bufferStrategy.contentsRestored()){
            return false; // se ha restaurado en algun momento el bufer
        }
        return !this.bufferStrategy.contentsLost();
    }

    // METODOS PARA DIBUJAR
    @Override
    public void drawImage(IntImage image, int x, int y, int w, int h) {       //DIBUJA LA IMAGEN CON POSICION Y TAMAÑO
        this.graphics2D.drawImage(((IntImagePC) image).getImg(),
                logicToRealX(x) - (scaleToReal(w)/2),logicToRealY(y) - (scaleToReal(h)/2) + borderTop,
              (scaleToReal(w)),(scaleToReal(h)),null);
    }

    // METODOS PARA CREAR RECURSOS
    @Override
    public IntImage newImage(String filename) { //ruta nombreproyecto/  //CREA UNA NUEVA IMAGEN

        Image img = null;
        try {
            img = ImageIO.read(new File("data/"+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        IntImage imgPC = new IntImagePC(img);
        return imgPC; //"/data/button.png"
    }
    @Override                                                    // ANCHO DE UNA CADENA DE TEXTO
    public int getWidthString(String text) {
        return (int)this.graphics2D.getFont().getStringBounds(text,this.graphics2D.getFontRenderContext()).getWidth();
    }

    @Override                                                    // ALTO DE UNA CADENA DE TEXTO
    public int getHeightString(String text) {
        return (int)this.graphics2D.getFont().getStringBounds(text,this.graphics2D.getFontRenderContext()).getHeight();
    }

    @Override                                                //DIBUJA TEXTO
    public void drawText(String text, int x, int y, int color, float tam) {

        this.graphics2D.setColor(new Color (color));
        this.graphics2D.drawString(text, x - (getWidthString(text)/2), y - (getHeightString(text)/2) + borderTop);
    }

    @Override                                               //DIBUJA RECTANGULO
    public void drawRect(int x, int y, int width, int height) {
        this.graphics2D.drawRect(x,y + borderTop, width,height);
    }
    @Override                                               //DIBUJA CUADRADO RELLENO
    public void fillSquare(int cx, int cy, int side) {      //RELLENAR CUADRADO
        this.graphics2D.fillRect(cx,cy + borderTop,side,side);
    }

    @Override                                               // DIBUJA RECTANGULO RELLENO
    public void fillRect(int x, int y, int w, int h) {      //RELLENAR RECTANGULO
        this.graphics2D.fillRect(x,y + borderTop,w,h);
    }
    @Override
    public void drawSquare(int cx, int cy, int side) {      //DIBUJA CUADRADO
        this.graphics2D.drawRect(cx,cy + borderTop,side,side);
        this.graphics2D.setPaintMode();
    }
    @Override                                                //DIBUJA LINEA
    public void drawLine(int initX, int initY, int endX, int endY) {
        this.graphics2D.drawLine(initX,initY + borderTop,endX,endY + borderTop);
    }
    //CONVERSORES DE COORDENADAS
    @Override
    public int logicToRealX(int x) { return (int)(x*factorScale + borderWidth); }

    @Override
    public int logicToRealY(int y) {        //CONVERSOR DE TAMAÑO LOGICO A REAL EN Y
        return (int)(y*factorScale + borderHeight);
    }

    @Override
    public int scaleToReal(int s) {
        return (int)(s*(factorScale));
    }
    //GETTERS
    @Override
    public int getWidth() { return this.myView.getWidth();}      // ANCHO DE LA VENTANA
    @Override
    public int getHeight() {
        return this.myView.getHeight();
    }   // ALTO DE LA VENTANA

    @Override
    public int getHeightLogic() { return this.logicHeight; }     // ALTURA LOGICA

    @Override
    public int getWidthLogic() { return this.logicWidth; }       //ANCHO LOGICO

    @Override
    public int getBorderTop() {
        return this.borderTop;
    }

    @Override
    public int getWindow() {
        return window;
    }

    //SETTERS
    @Override
    public void setResolution(int w, int h) {                    // ACTUALIZA LA RESOLUCION
        this.myView.setSize(w, h);

        this.factorX = (float)w / (float)this.logicWidth;
        this.factorY = (float)h / (float)this.logicHeight;
        this.factorScale = Math.min(this.factorX, this.factorY);

        //DEPENDIENDO DE LA RESOLUCION DE LA VENTANA SE CREAN LOS BORDES POR ARRIBA Y ABAJO O LOS LADOS
        if(((float)getWidth()/(float)getHeight())<((float)2/(float)3))
        {
            this.window = (int)(this.logicWidth * this.factorX);
            int a = (int) ((getHeight() - (this.logicHeight * this.factorX)) / 2);
            this.borderHeight = a; //Bordes arriba y abajo
            this.borderWidth=0;
        }
        else {
            this.window = (int)(this.logicWidth*this.factorY);
            int a = (int) ((getWidth() - (this.logicWidth * this.factorY)) / 2);
            this.borderWidth = a; //Bordes Laterales
            this.borderHeight=0;
        }
    }


}
