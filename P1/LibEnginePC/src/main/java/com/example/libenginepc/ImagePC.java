package com.example.libenginepc;
import com.example.aninterface.Image;
public class ImagePC implements Image {
    private final java.awt.Image _image;
    ImagePC(java.awt.Image image){
        _image = image;
    }

    public java.awt.Image getImage(){
        return _image;
    }
    @Override
    public int getWidth() {
        return _image.getWidth(null);
    }
    @Override
    public int getHeight() {
        return _image.getHeight(null);
    }
}