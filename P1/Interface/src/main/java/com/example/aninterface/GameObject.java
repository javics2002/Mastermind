package com.example.aninterface;

public interface GameObject {
    int[] colors = {
            0xFF0000, // Rojo
            0xFFA500, // Naranja
            0xFFFFFF00, // Amarillo
            0xFF00FF00, // Verde claro
            0x00FFFF, // Azul Oscuro
            0x0000FF, // Azul Cyan
            0xFFFF00FF, // Magenta (no azul oscuro)
            0xFFFF69B4, // Rosa más claro
            0xFF4B0082  // Morado más oscuro
    };

    String[] colorNames = {"Rojo", "Naranja", "Amarillo", "Verde", "Azul Cyan", "Azul Oscuro", "Magenta", "Rosa", "Morado"};
    void render();

    void update();

    boolean handleEvents(Input.TouchEvent e);
}
