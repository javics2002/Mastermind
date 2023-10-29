package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

public class ColorButton implements Interface {
    private Image _image;
    private final Graphics _graphics;
    private final int _positionX, _positionY;
    private final int _width, _height;
    public int _colorID;

    ColorButton(String filename, Engine engine, int positionX, int positionY, int width, int height, int colorID) {
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);
        _positionX = positionX;
        _positionY = positionY ;
        _width = width;
        _height = height;
        _colorID = colorID;
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
    }

    @Override
    public void update() {
        // Cuando el botón del OJO se pulsa, esta función se encarga de cambiar todos los colores
        // a sus respectivas imagenes para daltónicos. Además, si el modo daltónico está activado,
        // también se encarga de quitar los números para volver al modo normal.

        if (GameAttributes.Instance().isEyeOpen){
            String colorButtonName = "color" + _colorID + ".png";
            colorButtonName = colorButtonName.replace(".png", "CB.png");
            setImage(colorButtonName);
        }
        else {
            String colorButtonName = "color" + _colorID + "CB.png";
            colorButtonName = colorButtonName.replace("CB.png", ".png");
            setImage(colorButtonName);
        }
    }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    public void setImage(String filename) {
        _image = _graphics.newImage(filename);
    }

    public boolean inBounds(int mX, int mY) {
        return (mX >= (_graphics.logicToRealX(_positionX))
                && mX <=  _graphics.logicToRealX(_positionX)+ _graphics.scaleToReal(_width)
                && mY >= _graphics.logicToRealY(_positionY)
                && mY <= _graphics.logicToRealY(_positionY)+ _graphics.scaleToReal(_height));
    }
}
