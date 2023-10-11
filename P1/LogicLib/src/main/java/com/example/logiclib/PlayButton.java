package com.example.logiclib;
import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
import com.example.aninterface.IntImage;
public class PlayButton implements Interface {

    private IntImage img;
    private Engine engine;
    private Graphics gr;
    private int x;
    private int y;
    private int w;
    private int h;


   PlayButton(String filename,Engine engine, int x, int y, int w, int h ){
       //Construimos el boton a partir del motor , que nos da los graficos y se encarga de decirle al mismo que cree una nueva
        this.engine = engine;
        this.gr = engine.getGraphics();
        this.img = this.gr.newImage(filename);
        this.x = x;
        this.y = y ;
        this.w = w;
        this.h = h;

    }
    @Override
    public void render() {
        this.gr.drawImage(this.img,x,y,w,h);
    }

    @Override
    public void update() { }
}
