package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;

public class ColorSlot implements GameObject {
    private final Graphics _graphics;
    private final Engine _engine;
    private int _positionX, _positionY;
    private final int _width, _height;
    private String _name;
    private boolean _hasColor;
    private int _colorID;
    private Font _colorNum;
    private Text _numberText;
    private GameAttributes _gameAttributes;
    private Image _icon;
    private int _color;

    public ColorSlot(Engine engine, int positionX, int positionY, int width, int height, GameAttributes gameAttributes) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _gameAttributes = gameAttributes;
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _hasColor = false;
        _colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);
        _numberText = new Text("", _colorNum, engine, 0, 0, 0);
        _colorID = -1;
    }

    public ColorSlot(Engine engine, int positionX, int positionY, int width, int height, int colorID, GameAttributes gameAttributes) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _gameAttributes = gameAttributes;
        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _hasColor = colorID != -1 ? true : false;
        _colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);
        _numberText = new Text("", _colorNum, engine, 0, 0, colorID);
        _colorID = colorID;

        if(_gameAttributes.selectedWorld == -1 && _hasColor){
            _color = Colors.getColor(_colorID - 1);
            _icon = null;
        }
        else if (_gameAttributes.selectedWorld >= 0 && _hasColor){
            _icon = _graphics.loadImage(GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getWorldName()
                    + "/icon" + _colorID + ".png");
            _color = Colors.getColor(_colorID - 1);
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (hasColor()) {
            if(_icon != null)
                _graphics.drawImage(_icon, _positionX, _positionY, _width, _height);
            else
                _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2,
                        _width / 2, _color);

            if(_gameAttributes.isEyeOpen)
                _numberText.render(graphics);
        } else { // Gris
            _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2,
                    _width / 2, Colors.colorValues.get(Colors.ColorName.LIGHTGRAY));
            _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2,
                    _width / 8, Colors.colorValues.get(Colors.ColorName.DARKGRAY));
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
        int textX = _positionX + _width / 2 - _graphics.getStringWidth(num, _colorNum) / 2;
        int textY = _positionY + _height / 2 + _graphics.getStringHeight(num, _colorNum) / 2;

        _numberText.setText(num);
        _numberText.setPos(textX, textY);

        if(_gameAttributes.selectedWorld == -1){
            if(GameData.Instance().getCurrentCircles() < 0){
                _color = Colors.getColor(_colorID - 1);
                _icon = null;
            }
            else{
                final Circles circles = GameData.Instance().getCircles().get(GameData.Instance().getCurrentCircles());

                if(circles.skin){
                    _icon = _graphics.loadImage(circles.packPath + "/icon" + _colorID + ".png");
                    _color = Colors.getColor(_colorID - 1);
                }
                else{
                    _icon = null;
                    _color = Colors.parseARGB(circles.colors[color]);
                }
            }
        }
        else {
            _icon = _graphics.loadImage(GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getWorldName()
                    + "/icon" + _colorID + ".png");
            _color = Colors.getColor(_colorID - 1);
        }
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
    public void deleteColor() {
        _hasColor = false;
    }

    public void setPositionY(int posY){
        _positionY = posY;
    }
}



