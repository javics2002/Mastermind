package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

import java.util.ArrayList;
import java.util.List;

public class CombinationLayout implements Interface {
    private Text _attemptText;
    private Image _separatorImage;
    private Text _combinationNumber;
    private List<ColorSlot> _colors;
    private List<HintSlot> _hints;

    private Engine _engine;
    private Graphics _graphics;
    private int _positionX;
    private int _positionY;

    public CombinationLayout(Engine engine, int number, int combinationLength, int positionX, int positionY) {
        _engine = engine;
        _graphics = _engine.get_graphics();

        _positionX = positionX;
        _positionY = positionY;

        Font numberFont = _graphics.newFont("Comfortaa-Regular.ttf", 10f);
        _combinationNumber = new Text(Integer.toString(number + 1), numberFont, _engine, 30, _positionY, 0);

        int scale = 32;
        int padding = 5;

        _colors = new ArrayList<>();
        for(int i = 0; i < combinationLength; i++){
            _colors.add(new ColorSlot(_engine, "colorEmpty.png", (int) (positionX + (i - combinationLength / 2f) * scale + padding * i), _positionY, scale, scale));
        }

        _hints = new ArrayList<>();
        for(int i = 0; i < combinationLength; i++){
            _colors.add(new ColorSlot(_engine, "colorEmpty.png", (int) (positionX + (i + 1 + combinationLength / 2f) * scale + padding * i), _positionY, scale, scale));
        }
    }

    @Override
    public void render() {
        _combinationNumber.render();

        for(ColorSlot color : _colors){
            color.render();
        }

        for(HintSlot hint : _hints){
            hint.render();
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
