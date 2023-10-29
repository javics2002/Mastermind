package com.example.libengineandroid;
import android.graphics.Bitmap;

import com.example.aninterface.Image;

public class ImageAndroid implements Image {

    // En lugar de usar la clase de Java `Image`, se utiliza la clase `Bitmap` de los gráficos de Android.
    private Bitmap img;

    // Constructor de la clase ImageAndroid que recibe un objeto Bitmap.
    ImageAndroid(Bitmap img) {
        this.img = img;
    }

    // Método para obtener el objeto Bitmap que representa la imagen.
    public Bitmap getImg() {
        return img;
    }

    // Implementación del método de la interfaz Image para obtener el ancho de la imagen.
    @Override
    public int getWidth() {
        return this.img.getWidth();
    }

    // Implementación del método de la interfaz Image para obtener la altura de la imagen.
    @Override
    public int getHeight() {
        return this.img.getHeight();
    }
}
