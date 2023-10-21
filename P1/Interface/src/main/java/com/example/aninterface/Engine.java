package com.example.aninterface;

public interface Engine {

    Graphics getGraphics();
    void setCurrentScene(State currentScene);
    State getScene();

    Input getInput();

    void resume();
    void pause();
}
