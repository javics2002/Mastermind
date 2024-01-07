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

    private final HashMap<String, Image> images;

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

            drawRect(0, 0, _logicWidth, _logicHeight, 1f, color);
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
        int colorRGB = color;
        colorRGB += 0xFF000000;

        int colorARGB = colorRGB + 0xFF000000;
        Color test = new Color(colorARGB, true);

        _graphics2D.setColor(test);
    }

    private int combineAlphaAndColor(int alpha, int colorRGB) {
        return (alpha << 24) | (colorRGB & 0x00FFFFFF);
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
    public void drawText(String text, Font font, float logicX, float logicY, float scale, int color) {
        setColor(color);
        _graphics2D.setFont(((FontPC) font).getFont().deriveFont(font.getFontSize() * _scaleFactor * scale));

        _graphics2D.drawString(text, logicToRealX(logicX + (1 - scale) * getStringWidth(text, font) / 2),
                logicToRealY(logicY - (1 - scale) * getStringHeight(text, font) / 2));
    }

    @Override
    public void drawCircleWithBorder(float logicX, float logicY, float radius, float borderWidth, float scale, int circleColor, int borderColor) {
        final int realX = logicToRealX(logicX + (1 - scale) * radius);
        final int realY = logicToRealY(logicY + (1 - scale) * radius);
        final int realRadius = scaleToReal(radius, scale);
        final int realBorderRadius = realRadius + scaleToReal(borderWidth, scale);

        // Dibuja el borde del círculo
        setColor(borderColor);
        _graphics2D.fillOval(realX - realBorderRadius, realY - realBorderRadius, 2 * realBorderRadius, 2 * realBorderRadius);

        // Dibuja el círculo interior
        setColor(circleColor);
        _graphics2D.fillOval(realX - realRadius, realY - realRadius, 2 * realRadius, 2 * realRadius);
    }
    @Override
    public void drawImage(Image image, float logicX, float logicY, float logicWidth, float logicHeight, float scale) {
        _graphics2D.drawImage(((ImagePC) image).getImage(), logicToRealX(logicX), logicToRealY(logicY),
                scaleToReal(logicWidth, scale), scaleToReal(logicHeight, scale), null);
    }
    @Override
    public void drawCircle(float logicX, float logicY, float radius, float scale, int color) {
        setColor(color);
        int realX = logicToRealX(logicX + (1 - scale) * radius);
        int realY = logicToRealY(logicY + (1 - scale) * radius);
        int realRadius = scaleToReal(radius, scale);

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
    public float getStringWidth(String text, Font font) {
        return (float) _graphics2D.getFontMetrics(((FontPC) font).getFont()).getStringBounds(text, _graphics2D).getWidth();
    }

    @Override
    public float getStringHeight(String text, Font font) {
        return _graphics2D.getFontMetrics(((FontPC)font).getFont()).getAscent() -
                _graphics2D.getFontMetrics(((FontPC)font).getFont()).getDescent();
    }

    @Override
    public void drawRect(float logicX, float logicY, float logicWidth, float logicHeight, float scale, int color) {
        setColor(color);
        _graphics2D.fillRect(logicToRealX(logicX), logicToRealY(logicY), scaleToReal(logicWidth, scale), scaleToReal(logicHeight, scale));
    }

    @Override
    public void drawRealRect(int realX, int realY, int realWidth, int realHeight, int color) {
        setColor(color);
        _graphics2D.fillRect(realX, realY, realWidth + _frame.getInsets().left, realHeight + _frame.getInsets().top);
    }

    @Override
    public void drawRoundedRect(float logicX, float logicY, float logicWidth, float logicHeight,
                                float arcWidth, float arcHeight, float scale, int color) {
        setColor(color);
        _graphics2D.fillRoundRect(logicToRealX(logicX), logicToRealY(logicY),
                scaleToReal(logicWidth, scale), scaleToReal(logicHeight, scale),
                scaleToReal(2 * arcWidth, scale), scaleToReal(2 * arcHeight, scale));
    }

    // Funciones encargadas de convertir las dimensiones lógicas a dimensiones reales.
    // Tienen en cuenta los insets para realizar el calculo.

    public int logicToRealX(float logicX) {
        return (int) (logicX * _scaleFactor + _leftInset + _borderWidth);
    }


    public int logicToRealY(float logicY) {
        return (int) (logicY * _scaleFactor + _topInset + _borderHeight);
    }

    // Función que no tiene en cuenta los Insets al convertir de tamaño lógico a real, debido a que
    // no podemos tener en cuenta el offset dos veces seguidas (Por ejemplo, al crear un rectángulo,
    // se tiene en cuenta el offset en las coordenadas x,y, pero no en el ancho/alto del rectángulo)

    public int scaleToReal(float realScale, float specificScale) {
        return (int) (realScale * _scaleFactor * specificScale);
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
    public boolean inBounds(float posX, float posY, int checkX, int checkY, float width, float height, float scale)
    {
        return(checkX >=logicToRealX(posX)
                && checkX <= logicToRealX(posX) + scaleToReal(width, scale)
                && checkY >= logicToRealY(posY)
                && checkY <= logicToRealY(posY) + scaleToReal(height, scale));
    }
}
