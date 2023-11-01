package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;

public class HintSlot implements GameObject {
    private Image _image;
    private final Graphics _graphics;
    private final int _positionX, _positionY;
    private final int _width, _height;

    public HintSlot(Engine engine, String filename, int positionX, int positionY, int width, int height) {
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
    }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }

    public void setImage(String filename) {
        _image = _graphics.newImage(filename);
    }
}
