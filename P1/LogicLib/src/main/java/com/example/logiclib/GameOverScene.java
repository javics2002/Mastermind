package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameOverScene implements Scene {
    private final Text _resultText, _attemptsText, _attemptsNumberText, _codeText;
    private final Button _playAgainButton;
    private final Button _chooseDifficultyButton;
    private final List<ColorSlot> _resultCombination;
    private Transition _transition;

    Engine _engine;
    GameAttributes _gameAttributes;

    public GameOverScene(Engine engine, GameAttributes gameAttributes) {
        _engine = engine;
        Graphics graphics = _engine.getGraphics();

        // Init GameAttributes
        _gameAttributes = gameAttributes;

        // Transition
        _transition = new Transition(_engine, graphics.getWidth(), graphics.getHeight());

        //Create scene
        Font resultFont = graphics.newFont("Comfortaa-Regular.ttf", 40f),
                attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 18f),
                attemptsNumberFont = graphics.newFont("Comfortaa-Regular.ttf", 24f),
                codeFont = graphics.newFont("Comfortaa-Regular.ttf", 18f);
        String resultString, attemptsString, attemptsNumberString, codeString = "Código:";

        if (_gameAttributes.attemptsLeft != 0) {
            //Has ganado
            resultString = "ENHORABUENA!!";
            attemptsString = "Has averiguado el código en";
            attemptsNumberString = Integer.toString(_gameAttributes.activeLayout + 1) + " intentos";
        } else {
            //Has perdido
            resultString = "GAME OVER";
            attemptsString = "Te has quedado sin intentos";
            attemptsNumberString = "";
        }

        _resultText = new Text(resultString, resultFont, _engine,
                graphics.getLogicWidth() / 2, 130, 0, true);
        _attemptsText = new Text(attemptsString, attemptsFont, _engine,
                graphics.getLogicWidth() / 2, 170, 0, true);
        _attemptsNumberText = new Text(attemptsNumberString, attemptsNumberFont, _engine,
                graphics.getLogicWidth() / 2, 220, 0, true);
        _codeText = new Text(codeString, codeFont, _engine,
                graphics.getLogicWidth() / 2, 260, 0, true);

        int scale = 40;
        int padding = 6;
        _resultCombination = new ArrayList<>();
        for (int i = 0; i < _gameAttributes.combinationLength; i++) {
            ColorSlot cSlotX = new ColorSlot(_engine,
                    (int) ((graphics.getLogicWidth() / 2) + (i - _gameAttributes.combinationLength / 2f) * (scale + padding)),
                    280, scale, scale, _gameAttributes);
            _resultCombination.add(cSlotX);
            cSlotX.setColor(_gameAttributes.resultCombination.getColors()[i] , _gameAttributes.isEyeOpen);
        }

        Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);
        _playAgainButton = new Button(Colors.ColorName.BACKGROUNDORANGE, "Volver a jugar", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - 400 / 2, 450, 400, 50){
            @Override
            public void callback() {
                Scene scene = new GameScene(_engine, _gameAttributes.attemptsNumber,  _gameAttributes.combinationLength,
                        _gameAttributes.colorNumber, _gameAttributes.repeatedColors);
                _transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
            }
        };
        _chooseDifficultyButton = new Button(Colors.ColorName.BACKGROUNDORANGE, "Elegir dificultad", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - 400 / 2, 550, 400, 50) {
            @Override
            public void callback() {
                Scene scene = new DifficultyScene(_engine);
                _transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
            }
        };

        _transition.PlayTransition(Transition.TransitionType.fadeIn, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, null);
    }

    @Override
    public void update(double deltaTime) {
        _transition.update(deltaTime);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.clear(Colors.colorValues.get(Colors.ColorName.BACKGROUND));

        _resultText.render(graphics);
        _attemptsText.render(graphics);
        _attemptsNumberText.render(graphics);
        _codeText.render(graphics);

        for (ColorSlot colorSlot : _resultCombination) {
            colorSlot.render(graphics);
        }

        _playAgainButton.render(graphics);
        _chooseDifficultyButton.render(graphics);
        _transition.render(graphics);
    }

    @Override
    public void handleEvents(Input.TouchEvent event) {
        _playAgainButton.handleEvents(event);
        _chooseDifficultyButton.handleEvents(event);
    }

}
