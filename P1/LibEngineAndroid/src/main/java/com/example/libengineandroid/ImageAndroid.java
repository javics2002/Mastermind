package com.example.libengineandroid;

import android.graphics.Bitmap;

import com.example.aninterface.Image;

public class ImageAndroid implements Image {

    // En lugar de usar la clase de Java `Image`, se utiliza la clase `Bitmap` de los gráficos de Android.
    private final Bitmap _image;

    // Constructor de la clase ImageAndroid que recibe un objeto Bitmap.
    ImageAndroid(Bitmap image) {
        _image = image;
    }

    // Método para obtener el objeto Bitmap que representa la imagen.
    public Bitmap getImage() {
        return _image;
    }

    // Implementación del método de la interfaz Image para obtener el ancho de la imagen.
    @Override
    public int getWidth() {
        return _image.getWidth();
    }

    // Implementación del método de la interfaz Image para obtener la altura de la imagen.
    @Override
    public int getHeight() {
        return _image.getHeight();
    }
}
