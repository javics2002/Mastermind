package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;
import com.example.aninterface.Interface;

import java.awt.Color;

public class Text implements Interface {
    private String _text;
    private int _color;
    private Engine _engine;
    private Graphics _gr;

    private int _posX;
    private int _posY;

    Text(String text, Engine engine, int posX, int posY, int color) {
        _engine = engine;
        _gr = engine.getGraphics();

        _posX = posX;
        _posY = posY;
        _color = color;

        _text = text;
    }
    @Override
    public void render() {
        _gr.drawText(_text, _posX, _posY, _color);
    }
    @Override
    public void update() { }
}
