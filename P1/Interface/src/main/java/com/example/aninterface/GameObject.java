package com.example.aninterface;

public interface GameObject {
    boolean handleEvents(Input.TouchEvent input);
    void update(double deltaTime);
    void render(Graphics graphics);
}
