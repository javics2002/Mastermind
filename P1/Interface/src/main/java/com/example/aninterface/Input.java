package com.example.aninterface;

import java.util.List;

public interface Input {
    enum InputType {PRESSED, RELEASED, MOVE}

    class TouchEvent {
        public int x, y;
        public InputType type;

        public TouchEvent(int x_, int y_, InputType type_) {
            x = x_;
            y = y_;
            type = type_;
        }
    }

    List<TouchEvent> getTouchEvent();


    // TODO: make clear events in interface

    // TODO: CHECK RELEASE IN INPUT
}
