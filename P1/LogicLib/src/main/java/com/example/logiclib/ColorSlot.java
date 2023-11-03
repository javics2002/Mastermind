package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;

public class ColorSlot implements GameObject {
    private Image _image;
    private final Graphics _graphics;
    private final int _positionX, _positionY;
    private final int _width, _height;
    private String _name;
    private boolean _hasColor;
    private int _colorID;
    private Font _colorNum;
    private Text _numberText;

    public ColorSlot(Engine engine, String filename, int positionX, int positionY, int width, int height) {
        _graphics = engine.getGraphics();
         if(filename!="")_image = _graphics.newImage(filename);

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _name = filename;
        _hasColor = false;
        _colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);
        _numberText = new Text("", _colorNum, engine,0, 0,0);
        _colorID = -1;
    }



    @Override
    public void render() {


        if (hasColor()&&GameAttributes.Instance().isEyeOpen) {
            _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2, _width / 2, colors[_colorID-1]);
            _numberText.render();
        }
        else if(hasColor()){
            _graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2, _width / 2, colors[_colorID-1]);
        }
        else {
            _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
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
        int textX = _positionX + _width / 2 - _graphics.getStringWidth(num, _colorNum)/ 2;
        int textY = _positionY + _height / 2 + _graphics.getStringHeight(num, _colorNum) / 4;

        _numberText.setText(num);
        _numberText.setPos(textX,textY);

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



