package com.example.libengineandroid;
import android.view.MotionEvent;
import android.view.View;

import com.example.aninterface.Input;

import java.util.ArrayList;


import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;


public class InputAndroid implements Input, View.OnTouchListener {
    public ArrayList<TouchEvent> eventos;
    MotionEvent event;
    InputAndroid() {
        eventos = new ArrayList<Input.TouchEvent>();
    }

    @Override
    public ArrayList<TouchEvent> getTouchEvent() {
        return eventos;
    }       //COGE EL ARRAY DE EVENTOS

    @Override
    public void clearEvents() {
        eventos.clear();
    }


    @Override
    public void addTouchEvent() {

        InputType tipo = null;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            tipo = InputType.PRESSED;
        else if (event.getAction() == MotionEvent.ACTION_UP)
            tipo = InputType.RELEASED;
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
            tipo = InputType.MOVE;
        if (tipo != null)
            eventos.add(new TouchEvent((int) event.getX(0), (int) event.getY(0), tipo));
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) { // AL TOCAR LA PANTALLA ENVIAR EL EVENTO


        event= e;
        addTouchEvent();
        return true;
    }
}