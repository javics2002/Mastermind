package com.example.libengineandroid;
import android.graphics.Bitmap;

import com.example.aninterface.Image;

public class ImageAndroid implements Image{

    //En vez de usar la clase de jave image usamos la clase Bitmap de los graficos de android
    private Bitmap img;
    ImageAndroid(Bitmap img){
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
