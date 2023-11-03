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
    private final DifficultyButton _playAgainButton;
    private final PlayButton _chooseDifficultyButton;
    private final List<ColorSlot> _resultCombination;

    public GameOverScene(Engine engine) {
        Graphics graphics = engine.getGraphics();

        // Init GameAttributes
        GameAttributes gameAttributes = GameAttributes.Instance();

        //Create scene
        Font resultFont = graphics.newFont("Comfortaa-Regular.ttf", 40f),
                attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 18f),
                attemptsNumberFont = graphics.newFont("Comfortaa-Regular.ttf", 24f),
                codeFont = graphics.newFont("Comfortaa-Regular.ttf", 18f);
        String resultString, attemptsString, attemptsNumberString, codeString = "Código:";

        if (gameAttributes.attemptsLeft != 0) {
            //Has ganado
            resultString = "ENHORABUENA!!";
            attemptsString = "Has averiguado el código en";
            attemptsNumberString = Integer.toString(gameAttributes.activeLayout + 1) + " intentos";
        } else {
            //Has perdido
            resultString = "GAME OVER";
            attemptsString = "Te has quedado sin intentos";
            attemptsNumberString = "";
        }

        _resultText = new Text(resultString, resultFont, engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(resultString, resultFont) / 2, 70, 0);
        _attemptsText = new Text(attemptsString, attemptsFont, engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(attemptsString, attemptsFont) / 2, 110, 0);
        _attemptsNumberText = new Text(attemptsNumberString, attemptsNumberFont, engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(attemptsNumberString, attemptsNumberFont) / 2, 160, 0);
        _codeText = new Text(codeString, codeFont, engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(codeString, codeFont) / 2, 200, 0);

        int scale = 40;
        int padding = 6;
        _resultCombination = new ArrayList<>();
        for (int i = 0; i < gameAttributes.combinationLength; i++) {
            ColorSlot cSlotX=  new ColorSlot(engine, "",
                    (int) ((graphics.getLogicWidth() / 2) + (i - gameAttributes.combinationLength / 2f) * (scale + padding)),
                    250, scale, scale);
            _resultCombination.add(cSlotX);
            cSlotX.setColor(gameAttributes.resultCombination.getColors()[i] , gameAttributes.isEyeOpen);
        }

        _playAgainButton = new DifficultyButton("playAgain.png", engine,
                graphics.getLogicWidth() / 2 - 400 / 2, 450, 400, 50,
                gameAttributes.attemptsNumber, gameAttributes.combinationLength,
                gameAttributes.colorNumber, gameAttributes.repeatedColors);
        _chooseDifficultyButton = new PlayButton("chooseDifficulty.png", engine,
                graphics.getLogicWidth() / 2 - 400 / 2, 550, 400, 50);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
        _resultText.render();
        _attemptsText.render();
        _attemptsNumberText.render();
        _codeText.render();

        for (ColorSlot colorSlot : _resultCombination) {
            colorSlot.render();
        }

        _playAgainButton.render();
        _chooseDifficultyButton.render();
    }

    @Override
    public void handleEvents(Input input) {
        if (input.getTouchEvent().size() > 0) {
            _playAgainButton.handleEvents(input.getTouchEvent().get(0));
            _chooseDifficultyButton.handleEvents(input.getTouchEvent().get(0));
        }
    }
}
