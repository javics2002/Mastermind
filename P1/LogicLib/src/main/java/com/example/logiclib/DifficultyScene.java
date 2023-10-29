package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class DifficultyScene implements Scene {
    private final BackButton _backButton;
    private final DifficultyButton _easyDifficultyButton, _mediumDifficultyButton,
            _difficultDifficultyButton, _impossibleDifficultyButton;

    private final Text _titleText;
    private final int _padding = 20;

    public DifficultyScene(Engine engine) {
        Graphics graphics = engine.getGraphics();

        // Back button
        int backbuttonScale = 40;
        _backButton = new BackButton("UI/back.png", engine,
                backbuttonScale / 2,backbuttonScale /2,
                backbuttonScale, backbuttonScale);

        // Title
        Font font = graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String question = "¿En qué dificultad quieres jugar?";
        _titleText = new Text(question, font, engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(question, font) / 2, graphics.getLogicHeight() / 4, 0);

        // Game buttons
        int gameButtonsWidth = 330;
        int gameButtonsHeight = 90;
        int startingGameButtonsHeight = graphics.getLogicHeight() / 3;
        int padding = 20;
        _easyDifficultyButton = new DifficultyButton("facilButton.png", engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight,
                gameButtonsWidth, gameButtonsHeight, 6, 4, 4, false);
        _mediumDifficultyButton = new DifficultyButton("medioButton.png", engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight + gameButtonsHeight + padding,
                gameButtonsWidth, gameButtonsHeight, 8, 4, 6, false);
        _difficultDifficultyButton = new DifficultyButton("dificilButton.png", engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight + (gameButtonsHeight + padding) * 2,
                gameButtonsWidth, gameButtonsHeight, 10, 5, 8, true);
        _impossibleDifficultyButton = new DifficultyButton("imposibleButton.png", engine,
                graphics.getLogicWidth() / 2 - gameButtonsWidth / 2, startingGameButtonsHeight + (gameButtonsHeight + padding) * 3,
                gameButtonsWidth, gameButtonsHeight, 10, 6, 9, true);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
        _backButton.render();
        _titleText.render();
        _easyDifficultyButton.render();
        _mediumDifficultyButton.render();
        _difficultDifficultyButton.render();
        _impossibleDifficultyButton.render();
    }

    @Override
    public void handleEvents(Input a) {
        int numEvents=a.getTouchEvent().size();
        if(numEvents>0)
        {
            _backButton.handleEvents( a.getTouchEvent().get(0));
            _easyDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            _mediumDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            _difficultDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            _impossibleDifficultyButton.handleEvents( a.getTouchEvent().get(0));
        }
    }
}




