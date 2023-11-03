package com.example.logiclib;

import com.example.aninterface.Engine;

public class ColorblindButton extends Button {
    ColorblindButton(String filename, Engine engine, int positionX, int positionY, int width, int height) {
        super(filename, engine, positionX, positionY, width, height,new ButtonCallback() {
            public void onButtonClicked() {
                GameAttributes.Instance().isEyeOpen = !GameAttributes.Instance().isEyeOpen;
            }

        });

    }


}
