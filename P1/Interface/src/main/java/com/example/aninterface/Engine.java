package com.example.aninterface;

public interface Engine {

    Graphics get_graphics();
    void set_currentScene(Scene _currentScene);
    Scene getScene();

    Input get_input();

    void resume();
    void pause();
}
