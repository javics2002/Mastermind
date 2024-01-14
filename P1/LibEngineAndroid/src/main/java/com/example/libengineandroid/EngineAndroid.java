package com.example.libengineandroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.SurfaceView;
import android.view.View;

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

// Esta clase representa un motor de juego para Android que implementa la interfaz Runnable y la interfaz Engine.
public class EngineAndroid implements Runnable, Engine {
	// Atributos
	private final SurfaceView _surfaceView; // Superficie de renderización
	private Thread _renderThread; // Hilo de renderizado
	private volatile boolean _running; // Indica si el motor está en ejecución
	private Scene _currentScene; // Escena actual
	private final InputAndroid _input; // Manejador de entrada
	private final GraphicsAndroid _graphics; // Motor de renderizado
	private final AudioAndroid _audio;

	private FileInputStream _fileInputStream;
	private FileOutputStream _fileOutputStream;
	private Activity activity;

	// Constructor
	public EngineAndroid(SurfaceView myView, float aspectRatio, int logicHeight, Activity mainActivity) {

		activity=mainActivity;

		_surfaceView = myView;
		_input = new InputAndroid();
		// Administrador de activos del juego
		AssetManager _assetManager = myView.getContext().getAssets(); // Obtiene el administrador de activos del contexto de la vista
		myView.setOnTouchListener((View.OnTouchListener) _input.getTouchHandler()); // Configura el manejador de eventos táctiles
		_graphics = new GraphicsAndroid(_surfaceView, _assetManager, (int) (logicHeight * aspectRatio), logicHeight);
		_audio = new AudioAndroid(myView.getContext());

		_fileInputStream = null;
		_fileOutputStream = null;
	}

	@Override
	public void run() {
		// Espera a que el motor esté en ejecución y el ancho de la superficie sea válido
		while (_running && _surfaceView.getWidth() == 0) ;

		long lastFrameTime = System.nanoTime();

		// Bucle principal del motor
		while (_running) {
			long currTime = System.nanoTime();
			long nanoElTime = currTime - lastFrameTime;
			lastFrameTime = currTime;

			// Procesa eventos de entrada
			handleEvents();

			// Actualiza la lógica del juego
			double elapsedTime = (double) nanoElTime / 1.0E9;
			update(elapsedTime);

			// Renderiza el frame cuando los gráficos estén listos
			while (!_graphics.isValid()) ;
			_graphics.lockCanvas();
			_graphics.prepareFrame();
			render();
			_graphics.unlockCanvas();
		}
	}

	// Método para renderizar la escena actual
	protected void render() {
		_currentScene.render(_graphics);
	}

	// Método para pausar la ejecución del motor
	public void pause() {
		if (_running) {
			_running = false;
			while (true) {
				try {
					// Detiene el hilo de renderizado
					_renderThread.join();
					_renderThread = null;
					break;
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	public void resume() {
		// Si no se estaba ejecutando, inicia la ejecución del motor en un nuevo hilo de renderizado
		if (!_running) {
			_running = true;
			_renderThread = new Thread(this);
			_renderThread.start();
		}
	}

	// Maneja eventos de entrada
	protected void handleEvents() {
		if (_input.getTouchEvents().size() > 0) {
			for (Input.TouchEvent event : _input.getTouchEvents()) {
				_currentScene.handleEvents(event);
			}

			_input.clearEvents();
		}
	}

	// Actualiza la lógica del juego
	protected void update(double deltaTime) {
		_currentScene.update(deltaTime);
	}

	@Override
	public Input getInput() {
		return _input;
	}

	@Override
	public void setCurrentScene(Scene currentScene) {
		_currentScene = currentScene;
	}

	@Override
	public Scene getScene() {
		return _currentScene;
	}

	@Override
	public Graphics getGraphics() {
		return _graphics;
	}

	@Override
	public Audio getAudio() {
		return _audio;
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
			_fileInputStream = activity.openFileInput(fileName);
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
			_fileOutputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
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