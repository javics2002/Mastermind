package com.example.aninterface;
import java.util.List;

public interface Input {
    public static enum InputType { PRESSED, RELEASED, MOVE }
    public static class TouchEvent {
        public int x;
        public int y;
        public InputType type;
        public TouchEvent(int x, int y, InputType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
    List<TouchEvent> getTouchEvent();
    public void addTouchEvent();
    public void clearEvents();
}
