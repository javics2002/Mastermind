package com.example.aninterface;

import java.awt.Color;

public interface Graphics {
    Image newImage(String name);
    Font newFont(String filename, int size, boolean isBold);
    void clear(int color);
    void translate(int x, int y);
    void scale(int x, int y);
    void save();
    void restore();
    void drawImage(Image image);
    void setColor(Color color);
    void fillRectangle(int cx, int cy, int width, int height);
    void fillRoundRectangle(int cx, int cy, int width, int height);
    void drawRectangle(int cx, int cy, int width, int height);
    void drawRoundRectangle(int cx, int cy, int width, int height);
    void drawLine(int initX, int initY, int endX, int endY);
    void drawCircle(float cx, float cy, float radius);
    void fillCircle(float cx, float cy, float radius);
    void drawText(String text, int x, int y);
    int getWidth();
    int getHeight();
}
