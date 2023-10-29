package com.example.libenginepc;

import com.example.aninterface.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class InputPC implements Input {
   private final InputHandlerMouse _handler;
    InputPC(){
        _handler = new InputHandlerMouse();
    }
    public InputHandlerMouse getHandlerInput()
    {
        return _handler;
    }
    @Override
    public List<TouchEvent> getTouchEvent() {
        return _handler.getMouseEvent();
    }

    @Override
    public void addTouchEvent() {
        _handler.addMouseEvent();
    }

    @Override
    public void clearEvents() {
        _handler.clearMouseEvents();
    }
}
