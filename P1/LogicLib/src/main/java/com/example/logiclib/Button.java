package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;
import com.example.aninterface.Sound;

public abstract class Button implements GameObject {
    protected final Image _image;
    protected final Engine _engine;
    protected final Graphics _graphics;
    protected final int _positionX, _positionY;
    protected final int _width, _height;
    protected final Sound _clickSound;
    protected final ButtonCallback _callback;

    Button(String filename, Engine engine, int positionX, int positionY, int width, int height,ButtonCallback callback) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _callback=callback;

        _clickSound = _engine.getAudio().loadSound("click.wav", false);
        _clickSound.setVolume(.5f);
    }

    @Override
    public boolean handleEvents(Input.TouchEvent event) {
        if (event.type == Input.InputType.PRESSED && inBounds(event.x, event.y)) {
            _clickSound.play();
            _callback.onButtonClicked();

            return true;
        }

        return false;
    }

    @Override
    public void update() {
    }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    public boolean inBounds(int mouseX, int mouseY) {
        return (mouseX >= (_graphics.logicToRealX(_positionX))
                && mouseX <= _graphics.logicToRealX(_positionX) + _graphics.scaleToReal(_width)
                && mouseY >= _graphics.logicToRealY(_positionY)
                && mouseY <= _graphics.logicToRealY(_positionY) + _graphics.scaleToReal(_height));
    }


}
