package com.example.libengineandroid;
import android.graphics.Bitmap;

import com.example.aninterface.IntImage;
import android.graphics.Bitmap;

public class IntImageAndroid implements IntImage{

    //En vez de usar la clase de jave image usamos la clase Bitmap de los graficos de android
    private Bitmap img;
    IntImageAndroid(Bitmap img){
        this.img = img;
    }
    public Bitmap getImg(){
        return img;
    }
    @Override
    public int getWidth() {
        return this.img.getWidth();
    }
    @Override
    public int getHeight() {
        return this.img.getHeight();
    }





}
