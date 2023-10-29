package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;

public class PlayButton implements Interface {
    private final Image _image;
    private final Engine _engine;
    private final Graphics _graphics;
    private final int _positionX, _positionY;
    private final int _width, _height;

   PlayButton(String filename, Engine engine, int positionX, int positionY, int width, int height) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);
        _positionX = positionX;
        _positionY = positionY ;
        _width = width;
        _height = height;
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        // Change scene to difficulty scene
        if (e.type == Input.InputType.PRESSED && inBounds(e.x, e.y)) {
            DifficultyScene scene = new DifficultyScene(_engine);
            _engine.setCurrentScene(scene);
            return true;
        }
        return false;
    }

    @Override
    public void update() { }

    @Override
    public void render() {
        _graphics.drawImage(_image, _positionX, _positionY, _width, _height);
    }

    public boolean inBounds(int mX, int mY) {
            return (mX >= (_graphics.logicToRealX(_positionX))
                && mX <=  _graphics.logicToRealX(_positionX)+ _graphics.scaleToReal(_width)
                && mY >= _graphics.logicToRealY(_positionY)
                && mY <= _graphics.logicToRealY(_positionY)+ _graphics.scaleToReal(_height));
    }
}
