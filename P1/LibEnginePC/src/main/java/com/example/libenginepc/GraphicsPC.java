package com.example.libenginepc;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.example.aninterface.Font;
import com.example.aninterface.Image;
import com.example.aninterface.Graphics;

public class GraphicsPC implements Graphics {
    private JFrame _frame;
    private BufferStrategy _bufferStrategy;
    private Graphics2D _graphics;
    private int _logicWidth, _logicHeight;
    private int _borderWidth, _borderHeight, _borderTop, _borderBottom;
    private float _factorScale;
    private float _factorX, _factorY;
    private float _ratioX = 2f, _ratioY =  3f;
    private int _titleBarHeight = 37;
    private int _margin = 7;

    GraphicsPC(JFrame myView, int logicWidth, int logicHeight) {
        _frame = myView;
        _frame.createBufferStrategy(2);

        //Creamos el buffer y los graficos
        _bufferStrategy = _frame.getBufferStrategy();
        _graphics = (Graphics2D) _bufferStrategy.getDrawGraphics();

        //Ahora vamos a darle valor tanto al factor de escala como  a los bordes (Bandas al rededor del juego)
        /* Explicacion de por que pelotas pongo un inset para obtener los bordes
         * Por ejemplo, supongamos que myView es un JFrame y tiene una barra de título de 30 píxeles de altura.
         * Si el contenedor myView tiene un inset superior de 30 píxeles debido a la barra de título, this.myView.getInsets().top devolverá 30.
         * Esto significa que hay un margen superior de 30 píxeles en el contenedor myView que no puede ser utilizado para colocar componentes.
         * */
        _logicWidth = logicWidth;
        _logicHeight = logicHeight;

        //Establecemos el tamaño de mi view y creamos nuestro factor de escala
        setResolution(_logicWidth, _logicHeight);
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
        _borderTop = _frame.getInsets().top;
        _borderBottom = _frame.getInsets().bottom;
    }

    @Override
    public void clear(int color) { //LIMPIA PANTALLA CON COLOR
        _graphics.setColor(Color.black);

        //insets para title height
        _graphics.fillRect(0, 0, getWidth(), getHeight());

        _graphics.setColor(new Color(color));
        _graphics.fillRect(logicToRealX(0), logicToRealY(0), scaleToReal(getWidthLogic()) , scaleToReal(getHeightLogic()) );

        // TODO: Poner false para release
        final boolean debug = false;
        if(debug){
            _graphics.setColor(Color.black);

            _graphics.fillRect(_margin, 0, _borderWidth, getHeight());
            _graphics.fillRect(0, _titleBarHeight, getWidth(), _borderHeight);
            _graphics.fillRect(getWidth() - _borderWidth - _margin, 0, _borderWidth, getHeight());
            _graphics.fillRect(0, getHeight() - _borderHeight - _margin, getWidth(), _borderHeight);
        }
    }

    @Override
    public Font newFont(String fileName, float size) {
        java.awt.Font customFont;

        try {
            customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("data/Fonts/"+fileName)).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        FontPC fontPC = new FontPC(customFont);
        return fontPC;
    }

    @Override
    public void setColor(int color) {
        _graphics.setColor(new Color(color));
    } // CAMBIA COLOR


    //METODOS PARA GESTION DE FRAME
    public void show() { _bufferStrategy.show(); }           // MUESTRA EL BUFFER STRATEGY

    @Override
    public void prepareFrame() {                                // ACTUALIZA LA NUEVA RESOLUCION EN CADA FRAME
        setResolution(getWidth(),getHeight());
        _graphics = (Graphics2D) _bufferStrategy.getDrawGraphics();
    }

    public void finishFrame() {
        _graphics.dispose();
    }    // LIBERA EL GRAPHICS

    // Para dibujar seguimos el siguiente esquema
    // Pos x = Pos x real - Ancho w real Centrado ( /2)
    // Pos y = Pos y real - Alto h real Centrado (/2) + Margen myView ( bordertop)
    //Solo aplicable a imagenes y a texto , en los square rect se usa sin conversion
    //Por que es directo a ventana (Aunque se sigue teniendo en cuenta border top)

    @Override
    public void drawText(String text, Font font, int x, int y, int color) {
        _graphics.setColor(new Color (color));

        // Calcula el tamaño de la fuente aplicando el factor de escala¡
        //_activeFont = _activeFont.deriveFont(font.getFontSize() * _factorScale);
        _graphics.setFont(((FontPC) font).getFont().deriveFont(font.getFontSize() * _factorScale));

        // Calcula las coordenadas de dibujo ajustadas según el tamaño de la fuente escalado
        int adjustedX = logicToRealX(x) - (getStringWidth(text, font) / 2);
        int adjustedY = (logicToRealY(y) - (getStringHeight(text, font) / 2) + _borderTop);

        // Dibuja el texto con el tamaño de fuente ajustado
        _graphics.drawString(text, adjustedX, adjustedY);
    }

    //Dibujamos la imagen en la posicion x e y con un taño w y h
    //Se ha tenido que aplicar un offset en los metodos de escalado
    @Override
    public void drawImage(Image image, int x, int y, int w, int h) {
        int newW = (int)(scaleToReal(w));
        int newH = (int)(scaleToReal(h));
        _graphics.drawImage(((ImagePC) image).getImage(),
                logicToRealX(x) - (scaleToReal(w)/2), logicToRealY(y) - (scaleToReal(h)/2) + _borderTop,
                newW, newH, null);
    }

    // Creacion de obejtos  (Imagen Fuentes...)
    @Override
    public Image newImage(String filename) { //Creacion de imagen
        java.awt.Image image = null;
        try {

            image = ImageIO.read(new File("data/"+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImagePC(image);
    }

    @Override // Getter ancho cadena
    public int getStringWidth(String text, Font font) {
        return (int)((FontPC) font).getFont().deriveFont(font.getFontSize() * _factorScale).getStringBounds(text, _graphics.getFontRenderContext()).getWidth();
    }
    @Override //Getter alto cadena
    public int getStringHeight(String text, Font font) {
        return (int)((FontPC) font).getFont().deriveFont(font.getFontSize() * _factorScale).getStringBounds(text, _graphics.getFontRenderContext()).getHeight();
    }

    //Para el dibujo de rectangulos y cosas que no son una imagen
    @Override //dibujar un rectangulo en x y con w h de atributos
    public void drawRect(int x, int y, int width, int height) {
        _graphics.drawRect(x,y + _borderTop, width,height);
    }

    @Override   // Dibujar un cuadrado (relleno) en cx y cy con side (ancho y alto) de atributors
    public void fillSquare(int cx, int cy, int side) {
        _graphics.fillRect(cx,cy + _borderTop,side,side);
    }
    @Override//dibujar un rectangulo(relleno) en x y con w h de atributos
    public void fillRect(int x, int y, int w, int h) {      //RELLENAR RECTANGULO
        _graphics.fillRect(x,y + _borderTop,w,h);   // DIBUJA RECTANGULO RELLENO
    }

    @Override// Dibujar un cuadrado  en cx y cy con side (ancho y alto) de atributors
    public void drawSquare(int cx, int cy, int side) {      //DIBUJA CUADRADO
        _graphics.drawRect(cx,cy + _borderTop,side,side);
        _graphics.setPaintMode();
    }

    @Override    // Dibujar una lina en initx y inity hasta end y endY
    public void drawLine(int initX, int initY, int endX, int endY) { //DIBUJA LINEA
        _graphics.drawLine(initX,initY + _borderTop,endX,endY + _borderTop);
    }

    //CONVERSORES DE COORDENADAS


    //Pos real desde logica = int (Pos logica * factor de escala + ancho de bordes)
    //Pos real desde escalada = (int) Pos escalada (Pos logica * tamaño de escala)
    //Lo unico que cambia es que desde la posicion logica añadimos los bordes (BH y BW)
    //La posicion real desde logica se usa para x e y en dibujo
    //La posicion real desde el escalado se usa para w y h en dibujo
    @Override //De posicion logica a  real
    public int logicToRealX(int x) {
        return (int) (x * _factorScale + _borderWidth);
    }
    @Override
    public int logicToRealY(int y) {        //CONVERSOR DE TAMAÑO LOGICO A REAL EN Y
        return (int) (y * _factorScale + _borderHeight);
    }
    @Override
    public int scaleToReal(int s) {
        return (int) (s * _factorScale);
    }

    //GETTERS
    @Override
    public int getWidth() { return _frame.getWidth();}      // ANCHO DE LA VENTANA
    @Override
    public int getHeight() {
        return _frame.getHeight();
    }
    @Override
    public int getHeightLogic() { return _logicHeight; }     // ALTURA LOGICA
    @Override
    public int getWidthLogic() { return _logicWidth; }       //ANCHO LOGICO
    @Override
    public int get_borderTop() {
        return _borderTop;
    }
    //SETTERS
    @Override
    public void setResolution(int w, int h) {                    // ACTUALIZA LA RESOLUCION
        _frame.setSize(w, h);

        //Calculo factor escala -> ancho de la ventana / ancho logico del juego
        _factorX = (float) w / (float) _logicWidth;
        _factorY = (float) (h - _titleBarHeight) / (float) _logicHeight;

        _factorScale = Math.min(_factorX, _factorY);

        //Comprobamos si en este caso el escalado de miView (ancho /alto) es menor que la relacion de aspecto que ponemos nosotros (2/3)
        //Por que si es menor añadimos un ancho de bordes por arriba y abajo (Height)
        //Si no se los añadimos por los lados( Width)
        if (((float) getWidth() / ((float) getHeight() - _titleBarHeight)) < (_ratioX / _ratioY)){
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.
            _borderWidth = 0;
            _borderHeight =  (int) ((getHeight() - (_logicHeight * _factorX)) / 2);
        } else  {
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.

            _borderWidth = (int) ((getWidth() - (_logicWidth * _factorY)) / 2);
            _borderHeight = 0;
        }
    }
}
