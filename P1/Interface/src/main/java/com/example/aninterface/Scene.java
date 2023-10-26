package com.example.aninterface;

public interface Scene {
    void handleEvents(Input input);
    void update(double deltaTime);
    void render(Graphics graphics);
}
