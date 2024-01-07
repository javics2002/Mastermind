package com.example.aninterface;

public interface Sound {
	/**
	 * Reproduce el sonido desde el principio
	 */
	void play();

	/**
	 * Para la reproducción del sonido
	 */
	void stop();

	/**
	 * Cambia el volumen del sonido
	 *
	 * @param newVolume nuevo volumen para el sonido entre 0 y 1,
	 *                  en escala de percepcion de volumen lineal para el oido humano.
	 */
	void setVolume(float newVolume);
}
