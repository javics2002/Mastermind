package com.example.libenginepc;

import com.example.aninterface.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class InputPC implements Input, MouseListener {
    private List<TouchEvent> _events;

    InputPC(){
        _events = new ArrayList<TouchEvent>();
    }

    @Override
    public List<TouchEvent> getTouchEvent() {
        return _events;
    }

    @Override
    public void addTouchEvent() {
    }

    @Override
    public void clearEvents() {
        _events.clear();
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Create an event
        TouchEvent event = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), InputType.PRESSED);
        _events.add(event);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TouchEvent event = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), InputType.PRESSED);
        _events.add(event);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TouchEvent event = new TouchEvent(mouseEvent.getX(), mouseEvent.getY(), InputType.RELEASED);
        _events.add(event);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }
}
