package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;

public class ColorSlot extends GameObject {
    private boolean _hasColor;
    private int _colorID;
    private Font _colorNum;
    private Text _numberText;
    private GameAttributes _gameAttributes;

    public ColorSlot(Engine engine, int positionX, int positionY, int width, int height, GameAttributes gameAttributes) {
        super(engine, positionX, positionY, width, height, 1f);

        _gameAttributes = gameAttributes;
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _hasColor = false;
        _colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);
        _numberText = new Text("", _colorNum, engine, 0, 0, 0, true);
        _colorID = -1;
    }

    @Override
    public void render() {
        if (hasColor() && _gameAttributes.isEyeOpen) {
            _graphics.drawCircleWithBorder((int) (_positionX + _width / 2), (int) (_positionY + _height / 2),
                    (int) (_width * _scale/ 2), 1, Colors.getColor(_colorID - 1), Colors.colorValues.get(Colors.ColorName.BLACK));
            _numberText.render();
        } else if (hasColor()) {
            _graphics.drawCircleWithBorder((int) (_positionX + _width / 2), (int) (_positionY + _height / 2),
                    (int) (_width * _scale/ 2), 1, Colors.getColor(_colorID - 1), Colors.colorValues.get(Colors.ColorName.BLACK));
        } else { // Gris
            _graphics.drawCircleWithBorder((int) (_positionX + _width / 2), (int) (_positionY + _height / 2),
                    (int) (_width * _scale/ 2), 1, Colors.colorValues.get(Colors.ColorName.LIGHTGRAY), Colors.colorValues.get(Colors.ColorName.BLACK));
            _graphics.drawCircle((int) (_positionX + _width / 2), (int) (_positionY + _height / 2), (int) (_width * _scale / 8), Colors.colorValues.get(Colors.ColorName.DARKGRAY));
        }
    }

    @Override
    public void update() {

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
        return _graphics.inBounds((int) _positionX, (int) _positionY, mouseX, mouseY, (int) (_width * _scale), (int) (_height * _scale));
    }
    public boolean hasColor() {
        return _hasColor;
    }
    public void deleteColor() {
        _hasColor = false;
    }
}



