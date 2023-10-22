package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

public class Text implements Interface {
    private String _text;
    private int _color;
    private Engine _engine;
    private Graphics _graphics;

    private int _posX;
    private int _posY;

    Font _font;

    Text(String text, Font font, Engine engine, int posX, int posY, int color) {
        _engine = engine;
        _graphics = engine.get_graphics();

        _posX = posX;
        _posY = posY;
        _color = color;

        _text = text;
        _font = font;
    }
    @Override
    public void render() {
        _graphics.drawText(_text, _font, _posX, _posY, _color);
    }
    @Override
    public void update() { }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }
}
