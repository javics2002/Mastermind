package com.example.aninterface;

public interface Graphics {
    Font newFont(String fileName, float size);

    Image loadImage(String path);

    void drawImage(Image image, float logicX, float logicY, float logicWidth, float logicHeight, float scale);

    void setColor(int color);

    void clear(int color);

    void drawRect(float logicX, float logicY, float logicWidth, float logicHeight, float scale, int color);
    void drawRealRect(int realX, int realY, int realWidth, int realHeight, int color);
    void drawRoundedRect(float logicX, float logicY, float logicWidth, float logicHeight, float arcWidth, float arcHeight, float scale, int color);

    void drawCircleWithBorder(float logicX, float logicY, float radius, float borderWidth, float scale, int circleColor, int borderColor);
    void drawText(String text, Font font, float logicX, float logicY, float scale, int color);
    void drawCircle(float logicX, float logicY, float radius, float scale, int color);


    boolean inBounds(float posX, float posY, int checkX, int checkY, float width, float height, float scale);

    int getWidth();

    int getLogicWidth();

    int getHeight();

    int getLogicHeight();

    float getStringWidth(String text, Font font);

    float getStringHeight(String text, Font font);

    void setNewResolution(int w, int h);

    void prepareFrame();
}


