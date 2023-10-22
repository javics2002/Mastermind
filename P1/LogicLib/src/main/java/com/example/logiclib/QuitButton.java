package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

public class QuitButton implements Interface {
    private Image _image;
    private Engine _engine;
    private Graphics _graphics;
    private int _positionX;
    private int _positionY;
    private int _width;
    private int _height;

    QuitButton(String filename, Engine engine, int positionX, int positionY, int width, int height) {
        _engine = engine;
        _graphics = engine.get_graphics();
        _image = _graphics.newImage(filename);
        _positionX = positionX;
        _positionY = positionY ;
        _width = width;
        _height = height;
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        if (e.type == Input.InputType.PRESSED && inBounds(e.x, e.y)) {
            // Change scene
            DifficultyScene scene = new DifficultyScene(_engine);
            _engine.setCurrentScene(scene);
            // engine.resume();
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
        return (mX >= _graphics.logicToRealX(_positionX) - (_graphics.scaleToReal(_width)/2)
                && mX <= _graphics.scaleToReal(_width) + _graphics.logicToRealX(_positionX) - (_graphics.scaleToReal(_width)/2)
                && mY >= _graphics.logicToRealY(_positionY) - (_graphics.scaleToReal(_height)/2) + _graphics.get_borderTop()
                && mY <= _graphics.scaleToReal(_height) + _graphics.logicToRealY(_positionY) - (_graphics.scaleToReal(_height)/2) + _graphics.get_borderTop());
    }
}
