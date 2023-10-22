package com.example.aninterface;

public interface Scene {
    void update(double deltaTime);
    void render(Graphics graphics);
    void handleEvents(Input input);
}
