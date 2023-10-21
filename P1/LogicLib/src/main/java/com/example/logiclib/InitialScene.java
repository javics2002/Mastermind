package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.ErrorType;

public class InitialScene implements State {
    private PlayButton bPlay;
    private Font titleFont;
    private Text titleText;
    public InitialScene(Engine engine) {
        Graphics gr = engine.getGraphics();

        this.bPlay = new PlayButton("button.png", engine, (int)(gr.getWidthLogic()*0.55), (gr.getHeightLogic() / 10)*5, 1200, 700);
        this.titleFont = gr.newFont("Comfortaa-Regular.ttf", 48f);

        this.titleText = new Text("Master Mind", engine, gr.getWidthLogic() / 2, gr.getHeightLogic() / 4, 0);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
        this.bPlay.render();
        titleText.render();
    }
    @Override
    public void handleEvents(Input a) {


        // Bucle con lista touchEvent, el boton recibe un unico touchevent


            if(a.getTouchEvent().size()>0)
            {
                Input.TouchEvent elemento = a.getTouchEvent().get(0);
                this.bPlay.handleEvents( a.getTouchEvent().get(0));
            }
    }
}




