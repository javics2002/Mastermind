package com.example.libengineandroid;
import android.view.MotionEvent;
import android.view.View;

import com.example.aninterface.Input;

import java.util.ArrayList;


import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;


public class InputAndroid implements Input{

    private TouchHandlerAndroid _handler;
    InputAndroid() {
        _handler = new TouchHandlerAndroid();
    }

    @Override
    public ArrayList<TouchEvent> getTouchEvent() {
        return _handler.getTouchEvent();
    }

    @Override
    public void clearEvents() {
        _handler.clearEvents();
    }


    @Override
    public void addTouchEvent() {

        _handler.addTouchEvent();
    }
    public TouchHandlerAndroid getTouchHandler(){return _handler;};

}