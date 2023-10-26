package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class DifficultyScene implements Scene {
    private BackButton _backButton;
    private DifficultyButton _easyDifficultyButton, _mediumDifficultyButton,
            _difficultDifficultyButton, _impossibleDifficultyButton;

    private Text _titleText;

    public DifficultyScene(Engine engine) {
        Graphics graphics = engine.getGraphics();
        Font font = graphics.newFont("Comfortaa-Regular.ttf", 24f);

        _backButton = new BackButton("UI/back.png", engine, 30,30,40, 40);
        _titleText = new Text("¿En qué dificultad quieres jugar?", font, engine,
                graphics.getWidthLogic() / 2, graphics.getHeightLogic() / 4, 0);

        int padding = 20;
        int startHeight = 300;
        int buttonWidth = 331;
        int buttonHeight = 88;
        int centerWidth = (int)(graphics.getWidthLogic()*0.5);
        _easyDifficultyButton = new DifficultyButton("facilButton.png", engine,
                centerWidth, startHeight + (buttonHeight + padding) * 0, buttonWidth, buttonHeight);
        _mediumDifficultyButton =  new DifficultyButton("medioButton.png", engine,
                centerWidth, startHeight + (buttonHeight + padding) * 1, buttonWidth, buttonHeight);
        _difficultDifficultyButton = new DifficultyButton("dificilButton.png", engine,
                centerWidth, startHeight + (buttonHeight + padding) * 2, buttonWidth, buttonHeight);
        _impossibleDifficultyButton = new DifficultyButton("imposibleButton.png", engine,
                centerWidth, startHeight + (buttonHeight + padding) * 3, buttonWidth, buttonHeight);
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
            this._easyDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._backButton.handleEvents( a.getTouchEvent().get(0));
            this._easyDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._mediumDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._difficultDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._impossibleDifficultyButton.handleEvents( a.getTouchEvent().get(0));


        }
    }
}




