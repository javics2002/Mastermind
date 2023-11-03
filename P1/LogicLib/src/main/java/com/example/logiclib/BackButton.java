package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Scene;

public class BackButton extends Button {
    BackButton(String filename, final Engine engine, int positionX, int positionY, int width, int height) {
        super(filename, engine, positionX, positionY, width, height, new ButtonCallback() {
            @Override
            public void onButtonClicked() {
                Scene scene = new InitialScene(engine);
                engine.setCurrentScene(scene);
            }
        });
    }


}
