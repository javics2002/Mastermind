package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

import javax.print.DocFlavor;

public class ColorSlot implements Interface {
    private Image _image;
    private Engine _engine;
    private Graphics _graphics;
    private int _positionX;
    private int _positionY;
    private int _width;
    private int _height;
    private String _name;
    private boolean _hasColor;

    public ColorSlot(Engine engine, String filename, int positionX, int positionY, int width, int height){
        _engine = engine;
        _graphics = _engine.get_graphics();
        _image = _graphics.newImage(filename);

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _name=filename;
        setColor(1);
    }



    public void set_Image(String filename){

        _name=filename;
        _image = _graphics.newImage(filename);
    }

    public String get_name(){return _name;}
    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }

    public boolean hasColor(){return _hasColor;}
    public void setColor(int color)
    {
         _hasColor=true;
         set_Image("color"+color+".png");
    }
}
