package com.example.libengineandroid;
import android.view.MotionEvent;
import android.view.View;

import com.example.aninterface.Input;

import java.util.ArrayList;

public class TouchHandlerAndroid implements View.OnTouchListener {

    public ArrayList<Input.TouchEvent> eventos; // Lista de eventos táctiles
    MotionEvent event; // Variable para mantener el evento actual
    int i = 0; // Una variable entera que no parece utilizarse

    TouchHandlerAndroid() {
        eventos = new ArrayList<Input.TouchEvent>(); // Inicialización de la lista de eventos
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            event = e; // Almacenar el evento actual
            addTouchEvent(); // Agregar el evento táctil a la lista

        }
        return true; // Indicar que el evento táctil ha sido manejado
    }

    public ArrayList<Input.TouchEvent> getTouchEvent() {
        return eventos; // Obtener la lista de eventos táctiles
    }

    public void clearEvents() {
        eventos.clear(); // Borrar todos los eventos táctiles registrados
    }

    public void addTouchEvent() {
        Input.InputType tipo = Input.InputType.PRESSED; // Tipo de evento (presionado)
        eventos.add(new Input.TouchEvent((int) event.getX(0), (int) event.getY(0), tipo)); // Agregar evento a la lista
    }
}
