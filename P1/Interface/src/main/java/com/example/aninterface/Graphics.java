package com.example.aninterface;

public interface Graphics {
    Font newFont(String fileName, float size);
    Image newImage(String path);
    void drawImage(Image image, int x, int y, int w, int h);
    void setColor(int color);
    void clear(int color);
    void drawRect(int x, int y, int width, int height, int color);
    void drawText(String text, Font font, int x, int y, int color);
    int logicToRealX(int x);
    int logicToRealY(int y);
    int scaleToReal(int realScale);
    int getWidth();
    int getWidthLogic();
    int getHeight();
    int getHeightLogic();
    int getStringWidth(String text, Font font);
    int getStringHeight(String text, Font font);
    void setNewResolution(int w, int h);
    void prepareFrame();
}


