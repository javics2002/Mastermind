package com.example.aninterface;


import java.util.List;

public interface Input {
    public static enum InputType {

        PRESSED,
        RELEASED,
        MOVE

    }
    public static class TouchEvent {
        public int x;               // Pos x
        public int y;               // Pos y
        public InputType type; // Tipo de evento


        public TouchEvent(int x, int y, int index, InputType type){
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
    List<TouchEvent> getTouchEvent(); //Obtener la lista de eventos
    public void clearEvents();          // Limpiar la lista de eventos
}
