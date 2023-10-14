package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
import com.example.aninterface.IntImage;
import com.example.aninterface.Font;

public class InitialScene implements State {
    private PlayButton bPlay;
    private Font titleFont;
    private Text titleText;
    public InitialScene(Engine engine) {
        Graphics gr = engine.getGraphics();

        this.bPlay = new PlayButton("button.png", engine, (gr.getWidthLogic() / 2), (gr.getHeightLogic() / 10) * 6, 800, 800);
        this.titleFont = gr.newFont("Comfortaa-Regular.ttf", 48f);

        this.titleText = new Text("Master Mind", engine, gr.getWidthLogic() / 2, gr.getHeightLogic() / 4, 0);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
            this.bPlay.render();
            this.titleText.render();
        }
}




