package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.State;

public class DifficultyScene implements State {
    private Text titleText;
    public DifficultyScene(Engine engine) {
        Graphics gr = engine.getGraphics();
        gr.newFont("Comfortaa-Regular.ttf", 48f);
        this.titleText = new Text("Segunda Escena", engine, gr.getWidthLogic() / 2, gr.getHeightLogic() / 4, 0);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
            this.titleText.render();
    }

    @Override
    public void handleEvents(Input.TouchEvent touchEvent) {

    }
}




