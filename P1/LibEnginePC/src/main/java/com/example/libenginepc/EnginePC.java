package com.example.libenginepc;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

public class EnginePC implements Runnable, Engine {
	private Thread _renderThread;
	private volatile boolean _running;        // Boolean to know if the app is still running
	private Scene _currentScene;
	private final GraphicsPC _graphics;
	private final InputPC _input;
	private AudioPC _audio;

	private FileInputStream _fileInputStream;
	private FileOutputStream _fileOutputStream;

	public EnginePC(JFrame myView, float aspectRatio, int logicHeight) {
		int _logicWidth = (int) (logicHeight * aspectRatio);
		_graphics = new GraphicsPC(myView, _logicWidth, logicHeight);
		_input = new InputPC();
		_audio = new AudioPC();
		myView.addMouseListener(_input.getHandlerInput());
		_renderThread = null;

		_fileInputStream = null;
		_fileOutputStream = null;
	}

	protected void handleEvents() {
		if (_input.getMouseEvents().size() > 0) {

			for (Input.TouchEvent event : _input.getMouseEvents()) {
				_currentScene.handleEvents(event);
			}

			_input.clearEvents();
		}
	}

	protected void update(double deltaTime) {
		_currentScene.update(deltaTime);
	}

	protected void render() {
		_graphics.clear(0xe7d6bd);
		_currentScene.render(_graphics);
	}

	@Override
	public void run() {
		if (_renderThread != Thread.currentThread())
			throw new RuntimeException("run() should not be called directly");

		long lastFrameTime = System.nanoTime();

		while (_currentScene != null && _running) {
			long currentTime = System.nanoTime();
			long nanoElapsedTime = currentTime - lastFrameTime;
			lastFrameTime = currentTime;

			// Convertimos el tiempo de nanosegundos a segundos
			double deltaTime = (double) nanoElapsedTime / 1.0E9;

			handleEvents();
			update(deltaTime);

			_graphics.prepareFrame();
			render();
			_graphics.show();
			_graphics.finishFrame();
		}
	}

    /*
     Inicia un proceso en un hilo separado si la aplicación no está en curso.
    */

	public void resume() {
		if (!_running) {
			_running = true;

			if (_renderThread == null) {
				_renderThread = new Thread(this);
				_renderThread.start();
			}
		}
	}

	/*
	  Pausa el hilo de la aplicación. Es obligatorio hacer el join() dentro de un catch.
	*/
	public void pause() {
		if (_running) {
			_running = false;
			_renderThread = null;
		}
	}

	@Override
	public Graphics getGraphics() {
		return _graphics;
	}

	@Override
	public Scene getScene() {
		return _currentScene;
	}

	@Override
	public Input getInput() {
		return _input;
	}

	public Audio getAudio() {
		return _audio;
	}

	@Override
	public void setCurrentScene(Scene currentScene) {
		_currentScene = currentScene;
	}

	/*
	Esta funcion sirve para abrir el fichero de guardado para leer su contenido.
	Devuelve el objeto de tipo ObjectInputStream, que cuenta con funciones para lectura
	del fichero abierto por el objeto "_fileInputStream".
	Esta funcion NO cierra ninguno de los dos objetos anteriormente mencionados.
	Es RESPONSABILIDAD DEL USUARIO cerrar dichos objetos llamando a su funcion "close()".


	** Apunte para los desarrolladores:
	En el caso del objeto "_fileInputStream", close() esta siendo llamado en la funcion
	de la clase EngineAndroid "closeSaveFile().

	En el caso del objeto devuelto "ObjectInputStream", close() está siendo llamado al final
	de la lectura o escritura en GameData.loadGameData o GameData.saveGameData.
	 */
	@Override
	public ObjectInputStream openSaveFileForReading(String fileName) {
		try {
			_fileInputStream = new FileInputStream(fileName);
			ObjectInputStream objectInputStream = new ObjectInputStream(_fileInputStream);
			return objectInputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		_fileInputStream = null;
		return null;
	}

	/*
	Esta funcion sirve para abrir el fichero de guardado para escribir en él.
	Devuelve el objeto de tipo ObjectOutputStream, que cuenta con funciones para escribir
	en el fichero abierto por el objeto "_fileOutputStream".
	Esta funcion NO cierra ninguno de los dos objetos anteriormente mencionados.
	Es RESPONSABILIDAD DEL USUARIO cerrar dichos objetos llamando a su funcion "close()".


	** Apunte para los desarrolladores:
	En el caso del objeto "_fileOutputStream", close() esta siendo llamado en la funcion
	de la clase EngineAndroid "closeSaveFile().

	En el caso del objeto devuelto "ObjectOutputStream", close() está siendo llamado al final
	de la lectura o escritura en GameData.loadGameData o GameData.saveGameData.
	 */
	@Override
	public ObjectOutputStream openSaveFileForWriting(String fileName) {
		try {
			_fileOutputStream = new FileOutputStream(fileName);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(_fileOutputStream);
			return objectOutputStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		_fileOutputStream = null;
		return null;
	}


	/*
	Esta funcion sirve para cerrar los objetos de la clase FileInputStream y FileOutputStream
	abiertos por las funciones de esta misma clase "openSaveFileForWriting" y "openSaveFileForReading"
	debido a que estas funciones NO cierran el fichero.
	Por tanto, es RESPONSABILIDAD DEL USUARIO la de cerrar dichos objetos al terminar de
	leer o escribir en el fichero de guardado.
	 */
	@Override
	public void closeSaveFile() {
		assert(_fileInputStream != null || _fileOutputStream != null);

		try {
			if (_fileInputStream != null) {
				_fileInputStream.close();
			}
			if (_fileOutputStream != null){
				_fileOutputStream.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}