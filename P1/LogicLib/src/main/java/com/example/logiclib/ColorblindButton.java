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

    private GameScene gScene;



    private int open; //Dicta si el ojo esta abierto o cerrado

    ColorblindButton(String filename, Engine engine, int positionX, int positionY, int width, int height ,GameScene g) {
        _engine = engine;
        _graphics = engine.getGraphics();
        _image = _graphics.newImage(filename);
        _positionX = positionX;
        _positionY = positionY ;
        _width = width;
        _height = height;
         open = 0 ;
         gScene= g;

    }

    @Override
    public boolean handleEvents(Input.TouchEvent e) {
        if (e.type == Input.InputType.PRESSED && inBounds(e.x, e.y)) {

            if(open==0) //Si no estaba abierto se abre
            {
               open=1;
                _image = _graphics.newImage("UI/eyeOpened.png");
                List<CombinationLayout>cL= gScene.get_combinationLayouts();
                for(CombinationLayout c: cL) {

                    if(c!=null) {
                        for(ColorSlot cSL: c.getSlots()) {
                            if(cSL!=null) {



                                cSL.get_name();
                                if(cSL.hasColor()){
                                    String name = cSL.get_name().replace(".png", "CB.png");
                                    cSL.set_Image(name);
                                }

                            }
                        }
                    }



                }



            }
            else {//Si  esta abierto se cierra de nuevo
               open =0;
                _image = _graphics.newImage("UI/eyeClosed.png");
                List<CombinationLayout>cL= gScene.get_combinationLayouts();
                for(CombinationLayout c: cL) {

                    if(c!=null) {
                        for(ColorSlot cSL: c.getSlots()) {
                            if(cSL!=null) {

                                cSL.get_name();
                                if(cSL.hasColor()){
                                    String name = cSL.get_name().replace("CB.png", ".png");
                                    cSL.set_Image(name);
                                }
                            }
                        }
                    }



                }

            }


            // engine.resume();
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
