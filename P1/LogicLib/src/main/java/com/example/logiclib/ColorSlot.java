package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

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
    private int _colorID;

    public ColorSlot(Engine engine, String filename, int positionX, int positionY, int width, int height){
        _engine = engine;
        _graphics = _engine.getGraphics();
        _image = _graphics.newImage(filename);

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _name = filename;
        _hasColor = false;
        _colorID = -1;
    }

    public void setImage(String filename) {
        _name = filename;
        _image = _graphics.newImage(filename);
    }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    @Override
    public void update() {
        if (GameAttributes.Instance().isEyeOpen && _hasColor){
            String colorButtonName = "color" + _colorID + ".png";
            colorButtonName = colorButtonName.replace(".png", "CB.png");
            setImage(colorButtonName);
        }
        else {
            String colorSlotName = _name;
            _name = colorSlotName.replace("CB.png", ".png");
            setImage(_name);
        }
    }
    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        return false;
    }

    public boolean hasColor(){
        return _hasColor;
    }
    public void setColor(int color, boolean isEyeOpen)
    {
         _hasColor = true;
         _colorID = color;

         // Si tenemos el modo daltónico activado, usamos su respectiva imagen.
         if (isEyeOpen) {
             setImage("color"+_colorID+"CB.png");
         }
         else{
             setImage("color"+_colorID+".png");
         }
    }
}
