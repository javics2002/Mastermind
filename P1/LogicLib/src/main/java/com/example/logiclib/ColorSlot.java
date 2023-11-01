package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

public class ColorSlot implements Interface {
    private Image _image;
    private final Graphics _graphics;
    private final int _positionX, _positionY;
    private final int _width, _height;
    private String _name;
    private boolean _hasColor;
    private int _colorID;

    public ColorSlot(Engine engine, String filename, int positionX, int positionY, int width, int height) {
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _name = filename;
        _hasColor = false;
        _colorID = -1;
    }

    public void setImage(String filename) {
        _name = filename;
        _image = _graphics.newImage(filename);
    }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    @Override
    public void update() {
        if (GameAttributes.Instance().isEyeOpen && _hasColor) {
            String colorButtonName = "color" + _colorID + ".png";
            colorButtonName = colorButtonName.replace(".png", "CB.png");
            setImage(colorButtonName);
        } else {
            String colorSlotName = _name;
            _name = colorSlotName.replace("CB.png", ".png");
            setImage(_name);
        }
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
    }

    public void setColor(int color, boolean isEyeOpen) {
        _hasColor = true;
        _colorID = color;

        // Si tenemos el modo daltónico activado, usamos su respectiva imagen.
        if (isEyeOpen) {
            setImage("color" + _colorID + "CB.png");
        } else {
            setImage("color" + _colorID + ".png");
        }
    }

    public void deleteColor() {
        _colorID = -1;
        _hasColor = false;
        setImage("colorEmpty.png");
    }

    private boolean inBounds(int mX, int mY) {
        return (mX >= (_graphics.logicToRealX(_positionX))
                && mX <= _graphics.logicToRealX(_positionX) + _graphics.scaleToReal(_width)
                && mY >= _graphics.logicToRealY(_positionY)
                && mY <= _graphics.logicToRealY(_positionY) + _graphics.scaleToReal(_height));
    }

    public boolean hasColor() {
        return _hasColor;
    }
}
