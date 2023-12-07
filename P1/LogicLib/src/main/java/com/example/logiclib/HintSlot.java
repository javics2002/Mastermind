package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;

public class HintSlot implements GameObject {
    private Image _image;
    private final Graphics _graphics;
    private int _positionX, _positionY;
    private final int _width, _height;
    private Colors.ColorName _colorName;

    public HintSlot(Engine engine, int positionX, int positionY, int width, int height) {
        _graphics = engine.getGraphics();

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;

        _colorName = Colors.ColorName.LIGHTGRAY;
    }

    @Override
    public void render() {
        _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2,
                _width / 2,  Colors.colorValues.get(_colorName));
    }

    @Override
    public void update() {

    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }

    public void setColor(Colors.ColorName colorName) {
        _colorName = colorName;
    }

    public void setPositionY(int posY){
        _positionY = posY;
    }
}
