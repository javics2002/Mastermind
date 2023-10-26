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
    void drawRect(int x, int y, int width, int height, int color);
    // Dibujar texto
    void drawText(String text, Font font, int x, int y, int color);
    // Transformaciones
    int logicToRealX(int x);
    int logicToRealY(int y);

    //Getters
    int getWidth();
    int getWidthLogic();
    int getHeight();
    int getHeightLogic();
    int getStringWidth(String text, Font font);
    int getStringHeight(String text, Font font);

    //Setters
    void setNewResolution(int w, int h);

    void prepareFrame();
}


