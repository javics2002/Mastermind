package com.example.libengineandroid;

import android.graphics.Typeface;
import android.os.Build;
import com.example.aninterface.Font;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IntFontAndroid implements Font {
    Typeface font;

    IntFontAndroid(Typeface font) {
        this.font = font;
    }

    // GETTERS
    public Typeface getFont() {
        return this.font;
    }          // OBTENER FUENTE

    @Override
    public int getFontSize() {
        //Use codigo defensivo para poder devolver el tamaño
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            return font.getWeight();
        else {

            try {
                // Obtén la clase interna de Typeface llamada "TypefaceCompatApi26Impl"
                Class<?> clazz = Class.forName("android.graphics.Typeface$TypefaceCompatApi26Impl");

                // Obtén el método estático "create" en TypefaceCompatApi26Impl
                Method createMethod = clazz.getDeclaredMethod("create", Typeface.class, int.class, boolean.class);

                // Llama al método "create" para obtener una instancia de TypefaceCompatApi26Impl
                Object typefaceCompatApi26Impl = createMethod.invoke(null, font, 0, false);

                // Obtén el campo estático "WEIGHT" en TypefaceCompatApi26Impl
                Field weightField = clazz.getDeclaredField("WEIGHT");

                // Obtén el valor del campo WEIGHT
                return weightField.getInt(typefaceCompatApi26Impl);
            } catch (Exception e) {
                e.printStackTrace();
                // Maneja cualquier excepción que pueda ocurrir durante la reflexión
            }
        }
        return -1 ;

    }
}