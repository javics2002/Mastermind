package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Scene;

public class BackButton extends Button {
    BackButton(String filename, Engine engine, int positionX, int positionY, int width, int height) {
        super(filename, engine, positionX, positionY, width, height);
    }

    @Override
    void callback() {
        Scene scene = new InitialScene(_engine);
        _engine.setCurrentScene(scene);
    }
}
