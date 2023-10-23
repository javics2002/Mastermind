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

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Image;
import com.example.aninterface.Graphics;


public class GraphicsAndroid implements Graphics {


    private int window;



    // Tamaño  logico
    private int logicWidth;
    private int logicHeight;
    // Escalado
    private float factorScale;
    private float factorX;
    private float factorY;
    // Medidas de bordes
    private int borderWidth;
    private int borderHeight;
    private int borderTop;

    private float Ratio;
   private float baseFontSize = 48f;  // Puedes ajustar esto según tus necesidades


    //Surfaces , Manager y uso de clase Paint para el color
    private SurfaceView myView;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;
    private AssetManager mgr;
    GraphicsAndroid(SurfaceView myView, AssetManager mgr, int logicWidth, int logicHeight){
        this.myView = myView;
        this.mgr = mgr;

        //El borde empieza en 0
        this.borderTop = 0;

        this.logicWidth = logicWidth;
        this.logicHeight = logicHeight;
        // Estableemos un ratio
        Ratio=((float)2/(float)3);

        //Creamos los nuevos elementos y obtenemos el holder
        this.holder = this.myView.getHolder();
        this.paint = new Paint();
        this.canvas = new Canvas();

    }

    //Limpieza de pantalla poniendolo todo de un color usando la variable canvas
    @Override
    public void clear(int color) {
        color+= 0xFF000000;
        this.canvas.drawColor(color);
    }

    //El formato de android acepta por defecto un ARGB y nosotros queremos que aambas plataformas  tengan el color
    //RGB como entrada , con lo cual usando 0xFF000000 hacemos una rapida conversion y se la aplicamos a la pintura
    @Override
    public void setColor(int colorRGB) {
        colorRGB += 0xFF000000;
        int colorARGB = colorRGB + 0xFF000000;
        this.paint.setColor(colorARGB);
    }
    @Override
    public void prepareFrame() {

        //Calculo de bordes y de factor de escala

        //Calculo del factor de escala
        this.factorX = (float)getWidth() / (float)this.logicWidth;
        this.factorY = (float)getHeight() / (float)this.logicHeight;
        this.factorScale = Math.min(this.factorX, this.factorY);



        //Comprobamos si en este caso el escalado de miView (ancho /alto) es menor que la relacion de aspecto que ponemos nosotros (2/3)
        //Por que si es menor añadimos un ancho de bordes por arriba y abajo (Height)
        //Si no se los añadimos por los lados( Width)
        if (((float) getWidth() / (float) getHeight()) < Ratio){
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.


            int var = (int) ((getHeight() - (this.logicHeight * this.factorX)) / 2);
            this.borderHeight = var;
            this.borderWidth = 0;
        } else  {
            // Para calcular el tamaño de bordes restamos el ancho o alto de nuestro juego a
            // la dimensión correspondiente de la ventana y dividimos por 2 para centrar el juego.

            int var = (int) ((getWidth() - (this.logicWidth * this.factorY)) / 2);
            this.borderWidth = var;
            this.borderHeight = 0;
        }
    }

    //USO DEL CANVAS , BLOQUEO Y DESBLOQUEO
    public void lockCanvas(){
        this.canvas = this.myView.getHolder().lockCanvas();
    }
    public void unlockCanvas(){
        this.myView.getHolder().unlockCanvasAndPost(this.canvas);
    }
    //Crear imagenes , fuentes..ETC

    @Override
    public Image newImage(String imgName) {
        //Las imagenes en android son representadas con un bitmap
        //Con un stream abre la imagen por nombre
        Bitmap bitmap = null;
        try {
            InputStream inputS = this.mgr.open(imgName);
            bitmap = BitmapFactory.decodeStream(inputS);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        IntImageAndroid img = new IntImageAndroid(bitmap);
        return (Image) img;
    }

    //No podemos hacer carpetas puesto que busca desde la carptea assets solo
    @Override
    public Font newFont(String fileName, float size){
        //Con el elemento typeface podemos ajustar el tipo de fuente y decirselo al elemento paint para que adquiera esas caracteriticas
        Typeface tface = Typeface.createFromAsset(mgr, fileName);
        this.paint.setTypeface(tface);
        this.paint.setTextSize(size*(factorScale*2));

        //A parte creamos el objeto fuente y lo devolvemos
        IntFontAndroid fontA= new IntFontAndroid(tface);
        return fontA;
    }

    //Dibujo de lineas rectangulos , imagenes y fuente



    // Para dibujar seguimos el siguiente esquema
    // Pos x = Pos x real - Ancho w real Centrado ( /2)
    // Pos y = Pos y real - Alto h real Centrado (/2) + Margen myView ( bordertop)
    //Solo aplicable a imagenes y a texto , en los square rect se usa sin conversion
    //Por que es directo a ventana (Aunque se sigue teniendo en cuenta border top)
    @Override
    public void drawImage(Image image, int x, int y, int w, int h) {

        int x2=x;
        int y2= y;
        IntImageAndroid a = (IntImageAndroid)image;
        float newW = (scaleToReal(w));
        float newH = (scaleToReal(h));
        float b= logicToRealX(x) - (scaleToReal(w)/2);
        float c= logicToRealX(y) - (scaleToReal(h)/2) + borderTop;

        Bitmap aux = getResizedBitmap(a.getImg(),newW ,newH);
        this.canvas.drawBitmap(aux,logicToRealX(x) - (scaleToReal(w)/2) ,
                logicToRealX(y) - (scaleToReal(h)/2) + borderTop,this.paint);



    }

    @Override
    public void fillSquare(int cx, int cy, int side) {
        Rect rect = new Rect(cx,cy+borderTop,cx+side,cy+side+borderTop);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(rect, this.paint);
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        Rect rect = new Rect(x,y+borderTop,x+w,y+h+borderTop);
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(rect, this.paint);
    }

    @Override
    public void drawSquare(int cx, int cy, int side) {
        Rect rect = new Rect(cx,cy+borderTop,cx+side,cy+side+borderTop);
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(rect, this.paint);
    }

    @Override
    public void drawLine(int initX, int initY, int endX, int endY) {
        this.canvas.drawLine(initX,initY+borderTop,endX,endY+borderTop, this.paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        Rect rect = new Rect(x,y+borderTop,x+width,y+height+borderTop);
        this.paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawRect(rect, this.paint);
    }

    @Override
    public void drawText(String text,Font f, int x, int y, int color) {
        color += 0xFF000000;
        this.paint.setColor(color);


        //Esto no deberia de estar cableado pero de momento se queda asi
        this.paint.setTextSize(baseFontSize*(factorScale*2));
        this.canvas.drawText(text,x - (getStringWidth(text)/2), y-(getStringHeight(text)/2)+borderTop,this.paint);
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
        float sW = ((float) newWidth) / bm.getWidth();
        float sH = ((float) newHeight) /  bm.getHeight();
        Matrix m = new Matrix();
        m.postScale(sW,  sH);
        Bitmap bmresized = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),  bm.getHeight(), m, false);
        return bmresized;
    }

    @Override
    public int logicToRealX(int x) { return (int)(x*factorScale + borderWidth); }

    @Override
    public int logicToRealY(int y) {            //CONVERSOR DE LOGICO A REAL EN Y
        return (int)(y*factorScale + borderHeight);
    }

    @Override
    public int scaleToReal(int s) {     //CONVERSOR DE ESCALA A REAL
        return (int)(s*(factorScale));
    }


    // Con este metodo nos aseguramos que el surfaceView (obtenido a partir del holder) este disponible para usarse y renderizar
    public boolean isValid() { return this.holder.getSurface().isValid();}

    @Override
    public int getHeight() {return this.myView.getHeight();
    }
    public int getWidth() {     //ANCHO VENTANA
        return this.myView.getWidth();
    }



    //Para ver el tamaño del string trazamos un rectangulo que cubra el texto  asi vemos su alto
    //Pra ver el ancho de una cadena simplmente pasamos la longitud de la cadena y el propio paint nos lo calcula
    @Override
    public int getStringWidth(String text) {
        return (int)this.paint.measureText(text,0,text.length());
    }

    @Override
    public int getStringHeight(String t) {
        Rect bordes = new Rect();
        this.paint.getTextBounds(t,0,t.length(), bordes);
        return bordes.height();
    }

    @Override
    public int getWidthLogic() {        //ANCHO LOGICO
        return this.logicWidth;
    }

    @Override
    public int getHeightLogic() {       //ALTURA LOGICA
        return this.logicHeight;
    }

   @Override
   public int get_borderTop(){return this.borderTop;}

    //Set de la resolucion aunque creo que no es bueno llamarlo en android
    @Override
    public void setResolution(int w, int h) {
        this.myView.getHolder().setFixedSize(w,h);
    }



}
