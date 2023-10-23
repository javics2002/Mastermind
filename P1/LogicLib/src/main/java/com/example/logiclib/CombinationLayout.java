package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

import java.util.ArrayList;
import java.util.List;

public class CombinationLayout implements Interface {
    private Text _attemptText;
    private Image _separatorImage;
    private List<ColorLayout> _colors;
    private HintLayout _hintLayout;

    private Engine _engine;
    private Graphics _graphics;
    private int _positionX;
    private int _positionY;

    public CombinationLayout(Engine engine, int combinationLength, int positionX, int positionY) {
        _engine = engine;
        _graphics = _engine.get_graphics();

        _positionX = positionX;
        _positionY = positionY;

        int scale = 32;
        int padding = 5;
        _colors = new ArrayList<>();
        for(int i = 0; i < combinationLength; i++){
            _colors.add(new ColorLayout(_engine, "colorEmpty.png", (int) (positionX + (i - combinationLength / 2f) * scale + padding * i), _positionY, scale, scale));
        }
    }

    @Override
    public void render() {
        for(ColorLayout color : _colors){
            color.render();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }
}
