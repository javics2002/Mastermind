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

    Engine _engine;

    public InitialScene(Engine engine) {
        _engine = engine;
        Graphics graphics = engine.getGraphics();

        _titleFont = graphics.newFont("Comfortaa-Regular.ttf", 48f);




        int topMargin = 60;
        int lineSpacing = 20;
        int padding =2;
        String objectiveString = "MasterMind";





        int a= graphics.getStringWidth(objectiveString,_titleFont);
        System.out.print(a);
        _titleText = new Text(objectiveString, _titleFont, engine,
                (graphics.getWidthLogic()/2)-a/2 , topMargin*padding, 0);
        padding=padding+2;
        int button_Width=331;
        int button_Height=88;
        _playButton = new PlayButton("playButton.png", engine,
                (int)(graphics.getWidthLogic()/2)-(button_Width/2), topMargin*padding, button_Width, button_Height);
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
        // Bucle con lista touchEvent, el boton recibe un unico touchevent
        if(a.getTouchEvent().size()>0)
        {
            Input.TouchEvent elemento = a.getTouchEvent().get(0);
            this._playButton.handleEvents( a.getTouchEvent().get(0));
        }
    }
}




