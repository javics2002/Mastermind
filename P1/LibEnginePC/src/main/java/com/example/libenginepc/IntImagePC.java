package com.example.libenginepc;
import java.awt.Image;
import com.example.aninterface.IntImage;
public class IntImagePC implements IntImage {
    private Image img;
    IntImagePC(Image img){
        this.img = img;
    }
    public Image getImg(){
        return this.img;
    }
    @Override
    public int getWidth() {
        return this.img.getWidth(null);
    }
    @Override
    public int getHeight() {
        return this.img.getHeight(null);
    }
}