package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.GameObject;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;

public class ColorButton implements GameObject {
    private final Graphics _graphics;
    private final int _positionX, _positionY;
    private final int _width, _height;
    public int _colorID;
    private Font _colorNum;
    private Text _numberText;
    private GameAttributes _gameAttributes;

    ColorButton(Engine engine, int positionX, int positionY, int width, int height, int colorID, GameAttributes gameAttributes) {
        _graphics = engine.getGraphics();
        _gameAttributes = gameAttributes;

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _colorID = colorID;
        String num = String.valueOf(_colorID);
        _colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);

        int textX = _positionX + _width / 2 - _graphics.getStringWidth(num, _colorNum) / 2;
        int textY = _positionY + _height / 2 + _graphics.getStringHeight(num, _colorNum) / 2;
        _numberText = new Text(num, _colorNum, engine, textX, textY, 0);
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
    }

    @Override
    public void update() {
    }

    @Override
    public void render() {
        // Cuando el botón del OJO se pulsa, este condicional se encarga de cambiar todos los colores
        // a sus respectivas imagenes para daltónicos. Además, si el modo daltónico está activado,
        // también se encarga de quitar los números para volver al modo normal.
        _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2, _width / 2, Colors.getColor(_colorID - 1));

        if (_gameAttributes.isEyeOpen) {
            _numberText.render();
        }
    }

    public boolean inBounds(int mouseX, int mouseY) {
        return _graphics.inBounds(_positionX,_positionY,mouseX,mouseY,_width,_height);
    }
}
