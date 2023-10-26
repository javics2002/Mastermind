package com.example.aninterface;

public interface Graphics {
    //Todo a coordenadas de logica
    Font newFont(String fileName, float size);
    Image newImage(String path);
    // Dibujar una imagen
    void drawImage(Image image, int x, int y, int w, int h);
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
    void drawText(String text, Font font, int x, int y, int color);
    // Transformaciones
    int logicToRealX(int x);
    int logicToRealY(int y);
    int scaleToReal(int s);

    //Getters
    int getWidth();
    int getWidthLogic();
    int getHeight();
    int getHeightLogic();
    int get_borderTop();
    int getStringWidth(String text, Font font);
    int getStringHeight(String text, Font font);

    //Setters
    void setResolution(int w, int h);

    void prepareFrame();

}


