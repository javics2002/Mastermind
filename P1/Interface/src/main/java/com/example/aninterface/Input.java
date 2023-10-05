package com.example.aninterface;


import java.util.List;

public interface Input {
    public enum InputType {}
    class TouchEvent {

    }
    List<TouchEvent> getTouchEvent();
}
