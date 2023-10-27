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
    private int _logicWidth, _logicHeight;
    private int _topInset, _bottomInset, _leftInset, _rightInset;
    private int _borderWidth, _borderHeight;
    private float _scaleFactor;

    GraphicsPC(JFrame myView, int logicWidth, int logicHeight) {
        _frame = myView;
        _frame.createBufferStrategy(2);

        //Creamos el buffer y los graficos
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
    } // CAMBIA COLOR


    //METODOS PARA GESTION DE FRAME
    public void show() { _bufferStrategy.show(); }           // MUESTRA EL BUFFER STRATEGY

    @Override
    public void prepareFrame() {                                // ACTUALIZA LA NUEVA RESOLUCION EN CADA FRAME
        setNewResolution(getWidth(), getHeight());
        _graphics2D = (Graphics2D) _bufferStrategy.getDrawGraphics();
    }

    public void finishFrame() {
        _graphics2D.dispose();
    }    // LIBERA EL GRAPHICS

    @Override
    public void drawText(String text, Font font, int logicX, int logicY, int color) {
        _graphics2D.setColor(new Color (color));
        _graphics2D.setFont(((FontPC) font).getFont().deriveFont(font.getFontSize() * _scaleFactor));

        _graphics2D.drawString(text, logicToRealX(logicX), logicToRealY(logicY));
    }

    @Override
    public void drawImage(Image image, int logicX, int logicY, int logicWidth, int logicHeight) {
        _graphics2D.drawImage(((ImagePC) image).getImage(),
                logicToRealX(logicX), logicToRealY(logicY) ,
                scaleToReal(logicWidth), scaleToReal(logicHeight), null);
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
        return (int)((FontPC) font).getFont().deriveFont(font.getFontSize()).getStringBounds(text, _graphics2D.getFontRenderContext()).getWidth();
    }
    @Override //Getter alto cadena
    public int getStringHeight(String text, Font font) {
        return (int)((FontPC) font).getFont().deriveFont(font.getFontSize()).getStringBounds(text, _graphics2D.getFontRenderContext()).getHeight();
    }

    //Para el dibujo de rectangulos y cosas que no son una imagen
    @Override //dibujar un rectangulo en x y con w h de atributos
    public void drawRect(int logicX, int logicY, int logicWidth, int logicHeight, int color) {
        _graphics2D.setColor(new Color(color));
        _graphics2D.fillRect(logicToRealX(logicX), logicToRealY(logicY), scaleToReal(logicWidth), scaleToReal(logicHeight));
    }

    @Override
    public int logicToRealX(int logicX) {
        return (int)(logicX * _scaleFactor + _leftInset + _borderWidth);
    }
    @Override
    public int logicToRealY(int logicY) {
        return (int)(logicY * _scaleFactor + _topInset + _borderHeight);
    }


    @Override
    public int scaleToReal(int realScale){
        return (int)(realScale * _scaleFactor);
    }

    //GETTERS
    @Override
    public int getTopInset(){ return _topInset;}

    @Override
    public int getLatInsets() {
        return _rightInset+_leftInset;
    }

    @Override
    public int getBotInset() {
        return _bottomInset;
    }
    @Override
    public int getRightInset()
    {

        return _rightInset;
    }
    @Override
    public int getLeftInset()
    {
        return _leftInset;
    }
    @Override
    public int getWidth() { return _frame.getWidth() - _leftInset - _rightInset; }
    @Override
    public int getHeight() {
        return _frame.getHeight() - _topInset - _bottomInset;
    }
    @Override
    public int getHeightLogic() { return _logicHeight; }     // ALTURA LOGICA
    @Override
    public int getWidthLogic() { return _logicWidth; }       //ANCHO LOGICO

    @Override
    public void setNewResolution(int newRealWidth, int newRealHeight) {
        _frame.setSize(newRealWidth + _leftInset + _rightInset,
                newRealHeight + _topInset + _bottomInset);

        float factorX = (float) getWidth() / _logicWidth;
        float factorY = (float) getHeight() / _logicHeight;

        _scaleFactor = Math.min(factorX, factorY);

        if ((float) getWidth() / getHeight() < (float) _logicWidth / _logicHeight) {
            _borderWidth = 0;
            _borderHeight =  (int) ((getHeight() - (_logicHeight * factorX)) / 2);
        } else  {
            _borderWidth = (int) ((getWidth() - (_logicWidth * factorY)) / 2);
            _borderHeight = 0;
        }
    }

}
