package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;

public class HintSlot extends GameObject {
    private Colors.ColorName _colorName;

    public HintSlot(Engine engine, int positionX, int positionY, int width, int height) {
        super(engine, positionX, positionY, width, height, 1f);

        _colorName = Colors.ColorName.LIGHTGRAY;
    }

    @Override
    public void render(Graphics graphics) {
        _graphics.drawCircleWithBorder(_positionX + _width / 2, _positionY + _height / 2,
                _width / 2, 1, _scale,
                Colors.colorValues.get(_colorName), Colors.colorValues.get(Colors.ColorName.BLACK));
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }

    public void setColor(Colors.ColorName colorName) {
        _colorName = colorName;
    }
}
