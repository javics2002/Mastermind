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

    //Surfaces , Manager y uso de clase Paint para el color
    private final SurfaceView _surfaceView;
    private final Paint _paint;
    private Canvas _canvas;
    private final SurfaceHolder _holder;
    private final AssetManager _assetManager;

    private HashMap<String, Image> images;

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
        if (debug) {
            _canvas.drawColor(0);

            drawRect(0, 0, _logicWidth, _logicHeight, color);
        } else {
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
        if (images.containsKey(imgName)){
            return images.get(imgName);
        }
        //Las imagenes en android son representadas con un bitmap
        //Con un stream abre la imagen por nombre
        Bitmap bitmap = null;
        try {
            InputStream inputS = this._assetManager.open(imgName);
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
        Typeface tface = Typeface.createFromAsset(_assetManager, fileName);

        //A parte creamos el objeto fuente y lo devolvemos
        return new FontAndroid(tface, size);
    }

    //Dibujo de lineas rectangulos , imagenes y fuente

    // Para dibujar seguimos el siguiente esquema
    @Override
    public void drawImage(Image image, int logicX, int logicY, int logicWidth, int logicHeight) {
        ImageAndroid a = (ImageAndroid) image;
        Bitmap aux = getResizedBitmap(a.getImage(), scaleToReal(logicWidth), scaleToReal(logicHeight));
        if (aux != null)
            _canvas.drawBitmap(aux, logicToRealX(logicX), logicToRealY(logicY), _paint);
    }

    @Override
    public void drawRect(int logicX, int logicY, int logicWidth, int logicHeight, int color) {
        Rect rect = new Rect(logicToRealX(logicX), logicToRealY(logicY),
                logicToRealX(logicX + logicWidth), logicToRealY(logicY + logicHeight));

        setColor(color);
        _canvas.drawRect(rect, _paint);
    }

    @Override
    public void drawCircleWithBorder(int logicX, int logicY, int radius, int borderWidth, int circleColor, int borderColor) {
        // Dibuja el borde del círculo
        setColor(borderColor);
        int realX = logicToRealX(logicX);
        int realY = logicToRealY(logicY);
        int realRadius = scaleToReal(radius + borderWidth);

        _canvas.drawCircle(realX, realY, realRadius, _paint);

        // Dibuja el círculo interior
        setColor(circleColor);
        realRadius = scaleToReal(radius);
        _canvas.drawCircle(realX, realY, realRadius, _paint);
    }
    @Override
    public void drawRoundedRect(int logicX, int logicY, int logicWidth, int logicHeight, int color, int arcWidth, int arcHeight) {
        RectF rect = new RectF(logicToRealX(logicX), logicToRealY(logicY),
                logicToRealX(logicX + logicWidth), logicToRealY(logicY + logicHeight));

        setColor(color);
        _canvas.drawRoundRect(rect, arcWidth, arcHeight, _paint);
    }

    public void drawCircle(int logicX, int logicY, int radius, int color) {
        int realX = logicToRealX(logicX);
        int realY = logicToRealY(logicY);
        int realRadius = scaleToReal(radius);

        setColor(color);
        _canvas.drawCircle(realX, realY, realRadius, _paint);
    }

    @Override
    public void drawText(String text, Font font, int logicX, int logicY, int color) {
        setColor(color);

        _paint.setTypeface(((FontAndroid) font).getFont());
        _paint.setTextSize(font.getFontSize() * _scaleFactor);

        // Calcula las coordenadas de dibujo ajustadas según el tamaño de la fuente escalado
        _canvas.drawText(text, logicToRealX(logicX), logicToRealY(logicY), _paint);
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



    public int logicToRealX(int logicX) {
        return (int) (logicX * _scaleFactor + _borderWidth);
    }


    public int logicToRealY(int logicY) {
        return (int) (logicY * _scaleFactor + _borderHeight);
    }


    public int scaleToReal(int realScale) {
        return (int) (realScale * _scaleFactor);
    }

    // Con este metodo nos aseguramos que el surfaceView (obtenido a partir del holder) este disponible para usarse y renderizar
    public boolean isValid() {
        return _holder.getSurface().isValid();
    }

    //Para ver el tamaño del string trazamos un rectangulo que cubra el texto  asi vemos su alto
    //Pra ver el ancho de una cadena simplmente pasamos la longitud de la cadena y el propio paint nos lo calcula
    @Override
    public int getStringWidth(String text, Font font) {
        _paint.setTypeface(((FontAndroid) font).getFont());
        _paint.setTextSize(font.getFontSize());
        return (int) _paint.measureText(text, 0, text.length());
    }

    @Override
    public int getStringHeight(String text, Font font) {
        Rect bordes = new Rect();
        _paint.setTextSize(font.getFontSize());
        _paint.setTypeface(((FontAndroid) font).getFont());
        _paint.getTextBounds(text, 0, text.length(), bordes);
        return bordes.height();
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
    public boolean inBounds(int posX, int posY, int checkX, int checkY,int width, int height)
    {
        return(checkX >=logicToRealX(posX)
                && checkX <= logicToRealX(posX) + scaleToReal(width)
                && checkY >= logicToRealY(posY)
                && checkY <= logicToRealY(posY) + scaleToReal(height));
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
