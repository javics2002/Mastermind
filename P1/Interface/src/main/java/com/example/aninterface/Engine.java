package com.example.aninterface;

public interface Engine {
    Graphics getGraphics();
    void setCurrentScene(State currentScene);
    State getScene();
}
