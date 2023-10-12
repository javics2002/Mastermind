package com.example.aninterface;

public interface State {
    void update(double deltaTime);
    void render(Graphics graphics);
    //void handleInputs(Input inputs); // Cada elemento de la escena maneja los eventos
}
