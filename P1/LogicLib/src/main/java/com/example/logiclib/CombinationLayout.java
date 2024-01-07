package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Input;

import java.util.ArrayList;
import java.util.List;

public class CombinationLayout extends GameObject {
    private final Text _combinationNumber;
    private final Combination _currentCombination;
    private final List<ColorSlot> _colors;
    private final List<HintSlot> _hints;
    private final int _lateralMargin;

    public CombinationLayout(Engine engine, int number, int combinationLength, int positionX, int positionY, int height, GameAttributes gameAttributes) {
        super(engine, positionX, positionY, engine.getGraphics().getLogicWidth(), height, 1f);

        _lateralMargin = 5;
        _width -= - 2 * _lateralMargin;

        int padding = 6;

        Font numberFont = _graphics.newFont("Comfortaa-Regular.ttf", 24f * _scale);
        _combinationNumber = new Text(Integer.toString(number + 1), numberFont, engine,
                _lateralMargin + 50, (int) (_positionY + _height / 2), 0, true);

        _colors = new ArrayList<>();
        for (int i = 0; i < combinationLength; i++) {
            _colors.add(new ColorSlot(engine,
                    (int) (_positionX + (i - combinationLength / 2f) * (_height + padding)),
                    (int) (_positionY - _height / 2), (int) (_height * _scale), (int) (_height * _scale), gameAttributes));
        }

        _hints = new ArrayList<>();
        for (int i = 0; i < combinationLength; i++) {
            _hints.add(new HintSlot(engine, (int) (_graphics.getLogicWidth() - 50 - _lateralMargin + (i % ((combinationLength + 1) / 2)
                            - combinationLength / 4f) * _height / 2 + i % ((combinationLength + 1) / 2) * _lateralMargin / 2),
                    (int) (i < combinationLength / 2f ? _positionY - _height / 2 : _positionY) + (int) (_height * .2f / 4),
                    (int) (_height * .8f * _scale / 2), (int) (_height * .8f * _scale / 2)));
        }

        _currentCombination = new Combination(combinationLength);
    }

    @Override
    public void render() {
        _graphics.drawRoundedRect(_lateralMargin, _positionY - _height * 1.2f / 2,
                _graphics.getLogicWidth() - _lateralMargin * 2, _height * 1.2f,
                 20, 20 , _scale, Colors.colorValues.get(Colors.ColorName.COMBINATIONLAYOUT));

        _graphics.drawRect(_lateralMargin + 80, _positionY - _height / 2, 2, _height, _scale, 0);
        _graphics.drawRect(_graphics.getLogicWidth() - _lateralMargin - 90, _positionY - _height / 2,
                2, _height, _scale, 0);

        _combinationNumber.render();

        for (ColorSlot color : _colors) {
            color.render();
        }

        for (HintSlot hint : _hints) {
            hint.render();
        }
    }

    @Override
    public void update() {
        for (ColorSlot color : _colors) {
            color.update();
        }
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        for (int i = 0; i < _colors.size(); i++) {
            ColorSlot color = _colors.get(i);

            if (color.handleEvents(e)) {
                _currentCombination.deleteColor(i);
                color.deleteColor();
                return true;
            }
        }
        return false;
    }

    public void setNextColor(int colorID, boolean isEyeOpen) {
        // Coloca la imagen en el primer hueco del array
        for (int i = 0; i < _colors.size(); i++) {
            if (!_colors.get(i).hasColor()) {
                // Image
                _colors.get(i).setColor(colorID, isEyeOpen);

                // Combination
                _currentCombination.setNextColor(colorID);
                break;
            }
        }
    }

    // Devuelve true si el array de colores está lleno (Cuando el jugador completa una combinación)
    public boolean isFull() {
        for (int i = 0; i < _colors.size(); i++) {
            if (!_colors.get(i).hasColor()) {
                return false;
            }
        }
        return true;
    }

    public Combination getCurrentCombination() {
        return _currentCombination;
    }

    // Usando el array de las pistas, coloca la respectiva imagen, ya sea la pista negra o blanca
    public void setHints(Combination.HintEnum[] predictionHints) {
        for (int i = 0; i < predictionHints.length; i++) {
            if (predictionHints[i] == Combination.HintEnum.BLACK) {
                _hints.get(i).setColor(Colors.ColorName.BLACK);
            } else if (predictionHints[i] == Combination.HintEnum.WHITE) {
                _hints.get(i).setColor(Colors.ColorName.WHITE);
            }
        }
    }
}
