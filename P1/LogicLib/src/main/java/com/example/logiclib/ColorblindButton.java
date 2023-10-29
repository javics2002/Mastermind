package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;

import java.util.List;

public class ColorblindButton implements Interface {
    private Image _image;
    private Engine _engine;
    private Graphics _graphics;
    private int _positionX;
    private int _positionY;
    private int _width;
    private int _height;
    private List<ColorSlot> colorSlot;
    private GameScene _gameScene;

    ColorblindButton(String filename, Engine engine, int positionX, int positionY, int width, int height, GameScene g) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);
        _positionX = positionX;
        _positionY = positionY ;
        _width = width;
        _height = height;
        _gameScene = g;
    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        // Cuando el botón del OJO se pulsa, esta función se encarga de cambiar todos los colores
        // a sus respectivas imagenes para daltónicos. Además, si el modo daltónico está activado,
        // también se encarga de quitar los números para volver al modo normal.

        if (e.type == Input.InputType.PRESSED && inBounds(e.x, e.y)) {
            List<CombinationLayout> combinationLayouts = _gameScene.getCombinationLayouts();
            List<ColorButton> colorButtons = _gameScene.getColorButtons();
            if(!GameAttributes.Instance().isEyeOpen)
            {
                GameAttributes.Instance().isEyeOpen = true;
                _image = _graphics.newImage("UI/eyeOpened.png");

                for(CombinationLayout c: combinationLayouts) {
                    for(ColorSlot cSL: c.getColors()) {
                        if(cSL.hasColor()){
                            String name = cSL.getName().replace(".png", "CB.png");
                            cSL.setImage(name);
                        }
                    }
                }
                for (ColorButton colorButton : colorButtons){
                    String colorButtonName = "color" + colorButton._colorID + ".png";
                    colorButtonName = colorButtonName.replace(".png", "CB.png");
                    colorButton.setImage(colorButtonName);
                }
            }
            else {
                GameAttributes.Instance().isEyeOpen = false;
                _image = _graphics.newImage("UI/eyeClosed.png");

                for(CombinationLayout c: combinationLayouts) {
                    for(ColorSlot cSL: c.getColors()) {
                        if(cSL.hasColor()){
                            String name = cSL.getName().replace("CB.png", ".png");
                            cSL.setImage(name);
                        }
                    }
                }
                for (ColorButton colorButton : colorButtons){
                    String colorButtonName = "color" + colorButton._colorID + "CB.png";
                    colorButtonName = colorButtonName.replace("CB.png", ".png");
                    colorButton.setImage(colorButtonName);
                }
            }
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
