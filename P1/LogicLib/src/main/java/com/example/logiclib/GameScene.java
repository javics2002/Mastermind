package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.State;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;

public class GameScene implements State {
    private Font textFont;
    private Text objectiveText;
    private Text attemptsText;
    //private QuitButton quitButton;
    //private ColorblindButton colorblindButton;
    private List<CombinationLayout> combinationLayout;

    public GameScene(Engine engine) {
        Graphics gr = engine.getGraphics();

        textFont = gr.newFont("Comfortaa-Regular.ttf", 48f);

        objectiveText = new Text("Averigua el código!!!", engine,
                gr.getWidthLogic() / 2, gr.getHeightLogic() / 8, 0);
        attemptsText = new Text("Te quedan " + GameAttributes.Instance().attemptsLeft + " intentos!!!!", engine,
                gr.getWidthLogic() / 2, gr.getHeightLogic() / 4, 0);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render(Graphics gr) {
        objectiveText.render();
        attemptsText.render();
    }
    @Override
    public void handleEvents(Input.TouchEvent touchEvent) {
    }
}




