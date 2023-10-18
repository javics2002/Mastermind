package com.example.libenginepc;

import com.example.aninterface.Input;

import java.awt.event.MouseListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
public class InputHandlerMouse implements   MouseListener {

    private List<Input.TouchEvent> _eventsMouse;

    InputHandlerMouse(){
        _eventsMouse = new ArrayList<Input.TouchEvent>();
    }


    public List<Input.TouchEvent> getMouseEvent() {
        return _eventsMouse;
    }


    public void addMouseEvent() {
    }


    public void clearMouseEvents() {
        _eventsMouse.clear();
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Create an event
        Input.TouchEvent event = new Input.TouchEvent(mouseEvent.getX(), mouseEvent.getY(), Input.InputType.PRESSED);
        _eventsMouse.add(event);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Input.TouchEvent event = new Input.TouchEvent(mouseEvent.getX(), mouseEvent.getY(), Input.InputType.PRESSED);
        _eventsMouse.add(event);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Input.TouchEvent event = new Input.TouchEvent(mouseEvent.getX(), mouseEvent.getY(), Input.InputType.RELEASED);
        _eventsMouse.add(event);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }


}
