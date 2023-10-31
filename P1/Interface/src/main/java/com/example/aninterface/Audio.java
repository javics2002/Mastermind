package com.example.aninterface;

public interface Audio {

        Sound newSound(String file, boolean loop); // Crear nuevo sonido
        void playSound(String id);                  // Reproducir un sonido
        boolean isLoaded(String id);                // Saber si un sonido ya está cargado

}
