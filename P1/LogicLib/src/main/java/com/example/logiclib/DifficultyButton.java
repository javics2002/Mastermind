package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Scene;

public class DifficultyButton extends Button {

    DifficultyButton(String filename, final Engine engine, final int positionX, final int positionY, final int width, final int height,
                     final int tryNumber,final int combinationLength, final int numberOfColors,final  boolean repeatedColors) {
        super(filename, engine, positionX, positionY, width, height, new ButtonCallback() {
            @Override
            public void onButtonClicked() {
                Scene scene = new GameScene(engine, tryNumber, combinationLength, numberOfColors, repeatedColors);
                engine.setCurrentScene(scene);
            }
        });
    }


}
