package com.example.aninterface;

public interface State {

    void update(double deltaTime);    // Actualizar elementos de la escena
    void render(Graphics graphics);  // Renderizar elementos de la escena
    //void handleInputs(Input inputs); // Cada elemento de la escena maneja los eventos
}
