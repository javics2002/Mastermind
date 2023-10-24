package com.example.logiclib;
import com.example.aninterface.Engine;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Graphics;
import com.example.aninterface.Font;

public class InitialScene implements Scene {
    private PlayButton _playButton;
    private Font _titleFont;
    private Text _titleText;

    Engine _engine;

    public InitialScene(Engine engine) {
        _engine = engine;
        Graphics graphics = engine.get_graphics();

        _titleFont = graphics.newFont("Comfortaa-Regular.ttf", 24f);

        _titleText = new Text("Master Mind", _titleFont, engine,
              graphics.getWidthLogic() / 2, graphics.getHeightLogic() / 4, 0);

        _playButton = new PlayButton("playButton.png", engine,
                (int)(graphics.getWidthLogic()*0.5), (int)(graphics.getHeightLogic() *.5f), 331, 88);
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(Graphics gr) {
        _playButton.render();
        _titleText.render();
    }

    @Override
    public void handleEvents(Input a) {
        // Bucle con lista touchEvent, el boton recibe un unico touchevent
        if(a.getTouchEvent().size()>0)
        {
            Input.TouchEvent elemento = a.getTouchEvent().get(0);
            this._playButton.handleEvents( a.getTouchEvent().get(0));
        }
    }
}




