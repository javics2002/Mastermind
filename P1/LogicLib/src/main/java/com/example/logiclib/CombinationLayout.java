package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

import java.util.ArrayList;
import java.util.List;

public class CombinationLayout implements Interface {
    private Text _combinationNumber;
    private Combination _currentCombination;
    private List<ColorSlot> _colors;
    private List<HintSlot> _hints;
    private Engine _engine;
    private Graphics _graphics;
    private int _positionX;
    private int _positionY;
    private int scale = 30;
    private int padding = 5;

    public CombinationLayout(Engine engine, int number, int combinationLength, int positionX, int positionY) {
        _engine = engine;
        _graphics = _engine.getGraphics();

        _positionX = positionX;
        _positionY = positionY;

        Font numberFont = _graphics.newFont("Comfortaa-Regular.ttf", 20f);
        _combinationNumber = new Text(Integer.toString(number + 1), numberFont, _engine, 50, _positionY + 25, 0);

        _colors = new ArrayList<>();
        for(int i = 0; i < combinationLength; i++){
            _colors.add(new ColorSlot(_engine, "colorEmpty.png",
                    (int) (positionX + (i - combinationLength / 2f) * scale + padding * i), _positionY + padding / 2, scale, scale));
        }

        _hints = new ArrayList<>();
        for(int i = 0; i < combinationLength; i++){
            _hints.add(new HintSlot(_engine, "colorEmpty.png",
                    (int) (_graphics.getWidthLogic() - 70 + (i - combinationLength / 2f) * scale / 2 + i * padding / 2), _positionY + padding / 2, scale / 2, scale / 2));
        }

        _currentCombination = new Combination(combinationLength);
    }

    @Override
    public void render() {
        _graphics.setColor(0xFFf8f4ed);

        //_graphics.fillRect(_graphics.logicToRealX(padding), _graphics.logicToRealY(_positionY - scale / 2),
        //        _graphics.scaleToReal(_graphics.getWidthLogic() - padding * 2), _graphics.scaleToReal((int) (scale * 1.2f)));

        _combinationNumber.render();

        for(ColorSlot color : _colors){
            color.render();
        }

        for(HintSlot hint : _hints){
            hint.render();
        }
    }

    @Override
    public void update() {
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }
    public List<ColorSlot> getColors() {
        return _colors;
    }

    public void setNextColor(int colorID, boolean isEyeOpen){
        // Obtain next color
        for (int i = 0; i < _colors.size(); i++){
            if (!_colors.get(i).hasColor()) {
                // Image
                _colors.get(i).setColor(colorID, isEyeOpen);

                // Combination
                _currentCombination.setNextColor(colorID);
                break;
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < _colors.size(); i++){
            if (!_colors.get(i).hasColor()){
                return false;
            }
        }
        return true;
    }

    public Combination getCurrentCombination() {
        return _currentCombination;
    }

    public void setHints(Combination.HintEnum[] predictionHints) {
        for (int i = 0; i < predictionHints.length; i++){
            if (predictionHints[i] == Combination.HintEnum.BLACK){
                _hints.get(i).setImage("hintBlack.png");
            }
            else if (predictionHints[i] == Combination.HintEnum.WHITE){
                _hints.get(i).setImage("hintWhite.png");
            }
        }
    }
}
