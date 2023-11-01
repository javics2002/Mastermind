package com.example.aninterface;

public interface Audio {

        Sound newSound(String file, boolean loop); // Crear nuevo sonido
        void playSound(String id);                  // Reproducir un sonido
        boolean isLoaded(String id);                // Saber si un sonido ya está cargado
        public void increaseVolume(float increaseAmount,String id);

        // Método para disminuir el volumen en una cantidad específica (en decibeles)
        public void decreaseVolume(float decreaseAmount,String id);

}
