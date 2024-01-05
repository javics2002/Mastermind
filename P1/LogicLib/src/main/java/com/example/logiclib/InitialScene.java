package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import java.util.List;

public class InitialScene implements Scene {
    private final Button _playButton;
    private final Text _titleText;
    Engine _engine;

    public InitialScene(Engine engine) {
        _engine = engine;

        Graphics graphics = _engine.getGraphics();

        Font _titleFont = graphics.newFont("Comfortaa-Regular.ttf", 48f);
        String title = "Master Mind";

        int titleWidth = graphics.getStringWidth(title, _titleFont);
        _titleText = new Text(title, _titleFont, _engine,
                graphics.getLogicWidth() / 2 - titleWidth / 2, 200, 0);

        int buttonWidth = 330;
        int buttonHeight = 90;

        Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);
        _playButton = new Button(Colors.ColorName.BACKGROUNDBLUE, "Jugar", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - buttonWidth / 2, graphics.getLogicHeight() / 2, buttonWidth, buttonHeight) {
            @Override
            public void callback() {
                Scene scene = new DifficultyScene(_engine);
                _engine.setCurrentScene(scene);
            }
        };
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics graphics) {
        graphics.clear(Colors.colorValues.get(Colors.ColorName.BACKGROUND));

        _playButton.render();
        _titleText.render();
    }

    public void handleEvents(Input input) {
        List<Input.TouchEvent> touchEvents = input.getTouchEvent();

        for (Input.TouchEvent touchEvent : touchEvents) {
            _playButton.handleEvents(touchEvent);

        }
    }


}




