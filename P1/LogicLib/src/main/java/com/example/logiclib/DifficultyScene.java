package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class DifficultyScene implements Scene {
    //private SetDifficultyButton easyDifficultyButton, mediumDifficultyButton,
    //        difficultDifficultyButton, impossibleDifficultyButton;

    private Text titleText;
    public DifficultyScene(Engine engine) {
        Graphics gr = engine.get_graphics();
        Font font = gr.newFont("Comfortaa-Regular.ttf", 48f);
        titleText = new Text("Segunda Escena", font, engine, gr.getWidthLogic() / 2, gr.getHeightLogic() / 4, 0);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
            this.titleText.render();
    }

    @Override
    public void handleEvents(Input a) {
        // a.getTouchEvent().get(0)
    }
}




