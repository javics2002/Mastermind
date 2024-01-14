package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.util.ArrayList;
import java.util.List;

public class SolutionScene implements Scene {
    private final Engine _engine;
    private final GameAttributes _gameAttributes;
    private final Text _objectiveText;
    private final Button _quitButton, _colorblindButton;
    private final List<CombinationLayout> _combinationLayouts;
    private final List<Combination> _combinations;
    private final List<ColorButton> _colorButtons;
    private final Transition _transition;
    private boolean _gameFinished;
    private final int _visibleLayouts = 10;

    public SolutionScene(Engine engine, int tryNumber, int combinationLength, int numberOfColors, boolean repeatedColors) {
        _engine = engine;
        Graphics graphics = _engine.getGraphics();

        // Init GameAttributes
        _gameAttributes = new GameAttributes();

        _gameAttributes.attemptsNumber = tryNumber;
        _gameAttributes.attemptsLeft = tryNumber;
        _gameAttributes.combinationLength = combinationLength;
        _gameAttributes.colorNumber = numberOfColors;
        _gameAttributes.repeatedColors = repeatedColors;
        _gameAttributes.activeLayout = 0;
        _gameAttributes.isEyeOpen = false;
        _gameAttributes.resultCombination = new Combination(combinationLength, numberOfColors, repeatedColors);
        _gameAttributes.multiplayer = true;
        _gameFinished = false;

        // Transition
        _transition = new Transition(_engine, graphics.getWidth(), graphics.getHeight());

        // Title
        final int verticalMargin = 5;
        Font objetiveFont = graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String objectiveString = "Elige la solución";
        _objectiveText = new Text(objectiveString, objetiveFont, _engine, graphics.getLogicWidth() / 2,
                verticalMargin + 40, 0, true);

        // Quit button
        int buttonDimension = 50;
        int horizontalMargin = 5;
        _quitButton = new Button("UI/close.png", _engine,
                horizontalMargin, verticalMargin, buttonDimension, buttonDimension) {
            @Override
            public void callback() {
                Scene scene = new DifficultyScene(_engine, true);
                _transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
            }
        };

        // ColorBlind button
        _colorblindButton = new Button("UI/eyeClosed.png", _engine,
                graphics.getLogicWidth() - buttonDimension - horizontalMargin, verticalMargin,
                buttonDimension, buttonDimension) {
            @Override
            public void callback() {
                _gameAttributes.isEyeOpen = !_gameAttributes.isEyeOpen;
                _colorblindButton.setImage(_gameAttributes.isEyeOpen ? "UI/eyeOpened.png" : "UI/eyeClosed.png");
            }
        };

        // Combinations
        int initialHeight = 100;
        int verticalPadding = 15, scale = 40;
        _combinations = new ArrayList<>();
        _combinationLayouts = new ArrayList<>();
        _combinations.add(new Combination(combinationLength));
        _combinationLayouts.add(new CombinationLayout(_engine, -1, combinationLength,
                graphics.getLogicWidth() / 2, initialHeight + (verticalPadding + scale) * 0,
                scale, _gameAttributes, _combinations.get(0)));

        // Color buttons
        int horizontalPadding = 6;
        _colorButtons = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            _colorButtons.add(new ColorButton(_engine,
                    (int) (graphics.getLogicWidth() / 2 + (i - numberOfColors / 2f) * (scale + horizontalPadding)),
                    graphics.getLogicHeight() - 60, scale, scale, i + 1, _gameAttributes));
        }

        _transition.PlayTransition(Transition.TransitionType.fadeIn, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, null);
    }

    @Override
    public void update(double deltaTime) {
        _transition.update(deltaTime);

        if (_gameFinished) {
            return;
        }

        updateTriesText();

        int firstCombination = _gameAttributes.activeLayout - _visibleLayouts + 1;
        if (firstCombination < 0)
            firstCombination = 0;

        for (int i = firstCombination; i < _combinationLayouts.size() && i < firstCombination + _visibleLayouts; i++) {
            _combinationLayouts.get(i).setPositionY(100 + 55 * (i - firstCombination));
        }

        // _combinationLayouts.get(gameAttributes._activeLayout).getCurrentCombination().printCombination();
        CombinationLayout activeLayout = _combinationLayouts.get(_gameAttributes.activeLayout);
        if (activeLayout.isFull()) {
            Scene scene = new GameScene(_engine, _gameAttributes.attemptsNumber, _gameAttributes.combinationLength,
                    _gameAttributes.colorNumber, _gameAttributes.repeatedColors, _gameAttributes.multiplayer,
                    _combinations.get(0), _gameAttributes.difficultyIndex);
            _transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
            _gameFinished = true;
        }

        for (CombinationLayout combination : _combinationLayouts) {
            combination.update(deltaTime);
        }

        for (ColorButton colorButton : _colorButtons) {
            colorButton.update(deltaTime);
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.clear(Colors.colorValues.get(Colors.ColorName.BACKGROUND));

        _objectiveText.render(graphics);
        _quitButton.render(graphics);
        _colorblindButton.render(graphics);

        _combinationLayouts.get(0).render(graphics);

        final int colorButtonBackgroundHeight = 80;
        graphics.drawRect(0, graphics.getLogicHeight() - colorButtonBackgroundHeight,
                graphics.getLogicWidth(), colorButtonBackgroundHeight, 1f, 0xFFFAFAFA);

        for (ColorButton colorButton : _colorButtons) {
            colorButton.render(graphics);
        }

        _transition.render(graphics);
    }

    @Override
    public void handleEvents(Input.TouchEvent event) {
        if (_gameFinished) {
            return;
        }

        _colorblindButton.handleEvents(event);
        _quitButton.handleEvents(event);

        // Detectar click en colores ya colocados
        // Sirve para borrarlos
        if (_combinationLayouts.get(_gameAttributes.activeLayout).handleEvents(event)) {
            _combinationLayouts.get(_gameAttributes.activeLayout).updateCombination(_gameAttributes.isEyeOpen);
        }

        // Cuando detecta un click en un color, se coloca en el primer hueco posible.
        for (ColorButton colorButton : _colorButtons) {
            if (colorButton.handleEvents(event)) {
                int index = _combinations.get(_gameAttributes.activeLayout).setNextColor(colorButton._colorID);
                _combinationLayouts.get(_gameAttributes.activeLayout).updateCombination(_gameAttributes.isEyeOpen);
                _combinationLayouts.get(_gameAttributes.activeLayout).animateSlot(index);
                break;
            }
        }
    }

    private void updateTriesText() {
        String attemptsString = "Te quedan " + _gameAttributes.attemptsLeft + " intentos.";
        if (_gameAttributes.attemptsLeft == 1) {
            attemptsString = "Este es tu último intento!";
        }
    }
}
