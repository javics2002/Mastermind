package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Image;

public class CombinationLayout {
    private Text attemptText;
    private Image separatorImage;
    private ColorLayout colorLayout;
    private HintLayout hintLayout;

    public CombinationLayout(Engine engine) {
        colorLayout = new ColorLayout(engine, 5);
    }
}
