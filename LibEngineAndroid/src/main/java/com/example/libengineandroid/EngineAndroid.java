package com.example.libengineandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import com.google.android.gms.ads.AdView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Esta clase representa un motor de juego para Android que implementa la interfaz Runnable y la interfaz Engine.
public class EngineAndroid implements Runnable, Engine {
	// Atributos
	private final SurfaceView _surfaceView; // Superficie de renderización
	private Thread _renderThread; // Hilo de renderizado
	private boolean _running; // Indica si el motor está en ejecución
	private Scene _currentScene; // Escena actual
	private final InputAndroid _input; // Manejador de entrada
	private final GraphicsAndroid _graphics; // Motor de renderizado
	private final AudioAndroid _audio;
	private Activity activity;
	private AndroidRewardedAd _rewardedAd;
	private final Gson _gson;
	private AdView mAdview;
	private FileInputStream _fileInputStream;
	private FileOutputStream _fileOutputStream;

	// Constructor
	public EngineAndroid(SurfaceView myView, float aspectRatio, int logicHeight,Activity mainActivity,AdView mad) {
		_surfaceView = myView;
		_input = new InputAndroid();
		activity=mainActivity;
		mAdview=mad;
		//Cracion del anuncio recompensado
		createRewardedAd();
		AssetManager _assetManager = myView.getContext().getAssets(); // Obtiene el administrador de activos del contexto de la vista
		myView.setOnTouchListener((View.OnTouchListener) _input.getTouchHandler()); // Configura el manejador de eventos táctiles
		_graphics = new GraphicsAndroid(_surfaceView, _assetManager, (int) (logicHeight * aspectRatio), logicHeight);
		_audio = new AudioAndroid(myView.getContext());
		_gson = new Gson();
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

	@Override
	public void showAd() {
		_rewardedAd.show();
	}

	private void createRewardedAd() {
		OnUserEarnedRewardListener _rewardEarnedCallback = new OnUserEarnedRewardListener() {
			@Override
			public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
				Log.d("Ad", "User has finished Loading Reward");
			}
		};

		_rewardedAd = new AndroidRewardedAd(this, _rewardEarnedCallback);
	}

	@Override
	public void shareScreenshot(int width, int height) {
		// Create bitmap
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// Create thread
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			HandlerThread handlerThread = new HandlerThread("PixelCopier");
			handlerThread.start();
			PixelCopy.request(_surfaceView, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
				@Override
				public void onPixelCopyFinished(int copyResult) {
					if (copyResult == PixelCopy.SUCCESS) {
						// generar Bitmap a partir de otro dadas unas coordenadas y un tamaño
						Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

						File file = saveBitmapToFile(activity, finalBitmap);
						Uri fileUri = FileProvider.getUriForFile(activity, "com.mydomain.fileprovider", file);
						//activity.grantUriPermission(activity.getPackageName(), fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

						Intent intent = new Intent(android.content.Intent.ACTION_SEND);
						intent.putExtra(Intent.EXTRA_STREAM, fileUri);
						intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						intent.putExtra(Intent.EXTRA_TEXT, "¡He completado el nivel!");
						intent.setType("image/png");

						activity.startActivity(Intent.createChooser(intent, "Share"));
					}
					handlerThread.quitSafely();
				}
			}, new Handler(handlerThread.getLooper()));
		}
	}

	private File saveBitmapToFile(Context context, Bitmap bitmap) {
		// Crear un archivo en el directorio de almacenamiento externo
		File file = null;

		try {
			File imagesFolder = new File(context.getCacheDir(), "images");
			imagesFolder.mkdirs();
			file = new File(imagesFolder, "shared_image.png");

			FileOutputStream stream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
			stream.flush();
			stream.close();

		} catch (IOException e) {
			System.out.println("IOException while trying to write file for sharing: " + e.getMessage());
		}

		return file;
	}

	@Override
	public <T> T jsonToObject(String fileName, Class<T> classOfT) {
		AssetManager assetManager = _surfaceView.getContext().getAssets();

		try {
			// Abre un InputStream para el archivo JSON
			InputStream inputStream = assetManager.open(fileName);

			// Convierte el InputStream a una cadena JSON
			String jsonString = convertInputStreamToString(inputStream);

			// Parsea la cadena JSON a un objeto Java usando Gson

			T jsonObject = _gson.fromJson(jsonString, classOfT);

			// Cierra el InputStream cuando hayas terminado
			inputStream.close();

			return jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String objectToJson(Object object) {
		return _gson.toJson(object);
	}

	public static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line).append("\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public int filesInFolder(String folderPath) {
		try {
			AssetManager assetManager = _surfaceView.getContext().getAssets();
			String[] files = assetManager.list(folderPath);

			// Comprueba si la lista de archivos es nula o vacía
			if (files == null || files.length == 0)
				return 0;

			// Cuenta el número de archivos con la extensión específica
			int fileCount = 0;
			for (String file : files)
				fileCount++;

			return fileCount;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public String[] getFileNames(String folderPath) {
		try {
			AssetManager assetManager = _surfaceView.getContext().getAssets();
			String[] files = assetManager.list(folderPath);

			return files;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Input getInput() {
		return _input;
	}

	// Actualiza la lógica del juego
	protected void update(double deltaTime) {
		_currentScene.update(deltaTime);
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
	public Audio getAudio() {
		return _audio;
	}

	public Activity getActivity() {
		return activity;
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

	@Override
	public native String hashJson(String fileContent);

	@Override
	public Graphics getGraphics() {
		return _graphics;
	}
	@Override
	public void appeareanceBanner(final boolean hasTo) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (hasTo) {
					mAdview.setVisibility(View.VISIBLE);
				} else {
					mAdview.setVisibility(View.GONE);
				}
			}
		});
	}

}