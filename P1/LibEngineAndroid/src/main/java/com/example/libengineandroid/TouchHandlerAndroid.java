package com.example.libengineandroid;
import android.view.MotionEvent;
import android.view.View;

import com.example.aninterface.Input;

import java.util.ArrayList;

public class TouchHandlerAndroid implements View.OnTouchListener {

    public ArrayList<Input.TouchEvent> eventos;
    MotionEvent event;
    boolean touchRegistered = false;
    int i=0;

    TouchHandlerAndroid() {
        eventos = new ArrayList<Input.TouchEvent>();
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {



            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                event = e;
                addTouchEvent();
                touchRegistered = true; // Marcar que se ha registrado un toque
            }

        return true;
    }

    public ArrayList<Input.TouchEvent> getTouchEvent() {
        return eventos;
    }

    public void clearEvents() {
        eventos.clear();
    }

    public void addTouchEvent() {
        Input.InputType tipo = Input.InputType.PRESSED;
        eventos.add(new Input.TouchEvent((int) event.getX(0), (int) event.getY(0), tipo));
    }

}
