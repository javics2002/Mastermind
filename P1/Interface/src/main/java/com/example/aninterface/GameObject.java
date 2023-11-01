package com.example.aninterface;

public interface GameObject {
    void render();

    void update();

    boolean handleEvents(Input.TouchEvent e);
}
