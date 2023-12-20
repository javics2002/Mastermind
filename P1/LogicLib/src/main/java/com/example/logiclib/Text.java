package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.GameObject;

public class Text implements GameObject {
    private String _text;
    private final int _color;
    private final Graphics _graphics;
    private int _posX, _posY;
    Font _font;

    Text(String text, Font font, Engine engine, int posX, int posY, int color) {
        _graphics = engine.getGraphics();

        _posX = posX;
        _posY = posY;
        _color = color;

        _text = text;
        _font = font;
    }

    @Override
    public void render(Graphics graphics) {
        _graphics.drawText(_text, _font, _posX, _posY, _color);
    }

    @Override
    public void update(double deltaTime) {
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

    public String getText(){
        return _text;
    }
    public Font getFont(){
        return _font;
    }

    public int getPosX(){
        return _posX;
    }

    public int getPosY(){
        return _posY;
    }
}
