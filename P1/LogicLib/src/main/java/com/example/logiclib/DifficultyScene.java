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
    private int _padding = 20;

    public DifficultyScene(Engine engine) {
        Graphics graphics = engine.getGraphics();
        Font font = graphics.newFont("Comfortaa-Regular.ttf", 24f);

        // Dimensions
        int buttonWidth = 330;
        int buttonHeight = 90;
        int backbuttonWidth = 40;
        int backbuttonHeight = 40;

        // Back button
        _backButton = new BackButton("UI/back.png", engine, backbuttonWidth/2,backbuttonHeight/2, backbuttonWidth, backbuttonHeight);

        // Title
        String question = "¿En qué dificultad quieres jugar?";
        _titleText = new Text(question, font, engine,
                graphics.getWidthLogic()/2-graphics.getStringWidth(question, font)/2, graphics.getHeightLogic() / 4, 0);

        // Game buttons
        _easyDifficultyButton = new DifficultyButton("facilButton.png", engine,
                graphics.getWidthLogic()/2-buttonWidth/2, graphics.getHeightLogic() / 3, buttonWidth, buttonHeight);

        //_mediumDifficultyButton = new DifficultyButton("medioButton.png", engine,
                //graphics.getWidthLogic()/2-buttonWidth/2, topMargin*padding, buttonWidth, buttonHeight);
        //_difficultDifficultyButton = new DifficultyButton("dificilButton.png", engine,
                //graphics.getWidthLogic()/2-buttonWidth/2, topMargin*padding, buttonWidth, buttonHeight);
        //_impossibleDifficultyButton = new DifficultyButton("imposibleButton.png", engine,
                //graphics.getWidthLogic()/2-buttonWidth/2, topMargin*padding, buttonWidth, buttonHeight);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
        _backButton.render();
        _titleText.render();
        _easyDifficultyButton.render();
        //_mediumDifficultyButton.render();
        //_difficultDifficultyButton.render();
        //_impossibleDifficultyButton.render();
    }

    @Override
    public void handleEvents(Input a) {
        int numEvents=a.getTouchEvent().size();
        if(numEvents>0)
        {
            this._easyDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._backButton.handleEvents( a.getTouchEvent().get(0));
            //this._mediumDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            //this._difficultDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            //this._impossibleDifficultyButton.handleEvents( a.getTouchEvent().get(0));
        }
    }
}




