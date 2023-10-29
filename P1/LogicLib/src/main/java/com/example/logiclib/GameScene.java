package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements Scene {
    private Engine _engine;
    private GameAttributes gameAttributes;
    private Text _objectiveText;
    private Text _attemptsText;
    private QuitButton _quitButton;
    private ColorblindButton _colorblindButton;
    private List<CombinationLayout> _combinationLayouts;
    private List<ColorButton> _colorButtons;

    private boolean eventHandled = false;
    public GameScene(Engine engine, int tryNumber, int combinationLength, int numberOfColors) {
        _engine = engine;
        Graphics graphics = _engine.getGraphics();


        // Init GameAttributes
        gameAttributes = GameAttributes.Instance();
        gameAttributes._attemptsNumber = tryNumber;
        gameAttributes._attemptsLeft = tryNumber;
        gameAttributes._combinationLength = combinationLength;
        gameAttributes._colorNumber = numberOfColors;
        gameAttributes._activeLayout = 0;
        gameAttributes._isEyeOpen = false;
        gameAttributes._resultCombination = new Combination(combinationLength, numberOfColors);

        // Print Temp
        gameAttributes._resultCombination.printCombination();

        // Title Font
        Font objetiveFont = graphics.newFont("Comfortaa-Regular.ttf", 24f);

        // Dimensions
        int lineSpacing = 20;
        int topMargin = objetiveFont.getFontSize();

        // Title
        String objectiveString = "Averigua el código";
        _objectiveText = new Text(objectiveString, objetiveFont, _engine,
                (graphics.getWidthLogic()/2)-(graphics.getStringWidth(objectiveString,objetiveFont)/2), topMargin, 0);

        // Attempts
        String attemptsString = "Te quedan " + GameAttributes.Instance()._attemptsLeft + " intentos.";
        Font attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 16f);
        _attemptsText = new Text(attemptsString, attemptsFont, _engine,
                (graphics.getWidthLogic()/2)-(graphics.getStringWidth(objectiveString,objetiveFont)/2), topMargin + lineSpacing, 0);

        // Quit button
        int quitButtonDimensions=50;
        int offsetSize=quitButtonDimensions*2;
        _quitButton = new QuitButton("UI/close.png", _engine, quitButtonDimensions/offsetSize,quitButtonDimensions/offsetSize, quitButtonDimensions, quitButtonDimensions);

        // ColorBlind button
        _colorblindButton = new ColorblindButton("UI/eyeClosed.png", _engine, graphics.getWidthLogic() - quitButtonDimensions, quitButtonDimensions/offsetSize, quitButtonDimensions, quitButtonDimensions,this);

        // Combination
        int initialHeight = 100;
        int padding = 40;
        _combinationLayouts = new ArrayList<>();
        for(int i = 0; i < tryNumber; i++){
            _combinationLayouts.add(new CombinationLayout(_engine, i, combinationLength,
                    graphics.getWidthLogic() / 2, initialHeight + i * padding));
        }

        // Color buttons
        _colorButtons = new ArrayList<>();
        int scale = 32;
        padding = 15;
        for(int i = 0; i < numberOfColors; i++) {
            _colorButtons.add(new ColorButton("color" + (i + 1) + ".png", _engine,
                    (int) (graphics.getWidthLogic() / 2 + (i - numberOfColors / 2f) * scale - padding / 2 + i * padding / 2),
                    graphics.getHeightLogic() - 70 , scale, scale, i + 1));
        }
    }


    @Override
    public void update(double deltaTime) {
        updateTriesText();

        // _combinationLayouts.get(gameAttributes._activeLayout).getCurrentCombination().printCombination();
        CombinationLayout activeLayout = _combinationLayouts.get(gameAttributes._activeLayout);
        if (activeLayout.isFull()){
            if (activeLayout.getCurrentCombination().equals(gameAttributes._resultCombination)){ // USER WON
                // Change scene TEMP
                InitialScene scene = new InitialScene(_engine);
                _engine.setCurrentScene(scene);

                System.out.println("USER WON");
                return;
            }

            // Hints
            Combination.HintEnum[] hints = activeLayout.getCurrentCombination().getHint(gameAttributes._resultCombination);
            activeLayout.setHints(hints);

            gameAttributes._attemptsLeft--;
            gameAttributes._activeLayout++;

            if (gameAttributes._attemptsLeft == 0) { // User LOST
                // Change scene
                InitialScene scene = new InitialScene(_engine);
                _engine.setCurrentScene(scene);

                System.out.println("USER LOST");
            }
        }
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

        for(ColorButton colorButton : _colorButtons) {
            colorButton.render();
        }
    }

    @Override
    public void handleEvents(Input input) {
        if(input.getTouchEvent().size()>0)
        {
            Input.TouchEvent touchEvent = input.getTouchEvent().get(0);
            _colorblindButton.handleEvents(touchEvent);
            _quitButton.handleEvents(touchEvent);

            // Cuando detecta un click en un color, se coloca en el primer hueco posible.
            for(ColorButton colorButton : _colorButtons) {
                if (colorButton.handleEvents(touchEvent)){
                    _combinationLayouts.get(gameAttributes._activeLayout).setNextColor(colorButton._colorID, gameAttributes._isEyeOpen);
                    _combinationLayouts.get(gameAttributes._activeLayout).getCurrentCombination().printCombination();
                    break;
                }
            }
        }

    }

    public List<CombinationLayout> getCombinationLayouts() {
        return _combinationLayouts;
    }

    public List<ColorButton> getColorButtons() {
        return _colorButtons;
    }

    private void updateTriesText() {
        String attemptsString = "Te quedan " + gameAttributes._attemptsLeft + " intentos.";
        if (gameAttributes._attemptsLeft == 1){
            attemptsString = "Este es tu último intento!";
        }

        _attemptsText.setText(attemptsString);
    }
}
