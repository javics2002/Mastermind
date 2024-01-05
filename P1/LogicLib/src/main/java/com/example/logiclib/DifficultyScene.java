package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import java.util.List;

public class DifficultyScene implements Scene {
    private final Button _backButton;
    private final Button _easyDifficultyButton, _mediumDifficultyButton,
            _difficultDifficultyButton, _impossibleDifficultyButton;

    private final Text _titleText;
    private final int _padding = 20;

    Engine _engine;

    public DifficultyScene(Engine engine) {
        _engine = engine;

        Graphics graphics = _engine.getGraphics();

        // Back button
        int backbuttonScale = 40;
        _backButton = new Button("UI/back.png", _engine,
                backbuttonScale / 2, backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                Scene scene = new InitialScene(_engine);
                _engine.setCurrentScene(scene);
            }
        };

        // Title
        Font font = graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String question = "¿En qué dificultad quieres jugar?";
        _titleText = new Text(question, font, _engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(question, font) / 2, graphics.getLogicHeight() / 4, 0);

        // Game buttons
        int gameButtonsWidth = 330;
        int gameButtonsHeight = 90;
        int startingGameButtonsHeight = graphics.getLogicHeight() / 3;
        int padding = 20;

        Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);

        _easyDifficultyButton = new Button(Colors.ColorName.BACKGROUNDGREEN, "Fácil", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight,
                gameButtonsWidth, gameButtonsHeight) {
            @Override
            public void callback() {
                Scene scene = new GameScene(_engine, 6, 4, 4, false);
                _engine.setCurrentScene(scene);
            }
        };
        _mediumDifficultyButton = new Button(Colors.ColorName.BACKGROUNDYELLOW, "Medio", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight + gameButtonsHeight + padding,
                gameButtonsWidth, gameButtonsHeight) {
            @Override
            public void callback() {
                Scene scene = new GameScene(_engine,  8, 4, 6, false);
                _engine.setCurrentScene(scene);
            }
        };
        _difficultDifficultyButton = new Button(Colors.ColorName.BACKGROUNDORANGE, "Difícil", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight + (gameButtonsHeight + padding) * 2,
                gameButtonsWidth, gameButtonsHeight) {
            @Override
            public void callback() {
                Scene scene = new GameScene(_engine, 10, 5, 8, true);
                _engine.setCurrentScene(scene);
            }
        };
        _impossibleDifficultyButton = new Button(Colors.ColorName.BACKGROUNDRED, "Imposible", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight + (gameButtonsHeight + padding) * 3,
                gameButtonsWidth, gameButtonsHeight) {
            @Override
            public void callback() {
                Scene scene = new GameScene(_engine, 10, 6, 9, true);
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

        _backButton.render();
        _titleText.render();
        _easyDifficultyButton.render();
        _mediumDifficultyButton.render();
        _difficultDifficultyButton.render();
        _impossibleDifficultyButton.render();
    }

    @Override
    public void handleEvents(Input input) {
        List<Input.TouchEvent> touchEvents = input.getTouchEvent();

        for (Input.TouchEvent touchEvent : touchEvents) {
            _backButton.handleEvents(touchEvent);
            _easyDifficultyButton.handleEvents(touchEvent);
            _mediumDifficultyButton.handleEvents(touchEvent);
            _difficultDifficultyButton.handleEvents(touchEvent);
            _impossibleDifficultyButton.handleEvents(touchEvent);
            // Puedes agregar más llamadas a handleEvents aquí según sea necesario.
        }
    }

}




