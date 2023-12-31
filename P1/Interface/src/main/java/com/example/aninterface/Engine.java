package com.example.aninterface;

public interface Engine {
    Graphics getGraphics();

    void setCurrentScene(Scene _currentScene);

    Scene getScene();

    Input getInput();

    Audio getAudio();
}
