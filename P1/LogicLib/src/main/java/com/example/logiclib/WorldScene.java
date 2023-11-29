package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class WorldScene implements Scene {
    private final Button _backButton;
    private Button _levelButtons[];

    private final Text _titleText;

    Engine _engine;

    public WorldScene(Engine engine, int worldId) {
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
        String worldTitle = "Mundo " + Integer.toString(worldId);
        _titleText = new Text(worldTitle, font, _engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(worldTitle, font) / 2, graphics.getLogicHeight() / 4, 0);

        // Game buttons
        final int padding = 20;
        final int levelsPerRow = 3;
        final int gameButtonsSize = (graphics.getLogicWidth() - (levelsPerRow + 1) * padding) / levelsPerRow;
        final int startingGameButtonsHeight = graphics.getLogicHeight() / 3;

        Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);

        //TODO cambiar 10 por numero de niveles que haya en la carpeta
        _levelButtons = new Button[10];
        for(int i = 0; i < 10; i++){
            int row = i / levelsPerRow;
            int column = i % levelsPerRow;
            _levelButtons[i] = new Button(Colors.ColorName.BACKGROUNDGREEN, Integer.toString(i + 1), buttonFont, _engine,
                    padding + column * (gameButtonsSize + padding),
                    startingGameButtonsHeight + row * (gameButtonsSize + padding),
                    gameButtonsSize, gameButtonsSize) {
                @Override
                public void callback() {
                    Scene scene = new GameScene(_engine, 6, 4, 4, false);
                    _engine.setCurrentScene(scene);
                }
            };
        }
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics gr) {
        _backButton.render();
        _titleText.render();

        for(int i =0; i < 10; i ++){
            _levelButtons[i].render();
        }
    }

    @Override
    public void handleEvents(Input a) {
        int numEvents = a.getTouchEvent().size();
        if (numEvents > 0) {
            _backButton.handleEvents(a.getTouchEvent().get(0));

            for(int i =0; i < 10; i ++){
                _levelButtons[i].handleEvents(a.getTouchEvent().get(0));
            }
        }
    }
}




