package com.example.aninterface;
import com.example.aninterface.Input;

public interface Interface {
    //Creacion de la clase de una interfaz basica
    //Queremos que todos los elementos que hereden de esto tengan su propio :
    //Mostrar -> render
    //Update () ->Actualizarse todo el rato
    //HandleEvent -> Por ejemplo un boton como no usamos los de desktop manejamos nosotros el evento

    void render();
    void update();

    // Esta comentado por que de momento no tenemos eventos
    //boolean handleEvent(Input.TouchEvent e);
}
