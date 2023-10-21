package com.example.aninterface;

import java.awt.Color;

public interface Graphics {

    Font newFont(String fileName, float size);
    IntImage newImage(String path);
    // Dibujar una imagen
    void drawImage(IntImage image, int x, int y, int w, int h);
    // Cambiar el color con el que se pintan
    void setColor(int color);
    // Limpiar la pantalla con un color
    void clear(int color);
    // Dibujar cuadrados y rectángulos rellenos
    void fillSquare(int cx, int cy, int side);
    void fillRect(int x, int y, int w, int h);
    // Dibujar cuadrados y rectángulos borde
    void drawSquare(int cx, int cy, int side);
    void drawRect(int x, int y, int width, int height);
    // Dibujar línea
    void drawLine(int initX, int initY, int endX, int endY);
    // Dibujar texto
    void drawText(String text, int x, int y, int color);
    // Transformaciones
    int logicToRealX(int x);
    int logicToRealY(int y);
    int scaleToReal(int s);
    //Getters
    int getWidth();
    int getWidthLogic();
    int getHeight();
    int getHeightLogic();
    int getBorderTop();
    int getWindow();
    int getSWidth(String text);
    int getSHeight(String text);
    //Setters
    void setResolution(int w, int h);

    void prepareFrame();
}


