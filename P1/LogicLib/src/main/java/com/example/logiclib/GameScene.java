package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements Scene {
    private final Engine _engine;
    private final Graphics _graphics;
    private final GameAttributes _gameAttributes;
    private final Text _objectiveText, _attemptsText;
    private final Button _quitButton, _colorblindButton;
    private final List<CombinationLayout> _combinationLayouts;
    private final List<ColorButton> _colorButtons;
    private final Image _backgroundImage;

    private final int _visibleLayouts = 10;

    public GameScene(Engine engine, int tryNumber, int attemptsLeft, int combinationLength, int numberOfColors,
                     boolean repeatedColors, final Scene returnScene, int backgroundSkinId, int iconSkin, int worldId, Combination cResult) {
        _engine = engine;
        _graphics = _engine.getGraphics();

        // Init GameAttributes
        _gameAttributes = new GameAttributes();

        _gameAttributes.attemptsNumber = tryNumber;
        _gameAttributes.attemptsLeft = attemptsLeft;
        _gameAttributes.combinationLength = combinationLength;
        _gameAttributes.colorNumber = numberOfColors;
        _gameAttributes.repeatedColors = repeatedColors;
        _gameAttributes.isEyeOpen = false;

        if(cResult==null)_gameAttributes.resultCombination = new Combination(combinationLength, numberOfColors, repeatedColors);
        else _gameAttributes.resultCombination=cResult;

        _gameAttributes.returnScene = returnScene;
        _gameAttributes.skin = iconSkin;
        _gameAttributes.backGroundSkinId = backgroundSkinId;
        _gameAttributes.selectedWorld = worldId;


        // Save data attributes
        if (GameData.Instance().getCurrentLevelData() == null){
            _gameAttributes.activeLayout = 0;

            LevelData data = new LevelData();
            data.attempts = tryNumber;
            data.codeSize = combinationLength;
            data.codeOpt = numberOfColors;
            data.repeat = repeatedColors;

            data.resultCombination = _gameAttributes.resultCombination;
            data.leftAttemptsNumber = _gameAttributes.attemptsLeft;

            data.combinations = new ArrayList<>();
            GameData.Instance().setCurrentLevelData(data);
        }
        else{
            _gameAttributes.activeLayout = _gameAttributes.attemptsNumber - _gameAttributes.attemptsLeft;
        }

        // Title
        int verticalMargin = 5;
        Font objetiveFont = _graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String objectiveString = "Averigua el código";
        _objectiveText = new Text(objectiveString, objetiveFont, _engine,
                (_graphics.getLogicWidth() / 2) - (_graphics.getStringWidth(objectiveString, objetiveFont) / 2),
                verticalMargin + _graphics.getStringHeight(objectiveString, objetiveFont), 0);

        // Attempts
        String attemptsString = "Te quedan " + _gameAttributes.attemptsLeft + " intentos.";
        Font attemptsFont = _graphics.newFont("Comfortaa-Regular.ttf", 16f);
        _attemptsText = new Text(attemptsString, attemptsFont, _engine,
                (_graphics.getLogicWidth() / 2) - (_graphics.getStringWidth(attemptsString, attemptsFont) / 2),
                verticalMargin + _graphics.getStringHeight(objectiveString, objetiveFont)
                        + _graphics.getStringHeight(attemptsString, attemptsFont) + 5,
                0);

        // Quit button
        int buttonDimension = 50;
        int horizontalMargin = 5;
        _quitButton = new Button("UI/close.png", _engine,
                horizontalMargin, verticalMargin, buttonDimension, buttonDimension) {
            @Override
            public void callback() {
                GameData.Instance().resetCurrentLevelData();
                _engine.setCurrentScene(returnScene);
            }
        };

        // ColorBlind button
        _colorblindButton = new Button("UI/eyeClosed.png", _engine,
                _graphics.getLogicWidth() - buttonDimension - horizontalMargin, verticalMargin,
                buttonDimension, buttonDimension) {
            @Override
            public void callback() {
                _gameAttributes.isEyeOpen = !_gameAttributes.isEyeOpen;
                _colorblindButton.setImage(_gameAttributes.isEyeOpen ? "UI/eyeOpened.png" : "UI/eyeClosed.png");
            }
        };

        // Combination
        int initialHeight = 100;
        int verticalPadding = 15, scale = 40;
        _combinationLayouts = new ArrayList<>();

        int cont = 0;

        for (int i = 0; i < GameData.Instance().getCurrentLevelData().combinations.size(); i++) {
            CombinationLayout newCombinationLayout = new CombinationLayout(_engine, i, combinationLength,
                    _graphics.getLogicWidth() / 2, initialHeight + (verticalPadding + scale) * i,
                    scale, GameData.Instance().getCurrentLevelData().combinations.get(i).getColors(), _gameAttributes);


            // Update hints
            Combination.HintEnum[] hints = newCombinationLayout.getCurrentCombination().getHint(_gameAttributes.resultCombination);
            newCombinationLayout.setHints(hints);

            _combinationLayouts.add(newCombinationLayout);
            cont++;
        }

        if (GameData.Instance().getCurrentLevelData().currentCombination != null){
            _combinationLayouts.add(new CombinationLayout(_engine, cont, combinationLength,
                    _graphics.getLogicWidth() / 2, initialHeight + (verticalPadding + scale) * cont,
                    scale, GameData.Instance().getCurrentLevelData().currentCombination.getColors(), _gameAttributes));
            cont++;
        }

        for (int i = cont; i < tryNumber; i++) {
            _combinationLayouts.add(new CombinationLayout(_engine, i, combinationLength,
                    _graphics.getLogicWidth() / 2, initialHeight + (verticalPadding + scale) * i,
                    scale, _gameAttributes));
        }

        // Color buttons
        int horizontalPadding = 6;
        _colorButtons = new ArrayList<>();
        for (int i = 0; i < numberOfColors; i++) {
            _colorButtons.add(new ColorButton(_engine,
                    (int) (_graphics.getLogicWidth() / 2 + (i - numberOfColors / 2f) * (scale + horizontalPadding)),
                    _graphics.getLogicHeight() - 60, scale, scale, i + 1, _gameAttributes));
        }

        _backgroundImage =
                _graphics.loadImage(GameData.Instance().getWorldDataByIndex(worldId).getWorldName() + "/background.png");
    }

    @Override
    public void update(double deltaTime) {
        updateTriesText();

        CombinationLayout activeLayout = _combinationLayouts.get(_gameAttributes.activeLayout);
        GameData.Instance().getCurrentLevelData().currentCombination = activeLayout.getCurrentCombination();
        GameData.Instance().getCurrentLevelData().leftAttemptsNumber = _gameAttributes.attemptsLeft;

        if (activeLayout.isFull()) {
            if (activeLayout.getCurrentCombination().equals(_gameAttributes.resultCombination)) {
                // USER WON
                Scene gameOverScene = new GameOverScene(_engine, _gameAttributes);
                _engine.setCurrentScene(gameOverScene);
                GameData.Instance().resetCurrentLevelData();
                return;
            }

            // Hints
            Combination.HintEnum[] hints = activeLayout.getCurrentCombination().getHint(_gameAttributes.resultCombination);
            activeLayout.setHints(hints);

            _gameAttributes.attemptsLeft--;
            _gameAttributes.activeLayout++;

            GameData.Instance().getCurrentLevelData().combinations.add(activeLayout.getCurrentCombination());

            if (_gameAttributes.attemptsLeft == 0) {
                // User LOST
                Scene gameOverScene = new GameOverScene(_engine, _gameAttributes);
                _engine.setCurrentScene(gameOverScene);
                GameData.Instance().resetCurrentLevelData();
            }
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
        if(_backgroundImage != null)
            _graphics.drawImage(_backgroundImage, 0, 60,
                    _graphics.getLogicWidth(), _graphics.getLogicHeight() - 60);

        _objectiveText.render(graphics);
        _attemptsText.render(graphics);
        _quitButton.render(graphics);
        _colorblindButton.render(graphics);

        int firstCombination = _gameAttributes.activeLayout - _visibleLayouts + 1;
        if (firstCombination < 0)
            firstCombination = 0;

        for (int i = firstCombination; i < _combinationLayouts.size() && i < firstCombination + _visibleLayouts; i++){
            _combinationLayouts.get(i).setPositionY(100 + (15 + 40) * (i - firstCombination));
            _combinationLayouts.get(i).render(graphics);
        }

        final int colorButtonBackgroundHeight = 80;
        graphics.drawRect(0, graphics.getLogicHeight() - colorButtonBackgroundHeight,
                graphics.getLogicWidth(), colorButtonBackgroundHeight, Colors.colorValues.get(Colors.ColorName.TRASPARENTBACKGROUND));

        for (ColorButton colorButton : _colorButtons)
            colorButton.render(graphics);
    }

    @Override
    public void handleEvents(Input input) {
        if (input.getTouchEvent().size() > 0) {
            Input.TouchEvent touchEvent = input.getTouchEvent().get(0);
            _colorblindButton.handleEvents(touchEvent);
            _quitButton.handleEvents(touchEvent);

            // Detectar click en colores ya colocados
            // Sirve para borrarlos
            _combinationLayouts.get(_gameAttributes.activeLayout).handleEvents(touchEvent);

            // Cuando detecta un click en un color, se coloca en el primer hueco posible.
            for (ColorButton colorButton : _colorButtons) {
                if (colorButton.handleEvents(touchEvent)) {
                    _combinationLayouts.get(_gameAttributes.activeLayout).setNextColor(colorButton._colorID,
                            _gameAttributes.isEyeOpen);
                    break;
                }
            }
        }

    }

    private void updateTriesText() {
        String attemptsString = "Te quedan " + _gameAttributes.attemptsLeft + " intentos.";
        if (_gameAttributes.attemptsLeft == 1) {
            attemptsString = "Este es tu último intento!";
        }

        _attemptsText.setText(attemptsString);
    }
}
