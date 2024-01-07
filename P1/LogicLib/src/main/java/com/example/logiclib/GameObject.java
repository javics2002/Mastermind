package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;

public abstract class GameObject {
    protected final Engine _engine;
    protected final Graphics _graphics;
    protected float _positionX, _positionY;
    protected float _width, _height;
    protected float _scale;

    GameObject(Engine engine, float positionX, float positionY, float width, float height, float scale){
        _engine = engine;
        _graphics = _engine.getGraphics();

        _positionX = positionX;
        _positionY = positionY;
        _width = width;
        _height = height;
        _scale = scale;
    }

    public abstract boolean handleEvents(Input.TouchEvent e);
    public abstract void update(double deltaTime);
    public abstract void render(Graphics graphics);

    public void setPosition(float newPositionX, float newPositionY){
        _positionX = newPositionX;
        _positionY = newPositionY;
    }

    public void setScale(float newScale){
        _scale = newScale;
    }

    public void translate(float deltaPositionX, float deltaPositionY){
        _positionX += deltaPositionX;
        _positionY += deltaPositionY;
    }

    public void scale(float deltaScale){
        _scale *= deltaScale;
    }
}