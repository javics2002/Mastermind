package com.example.libenginepc;

import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GraphicsPC implements Graphics {
    private final JFrame _frame;
    private final BufferStrategy _bufferStrategy;
    private Graphics2D _graphics2D;

    // Dimensiones lógicas de la aplicación.
    // La aplicación diferencia entre dimensiones lógicas (Ej. 480 x 720) y dimensiones reales (lo que la ventana
    // ocupa realmente (Ej. 482 x 983).
    // Los elementos siempre se colocan en la pantalla con dimensiones lógicas, y luego la propia aplicación
    // los convertirá a dimensiones reales a través de una función
    private final int _logicWidth, _logicHeight;

    // Insets de la aplicación. Son la cantidad de pixeles que tenemos que tener en cuenta para dibujar.
    // Por ejemplo, el topInset son 30px (la barra propia de Windows), y tenemos que tenerlo en cuenta para
    // dibujar los elementos correctamente.
    private final int _topInset, _bottomInset, _leftInset, _rightInset;

    // Tamaños de los bordes que se usan para redimensionar la pantalla.
    // En este caso, los bordes tienen el mismo color de la aplicación, así que no son perceptibles.
    private int _borderWidth, _borderHeight;
    private float _scaleFactor;

    private HashMap<String, Image> images;

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

        images = new HashMap<>();

        setNewResolution(_logicWidth, _logicHeight);
    }

    @Override
    public void clear(int color) {
        final boolean debug = false;
        if (debug) {
            _graphics2D.setColor(Color.black);
            _graphics2D.fillRect(0, 0, _frame.getWidth(), _frame.getHeight());

            drawRect(0, 0, _logicWidth, _logicHeight, color);
        } else {
            _graphics2D.setColor(new Color(color));
            _graphics2D.fillRect(0, 0, _frame.getWidth(), _frame.getHeight());
        }
    }

    @Override
    public Font newFont(String fileName, float size) {
        java.awt.Font customFont;

        try {
            customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("data/Fonts/" + fileName)).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        return new FontPC(customFont);
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
        setColor(color);
        _graphics2D.setFont(((FontPC) font).getFont().deriveFont(font.getFontSize() * _scaleFactor));

        _graphics2D.drawString(text, logicToRealX(logicX), logicToRealY(logicY));
    }

    @Override
    public void drawCircleWithBorder(int logicX, int logicY, int radius, int borderWidth, int circleColor, int borderColor) {
        final int realX = logicToRealX(logicX);
        final int realY = logicToRealY(logicY);
        final int realRadius = scaleToReal(radius);
        final int realBorderRadius = realRadius + scaleToReal(borderWidth);

        // Dibuja el borde del círculo
        setColor(borderColor);
        _graphics2D.fillOval(realX - realBorderRadius, realY - realBorderRadius, 2 * realBorderRadius, 2 * realBorderRadius);

        // Dibuja el círculo interior
        setColor(circleColor);
        _graphics2D.fillOval(realX - realRadius, realY - realRadius, 2 * realRadius, 2 * realRadius);
    }
    @Override
    public void drawImage(Image image, int logicX, int logicY, int logicWidth, int logicHeight) {
        _graphics2D.drawImage(((ImagePC) image).getImage(), logicToRealX(logicX), logicToRealY(logicY),
                scaleToReal(logicWidth), scaleToReal(logicHeight), null);
    }
    @Override
    public void drawCircle(int logicX, int logicY, int radius, int color) {
        setColor(color);
        int realX = logicToRealX(logicX);
        int realY = logicToRealY(logicY);
        int realRadius = scaleToReal(radius);

        _graphics2D.fillOval(realX - realRadius, realY - realRadius, 2 * realRadius, 2 * realRadius);
    }
    @Override
    public Image loadImage(String filename) {
        if (images.containsKey(filename)){
            return images.get(filename);
        }
        try {
            java.awt.Image image = ImageIO.read(new File("data/" + filename));
            images.put(filename, new ImagePC(image));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return images.get(filename);
    }

    @Override
    public int getStringWidth(String text, Font font) {
        return (int) _graphics2D.getFontMetrics(((FontPC) font).getFont()).getStringBounds(text, _graphics2D).getWidth();
    }

    @Override
    public int getStringHeight(String text, Font font) {
        return (int) _graphics2D.getFontMetrics(((FontPC)font).getFont()).getAscent() -
                _graphics2D.getFontMetrics(((FontPC)font).getFont()).getDescent();
    }

    @Override
    public void drawRect(int logicX, int logicY, int logicWidth, int logicHeight, int color) {
        setColor(color);
        _graphics2D.fillRect(logicToRealX(logicX), logicToRealY(logicY), scaleToReal(logicWidth), scaleToReal(logicHeight));
    }

    @Override
    public void drawRoundedRect(int logicX, int logicY, int logicWidth, int logicHeight, int color, int arcWidth, int arcHeight) {
        setColor(color);
        _graphics2D.fillRoundRect(logicToRealX(logicX), logicToRealY(logicY),
                scaleToReal(logicWidth), scaleToReal(logicHeight), scaleToReal(2 * arcWidth), scaleToReal(2 * arcHeight));
    }

    // Funciones encargadas de convertir las dimensiones lógicas a dimensiones reales.
    // Tienen en cuenta los insets para realizar el calculo.

    public int logicToRealX(int logicX) {
        return (int) (logicX * _scaleFactor + _leftInset + _borderWidth);
    }


    public int logicToRealY(int logicY) {
        return (int) (logicY * _scaleFactor + _topInset + _borderHeight);
    }

    // Función que no tiene en cuenta los Insets al convertir de tamaño lógico a real, debido a que
    // no podemos tener en cuenta el offset dos veces seguidas (Por ejemplo, al crear un rectángulo,
    // se tiene en cuenta el offset en las coordenadas x,y, pero no en el ancho/alto del rectángulo)

    public int scaleToReal(int realScale) {
        return (int) (realScale * _scaleFactor);
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
    public int getLogicHeight() {
        return _logicHeight;
    }

    @Override
    public int getLogicWidth() {
        return _logicWidth;
    }

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
            _borderHeight = (int) ((getHeight() - (_logicHeight * factorX)) / 2);
        } else {
            _borderWidth = (int) ((getWidth() - (_logicWidth * factorY)) / 2);
            _borderHeight = 0;
        }
    }
    @Override
    public boolean inBounds(int posX, int posY, int checkX, int checkY,int width, int height)
    {
        return(checkX >=logicToRealX(posX)
                && checkX <= logicToRealX(posX) + scaleToReal(width)
                && checkY >= logicToRealY(posY)
                && checkY <= logicToRealY(posY) + scaleToReal(height));
    }
}
