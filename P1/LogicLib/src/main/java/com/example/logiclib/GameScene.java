package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements Scene {
    private final Engine _engine;
    private final GameAttributes gameAttributes;
    private final Text _objectiveText, _attemptsText;
    private final QuitButton _quitButton;
    private final ColorblindButton _colorblindButton;
    private final List<CombinationLayout> _combinationLayouts;
    private final List<ColorButton> _colorButtons;

    public GameScene(Engine engine, int tryNumber, int combinationLength, int numberOfColors, boolean repeatedColors) {
        _engine = engine;
        Graphics graphics = _engine.getGraphics();

        // Init GameAttributes
        gameAttributes = GameAttributes.Instance();
        gameAttributes.attemptsNumber = tryNumber;
        gameAttributes.attemptsLeft = tryNumber;
        gameAttributes.combinationLength = combinationLength;
        gameAttributes.colorNumber = numberOfColors;
        gameAttributes.repeatedColors = repeatedColors;
        gameAttributes.activeLayout = 0;
        gameAttributes.isEyeOpen = false;
        gameAttributes.resultCombination = new Combination(combinationLength, numberOfColors, repeatedColors);

        // Print Temp
        gameAttributes.resultCombination.printCombination();

        // Title
        int verticalMargin = 5;
        Font objetiveFont = graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String objectiveString = "Averigua el código";
        _objectiveText = new Text(objectiveString, objetiveFont, _engine,
                (graphics.getLogicWidth() / 2) - (graphics.getStringWidth(objectiveString, objetiveFont) / 2),
                verticalMargin + graphics.getStringHeight(objectiveString, objetiveFont), 0);

        // Attempts
        String attemptsString = "Te quedan " + GameAttributes.Instance().attemptsLeft + " intentos.";
        Font attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 16f);
        _attemptsText = new Text(attemptsString, attemptsFont, _engine,
                (graphics.getLogicWidth() / 2) - (graphics.getStringWidth(attemptsString, attemptsFont) / 2),
                verticalMargin + graphics.getStringHeight(objectiveString, objetiveFont) + graphics.getStringHeight(attemptsString, attemptsFont),
                0);

        // Quit button
        int buttonDimension = 50;
        int horizontalMargin = 5;
        _quitButton = new QuitButton("UI/close.png", _engine,
                horizontalMargin, verticalMargin, buttonDimension, buttonDimension);

        // ColorBlind button
        _colorblindButton = new ColorblindButton("UI/eyeClosed.png", _engine,
                graphics.getLogicWidth() - buttonDimension - horizontalMargin, verticalMargin,
                buttonDimension, buttonDimension);

        // Combination
        int initialHeight = 100;
        int verticalPadding = 15, scale = 40;
        _combinationLayouts = new ArrayList<>();
        for (int i = 0; i < tryNumber; i++) {
            _combinationLayouts.add(new CombinationLayout(_engine, i, combinationLength,
                    graphics.getLogicWidth() / 2, initialHeight + (verticalPadding + scale) * i, scale));
        }

        // Color buttons
        int horizontalPadding = 6;
        _colorButtons = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            _colorButtons.add(new ColorButton("color" + (i + 1) + ".png", _engine,
                    (int) (graphics.getLogicWidth() / 2 + (i - numberOfColors / 2f) * (scale + horizontalPadding)),
                    graphics.getLogicHeight() - 60, scale, scale, i + 1));
        }
    }


    @Override
    public void update(double deltaTime) {
        updateTriesText();

        // _combinationLayouts.get(gameAttributes._activeLayout).getCurrentCombination().printCombination();
        CombinationLayout activeLayout = _combinationLayouts.get(gameAttributes.activeLayout);
        if (activeLayout.isFull()) {
            if (activeLayout.getCurrentCombination().equals(gameAttributes.resultCombination)) {
                // USER WON
                Scene gameOverScene = new GameOverScene(_engine);
                _engine.setCurrentScene(gameOverScene);
                return;
            }

            // Hints
            Combination.HintEnum[] hints = activeLayout.getCurrentCombination().getHint(gameAttributes.resultCombination);
            activeLayout.setHints(hints);

            gameAttributes.attemptsLeft--;
            gameAttributes.activeLayout++;

            if (gameAttributes.attemptsLeft == 0) {
                // User LOST
                Scene gameOverScene = new GameOverScene(_engine);
                _engine.setCurrentScene(gameOverScene);
            }
        }


        for (CombinationLayout combination : _combinationLayouts) {
            combination.update();
        }

        for (ColorButton colorButton : _colorButtons) {
            colorButton.update();
        }
    }

    @Override
    public void render(Graphics graphics) {
        //TODO pasar graphics a todos los render
        _objectiveText.render();
        _attemptsText.render();

        _quitButton.render();
        _colorblindButton.render();

        for (CombinationLayout combination : _combinationLayouts) {
            combination.render();
        }

        final int colorButtonBackgroundHeight = 80;
        graphics.drawRect(0, graphics.getLogicHeight() - colorButtonBackgroundHeight,
                graphics.getLogicWidth(), colorButtonBackgroundHeight, 0xFFFAFAFA);

        for (ColorButton colorButton : _colorButtons) {
            colorButton.render();
        }
    }

    @Override
    public void handleEvents(Input input) {
        if (input.getTouchEvent().size() > 0) {
            Input.TouchEvent touchEvent = input.getTouchEvent().get(0);
            _colorblindButton.handleEvents(touchEvent);
            _quitButton.handleEvents(touchEvent);

            // Detectar click en colores ya colocados
            // Sirve para borrarlos
            _combinationLayouts.get(GameAttributes.Instance().activeLayout).handleEvents(touchEvent);

            // Cuando detecta un click en un color, se coloca en el primer hueco posible.
            for (ColorButton colorButton : _colorButtons) {
                if (colorButton.handleEvents(touchEvent)) {
                    _combinationLayouts.get(gameAttributes.activeLayout).setNextColor(colorButton._colorID, gameAttributes.isEyeOpen);
                    _combinationLayouts.get(gameAttributes.activeLayout).getCurrentCombination().printCombination();
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
        String attemptsString = "Te quedan " + gameAttributes.attemptsLeft + " intentos.";
        if (gameAttributes.attemptsLeft == 1) {
            attemptsString = "Este es tu último intento!";
        }

        _attemptsText.setText(attemptsString);
    }
}
