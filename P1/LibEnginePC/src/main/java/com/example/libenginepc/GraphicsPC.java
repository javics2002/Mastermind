package com.example.libenginepc;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.example.aninterface.IntImage;
import com.example.aninterface.Graphics;
import javax.swing.border.LineBorder;

public class GraphicsPC implements Graphics {
    private JFrame myView;
    private BufferStrategy bufferStrategy;
    float Ratiox = 2f;
    float RatioY=  3f;
    private Graphics2D graphics2D;
    private int logicWidth;
    private int logicHeight;
    private int windowSX;
    private int windowSY;

    private int borderWidth;
    private int borderHeight;
    private int borderTop;
    private int window; // TODO: ?
    private float factorScale;
    private float factorX;
    private float factorY;


    //Creamos dos buffers para evitar el parapadeo (Uno dibuja el otro enseña)
    GraphicsPC(JFrame myView, int logicWidth, int logicHeight,int windowSX, int windowSY) {
        this.myView = myView;
        this.myView.createBufferStrategy(2);


        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

        this.logicWidth = logicWidth;
        this.logicHeight = logicHeight;
        this.windowSX= windowSX;
        this.windowSY= windowSY;

        //Le damos un tamaño logico a la ventana
        this.setResolution(this.windowSX, this.windowSY);


        this.factorY = (float)getHeight() / (float)this.logicHeight;

        this.factorX = (float)getWidth() / (float)this.logicWidth;
        this.factorScale = Math.min(this.factorX, this.factorY);






    }
    @Override
    public void setColor(int color) {
        this.graphics2D.setColor(new Color(color));
    }
    @Override
    public void clear(int color) {
        this.graphics2D.setColor(new Color(color));
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        this.graphics2D.setPaintMode();
    }

    // Frame Management
    public void show() {
        this.bufferStrategy.show();
    }
    public void prepareFrame() {
        // Actualizacion de la resolucion a partir
        setResolution(getWidth(), getHeight());
        this.graphics2D = (Graphics2D)this.bufferStrategy.getDrawGraphics();
    }
    public void finishFrame() {
        //Estuve viendo que con el dispose se liberan los recursos asociado a los graficos
        this.graphics2D.dispose();
    }

    @Override
    public void drawImage(IntImage image, int x, int y, int w, int h) {
        this.graphics2D.drawImage(((IntImagePC) image).getImg(),
                logicToRealX(x) - (scaleToReal(w)/2),logicToRealY(y) - (scaleToReal(h)/2) + borderTop,
                scaleToReal(w),scaleToReal(h),null);
    }

    // TODO: Cambiar filepath para incluir para que el usuario incluya data?
    @Override
    public IntImage newImage(String fileName) {
        Image img = null;
        try {
            img = ImageIO.read(new File("data/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        IntImage imgPC = new IntImagePC(img);
        return imgPC; // Example: "/data/button.png"
    }
    @Override
    public int getWidthString(String text) {
        return (int)this.graphics2D.getFont().getStringBounds(text, this.graphics2D.getFontRenderContext()).getWidth();
    }
    @Override
    public int getHeightString(String text) {
        return (int)this.graphics2D.getFont().getStringBounds(text,this.graphics2D.getFontRenderContext()).getHeight();
    }
    @Override
    public void drawText(String text, int x, int y, int color, float tam) {
        this.graphics2D.setColor(new Color (color));
        this.graphics2D.drawString(text, x - (getWidthString(text)/2), y - (getHeightString(text)/2) + borderTop);
    }
    @Override
    public void drawRect(int x, int y, int width, int height) {
        this.graphics2D.drawRect(x,y + borderTop, width,height);
    }
    @Override
    public void fillSquare(int cx, int cy, int side) {
        this.graphics2D.fillRect(cx,cy,side,side);
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        this.graphics2D.fillRect(x,y ,w,h);
    }
    @Override
    public void drawSquare(int cx, int cy, int side) {
        this.graphics2D.drawRect(cx,cy ,side,side);
        this.graphics2D.setPaintMode();
    }
    @Override
    public void drawLine(int initX, int initY, int endX, int endY) {
        this.graphics2D.drawLine(initX,initY ,endX,endY );
    }

    // Conversors
    @Override
    public int logicToRealX(int x) {
        return (int)(x*factorScale );
    }
    @Override
    public int logicToRealY(int y) {        //CONVERSOR DE TAMAÑO LOGICO A REAL EN Y
        return (int)(y*factorScale );
    }
    @Override
    public int scaleToReal(int s) {
        return (int)(s*(factorScale));
    }
    @Override
    public int getWidth() {
        return this.myView.getWidth();
    }
    @Override
    public int getHeight() {
        return this.myView.getHeight();
    }
    @Override
    public int getHeightLogic() {
        return this.logicHeight;
    }
    @Override
    public int getWidthLogic() {
        return this.logicWidth;
    }
    @Override
    public int getBorderTop() {
        return this.borderTop;
    }
    @Override
    public int getWindow() {
        return window;
    }

    // TODO: Investigar sobre bordes y resolucion
    @Override
    public void setResolution(int w, int h) {
        this.myView.setSize(w, h);
    }
}
