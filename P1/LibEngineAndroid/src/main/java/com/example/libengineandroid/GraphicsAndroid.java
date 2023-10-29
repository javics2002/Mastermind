package com.example.libengineandroid;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

import com.example.aninterface.Font;
import com.example.aninterface.Image;
import com.example.aninterface.Graphics;


public class GraphicsAndroid implements Graphics {

    private float _scaleFactor;
    private int _logicWidth, _logicHeight;
    private int _topInset, _bottomInset, _leftInset, _rightInset;

    // Medidas de bordes
    private int _borderWidth, _borderHeight;

    //Surfaces , Manager y uso de clase Paint para el color
    private SurfaceView _surfaceView;
    private Paint _paint;
    private Canvas _canvas;
    private SurfaceHolder _holder;
    private AssetManager _assetManager;

    GraphicsAndroid(SurfaceView myView, AssetManager mgr, int logicWidth, int logicHeight) {
        _surfaceView = myView;
        _assetManager = mgr;


        _logicWidth = logicWidth;
        _logicHeight = logicHeight;

        //Creamos los nuevos elementos y obtenemos el holder
        _holder = _surfaceView.getHolder();



        _paint = new Paint();
        _canvas = new Canvas();
        setNewResolution(_logicWidth, _logicHeight);
    }

    //Limpieza de pantalla poniendolo todo de un color usando la variable canvas
    @Override
    public void clear(int color) {
        color += 0xFF000000;
        _canvas.drawColor(color);

        // TODO: Poner false para release
        final boolean debug = false;
        if(debug){
            setColor(0);

            drawRect(0, 0, _logicWidth, _logicHeight, color);
        }
        else{
           setColor(1);
            drawRect(0, 0, _logicWidth, _logicHeight, color);
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

    //USO DEL CANVAS , BLOQUEO Y DESBLOQUEO
    public void lockCanvas(){
        _canvas = _surfaceView.getHolder().lockCanvas();
    }

    public void unlockCanvas(){
        _surfaceView.getHolder().unlockCanvasAndPost(_canvas);
    }

    //Crear imagenes , fuentes..ETC

    @Override
    public Image newImage(String imgName) {
        //Las imagenes en android son representadas con un bitmap
        //Con un stream abre la imagen por nombre
        Bitmap bitmap = null;
        try {
            InputStream inputS = this._assetManager.open(imgName);
            bitmap = BitmapFactory.decodeStream(inputS);
            inputS.close(); // Cierra el InputStream después de usarlo
        } catch (final IOException e) {
            e.printStackTrace();
        }
        ImageAndroid img = new ImageAndroid(bitmap);
        return (Image) img;
    }

    //No podemos hacer carpetas puesto que busca desde la carptea assets solo
    @Override
    public Font newFont(String fileName, float size){
        //Con el elemento typeface podemos ajustar el tipo de fuente y decirselo al elemento paint para que adquiera esas caracteriticas
        Typeface tface = Typeface.createFromAsset(_assetManager, fileName);

        //A parte creamos el objeto fuente y lo devolvemos
        return new FontAndroid(tface, size);
    }

    //Dibujo de lineas rectangulos , imagenes y fuente

    // Para dibujar seguimos el siguiente esquema
    @Override
    public void drawImage(Image image, int logicX, int logicY, int logicWidth, int logicHeight) {
        ImageAndroid a = (ImageAndroid)image;
        Bitmap aux = getResizedBitmap(a.getImg(),scaleToReal(logicWidth), scaleToReal(logicHeight));
        _canvas.drawBitmap(aux, logicToRealX(logicX) ,
                logicToRealY(logicY) , _paint);
    }

    @Override
    public void drawRect(int logicX, int logicY, int logicWidth, int logicHeight, int color)  {

        Rect rect = new Rect( logicToRealX(logicX), logicToRealY(logicY), scaleToReal(logicWidth), scaleToReal(logicHeight));
        _paint.setStyle(Paint.Style.STROKE);
        _canvas.drawRect(rect, _paint);
    }
    @Override
    public int scaleToReal(int realScale){
        return (int)(realScale * _scaleFactor);
    }






    @Override
    public void drawText(String text, Font f, int logicX, int logicY, int color) {
        color += 0xFF000000;
        _paint.setColor(color);

        _paint.setTypeface(((FontAndroid) f).getFont());
        _paint.setTextSize(f.getFontSize() * _scaleFactor);

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
        int width = bm.getWidth();
        int height = bm.getHeight();
        float sW = ((float) newWidth) /width;
        float sH = ((float) newHeight) / height;
        Matrix m = new Matrix();
        m.postScale(sW,  sH);
        Bitmap bmresized = Bitmap.createBitmap(bm, 0, 0, width, height, m, false);
        return bmresized;
    }


    @Override
    public int logicToRealX(int logicX) {
        return (int)(logicX * _scaleFactor + _leftInset + _borderWidth);
    }
    @Override
    public int logicToRealY(int logicY) {
        return (int)(logicY * _scaleFactor + _topInset + _borderHeight);
    }

    // Con este metodo nos aseguramos que el surfaceView (obtenido a partir del holder) este disponible para usarse y renderizar
    public boolean isValid() { return _holder.getSurface().isValid();}

    @Override
    public int getHeight() {
        return _surfaceView.getHeight();
    }
    public int getWidth() {     //ANCHO VENTANA
       return _surfaceView.getWidth();
    }

    //Para ver el tamaño del string trazamos un rectangulo que cubra el texto  asi vemos su alto
    //Pra ver el ancho de una cadena simplmente pasamos la longitud de la cadena y el propio paint nos lo calcula
    @Override
    public int getStringWidth(String text, Font font) {
        _paint.setTypeface(((FontAndroid) font).getFont());
        return (int) _paint.measureText(text,0,text.length());
    }

    @Override
    public int getStringHeight(String t, Font f) {
        Rect bordes = new Rect();
        _paint.setTypeface(((FontAndroid) f).getFont());
        _paint.getTextBounds(t,0,t.length(), bordes);
        return bordes.height();
    }

    @Override
    public int getHeightLogic() { return _logicHeight; }     // ALTURA LOGICA
    @Override
    public int getWidthLogic() { return _logicWidth; }       //ANCHO LOGICO



    //Set de la resolucion aunque creo que no es bueno llamarlo en android
    @Override
    public void setNewResolution(int newRealWidth, int newRealHeight) {
        //_surfaceView.getHolder().setFixedSize(newRealWidth,newRealHeight);
        float factorX = (float) getWidth() / _logicWidth;
        float factorY = (float) getHeight() / _logicHeight;

        _scaleFactor = Math.min(factorX, factorY);

        if ((float) getWidth() / getHeight() < (float) _logicWidth / _logicHeight) {
            _borderWidth = 0;
            _borderHeight =  (int) ((getHeight() - (_logicHeight * factorX)) / 2);
        } else  {
            _borderWidth = (int) ((getWidth() - (_logicWidth * factorY)) / 2);
            _borderHeight = 0;
        }
    }
}
