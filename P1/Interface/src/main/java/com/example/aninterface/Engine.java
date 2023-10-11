package com.example.aninterface;

public interface Engine {
    Graphics getGraphics();
    void setCurrentScene(State currentScene); // Establecer una escena
    State getState();       // Obtener la escena actual
}
