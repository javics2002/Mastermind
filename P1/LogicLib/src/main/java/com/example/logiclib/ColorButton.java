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
    public void update() { }

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
