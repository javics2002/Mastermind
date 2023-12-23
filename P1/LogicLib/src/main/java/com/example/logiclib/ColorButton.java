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
    private final Text _numberText;
    private final GameAttributes _gameAttributes;
    private final Image _icon;
    private final int _color;

    ColorButton(Engine engine, int positionX, int positionY, int width, int height, int colorID, GameAttributes gameAttributes) {
        _graphics = engine.getGraphics();
        _gameAttributes = gameAttributes;

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _colorID = colorID;
        String num = String.valueOf(_colorID);

        final Font colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);

        int textX = _positionX + _width / 2 - _graphics.getStringWidth(num, colorNum) / 2;
        int textY = _positionY + _height / 2 + _graphics.getStringHeight(num, colorNum) / 2;
        _numberText = new Text(num, colorNum, engine, textX, textY, 0);

        if(_gameAttributes.selectedWorld == -1){
            if(GameData.Instance().getCurrentCircle() < 0){
                _color = Colors.getColor(_colorID - 1);
                _icon = null;
            }
            else{
                final Circles circles = engine.jsonToObject("Shop/Circles/circles_0"
                        + Integer.toString(GameData.Instance().getCurrentCircle() + 1) + ".json", Circles.class);

                if(circles.skin){
                    _icon = _graphics.loadImage(circles.packPath + "/icon" + _colorID + ".png");
                    _color = Colors.getColor(_colorID - 1);
                }
                else{
                    _icon = null;
                    _color = Colors.parseARGB(circles.colors[colorID]);
                }
            }
        }
        else {
            _icon = _graphics.loadImage(GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getWorldName()
                    + "/icon" + _colorID + ".png");
            _color = Colors.getColor(_colorID - 1);
        }
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics graphics) {
        // Cuando el botón del OJO se pulsa, este condicional se encarga de cambiar todos los colores
        // a sus respectivas imagenes para daltónicos. Además, si el modo daltónico está activado,
        // también se encarga de quitar los números para volver al modo normal.
        if(_icon != null)
            _graphics.drawImage(_icon, _positionX, _positionY, _width, _height);
        else
            _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2,
                    _width / 2, _color);

        if (_gameAttributes.isEyeOpen)
            _numberText.render(graphics);
    }

    public boolean inBounds(int mX, int mY) {
        return (mX >= (_graphics.logicToRealX(_positionX))
                && mX <= _graphics.logicToRealX(_positionX) + _graphics.scaleToReal(_width)
                && mY >= _graphics.logicToRealY(_positionY)
                && mY <= _graphics.logicToRealY(_positionY) + _graphics.scaleToReal(_height));
    }
}
