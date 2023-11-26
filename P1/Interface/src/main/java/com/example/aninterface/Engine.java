package com.example.aninterface;

public interface Engine {
    Graphics getGraphics();

    void setCurrentScene(Scene _currentScene);

    Scene getScene();

    Input getInput();

    void resume();

    void pause();

    Audio getAudio();

    void showAd();
}
