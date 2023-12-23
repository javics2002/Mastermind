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
    //private final Button _chooseDifficultyButton;
    private final List<ColorSlot> _resultCombination;

    private final Button _adButton;

    Engine _engine;
    GameAttributes _gameAttributes;



    public GameOverScene(Engine engine, final GameAttributes gameAttributes) {
        _engine = engine;
        Graphics graphics = _engine.getGraphics();

        // Init GameAttributes
        _gameAttributes = gameAttributes;

        //Create scene
        Font resultFont = graphics.newFont("Comfortaa-Regular.ttf", 40f),
                attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 18f),
                attemptsNumberFont = graphics.newFont("Comfortaa-Regular.ttf", 24f),
                codeFont = graphics.newFont("Comfortaa-Regular.ttf", 18f);
        String resultString, attemptsString, attemptsNumberString, codeString = "Código:";


        final int buttonWidth = 430;
        final int buttonHeight = 90;
        final int posY = graphics.getLogicHeight() / 2;
        final int paddingY = 30;
        Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);


        if (_gameAttributes.attemptsLeft != 0) {
            //Has ganado
            resultString = "ENHORABUENA!!";
            attemptsString = "Has averiguado el código en";
            attemptsNumberString = Integer.toString(_gameAttributes.activeLayout + 1) + " intentos";
            _adButton=null;
        } else {
            //Has perdido
            resultString = "GAME OVER";
            attemptsString = "Te has quedado sin intentos";
            attemptsNumberString = "";


            _adButton = new Button(Colors.ColorName.BACKGROUNDORANGE, "Consigue mas Intentos", buttonFont, _engine,
                    graphics.getLogicWidth() / 2 - buttonWidth / 2, posY + 2 * (buttonHeight + paddingY),
                    buttonWidth, buttonHeight) {
                @Override
                public void callback() {
                    _engine.showAd();
                }
            };

        }

        _resultText = new Text(resultString, resultFont, _engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(resultString, resultFont) / 2, 70, 0);
        _attemptsText = new Text(attemptsString, attemptsFont, _engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(attemptsString, attemptsFont) / 2, 110, 0);
        _attemptsNumberText = new Text(attemptsNumberString, attemptsNumberFont, _engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(attemptsNumberString, attemptsNumberFont) / 2, 160, 0);
        _codeText = new Text(codeString, codeFont, _engine,
                graphics.getLogicWidth() / 2 - graphics.getStringWidth(codeString, codeFont) / 2, 200, 0);

        int scale = 40;
        int padding = 6;
        _resultCombination = new ArrayList<>();
        for (int i = 0; i < _gameAttributes.combinationLength; i++) {
            ColorSlot cSlotX = new ColorSlot(_engine,
                    (int) ((graphics.getLogicWidth() / 2) + (i - _gameAttributes.combinationLength / 2f) * (scale + padding)),
                    250, scale, scale, _gameAttributes);
            _resultCombination.add(cSlotX);
            cSlotX.setColor(_gameAttributes.resultCombination.getColors()[i] , _gameAttributes.isEyeOpen);
        }

        //Atributos del juego declarados como final para su uso posterior en callback del boton de Volver a jugar
        _playAgainButton = new Button(Colors.ColorName.BACKGROUNDORANGE, "Volver a jugar", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - 400 / 2, 450, 400, 50){
            @Override
            public void callback() {
                Scene scene = new GameScene(_engine, _gameAttributes.attemptsNumber, _gameAttributes.attemptsNumber, _gameAttributes.combinationLength,
                        _gameAttributes.colorNumber, _gameAttributes.repeatedColors, _gameAttributes.returnScene,
                        _gameAttributes.backGroundSkinId, _gameAttributes.skin, _gameAttributes.selectedWorld,null);
                _engine.setCurrentScene(scene);
            }
        };
        /*_chooseDifficultyButton = new Button(Colors.ColorName.BACKGROUNDORANGE, "Elegir dificultad", buttonFont, _engine,
                graphics.getLogicWidth() / 2 - 400 / 2, 550, 400, 50) {
            @Override
            public void callback() {
                Scene scene = new DifficultyScene(_engine);
                _engine.setCurrentScene(scene);
            }
        };*/


    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics graphics) {
        _resultText.render(graphics);
        _attemptsText.render(graphics);
        _attemptsNumberText.render(graphics);
        _codeText.render(graphics);
        if(_adButton!=null)_adButton.render(graphics);

        for (ColorSlot colorSlot : _resultCombination) {
            colorSlot.render(graphics);
        }

        _playAgainButton.render(graphics);
        //_chooseDifficultyButton.render();
    }

    @Override
    public void handleEvents(Input input) {
        if (input.getTouchEvent().size() > 0) {
            _playAgainButton.handleEvents(input.getTouchEvent().get(0));
            //_chooseDifficultyButton.handleEvents(input.getTouchEvent().get(0));
            if(_adButton!=null)_adButton.handleEvents(input.getTouchEvent().get(0));
        }
    }
    public GameAttributes getGameAttributtes(){
        return _gameAttributes;
    }
    public void setScene(){

    }

}
