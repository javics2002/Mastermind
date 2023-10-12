package com.example.logiclib;
import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Interface;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
import com.example.aninterface.IntImage;

public class InitialScene implements State {
    private PlayButton bPlay;
    private IntImage logoMMind;
    public InitialScene(Engine engine) {
        //Obtenemos los graficos para poder crear una imagen y obtener width y height
        Graphics gr = engine.getGraphics();

        this.logoMMind = gr.newImage("fotito.png");
        this.bPlay = new PlayButton("button.png", engine, (gr.getWidthLogic() / 2), (gr.getHeightLogic() / 10) * 6, 800, 800);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
            this.bPlay.render();
            gr.drawImage(this.logoMMind, (gr.getWidthLogic() / 2), gr.getHeightLogic() / 6, 365, 67);
        }
}




