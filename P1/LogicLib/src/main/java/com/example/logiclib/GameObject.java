package com.example.logiclib;
import com.example.aninterface.Input;

public interface GameObject {
    boolean handleEvents(Input.TouchEvent e);
    void update();
    void render();
}