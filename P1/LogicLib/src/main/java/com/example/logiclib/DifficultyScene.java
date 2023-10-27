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

        int backbuttonWidth = 40;
        int backbuttonHeight = 40;
        int padding = 20;
        int topMargin = 60;
        int startHeight = 300;
        int buttonWidth = 331;
        int buttonHeight = 88;


        _backButton = new BackButton("UI/back.png", engine, backbuttonWidth/2,backbuttonHeight/2,backbuttonWidth, backbuttonHeight);
        String pregunta= "¿En qué dificultad quieres jugar?";
        _titleText = new Text("¿En qué dificultad quieres jugar?", font, engine,
                (graphics.getWidthLogic()/2)-(graphics.getStringWidth(pregunta,font)/2), graphics.getHeightLogic() / 4, 0);

        padding = 4;
        _easyDifficultyButton = new DifficultyButton("facilButton.png", engine,
                (graphics.getWidthLogic()/2)-(buttonWidth/2), topMargin*padding, buttonWidth, buttonHeight);
        padding = 6;
        _mediumDifficultyButton = new DifficultyButton("medioButton.png", engine,
                (graphics.getWidthLogic()/2)-(buttonWidth/2), topMargin*padding, buttonWidth, buttonHeight);
        padding = 8;
        _difficultDifficultyButton = new DifficultyButton("dificilButton.png", engine,
                (graphics.getWidthLogic()/2)-(buttonWidth/2), topMargin*padding, buttonWidth, buttonHeight);
        padding = 10;
        _impossibleDifficultyButton = new DifficultyButton("imposibleButton.png", engine,
                (graphics.getWidthLogic()/2)-(buttonWidth/2), topMargin*padding, buttonWidth, buttonHeight);
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


            this._mediumDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._difficultDifficultyButton.handleEvents( a.getTouchEvent().get(0));
            this._impossibleDifficultyButton.handleEvents( a.getTouchEvent().get(0));
        }
    }
}




