package com.example.libenginepc;

import com.example.aninterface.Input;

import java.util.List;

public class InputPC implements Input {
    private final InputHandlerMouse _handler;

    InputPC() {
        _handler = new InputHandlerMouse();
    }

    public InputHandlerMouse getHandlerInput() {
        return _handler;
    }

    public List<TouchEvent> getMouseEvents() {
        return _handler.getMouseEvents();
    }

    public void clearEvents() {
        _handler.clearMouseEvents();
    }
}
