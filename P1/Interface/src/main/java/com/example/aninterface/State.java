package com.example.aninterface;

public interface State {
    void update(double deltaTime);
    void render(Graphics graphics);
    void handleEvents(Input input);
}
