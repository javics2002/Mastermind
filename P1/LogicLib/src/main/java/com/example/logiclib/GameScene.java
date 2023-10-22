package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;

import java.awt.Color;
import java.util.List;

public class GameScene implements Scene {
    private Text _objectiveText;
    private Text _attemptsText;
    private QuitButton _quitButton;
    private ColorblindButton _colorblindButton;
    private List<CombinationLayout> combinationLayouts;

    public GameScene(Engine engine) {
        Graphics graphics = engine.get_graphics();

        int topMargin = 20;

        Font objetiveFont = graphics.newFont("Comfortaa-Regular.ttf", 24f);
        Font attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 18f);

        String objectiveString = "Averigua el código";
        String attemptsString = "Te quedan " + GameAttributes.Instance().attemptsLeft + " intentos!!!!";

        _objectiveText = new Text(objectiveString, objetiveFont, engine,
                graphics.getWidthLogic() / 2, topMargin, Color.black.getRGB());

        _attemptsText = new Text(attemptsString, attemptsFont, engine,
                graphics.getWidthLogic() / 2, topMargin + 2 * graphics.getStringHeight(objectiveString), Color.black.getRGB());

        _quitButton = new QuitButton("UI/close.png", engine, 0, 0, 50, 50);
        _colorblindButton = new ColorblindButton("UI/eyeClosed.png", engine, graphics.getWidthLogic() - 50, 0, 50, 50);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(Graphics gr) {
        _objectiveText.render();
        _attemptsText.render();

        _quitButton.render();
        _colorblindButton.render();
    }

    @Override
    public void handleEvents(Input input) {
        if(input.getTouchEvent().size()>0)
        {
            Input.TouchEvent elemento = input.getTouchEvent().get(0);
            //this.bPlay.handleEvents(input.getTouchEvent().get(0));
        }
    }
}
