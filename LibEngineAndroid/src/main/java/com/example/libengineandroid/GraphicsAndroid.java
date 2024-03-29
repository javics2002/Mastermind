package com.example.libengineandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.google.android.gms.ads.AdView;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class GraphicsAndroid implements Graphics {

	private float _scaleFactor;
	private final int _logicWidth, _logicHeight;

	// Medidas de bordes
	private int _borderWidth, _borderHeight;

	//Surfaces, Manager y uso de clase Paint para el color
	private final SurfaceView _surfaceView;
	private final Paint _paint;
	private Canvas _canvas;
	private final SurfaceHolder _holder;
	private final AssetManager _assetManager;

	private final HashMap<String, Image> images;


	GraphicsAndroid(SurfaceView myView, AssetManager mgr, int logicWidth, int logicHeight) {
		_surfaceView = myView;
		_assetManager = mgr;

		_logicWidth = logicWidth;
		_logicHeight = logicHeight;


		//Creamos los nuevos elementos y obtenemos el holder
		_holder = _surfaceView.getHolder();

		_paint = new Paint();
		_canvas = new Canvas();

		images = new HashMap<>();

		setNewResolution(_logicWidth, _logicHeight);
	}

	//Limpieza de pantalla poniendolo todo de un color usando la variable canvas
	@Override
	public void clear(int color) {
		final boolean debug = false;

		if (debug && _canvas != null) {
			_canvas.drawColor(0xFFFFFFFF);
			drawRect(0, 0, _logicWidth, _logicHeight, 1f, color);
		} else if (_canvas != null && _paint != null) {
			setColor(color);
			_canvas.drawColor(_paint.getColor());
		}
	}

	//El formato de android acepta por defecto un ARGB y nosotros queremos que aambas plataformas  tengan el color
	//RGB como entrada , con lo cual usando 0xFF000000 hacemos una rapida conversion y se la aplicamos a la pintura
	@Override
	public void setColor(int colorRGB) {
		colorRGB += 0xFF000000;
		int colorARGB = colorRGB + 0xFF000000;
		_paint.setColor(colorARGB);
	}

	@Override
	public void prepareFrame() {
		setNewResolution(getWidth(), getHeight());
	}

	//USO DEL CANVAS, BLOQUEO Y DESBLOQUEO
	public void lockCanvas() {
		_canvas = _surfaceView.getHolder().lockCanvas();
	}

	public void unlockCanvas() {
		_surfaceView.getHolder().unlockCanvasAndPost(_canvas);
	}

	//Crear imagenes , fuentes..ETC
	@Override
	public Image loadImage(String imgName) {
		if (images.containsKey(imgName)) {
			return images.get(imgName);
		}
		//Las imagenes en android son representadas con un bitmap
		//Con un stream abre la imagen por nombre
		Bitmap bitmap = null;
		try {
			InputStream inputS = _assetManager.open("Sprites/" + imgName);
			bitmap = BitmapFactory.decodeStream(inputS);
			inputS.close(); // Cierra el InputStream después de usarlo
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
		ImageAndroid img = new ImageAndroid(bitmap);
		images.put(imgName, img);

		return images.get(imgName);
	}

	//No podemos hacer carpetas puesto que busca desde la carptea assets solo
	@Override
	public Font newFont(String fileName, float size) {
		//Con el elemento typeface podemos ajustar el tipo de fuente y decirselo al elemento paint para que adquiera esas caracteriticas
		Typeface tface = Typeface.createFromAsset(_assetManager, "Fonts/" + fileName);

		//A parte creamos el objeto fuente y lo devolvemos
		return new FontAndroid(tface, size);
	}

	//Dibujo de lineas rectangulos , imagenes y fuente

	// Para dibujar seguimos el siguiente esquema
	@Override
	public void drawImage(Image image, float logicX, float logicY, float logicWidth, float logicHeight, float scale) {
		ImageAndroid imageAndroid = (ImageAndroid) image;

		int width = scaleToReal(logicWidth, scale);
		int height = scaleToReal(logicHeight, scale);

		if (width == 0 || height == 0)
			return;

		Bitmap bitmap = getResizedBitmap(imageAndroid.getImage(), width, height);
		if (bitmap != null)
			_canvas.drawBitmap(bitmap, logicToRealX(logicX + (1 - scale) * logicWidth / 2),
					logicToRealY(logicY + (1 - scale) * logicHeight / 2), _paint);
	}

	@Override
	public void drawRect(float logicX, float logicY, float logicWidth, float logicHeight, float scale, int color) {
		Rect rect = new Rect(logicToRealX(logicX), logicToRealY(logicY),
				logicToRealX(logicX) + scaleToReal(logicWidth, scale),
				logicToRealY(logicY) + scaleToReal(logicHeight, scale));

		setColor(color);
		_canvas.drawRect(rect, _paint);
	}

	@Override
	public void drawRealRect(int realX, int realY, int realWidth, int realHeight, int color) {
		Rect rect = new Rect(realX, realY, realWidth, realHeight);

		setColor(color);
		_canvas.drawRect(rect, _paint);
	}

	@Override
	public void drawCircleWithBorder(float logicX, float logicY, float radius, float borderWidth, float scale, int circleColor, int borderColor) {
		// Dibuja el borde del círculo
		setColor(borderColor);
		int realX = logicToRealX(logicX + (1 - scale) * radius);
		int realY = logicToRealY(logicY + (1 - scale) * radius);
		final int realRadius = scaleToReal(radius, scale);
		final int realBorderRadius = realRadius + scaleToReal(borderWidth, scale);

		// Dibuja el borde del círculo
		_canvas.drawCircle(realX, realY, realBorderRadius, _paint);

		// Dibuja el círculo interior
		setColor(circleColor);
		_canvas.drawCircle(realX, realY, realRadius, _paint);
	}

	@Override
	public void drawRoundedRect(float logicX, float logicY, float logicWidth, float logicHeight,
	                            float arcWidth, float arcHeight, float scale, int color) {
		RectF rect = new RectF(logicToRealX(logicX), logicToRealY(logicY),
				logicToRealX(logicX) + scaleToReal(logicWidth, scale),
				logicToRealY(logicY) + scaleToReal(logicHeight, scale));

		setColor(color);
		_canvas.drawRoundRect(rect, scaleToReal(arcWidth, scale), scaleToReal(arcHeight, scale), _paint);
	}

	@Override
	public void drawCircle(float logicX, float logicY, float radius, float scale, int color) {
		int realX = logicToRealX(logicX + (1 - scale) * radius);
		int realY = logicToRealY(logicY + (1 - scale) * radius);
		int realRadius = scaleToReal(radius, scale);

		setColor(color);
		_canvas.drawCircle(realX, realY, realRadius, _paint);
	}

	@Override
	public void drawText(String text, Font font, float logicX, float logicY, float scale, int color) {
		setColor(color);

		float stringWidth = getStringWidth(text, font);
		float stringHeight = getStringHeight(text, font);

		_paint.setTypeface(((FontAndroid) font).getFont());
		_paint.setTextSize(font.getFontSize() * _scaleFactor * scale);

		// Calcula las coordenadas de dibujo ajustadas según el tamaño de la fuente escalado
		_canvas.drawText(text, logicToRealX(logicX + (1 - scale) * stringWidth / 2),
				logicToRealY(logicY - (1 - scale) * stringHeight), _paint);
	}

	// Para el cambio de tamaño a una imagen (bitmap)
	//Tenemos que aplicar un escalado al Objeto nuevo a crear
	//Se crea un objeto Matrix que representa una matriz de transformación
	// se aplica la escala a la matriz utilizando el método postScale().
	//Utilizando la matriz de transformación, se crea una nueva imagen redimensionada (resizedBitmap) a partir de la imagen original (bm).
	// Se utilizan las coordenadas (0, 0) como punto de inicio y se toman todas las dimensiones de la imagen original.
	// El último parámetro false indica que no se debe filtrar la imagen durante la escala
	// lo que significa que se utilizará un método de interpolación rápido.
	public Bitmap getResizedBitmap(Bitmap bm, float newWidth, float newHeight) {

		if (bm != null) {
			int width = bm.getWidth();
			int height = bm.getHeight();
			float sW = ((float) newWidth) / width;
			float sH = ((float) newHeight) / height;
			Matrix m = new Matrix();
			m.postScale(sW, sH);
			Bitmap bmresized = Bitmap.createBitmap(bm, 0, 0, width, height, m, false);
			return bmresized;
		}
		return null;

	}

	public int logicToRealX(float logicX) {
		return (int) (logicX * _scaleFactor + _borderWidth);
	}

	public int logicToRealY(float logicY) {
		return (int) (logicY * _scaleFactor + _borderHeight);
	}

	public int scaleToReal(float realScale, float specificScale) {
		return (int) (realScale * _scaleFactor * specificScale);
	}

	// Con este metodo nos aseguramos que el surfaceView (obtenido a partir del holder) este disponible para usarse y renderizar
	public boolean isValid() {
		return _holder.getSurface().isValid();
	}

	//Para ver el tamaño del string trazamos un rectangulo que cubra el texto  asi vemos su alto
	//Pra ver el ancho de una cadena simplmente pasamos la longitud de la cadena y el propio paint nos lo calcula
	@Override
	public float getStringWidth(String text, Font font) {
		_paint.setTypeface(((FontAndroid) font).getFont());
		_paint.setTextSize(font.getFontSize());
		return _paint.measureText(text, 0, text.length());
	}

	@Override
	public float getStringHeight(String text, Font font) {
		Rect borders = new Rect();
		_paint.setTextSize(font.getFontSize());
		_paint.setTypeface(((FontAndroid) font).getFont());
		_paint.getTextBounds(text, 0, text.length(), borders);
		return borders.height();
	}

	@Override
	public int getWidth() {     //ANCHO VENTANA
		return _surfaceView.getWidth();
	}

	@Override
	public int getHeight() {
		return _surfaceView.getHeight();
	}

	@Override
	public int getLogicHeight() {
		return _logicHeight;
	}     // ALTURA LOGICA

	@Override
	public int getLogicWidth() {
		return _logicWidth;
	}       //ANCHO LOGICO

	@Override
	public void setNewResolution(int newRealWidth, int newRealHeight) {
		float factorX = (float) getWidth() / _logicWidth;
		float factorY = (float) getHeight() / _logicHeight;

		// El factor de escala se escoje del valor mínimo de cualquiera de las dos
		// dimensiones. Debido a que solo se reescala el mínimo factor.
		_scaleFactor = Math.min(factorX, factorY);

		// Se asigna el tamaño de los bordes, dividido entre dos porque la aplicación
		// estará en el centro, y tendrá los bordes a cada lado.
		if ((float) getWidth() / getHeight() < (float) _logicWidth / _logicHeight) {
			_borderWidth = 0;
			_borderHeight = (int) ((getHeight() - (_logicHeight * factorX)) / 2);
		} else {
			_borderWidth = (int) ((getWidth() - (_logicWidth * factorY)) / 2);
			_borderHeight = 0;
		}
	}

	@Override
	public boolean inBounds(float posX, float posY, int checkX, int checkY, float width, float height, float scale) {
		return (checkX >= logicToRealX(posX)
				&& checkX <= logicToRealX(posX) + scaleToReal(width, scale)
				&& checkY >= logicToRealY(posY)
				&& checkY <= logicToRealY(posY) + scaleToReal(height, scale));
	}

	@Override
	public void save() {
		_canvas.save(); // Guarda el estado actual del lienzo
	}

	@Override
	public void restore() {
		_canvas.restore(); // Restaura el estado guardado del lienzo
	}
}
