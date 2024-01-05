package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;

public class Text implements GameObject {
    private String _text;
    private final int _color;
    private final Graphics _graphics;
    private int _posX, _posY;
    private final Font _font;
    private final boolean _centered;

    Text(String text, Font font, Engine engine, int posX, int posY, int color, boolean centered) {
        _graphics = engine.getGraphics();

        _centered = centered;

        _text = text;
        _font = font;

        _posX = posX;
        _posY = posY;
        _color = color;
    }

    @Override
    public void render() {
        if(_centered)
            _graphics.drawText(_text, _font, _posX - _graphics.getStringWidth(_text, _font) / 2,
                    _posY - _graphics.getStringHeight(_text, _font) / 2, _color);
        else
            _graphics.drawText(_text, _font, _posX, _posY, _color);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }

    public void setText(String newText) {
        _text = newText;
    }

    public void setPos(int posX, int posY) {
        _posX = posX;
        _posY = posY;
    }
}
