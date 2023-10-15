package com.example.libenginepc;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.example.aninterface.Font;
import com.example.aninterface.IntImage;
import com.example.aninterface.Graphics;
import javax.swing.border.LineBorder;

public class GraphicsPC implements Graphics {
    private JFrame myView;
    private BufferStrategy bufferStrategy;
    float Ratiox = 2f;
    float RatioY=  3f;
    private Graphics2D graphics2D;
    private int logicWidth;
    private int logicHeight;
    private int windowSX;
    private int windowSY;

    private int window;

    private int borderWidth;
    private int borderHeight;
    private int borderTop;
    private int borderBot;
    private float factorScale;
    private float factorX;
    private float factorY;
    private float aspectR;

    private java.awt.Font activeFont;

    GraphicsPC(JFrame myView, int logicWidth, int logicHeight,int windowSX, int  windowSY) {
        this.myView = myView;
        this.myView.createBufferStrategy(2);

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


       //Establecemos el tamaño de mi view y creamos nuestro factor de escala
        setResolution(this.logicWidth,this.logicHeight);
        //Los tres componentes necesarios para poder ajustar son :
        //FactorScale // BorderWidth BorderHeight //  BorderTop

        //Para que se usa el order top?
        //Para tener en cuenta el espacio del borde
        //Contenedor myView (suponiendo que mide por ejemplo 3 de altura):
        //
        //+-----------------------------------------+
        //|         Espacio del borde               |
        //|                                         |
        //+-----------------------------------------+
        //|       Elemento correctamente            |
        //|       posicionado, teniendo en          |
        //|       cuenta el espacio del borde.      |
        //|                                         |
        //+-----------------------------------------+
        this.borderTop = this.myView.getInsets().top;
    }


    @Override
    public void clear(int color) {                                       //LIMPIA PANTALLA CON COLOR
        this.graphics2D.setColor(new Color(color));
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        this.graphics2D.setPaintMode();
    }

    @Override
    public Font newFont(String fileName, float size) {
        java.awt.Font customFont = null;
        try {
            customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("data/Fonts/"+fileName)).deriveFont(size);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntFontPC fontPC = new IntFontPC(customFont);
        activeFont = customFont;

        return fontPC;
    }

    @Override
    public void setColor(int color) { this.graphics2D.setColor(new Color(color)); } // CAMBIA COLOR
    //METODOS PARA GESTION DE FRAME
    public void show() { this.bufferStrategy.show(); }           // MUESTRA EL BUFFER STRATEGY
    public void prepareFrame() {                                // ACTUALIZA LA NUEVA RESOLUCION EN CADA FRAME
        setResolution(getWidth(),getHeight());
        this.graphics2D = (Graphics2D)this.bufferStrategy.getDrawGraphics();
    }
    public void finishFrame() {
        this.graphics2D.dispose();
    }    // LIBERA EL GRAPHICS




    // Para dibujar seguimos el siguiente esquema
    // Pos x = Pos x real - Ancho w real Centrado ( /2)
    // Pos y = Pos y real - Alto h real Centrado (/2) + Margen myView ( bordertop)
    //Solo aplicable a imagenes y a texto , en los square rect se usa sin conversion
    //Por que es directo a ventana (Aunque se sigue teniendo en cuenta border top)

    @Override
    public void drawText(String text, int x, int y, int color) {
        this.graphics2D.setColor(new Color (color));


        // Tamaño de fuente base
        int baseFontSize = 48;  // Puedes ajustar esto según tus necesidades

        // Calcula el tamaño de la fuente aplicando el factor de escala
        System.out.println(baseFontSize*factorScale);
        this.activeFont =this.activeFont.deriveFont(baseFontSize*factorScale);
        this.graphics2D.setFont(this.activeFont);


        // Calcula las coordenadas de dibujo ajustadas según el tamaño de la fuente escalado
        int adjustedX = logicToRealX(x) - (getWidthString(text) / 2);
        int adjustedY = logicToRealY(y) - (getHeightString(text) / 2) + this.borderTop;

        // Dibuja el texto con el tamaño de fuente ajustado
        this.graphics2D.drawString(text, adjustedX, adjustedY);
    }
    @Override
    public void drawImage(IntImage image, int x, int y, int w, int h) {       //DIBUJA LA IMAGEN CON POSICION Y TAMAÑO
        this.graphics2D.drawImage(((IntImagePC) image).getImg(),
                logicToRealX(x) - (scaleToReal(w)/2),logicToRealY(y) - (scaleToReal(h)/2) + borderTop,
                (scaleToReal(w)),(scaleToReal(h)),null);
    }
    // METODOS PARA CREAR RECURSOS
    @Override
    public IntImage newImage(String filename) { //Creacion de imagen
        Image img = null;
        try {
            img = ImageIO.read(new File("data/"+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        IntImage imgPC = new IntImagePC(img);
        return imgPC; //"/data/button.png"

    }
    @Override                                                    // Getter ancho cadena
    public int getWidthString(String text) {
        return (int)this.graphics2D.getFont().getStringBounds(text,this.graphics2D.getFontRenderContext()).getWidth();
    }
    @Override //Getter alto cadena
    public int getHeightString(String text) {
        return (int)this.graphics2D.getFont().getStringBounds(text,this.graphics2D.getFontRenderContext()).getHeight();
    }
    //Para el dibujo de rectangulos y cosas que no son una imagen
    @Override //dibujar un rectangulo en x y con w h de atributos
    public void drawRect(int x, int y, int width, int height) {
        this.graphics2D.drawRect(x,y + borderTop, width,height);
    }
    @Override   // Dibujar un cuadrado (relleno) en cx y cy con side (ancho y alto) de atributors
    public void fillSquare(int cx, int cy, int side) {
        this.graphics2D.fillRect(cx,cy + borderTop,side,side);
    }
    @Override//dibujar un rectangulo(relleno) en x y con w h de atributos                                               // DIBUJA RECTANGULO RELLENO
    public void fillRect(int x, int y, int w, int h) {      //RELLENAR RECTANGULO
        this.graphics2D.fillRect(x,y + borderTop,w,h);
    }
    @Override// Dibujar un cuadrado  en cx y cy con side (ancho y alto) de atributors
    public void drawSquare(int cx, int cy, int side) {      //DIBUJA CUADRADO
        this.graphics2D.drawRect(cx,cy + borderTop,side,side);
        this.graphics2D.setPaintMode();
    }
    @Override    // Dibujar una lina en initx y inity hasta end y endY                                             //DIBUJA LINEA
    public void drawLine(int initX, int initY, int endX, int endY) {
        this.graphics2D.drawLine(initX,initY + borderTop,endX,endY + borderTop);
    }

    //CONVERSORES DE COORDENADAS


    //Pos real desde logica = int (Pos logica * factor de escala + ancho de bordes)
    //Pos real desde escalada = (int) Pos escalada (Pos logica * tamaño de escala)
    //Lo unico que cambia es que desde la posicion logica añadimos los bordes (BH y BW)
    //La posicion real desde logica se usa para x e y en dibujo
    //La posicion real desde el escalado se usa para w y h en dibujo
    @Override //De posicion logica a  real
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
    }
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

        //Calculo factor escala -> ancho de la ventana / ancho logico del juego
        this.factorX = (float) w / (float) this.logicWidth;
        this.factorY = (float) h / (float) this.logicHeight;


        this.factorScale = Math.min(this.factorX, this.factorY);

        //Comprobamos si en este caso el escalado de miView (ancho /alto) es menor que la relacion de aspecto que ponemos nosotros (2/3)
        //Por que si es menor añadimos un ancho de bordes por arriba y abajo (Height)
        //Si no se los añadimos por los lados( Width)
        if (((float) getWidth() / (float) getHeight()) < (Ratiox/RatioY)){
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.


            int a = (int) ((getHeight() - (this.logicHeight * this.factorX)) / 2);
            this.borderHeight = a;
            this.borderWidth = 0;
        } else  {
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.

            int a = (int) ((getWidth() - (this.logicWidth * this.factorY)) / 2);
            this.borderWidth = a;
            this.borderHeight = 0;
        }
    }

}
