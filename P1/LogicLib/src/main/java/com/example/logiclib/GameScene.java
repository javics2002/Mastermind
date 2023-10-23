package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameScene implements Scene {
    private Text _objectiveText;
    private Text _attemptsText;
    private QuitButton _quitButton;
    private ColorblindButton _colorblindButton;
    private List<CombinationLayout> _combinationLayouts;

    public GameScene(Engine engine, int tryNumber) {
        Graphics graphics = engine.get_graphics();

        int topMargin = 50;

        Font objetiveFont = graphics.newFont("Comfortaa-Regular.ttf", 12f);
        Font attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 10f);

        String objectiveString = "Averigua el código";
        String attemptsString = "Te quedan " + GameAttributes.Instance().attemptsLeft + " intentos!!!!";

        _objectiveText = new Text(objectiveString, objetiveFont, engine,
                graphics.getWidthLogic() / 2, (int) (topMargin + graphics.getStringHeight(objectiveString) / 2f), Color.black.getRGB());

        _attemptsText = new Text(attemptsString, attemptsFont, engine,
                graphics.getWidthLogic() / 2, (int)(topMargin + 2 * graphics.getStringHeight(objectiveString) + graphics.getStringHeight(attemptsString) / 2f), Color.black.getRGB());

        _quitButton = new QuitButton("UI/close.png", engine, 50, 40, 50, 50);
        _colorblindButton = new ColorblindButton("UI/eyeClosed.png", engine, graphics.getWidthLogic() - 50, 40, 50, 50);

        int initialHeight = 100;
        int padding = 40;
        _combinationLayouts = new ArrayList<>();
        for(int i = 0; i < tryNumber; i++){
            _combinationLayouts.add(new CombinationLayout(engine, 4, graphics.getWidthLogic() / 2, initialHeight + i * padding));
        }
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

        for(CombinationLayout combination : _combinationLayouts) {
            combination.render();
        }

        gr.setColor(Color.lightGray.getRGB());
        gr.fillRect(gr.logicToRealX(0), gr.logicToRealY(gr.getHeightLogic() - 100), gr.scaleToReal(gr.getWidthLogic()), gr.scaleToReal(100));
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
