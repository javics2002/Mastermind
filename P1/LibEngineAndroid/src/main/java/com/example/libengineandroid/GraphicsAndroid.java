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
    // Tamaño  logico
    private int _logicWidth;
    private int _logicHeight;

    // Escalado
    private float _factorScale;
    private float _factorX;
    private float _factorY;

    // Medidas de bordes
    private int _borderWidth, _borderHeight, _borderTop, _borderBottom;
    private float _ratio = 2f / 3f;

    //Surfaces , Manager y uso de clase Paint para el color
    private SurfaceView _surfaceView;
    private Paint _paint;
    private Canvas _canvas;
    private SurfaceHolder _holder;
    private AssetManager _assetManager;

    GraphicsAndroid(SurfaceView myView, AssetManager mgr, int logicWidth, int logicHeight) {
        _surfaceView = myView;
        _assetManager = mgr;

        //El borde empieza en 0
        _borderTop = 0;

        _logicWidth = logicWidth;
        _logicHeight = logicHeight;

        //Creamos los nuevos elementos y obtenemos el holder
        _holder = _surfaceView.getHolder();
        _paint = new Paint();
        _canvas = new Canvas();
    }

    //Limpieza de pantalla poniendolo todo de un color usando la variable canvas
    @Override
    public void clear(int color) {
        color += 0xFF000000;
        _canvas.drawColor(color);

        // TODO: Poner false para release
        final boolean debug = true;
        if(debug){
            setColor(0);

            fillRect(0, 0, _borderWidth, getHeight());
            fillRect(0, 0, getWidth(), _borderHeight);
            fillRect(getWidth() - _borderWidth, 0, _borderWidth, getHeight());
            fillRect(0, getHeight() - _borderHeight, getWidth(), _borderHeight);
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
        //Calculo de bordes y de factor de escala
        //Calculo del factor de escala
        _factorX = (float)getWidth() / (float)getWidthLogic();
        _factorY = (float)getHeight() / (float)getHeightLogic();
        _factorScale = Math.min(_factorX, _factorY);

        //Comprobamos si en este caso el escalado de miView (ancho /alto) es menor que la relacion de aspecto que ponemos nosotros (2/3)
        //Por que si es menor añadimos un ancho de bordes por arriba y abajo (Height)
        //Si no se los añadimos por los lados( Width)
        if (((float) getWidth() / (float) getHeight()) < _ratio){
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.
            int var = (int) ((getHeight() - (_logicHeight * _factorX)) / 2);
            _borderHeight = var;
            _borderWidth = 0;
        } else  {
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.
            int var = (int) ((getWidth() - (_logicWidth * _factorY)) / 2);
            _borderWidth = var;
            _borderHeight = 0;
        }
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
    // Pos x = Pos x real - Ancho w real Centrado ( /2)
    // Pos y = Pos y real - Alto h real Centrado (/2) + Margen myView ( bordertop)
    //Solo aplicable a imagenes y a texto , en los square rect se usa sin conversion
    //Por que es directo a ventana (Aunque se sigue teniendo en cuenta border top)
    @Override
    public void drawImage(Image image, int x, int y, int w, int h) {
        ImageAndroid a = (ImageAndroid)image;
        int newW = (int)(scaleToReal(w));
        int newH = (int)(scaleToReal(h));
        float b= logicToRealX(x) - (scaleToReal(w)/2);
        float c= logicToRealX(y) - (scaleToReal(h)/2) + _borderTop;

        Bitmap aux = getResizedBitmap(a.getImg(),newW ,newH);
        _canvas.drawBitmap(aux,logicToRealX(x) - (scaleToReal(w)/2) ,
                logicToRealY(y) - (scaleToReal(h)/2) + _borderTop, _paint);
    }

    @Override
    public void fillSquare(int cx, int cy, int side) {
        Rect rect = new Rect(cx,cy+ _borderTop,cx+side,cy+side+ _borderTop);
        _paint.setStyle(Paint.Style.FILL);
        _canvas.drawRect(rect, _paint);
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        Rect rect = new Rect(x,y+ _borderTop,x+w,y+h+ _borderTop);
        _paint.setStyle(Paint.Style.FILL);
        _canvas.drawRect(rect, _paint);
    }

    @Override
    public void drawSquare(int cx, int cy, int side) {
        Rect rect = new Rect(cx,cy+ _borderTop,cx+side,cy+side+ _borderTop);
        _paint.setStyle(Paint.Style.STROKE);
        _canvas.drawRect(rect, _paint);
    }

    @Override
    public void drawLine(int initX, int initY, int endX, int endY) {
        _canvas.drawLine(initX,initY+ _borderTop,endX,endY+ _borderTop, _paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        Rect rect = new Rect(x,y+ _borderTop,x+width,y+height+ _borderTop);
        _paint.setStyle(Paint.Style.STROKE);
        _canvas.drawRect(rect, _paint);
    }

    @Override
    public void drawText(String text, Font f, int x, int y, int color) {
        color += 0xFF000000;
        _paint.setColor(color);

        _paint.setTypeface(((FontAndroid) f).getFont());
        _paint.setTextSize(f.getFontSize() * _factorScale);

        // Calcula las coordenadas de dibujo ajustadas según el tamaño de la fuente escalado
        int adjustedX = logicToRealX(x) - (getStringWidth(text, f) / 2);
        int adjustedY = logicToRealY(y) - (getStringHeight(text, f) / 2);

        _canvas.drawText(text, adjustedX, adjustedY, _paint);
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

    @Override //De posicion logica a  real
    public int logicToRealX(int x) {
        return (int)(x * _factorScale + _borderWidth);
    }

    @Override
    public int logicToRealY(int y) {        //CONVERSOR DE TAMAÑO LOGICO A REAL EN Y
        return (int)(y * _factorScale + _borderHeight);
    }
    @Override
    public int scaleToReal(int s) {
        return (int)(s *2);
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
    public int getWidthLogic() {        //ANCHO LOGICO
        return this._logicWidth;
    }

    @Override
    public int getHeightLogic() {       //ALTURA LOGICA
        return this._logicHeight;
    }

    @Override
    public int get_borderTop() {
        return _borderTop;
    }

    //Set de la resolucion aunque creo que no es bueno llamarlo en android
    @Override
    public void setNewResolution(int w, int h) {
        _surfaceView.getHolder().setFixedSize(w,h);
    }
}
