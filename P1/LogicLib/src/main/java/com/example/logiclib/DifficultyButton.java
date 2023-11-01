package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Scene;

public class DifficultyButton extends Button {
    private final int _tryNumber, _combinationLength, _numberOfColors;
    private final boolean _repeatedColors;

    DifficultyButton(String filename, Engine engine, int positionX, int positionY, int width, int height,
                     int tryNumber, int combinationLength, int numberOfColors, boolean repeatedColors) {
        super(filename, engine, positionX, positionY, width, height);

        _tryNumber = tryNumber;
        _combinationLength = combinationLength;
        _numberOfColors = numberOfColors;
        _repeatedColors = repeatedColors;
    }

    @Override
    void callback() {
        Scene scene = new GameScene(_engine, _tryNumber, _combinationLength, _numberOfColors, _repeatedColors);
        _engine.setCurrentScene(scene);
    }
}
