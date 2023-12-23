package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;
import com.example.aninterface.Sound;
import com.example.aninterface.Font;

public class Button implements GameObject {
    protected Image _image;
    protected final Engine _engine;
    protected final Graphics _graphics;
    protected final int _positionX, _positionY;
    protected final int _width, _height;
    protected final Sound _clickSound;
    protected int _backgroundColor;
    protected int _arc;
    protected final String _text;
    protected Font _font;

    Button(int backgroundColor, String text, Font font, Engine engine, int positionX, int positionY, int width, int height) {
        _engine = engine;
        _graphics = engine.getGraphics();

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _image = null;

        _backgroundColor = backgroundColor;
        _arc = 20;

        _text = text;
        _font = font;

        _clickSound = _engine.getAudio().loadSound("click.wav", false);
        _clickSound.setVolume(.5f);
    }
    Button(String filename, Colors.ColorName backgroundColor, String text, Font font, Engine engine,
           int positionX, int positionY, int width, int height) {
        _engine = engine;
        _graphics = engine.getGraphics();

        _image = _graphics.loadImage(filename);
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;

        _backgroundColor = Colors.colorValues.get(backgroundColor);
        _arc = 20;

        _text = text;
        _font = font;

        _clickSound = _engine.getAudio().loadSound("click.wav", false);
        _clickSound.setVolume(.5f);
    }

    Button(String filename, Engine engine, int positionX, int positionY, int width, int height) {
        _engine = engine;
        _graphics = engine.getGraphics();

        _image = _graphics.loadImage(filename);
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;

        _text = "";

        _clickSound = _engine.getAudio().loadSound("click.wav", false);
        _clickSound.setVolume(.5f);
    }

    @Override
    public boolean handleEvents(Input.TouchEvent event) {
        if (event.type == Input.InputType.PRESSED && inBounds(event.x, event.y)) {
            _clickSound.play();
            callback();

            return true;
        }

        return false;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics graphics) {
        if (_image != null)
            _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
        else {
            _graphics.drawRoundedRect(_positionX, _positionY, _width, _height, _backgroundColor, _arc, _arc);
            _graphics.drawText(_text, _font, _positionX + _width / 2 - _graphics.getStringWidth(_text, _font) / 2,
                    _positionY + _height / 2 + _graphics.getStringHeight(_text, _font) / 2, 0);
        }
    }

    public boolean inBounds(int mouseX, int mouseY) {
        return (mouseX >= (_graphics.logicToRealX(_positionX))
                && mouseX <= _graphics.logicToRealX(_positionX) + _graphics.scaleToReal(_width)
                && mouseY >= _graphics.logicToRealY(_positionY)
                && mouseY <= _graphics.logicToRealY(_positionY) + _graphics.scaleToReal(_height));
    }

    public void callback() {}

    public void setImage(String path) {
        _image = _graphics.loadImage(path);
    }
}
