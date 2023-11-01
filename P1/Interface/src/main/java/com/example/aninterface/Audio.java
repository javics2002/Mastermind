package com.example.aninterface;

public interface Audio {
    /**
     * Carga un sonido y lo devuelve. Si ya esta cargado, lo devuelve sin cargarlo.
     * Esta funcion puede lanzar excepciones si no se encuentra el archivo.
     *
     * @param file nombre del archivo de sonido que queremos cargar.
     * @param loop si el sonido se va a repetir en bucle.
     * @return el sonido de nombre file.
     */
    Sound loadSound(String file, boolean loop);

    /**
     * Devuelve el sonido de nombre file cargado anteriormente.
     * Si no esta cargado, devuelve null.
     *
     * @param file nombre del archivo de sonido que queremos obtener.
     * @return el sonido de nombre file.
     */
    Sound getSound(String file);

    /**
     * @param file nombre del archivo de sonido que queremos obtener.
     * @return si el sonido de nombre file esta cargado en memoria o no.
     */
    boolean isLoaded(String file);
}
