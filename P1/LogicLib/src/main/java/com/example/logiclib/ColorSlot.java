package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;

public class ColorSlot extends GameObject {
    private boolean _hasColor;
    private int _colorID;
    private final Text _numberText;
    private final GameAttributes _gameAttributes;
    private final float _appearenceTime = .5f;

    public ColorSlot(Engine engine, int positionX, int positionY, int width, int height, GameAttributes gameAttributes) {
        super(engine, positionX, positionY, width, height, 1f);

        _gameAttributes = gameAttributes;
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _hasColor = false;
        Font _colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);
        _numberText = new Text("", _colorNum, engine, 0, 0, 0, true);
        _colorID = -1;
    }

    @Override
    public void render(Graphics graphics) {
        if (hasColor() && _gameAttributes.isEyeOpen) {
            _graphics.drawCircleWithBorder(_positionX + _width * _scale / 2,
                    _positionY + _height * _scale / 2, _width / 2, 1f, _scale,
                    Colors.getColor(_colorID - 1), Colors.colorValues.get(Colors.ColorName.BLACK));
            _numberText.render(graphics);
        } else if (hasColor()) {
            _graphics.drawCircleWithBorder(_positionX + _width * _scale / 2,
                    _positionY + _height * _scale / 2, _width / 2, 1f, _scale,
                    Colors.getColor(_colorID - 1), Colors.colorValues.get(Colors.ColorName.BLACK));
        } else { // Gris
            _graphics.drawCircleWithBorder(_positionX + _width * _scale / 2,
                    _positionY + _height * _scale / 2, _width / 2, 1f, _scale,
                    Colors.colorValues.get(Colors.ColorName.LIGHTGRAY), Colors.colorValues.get(Colors.ColorName.BLACK));
            _graphics.drawCircle(_positionX + _width * _scale / 2, _positionY + _height * _scale / 2,
                    _width / 8, _scale, Colors.colorValues.get(Colors.ColorName.DARKGRAY));
        }
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
    }

    public void setColor(int color, boolean isEyeOpen) {
        _hasColor = true;
        _colorID = color;
        String num = String.valueOf(_colorID);

        _numberText.setText(num);
        _numberText.setPosition(_positionX + _width / 2, _positionY + _height);
    }


    public boolean inBounds(int mouseX, int mouseY) {
        return _graphics.inBounds(_positionX, _positionY, mouseX, mouseY, _width, _height, _scale);
    }
    public boolean hasColor() {
        return _hasColor;
    }

    public void deleteColor() {
        _hasColor = false;
    }
}



