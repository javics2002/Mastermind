package com.example.libengineandroid;

import android.graphics.Typeface;
import android.os.Build;
import com.example.aninterface.Font;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FontAndroid implements Font {
    Typeface _font;
    float _size;

    FontAndroid(Typeface font, float size) {
        _font = font;
        _size = size;
    }

    // GETTERS
    public Typeface getFont() {
        return _font;
    }          // OBTENER FUENTE

    @Override
    public int getFontSize() {
        return (int) _size;
    }
}