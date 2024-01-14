package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Input;

public class NButton extends Button {
    private int _timesClicked;
    private final int _numberOfClicks;

    NButton(int numberOfClicks, Engine engine, int positionX, int positionY, int width, int height) {
        super(engine, positionX, positionY, width, height);

        _numberOfClicks = numberOfClicks;
        _timesClicked = 0;
    }

    @Override
    public boolean handleEvents(Input.TouchEvent event) {
        if (event.type == Input.InputType.PRESSED && inBounds(event.x, event.y)) {
            _timesClicked += 1;
            _audio.playSound(_clickSound);

            if(_timesClicked >= _numberOfClicks){
                callback();
                _timesClicked = 0;
            }

            return true;
        }

        _timesClicked = 0;

        return false;
    }
}
