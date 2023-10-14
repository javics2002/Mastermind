package com.example.aninterface;

import java.awt.Color;

public interface Graphics {
    IntImage newImage(String path);
    Font newFont(String fileName, float size);
    void drawImage(IntImage image, int x, int y, int w, int h);
    void setColor(int color);
    void clear(int color);
    void fillSquare(int cx, int cy, int side);
    void fillRect(int x, int y, int w, int h);
    void drawSquare(int cx, int cy, int side);
    void drawRect(int x, int y, int width, int height);
    void drawLine(int initX, int initY, int endX, int endY);
    void drawText(String text, int x, int y, int color);
    int logicToRealX(int x);
    int logicToRealY(int y);
    int scaleToReal(int s);
    int getWidth();
    int getWidthLogic();
    int getHeight();
    int getHeightLogic();
    int getWidthString(String text);
    int getHeightString(String text);
    void setResolution(int w, int h);
}


