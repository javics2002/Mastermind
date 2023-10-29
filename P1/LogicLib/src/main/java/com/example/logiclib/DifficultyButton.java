package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

public class DifficultyButton implements Interface {
    private Image _image;
    private Engine _engine;
    private Graphics _graphics;
    private int _positionX, _positionY;
    private int _width, _height;
    private int _tryNumber, _combinationLength, _numberOfColors;
    private boolean _repeatedColors;

    DifficultyButton(String filename, Engine engine, int positionX, int positionY, int width, int height,
                     int tryNumber, int combinationLength, int numberOfColors, boolean repeatedColors) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);

        _positionX = positionX;
        _positionY = positionY ;

        _width = width;
        _height = height;

        _tryNumber = tryNumber;
        _combinationLength = combinationLength;
        _numberOfColors = numberOfColors;
        _repeatedColors = repeatedColors;
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        if (e.type == Input.InputType.PRESSED && inBounds(e.x, e.y)) {
            // Change scene
            GameScene scene = new GameScene(_engine, _tryNumber, _combinationLength, _numberOfColors, _repeatedColors);
            _engine.setCurrentScene(scene);
            return true;
        }
        return false;
    }

    @Override
    public void update() { }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    public boolean inBounds(int mX, int mY) {
        return (mX >= (_graphics.logicToRealX(_positionX))
                && mX <=  _graphics.logicToRealX(_positionX)+ _graphics.scaleToReal(_width)
                && mY >= _graphics.logicToRealY(_positionY)
                && mY <= _graphics.logicToRealY(_positionY)+ _graphics.scaleToReal(_height));

    }
}
