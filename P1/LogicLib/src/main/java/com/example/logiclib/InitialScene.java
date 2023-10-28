package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;

public class InitialScene implements Scene {
    private PlayButton _playButton;
    private Font _titleFont;
    private Text _titleText;
    private int _topMargin = 60;

    Engine _engine;

    public InitialScene(Engine engine) {
        _engine = engine;
        Graphics graphics = engine.getGraphics();

        _titleFont = graphics.newFont("Comfortaa-Regular.ttf", 48f);
        String title = "MasterMind";

        int titleWidth = graphics.getStringWidth(title, _titleFont);
        _titleText = new Text(title, _titleFont, engine, graphics.getWidthLogic()/2 -titleWidth/2, _topMargin, 0);

        int buttonWidth = 330;
        int buttonHeight = 90;
        _playButton = new PlayButton("playButton.png", engine,
                graphics.getWidthLogic()/2 - buttonWidth/2, _topMargin * 2, buttonWidth, buttonHeight);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
        _playButton.render();
        _titleText.render();
    }

    @Override
    public void handleEvents(Input a) {
        if(a.getTouchEvent().size()>0)
        {
            this._playButton.handleEvents( a.getTouchEvent().get(0));
        }
    }
}




