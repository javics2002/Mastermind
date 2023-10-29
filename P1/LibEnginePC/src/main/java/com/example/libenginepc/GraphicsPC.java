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
    private Graphics2D _graphics2D;

    // Dimensiones lógicas de la aplicación.
    // La aplicación diferencia entre dimensiones lógicas (Ej. 480 x 720) y dimensiones reales (lo que la ventana
    // ocupa realmente (Ej. 482 x 983).
    // Los elementos siempre se colocan en la pantalla con dimensiones lógicas, y luego la propia aplicación
    // los convertirá a dimensiones reales a través de una función
    private int _logicWidth, _logicHeight;

    // Insets de la aplicación. Son la cantidad de pixeles que tenemos que tener en cuenta para dibujar.
    // Por ejemplo, el topInset son 30px (la barra propia de Windows), y tenemos que tenerlo en cuenta para
    // dibujar los elementos correctamente.
    private int _topInset, _bottomInset, _leftInset, _rightInset;

    // Tamaños de los bordes que se usan para redimensionar la pantalla.
    // En este caso, los bordes tienen el mismo color de la aplicación, así que no son perceptibles.
    private int _borderWidth, _borderHeight;
    private float _scaleFactor;

    GraphicsPC(JFrame myView, int logicWidth, int logicHeight) {
        _frame = myView;
        _frame.createBufferStrategy(2);

        _bufferStrategy = _frame.getBufferStrategy();
        _graphics2D = (Graphics2D) _bufferStrategy.getDrawGraphics();

        _logicWidth = logicWidth;
        _logicHeight = logicHeight;

        _topInset = _frame.getInsets().top;
        _bottomInset = _frame.getInsets().bottom;
        _leftInset = _frame.getInsets().left;
        _rightInset = _frame.getInsets().right;

        setNewResolution(_logicWidth, _logicHeight);
    }

    @Override
    public void clear(int color) {
        // TODO: Poner false para release
        final boolean debug = false;
        if(debug){
            _graphics2D.setColor(Color.black);
            _graphics2D.fillRect(0, 0, _frame.getWidth(), _frame.getHeight());

            drawRect(0, 0, _logicWidth, _logicHeight, color);
        }
        else{
            _graphics2D.setColor(new Color(color));
            _graphics2D.fillRect(0, 0, _frame.getWidth(), _frame.getHeight());
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
        _graphics2D.setColor(new Color(color));
    }

    public void show() {
        _bufferStrategy.show();
    }

    @Override
    public void prepareFrame() {
        setNewResolution(getWidth(), getHeight());
        _graphics2D = (Graphics2D) _bufferStrategy.getDrawGraphics();
    }

    public void finishFrame() {
        _graphics2D.dispose();
    }

    @Override
    public void drawText(String text, Font font, int logicX, int logicY, int color) {
        _graphics2D.setColor(new Color (color));
        _graphics2D.setFont(((FontPC) font).getFont().deriveFont(font.getFontSize() * _scaleFactor));

        _graphics2D.drawString(text, logicToRealX(logicX), logicToRealY(logicY));
    }

    @Override
    public void drawImage(Image image, int logicX, int logicY, int logicWidth, int logicHeight) {
        _graphics2D.drawImage(((ImagePC) image).getImage(), logicToRealX(logicX), logicToRealY(logicY),
                scaleToReal(logicWidth), scaleToReal(logicHeight), null);
    }

    @Override
    public Image newImage(String filename) {
        java.awt.Image image = null;
        try {
            image = ImageIO.read(new File("data/"+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImagePC(image);
    }

    @Override
    public int getStringWidth(String text, Font font) {
        return (int)((FontPC) font).getFont().deriveFont(font.getFontSize()).getStringBounds(text, _graphics2D.getFontRenderContext()).getWidth();
    }
    @Override
    public int getStringHeight(String text, Font font) {
        return (int)((FontPC) font).getFont().deriveFont(font.getFontSize()).getStringBounds(text, _graphics2D.getFontRenderContext()).getHeight();
    }
    @Override
    public void drawRect(int logicX, int logicY, int logicWidth, int logicHeight, int color) {
        _graphics2D.setColor(new Color(color));
        _graphics2D.fillRect(logicToRealX(logicX), logicToRealY(logicY), scaleToReal(logicWidth), scaleToReal(logicHeight));
    }

    // Funciones encargadas de convertir las dimensiones lógicas a dimensiones reales.
    // Tienen en cuenta los insets para realizar el calculo.
    @Override
    public int logicToRealX(int logicX) {
        return (int)(logicX * _scaleFactor + _leftInset + _borderWidth);
    }
    @Override
    public int logicToRealY(int logicY) {
        return (int)(logicY * _scaleFactor + _topInset + _borderHeight);
    }

    // Función que no tiene en cuenta los Insets al convertir de tamaño lógico a real, debido a que
    // no podemos tener en cuenta el offset dos veces seguidas (Por ejemplo, al crear un rectángulo,
    // se tiene en cuenta el offset en las coordenadas x,y, pero no en el ancho/alto del rectángulo)
    @Override
    public int scaleToReal(int realScale){
        return (int)(realScale * _scaleFactor);
    }

    @Override
    public int getWidth() {
        return _frame.getWidth() - _leftInset - _rightInset;
    }
    @Override
    public int getHeight() {
        return _frame.getHeight() - _topInset - _bottomInset;
    }

    @Override
    public int getLogicHeight() { return _logicHeight; }
    @Override
    public int getLogicWidth() { return _logicWidth; }

    @Override
    public void setNewResolution(int newRealWidth, int newRealHeight) {
        _frame.setSize(newRealWidth + _leftInset + _rightInset,
                newRealHeight + _topInset + _bottomInset);

        float factorX = (float) getWidth() / _logicWidth;
        float factorY = (float) getHeight() / _logicHeight;

        // El factor de escala se escoje del valor mínimo de cualquiera de las dos
        // dimensiones. Debido a que solo se reescala el mínimo factor.
        _scaleFactor = Math.min(factorX, factorY);

        // Se asigna el tamaño de los bordes, dividido entre dos porque la aplicación
        // estará en el centro, y tendrá los bordes a cada lado.
        if ((float) getWidth() / getHeight() < (float) _logicWidth / _logicHeight) {
            _borderWidth = 0;
            _borderHeight =  (int) ((getHeight() - (_logicHeight * factorX)) / 2);
        } else  {
            _borderWidth = (int) ((getWidth() - (_logicWidth * factorY)) / 2);
            _borderHeight = 0;
        }
    }
}
